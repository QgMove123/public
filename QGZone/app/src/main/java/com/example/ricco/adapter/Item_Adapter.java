package com.example.ricco.adapter;

/**
 * Created by Mr_Do on 2016/8/10.
 * 用于加载首页的说说
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.ricco.entity.JsonModel;
import com.example.ricco.entity.NoteModel;
import com.example.ricco.entity.TwitterCommentModel;
import com.example.ricco.entity.TwitterModel;
import com.example.ricco.fragment.DongTaiFragment;
import com.example.ricco.others.ShuoshuoListview;
import com.example.ricco.qgzone.ImageDetailActivity;
import com.example.ricco.qgzone.MainActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.ImageLoader;
import com.example.ricco.utils.Item_Adapter_View;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.TalkPicGridView;
import com.example.ricco.utils.TalkRespondListView;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Mr_Do on 2016/8/8.
 */
public class Item_Adapter extends BaseAdapter{
    private final String IP = "192.168.43.172";
    private Handler handler;
    private View contentView;
    private PopupWindow mPopupWindow;
    private EditText huifuEditText;
    private ImageButton huifuImageButton;
    private ArrayList<ArrayList<HashMap<String, Object>>> mPicGridViewList = new ArrayList<ArrayList<HashMap<String, Object>>>();
    private List<Item_Adapter_View> holders = new ArrayList<Item_Adapter_View>();
    private Item_Adapter_View holder = null;//自定义的一个类用来缓存convertview
    private LayoutInflater inflater;
    private ArrayList<TwitterModel> twitterItems = null;
    private ArrayList<NoteModel> noteItems = null;
    private Context context;
    private TwitterModel twitterItem;
    private NoteModel noteItem;
    private TwitterCommentModel twitterCommentModel;
    private TwitterCommentModel comment;
    private int resourceId;
    private int ID;
    private String name;

    public Item_Adapter(ArrayList<TwitterModel> items, Context context,
                        ArrayList<ArrayList<HashMap<String, Object>>> mPicGridViewList, int id ,Handler handler){
        this.twitterItems = items;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.mPicGridViewList = mPicGridViewList;
        this.ID = id;
        this.handler = handler;
    }
    public Item_Adapter(Context context, ArrayList<NoteModel> items, int id){
        this.noteItems = items;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.ID = id;
    }

    @Override
    public int getCount() {

        return twitterItems.size();
    }

