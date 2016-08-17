package com.example.ricco.others;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ricco.qgzone.R;
import com.example.ricco.utils.LogUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author yason
 * 图片加载类
 * 1.根据url到 LruCache中查找，找到返回
 * 2.找不到则加入任务队列，通知后台轮询线程(Handler)
 * 3.轮询线程取出任务(Runnable)到线程池中去执行
 * 4.根据url加载图片
 *      1）获取图片显示的大小
 *      2）使用Options对图片进行压缩
 *      3）加载图片且放入LruCache
 */
public class ImageLoader {

    private static ImageLoader mInstance;
    //后台轮询线程
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;
    //缓存
    private LruCache<String, Bitmap> mLruCache;
    //任务队列
    private LinkedList<Runnable> mTaskQueue;
    //线程池
    private ExecutorService mThreadPool;
    private static final int DEFAULT_THREAD_COUNT = 1;
    //UI线程中的Handler
    private Handler mUIHandler;
    //控制并行顺序
    //保证轮询线程的Handler在发送消息前已被初始化
    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    //控制线程池内部队列
    //保证任务队列LIFO模式起作用
    private Semaphore mSemaphoreThreadPool;
    //默认图片
    private int defPath = R.mipmap.ic_launcher;

    /**
     * 构造方法，初始化变量
     * @param threadCount
     */
    private ImageLoader(int threadCount) {
        //后台轮询线程
        mPoolThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        //到任务队列取出一个任务交给线程池执行
                        Runnable task = mTaskQueue.removeLast();
                        mThreadPool.execute(task);
                        try {
                            //获取一个信号量
                            // 允许该任务从任务队列中添加到线程池内部队列，不足时阻塞
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //初始化Handler完毕
                //释放一个信号量AddTask()方法可执行
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }
        };
        mPoolThread.start();