    @Override
    public Object getItem(int i) {
        return twitterItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        final int sayItemPosition = position;
        if(twitterItems!=null)twitterItem = (TwitterModel) getItem(position);
        else if(noteItems != null)noteItem = (NoteModel) getItem(position);

        if (convertView ==null) {
            holder = new Item_Adapter_View();
            convertView =inflater.inflate(R.layout.dongtai_say_item, null);//引入布局
            /*ViewHolder与控件的绑定*/
            holder.wordTextView =(TextView)convertView.findViewById(R.id.say_textview);//说说或留言板内容
            holder.nameTextView =(TextView)convertView.findViewById(R.id.dongtai_name_text); //姓名文本框
            holder.timeTextView = (TextView) convertView.findViewById(R.id.dongtai_time_text); //时间文本框
            holder.listView =(TalkRespondListView) convertView.findViewById(R.id.respond_listview);//评论列表
            //以下两个需要在留言板开始时隐藏(暂不实现)
            holder.editText = (EditText) convertView.findViewById(R.id.respond_EditText);//用于回复留言或说说的编辑框
//            holder.editText.setTag(position+"");

            holder.huifuImageButton = (ImageButton)convertView.findViewById(R.id.respond_ImageButton);//回复发送按钮
//            holder.huifuImageButton.setTag(position+"");
            holder.picGridView= (TalkPicGridView) convertView.findViewById(R.id.dongtai_shuos_gridview);//图片列表(说说专用)
            //下面是三个小图标的按钮（说说专用）
            holder.pinglunImageBtn = (ImageButton) convertView.findViewById(R.id.dongtai_pinglun);//评论按钮（首页）
//            holder.pinglunImageBtn.setTag(position+"");
            holder.shanchuImageBtn = (ImageButton) convertView.findViewById(R.id.dongtai_shangchu);//删除按钮
//            holder.shanchuImageBtn.setTag(position+"");
            holder.zanImageBtn = (ImageButton)convertView. findViewById(R.id.dongtai_zan);//点赞按钮(首页)
//            holder.zanImageBtn.setTag(position+"");
            //下面是两个留言的回复和删除按钮（留言板专用）
            holder.huifuBtn = (Button)convertView.findViewById(R.id.liuyan_huifu_btn);//留言的回复按钮
//            holder.huifuBtn.setTag(position+"");
            holder.shanchuBtn = (Button)convertView.findViewById(R.id.liuyan_shanchu_btn);//留言的删除按钮
//            holder.shanchuBtn.setTag(position+"");

            convertView.setTag(holder);
        }else {
            holder = (Item_Adapter_View)convertView.getTag();
        }

         /*根据逻辑改变可见度*/
        if(noteItems != null){//改变可见度
            holder.picGridView.setVisibility(View.GONE);
            holder.pinglunImageBtn.setVisibility(View.GONE);
            holder.shanchuImageBtn.setVisibility(View.GONE);
            holder.zanImageBtn.setVisibility(View.GONE);
        }else if(twitterItems != null){
            holder.huifuBtn.setVisibility(View.GONE);
            holder.shanchuBtn.setVisibility(View.GONE);
            if(ID != twitterItem.getTalkId()){
                holder.shanchuImageBtn.setVisibility(View.GONE);
            }else{
                holder.shanchuImageBtn.setVisibility(View.VISIBLE);
            }
        }
        holder.huifuImageButton.setTag(position+"");
        holder.editText.setTag(position+"");
        holder.pinglunImageBtn.setTag(position+"");
        holder.shanchuImageBtn.setTag(position+"");
        holder.zanImageBtn.setTag(position+"");
        holder.huifuBtn.setTag(position+"");
        holder.shanchuBtn.setTag(position+"");
        holders.add(holder);




        /*根据逻辑改变点赞按钮*/
        if(twitterItem.getSupporterId()!=null&&twitterItem.getSupporterId().contains(3)){//这里需要id
            holder.zanImageBtn.setBackground(context.getDrawable(R.drawable.dongtai_has_zan));
        }else holder.zanImageBtn.setBackground(context.getDrawable(R.drawable.dongtai_before_zan));

        if(twitterItems != null){
            /*两种列表的数据绑定*/
            twitterItem = twitterItems.get(position);
            holder.wordTextView.setText(twitterItem.getTwitterWord());
            holder.timeTextView.setText(twitterItem.getTime());
            holder.nameTextView.setText(twitterItem.getTalkerName());
            final Respond_Adapter adapter = new Respond_Adapter(context,
                    R.layout.dongtai_respond_item, twitterItem.getComment());//说说回复的数据绑定
            holder.listView.setAdapter(adapter);
            SimpleAdapter sAdapter = new SimpleAdapter(context, mPicGridViewList.get(position),
                    R.layout.gridview_item_layout, new String[]{"image"}, new int[]{R.id.shuos_pic});
            sAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView iv = (ImageView) view;
                        iv.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            holder.picGridView.setAdapter(sAdapter);
            /*ListView的监听*/
            holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                private TwitterModel twitterModel = twitterItem;

                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    //回复列表的点击事件
                    showPopupWindow(position, twitterModel);
                }
            });
            /*ListView的长按监听*/
            holder.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    holder = holders.get(sayItemPosition);
                    twitterItem = twitterItems.get(sayItemPosition);
                    if (twitterItem.getTalkId() == ID || twitterItem.getComment().get(position).getCommenterId() == ID) {
                        final Dialog alertDialog = new AlertDialog.Builder(context).
                                //设置标题
                                        setTitle("提示").
                                //设置内容
                                        setMessage("你确定要删除吗?").
                                //设置按钮事件
                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        handler.sendEmptyMessage(1);
                                        String url = "http://" + IP + ":8080/QGzone/TwitterCommentDelete?commentId=" + twitterItem.getComment().get(position).getCommentId();
                                        Log.e("URL", url);
                                        HttpUtil.Get(url, new HttpUtil.CallBackListener() {
                                            @Override
                                            public void OnFinish(String result) {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        twitterItem.getComment().remove(position);
                                                        ShuoshuoListview.itemAdapter.notifyDataSetChanged();
                                                        handler.sendEmptyMessage(5);
                                                    }
                                                });
                                            }

                                            @Override
                                            public void OnError(Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                }).create();
                        alertDialog.show();
                    }else handler.sendEmptyMessage(6);
                    return true;
                }
            });
            /*ImageView的监听*/
            holder.picGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                private int twitterId = twitterItem.getTwitterId();
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //图片列表的点击事件
                    Intent intent = new Intent(view.getContext(), ImageDetailActivity.class);
                    String url = getURL(IP,twitterId,position);
                    intent.putExtra("URL",url);
                    view.getContext().startActivity(intent);
                }
            });
        }else if(noteItems != null){
            noteItem = noteItems.get(position);
            holder.wordTextView.setText(noteItem.getNote());
            holder.timeTextView.setText(noteItem.getTime());
            holder.nameTextView.setText(noteItem.getNoteManName());
            NoteRespond_Adapter adapter = new NoteRespond_Adapter(context,
                    R.layout.dongtai_respond_item,noteItem.getComment());//说说回复的数据绑定
            holder.listView.setAdapter(adapter);
        }



        /*添加按钮的监听*/


       /* 1.评论按钮 */
        holder.pinglunImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = Integer.parseInt(v.getTag().toString());
                holder = holders.get(index);
                if(twitterItems != null)twitterItem = twitterItems.get(position);
                else if(noteItems != null)noteItem = noteItems.get(position);
                holder.editText.requestFocus();
                showKeyBroad();
            }
        });
        holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {//防止编辑框无法获得焦点
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!holder.editText.hasFocus()){
                    View contentView = LayoutInflater.from(context).inflate(R.layout.popupwindow_layout, null);
                    mPopupWindow = new PopupWindow(contentView,
                            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
                    mPopupWindow.setContentView(contentView);
                    mPopupWindow.showAtLocation(contentView,Gravity.CENTER,0,65);
                    mPopupWindow.dismiss();
                }
            }
        });
        holder.huifuImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = Integer.parseInt(v.getTag().toString());
                holder = holders.get(index);
                if (twitterItems != null) twitterItem = twitterItems.get(position);
                else if (noteItems != null) noteItem = noteItems.get(position);
                HttpUtil.Get("http://" + IP + ":8080/QGzone/TwitterCommentAdd?twitterId=" + twitterItem.getTwitterId()
                        + "&targetId=" + twitterItem.getTalkId()
                        + "&comment=" + holder.editText.getText().toString(), new HttpUtil.CallBackListener() {
                    @Override
                    public void OnFinish(String result) {
                        final JsonModel<TwitterCommentModel,TwitterCommentModel> jsonModel = JsonUtil.toModel((String) result,new TypeToken<JsonModel<TwitterCommentModel,TwitterCommentModel>>(){}.getType());
                        twitterCommentModel = new TwitterCommentModel();
                        twitterCommentModel.setTime(jsonModel.getJsonObject().getTime());
                        twitterCommentModel.setTargetName(twitterItem.getTalkerName());
                        twitterCommentModel.setCommenterName("123");//这里需要名字
                        twitterCommentModel.setComment(holder.editText.getText().toString());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                twitterItem.getComment().add(twitterCommentModel);
                                ((Respond_Adapter)holder.listView.getAdapter()).notifyDataSetChanged();
                                holder.editText.setText("");
                            }
                        });
                        Log.e("Tag",result.toString());
                    }

                    @Override
                    public void OnError(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });





        /* 2.删除按钮 */
        holder.shanchuImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.shanchuBtn.setEnabled(false);
                final int index = Integer.parseInt(v.getTag().toString());
                holder = holders.get(index);
                if (twitterItems != null) twitterItem = twitterItems.get(position);
                else if (noteItems != null) noteItem = noteItems.get(position);
                final Dialog alertDialog = new AlertDialog.Builder(context).
                        //设置标题
                                setTitle("提示").
                        //设置内容
                                setMessage("你确定要删除吗?").
                        //设置按钮事件
                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = "http://" + IP + ":8080/QGzone/TwitterDelete?twitterId=" + twitterItem.getTwitterId();
                                Log.e("URL", url);
                                HttpUtil.Get(url, new HttpUtil.CallBackListener() {
                                    @Override
                                    public void OnFinish(String result) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ShuoshuoListview.itemList.remove(position);
                                                ShuoshuoListview.itemAdapter.notifyDataSetChanged();
                                                handler.sendEmptyMessage(3);
                                            }
                                        });
                                    }

                                    @Override
                                    public void OnError(Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        }).create();
                alertDialog.show();
                holder.shanchuBtn.setEnabled(true);
            }
        });







        /* 点赞按钮 */
        holder.zanImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {//点赞按钮
                Log.e("Click", "OK");
                final int index = Integer.parseInt(v.getTag().toString());
                holder = holders.get(index);
                v.setEnabled(false);
                if (twitterItems != null) twitterItem = twitterItems.get(position);
                String url = "http://"+IP+":8080/QGzone/TwitterSupport?twitterId="+twitterItem.getTwitterId();
                HttpUtil.Get(url, new HttpUtil.CallBackListener(){
                    @Override
                    public void OnFinish(String result) {
                        //返回状态
                        Log.e("Tag",result+"");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(twitterItem.getSupporterId().contains(3))twitterItem.getSupporterId().remove(Integer.valueOf(3));
                                else twitterItem.getSupporterId().add(Integer.valueOf(3));//这里需要id
                                ShuoshuoListview.itemAdapter.notifyDataSetChanged();
                                v.setEnabled(true);
                            }
                        });
                    }

                    @Override
                    public void OnError(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        return convertView;
    }










//    public void onClick(View view){
//        final int index = Integer.parseInt(view.getTag().toString());
//        holder = holders.get(index);
//        if(twitterItems != null)twitterItem = twitterItems.get(index);
//        else if(noteItems != null)noteItem = noteItems.get(index);
//        switch (view.getId()){
//            case R.id.dongtai_pinglun:{
//                //评论按钮的功能，焦点跳转到编辑框上,弹出软键盘
//                holder.editText.setVisibility(View.VISIBLE);
//                holder.huifuImageButton.setVisibility(View.VISIBLE);
//                holder.editText.requestFocus();
//                holder.editText.requestFocus();
//                break;
//            }
//            case R.id.dongtai_shangchu:{
//                //删除该item
//                //删除说说
//                String url = "http:"+IP+":8080:QGzone/TwitterDelete?twitterId="+twitterItem.getTalkId();
//                HttpUtil.Get(url, new HttpUtil.CallBackListener(){
//                    @Override
//                    public void OnFinish(Object result) {
//                        //返回状态
//                        holder.listView.removeViewAt(index);
//                    }
//
//                    @Override
//                    public void OnError(Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//                break;
//            }
//            case  R.id.dongtai_zan:{
//                //检测可见度，切换可不可见
//                //给说说点赞
//                String url = "http://"+IP+":8080/QGzone/TwitterSupport?twitterId="+twitterItem.getTalkId();
//                HttpUtil.Get(url, new HttpUtil.CallBackListener(){
//                    @Override
//                    public void OnFinish(Object result) {
//                        //返回状态
//                        if(holder.zanImageBtn.getVisibility()==View.INVISIBLE)
//                            holder.zanImageBtn.setVisibility(View.VISIBLE);
//                        else holder.zanImageBtn.setVisibility(View.INVISIBLE);
//                    }
//
//                    @Override
//                    public void OnError(Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//                break;
//            }
//            case R.id.liuyan_huifu_btn:{
//                //留言板中留言的回复
//
//                break;
//            }
//            case R.id.liuyan_shanchu_btn:{
//                //留言板中留言的删除
//
//                break;
//            }
//            case R.id.respond_ImageButton:{
//                //发送留言
//                if(twitterItems != null){
//                    if(!holder.editText.getText().toString().equals("")){
//                        TwitterCommentModel twitterCommentModel = new TwitterCommentModel();
//                        twitterCommentModel.setComment(holder.editText.getText().toString());
//                        twitterCommentModel.setTwitterId(twitterItem.getTwitterId());
//                        twitterCommentModel.setTargetId(twitterItem.getTalkId());
//                        twitterCommentModel.setTargetName(twitterItem.getTalkerName());
//                        //评论者id
//                        //评论者名字
//                        holder.editText.setText("");
//                        JsonUtil.toJson(twitterCommentModel);
//                    }
//                }else if(noteItems != null){
//
//                }
//                break;
//            }
//            default: break;
//        }
//    }
    /*要用到的方法*/
    private String getURL(String ip, int twitterId, int position){
        Log.e("Tag","http://"+ip+":8080/QGzone/twitterPhotos/"+twitterId+"_"+(position+1)+".jpg");
        return "http://"+ip+":8080/QGzone/twitterPhotos/"+twitterId+"_"+(position+1)+".jpg";
    }
    private void showPopupWindow(final int position, final TwitterModel twitterModel) {//弹出框，包含回复功能
        showKeyBroad();
        //设置contentView
        View contentView = LayoutInflater.from(context).inflate(R.layout.popupwindow_layout, null);
        mPopupWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setContentView(contentView);
        //显示PopupWindow
        mPopupWindow.setBackgroundDrawable(context.getDrawable(R.color.colorPrimary));
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAtLocation(contentView,Gravity.CENTER,0,65);
        final EditText huifuEditText = (EditText) contentView.findViewById(R.id.pophuifu_editText);
        ImageButton huifuImageBtn = (ImageButton) contentView.findViewById(R.id.pophuifu_imagebtn);
        huifuImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                if(!holder.editText.getText().equals("")) {
                    HttpUtil.Get("http://" + IP + ":8080/QGzone/TwitterCommentAdd?twitterId=" + twitterModel.getTwitterId()
                            + "&targetId=" + twitterModel.getComment().get(position).getCommenterId()
                            + "&comment=" + huifuEditText.getText().toString(), new HttpUtil.CallBackListener() {
                        @Override
                        public void OnFinish(String result) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessage(2);
                                }
                            });
                            Log.e("Tag", result.toString());
                        }

                        @Override
                        public void OnError(Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }
    private void showKeyBroad(){
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputMethodManager=(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 300);
    }

}