        //获取应用最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        mLruCache = new LruCache<String,Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //测量每个Bitmap的大小
                return value.getRowBytes()*value.getHeight();
            }
        };

        //任务队列
        mTaskQueue = new LinkedList<Runnable>();

        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);

        //控制线程池队列
        mSemaphoreThreadPool = new Semaphore(threadCount);
    }


    /**
     * 获取唯一实例
     * @param threadCount
     * @return
     */
    public static ImageLoader getInstance(int threadCount) {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(threadCount);
                }
            }
        }
        return mInstance;
    }


    /**
     * 加载图片，核心方法
     *
     * @param path
     * @param imageView
     */
    public void loadImage(final String path,final ImageView imageView, final boolean isLocal) {
        //子线程加载图片较慢，会造成错位
        imageView.setTag(path);
        if (mUIHandler == null) {
            mUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    //获取得到的图片，为imageView回调设置图片
                    ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
                    ImageView imageView = holder.imageView;
                    Bitmap bm = holder.bitmap;
                    String path = holder.path;
                    //将path与getTag存储路径进行比较
                    if (imageView.getTag().toString().equals(path)) {
                        if(bm == null){
                            imageView.setImageResource(defPath);
                        }else {
                            imageView.setImageBitmap(bm);
                        }
                    }
                }
            };
        }
        //根据path在缓存中获取bitmap
        Bitmap bm = mLruCache.get(path);

        if (bm != null) {
            //返回图片到UI线程
            refreashBitmap(bm, path,imageView);
        } else {
            addTask(new Runnable() {
                @Override
                public void run() {
                    //1.获得图片需要显示的大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    //2.获取图片并压缩
                    Bitmap bm;
                    if(isLocal){
                        bm = decodeSampleBitmapFromPath(path, imageSize.width, imageSize.height);
                    }else{
                        bm = decodeSampleBitmapFromUrl(path, imageSize.width, imageSize.height);
                    }
                    //3.把图片加入到缓存
                    if(bm != null){
                        addBipmapToLruCache(path, bm);
                    }
                    //4.返回图片到UI线程
                    refreashBitmap(bm, path,imageView);
                    //5.释放一个信号量
                    //允许一个新的任务加入到线程池内部队列中
                    mSemaphoreThreadPool.release();
                }
            });
        }
    }


    /**
     * 将图片返回UI线程
     * @param bm
     * @param path
     */
    private void refreashBitmap(Bitmap bm, String path,ImageView imageView) {
        Message message = Message.obtain();
        ImgBeanHolder holder = new ImgBeanHolder();
        holder.bitmap = bm;
        holder.path = path;
        holder.imageView = imageView;
        message.obj = holder;
        mUIHandler.sendMessage(message);
    }


    /**
     * 根据imageView获适当压缩的宽和高
     * @param imageView
     * @return
     */
    private ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();

        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();

        ViewGroup.LayoutParams Ip = imageView.getLayoutParams();
        //1.获取imageView的实际宽度,
        int width = imageView.getWidth();
        if(width <= 0){
            //2.获取imageView在layout中声明的宽度
            width = Ip.width;
        }
        if(width <= 0){
            //3.检查最大值
            width = imageView.getMaxWidth();
        }
        if(width <= 0){
            //4.获取屏幕宽度
            width = displayMetrics.widthPixels;
        }

        int height = imageView.getHeight();
        if(height <= 0){
            height = Ip.height;
        }
        if(height <= 0){
            height = imageView.getMaxHeight();
        }
        if(height <= 0){
            height = displayMetrics.heightPixels;
        }

        imageSize.width = width;
        imageSize.height = height;

        return imageSize;
    }


    /**
     * 加入任务队列，通知后台轮询线程
     * @param runnable
     */
    private synchronized void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);
        try {
            if(mPoolThreadHandler == null)
                mSemaphorePoolThreadHandler.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //发送通知
        //此时轮询线程可能未初始化Handler
        //通过信号量来控制并发，同步线程执行顺序
        mPoolThreadHandler.sendEmptyMessage(0);
    }


    /**
     * 获取网络图片并压缩
     * @param url
     * @param reqwidth
     * @param reqheight
     * @return
     */
    private Bitmap decodeSampleBitmapFromUrl(String url, int reqwidth, int reqheight) {
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;
        try {
            //1.获取conn
            URL httpUrl = new URL(url);
            conn = (HttpURLConnection)httpUrl.openConnection();
            //2.设置请求头内容
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            //3.建立连接
            conn.connect();
            LogUtil.e("UpLoadPhoto",conn.getResponseCode()+"");
            //4.获取输入流
            bis = new BufferedInputStream(conn.getInputStream());
//            //5.图片压缩
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(bis, null, options);
//            int width = options.outWidth;
//            int height = options.outHeight;
//            options.inSampleSize = 1;
//            if (width > reqwidth || height > reqheight) {
//                int widthRatio = Math.round(width * 1.0f / reqwidth);
//                int heightRatio = Math.round(height * 1.0f / reqheight);
//                options.inSampleSize = Math.max(widthRatio, heightRatio);
//            }
//            options.inJustDecodeBounds = false;
            Bitmap bitmap  = BitmapFactory.decodeStream(bis);
            return bitmap;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                bis.close();
                conn.disconnect();
            } catch (IOException e) {
                return null;
            } catch (Exception e){
                return null;
            }
        }
    }


    /**
     *
     * 获取本地图片并压缩
     * @param path
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap decodeSampleBitmapFromPath(String path, int reqWidth, int reqHeight) {
        //1.使用Options解析图片，不加载到内存
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        //2.获取原图大小
        int width = options.outWidth;
        int height = options.outHeight;
        //3.计算SampleSize
        if(width > reqWidth ||height > reqHeight){
            int widthRatio = Math.round(width *1.0f/ reqWidth);
            int heightRatio = Math.round(height *1.0f/ reqHeight);

            options.inSampleSize = Math.max(widthRatio, heightRatio);
        }
        //4.使用获得的InSampleSize再次解析
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }


    /**
     * 将图片加入缓存
     * @param path
     * @param bm
     */
    private void addBipmapToLruCache(String path, Bitmap bm) {
        if (mLruCache.get(path) == null) {
            if (bm != null) {
                mLruCache.put(path, bm);
            }
        }
    }


    private class ImageSize{
        int width;
        int height;
    }

    private class ImgBeanHolder{
        ImageView imageView;
        Bitmap bitmap;
        String path;
    }
}
