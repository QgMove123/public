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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.example.ricco.constant.Constant;
import com.example.ricco.entity.JsonModel;
import com.example.ricco.entity.NoteCommentModel;
import com.example.ricco.entity.NoteModel;
import com.example.ricco.entity.TwitterCommentModel;
import com.example.ricco.entity.TwitterModel;
import com.example.ricco.others.CircleImageVIew;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.qgzone.ImageDetailActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.Item_Adapter_View;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.LogUtil;
import com.example.ricco.utils.TalkPicGridView;
import com.example.ricco.utils.TalkRespondListView;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Mr_Do on 2016/8/8.
 */
public class ShuoshuoAdapter extends BaseAdapter{
    private final String IP = Constant.host;
    private int ID = Constant.HOST_ID;
    private String name;
    private Handler handler;
    private PopupWindow mPopupWindow;
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
    private NoteCommentModel noteCommentModel;

    public ShuoshuoAdapter(ArrayList<TwitterModel> items, Context context,
                           ArrayList<ArrayList<HashMap<String, Object>>> mPicGridViewList, Handler handler){
        this.twitterItems = items;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.mPicGridViewList = mPicGridViewList;
        this.handler = handler;
    }
    public ShuoshuoAdapter(ArrayList<NoteModel> items , Context context, Handler handler){
        this.noteItems = items;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.handler = handler;
    }

    @Override
    public int getCount() {

        if(twitterItems != null) return twitterItems.size();
        else if(noteItems != null) return noteItems.size();
        else return 0;
    }

    @Override
    public Object getItem(int i) {
        if(twitterItems != null) return twitterItems.get(i);
        else if(noteItems != null) return noteItems.get(i);
        else return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        final int sayItemPosition = position;
        if(twitterItems!=null&&twitterItems.size()>0)twitterItem = (TwitterModel) getItem(position);
        else if(noteItems != null&&noteItems.size()>0)noteItem = (NoteModel) getItem(position);

        if (convertView ==null) {
            holder = new Item_Adapter_View();
            convertView =inflater.inflate(R.layout.dongtai_say_item, null);//引入布局
            /*ViewHolder与控件的绑定*/
            holder.perPhotoImageVIew = (CircleImageVIew)convertView.findViewById(R.id.list_personphoto);

            holder.wordTextView =(TextView)convertView.findViewById(R.id.say_textview);//说说或留言板内容

            holder.nameTextView =(TextView)convertView.findViewById(R.id.dongtai_name_text); //姓名文本框

            holder.timeTextView = (TextView) convertView.findViewById(R.id.dongtai_time_text); //时间文本框

            holder.listView =(TalkRespondListView) convertView.findViewById(R.id.respond_listview);//评论列表

            holder.editText = (EditText) convertView.findViewById(R.id.respond_EditText);//用于回复留言或说说的编辑框

            holder.huifuImageButton = (ImageButton)convertView.findViewById(R.id.respond_ImageButton);//回复发送按钮

            holder.picGridView= (TalkPicGridView) convertView.findViewById(R.id.dongtai_shuos_gridview);//图片列表(说说专用)

            holder.pinglunImageBtn = (ImageButton) convertView.findViewById(R.id.dongtai_pinglun);//评论按钮（首页）

            holder.shanchuImageBtn = (ImageButton) convertView.findViewById(R.id.dongtai_shangchu);//删除按钮

            holder.zanImageBtn = (ImageButton)convertView. findViewById(R.id.dongtai_zan);//点赞按钮(首页)
            //下面是两个留言的回复和删除按钮（留言板专用）
            holder.huifuBtn = (Button)convertView.findViewById(R.id.liuyan_huifu_btn);//留言的回复按钮

            holder.shanchuBtn = (Button)convertView.findViewById(R.id.liuyan_shanchu_btn);//留言的删除按钮

            convertView.setTag(holder);

            holder.huifuImageButton.setTag(position+"");
            holder.editText.setTag(position+"");
            holder.pinglunImageBtn.setTag(position+"");
            holder.shanchuImageBtn.setTag(position+"");
            holder.zanImageBtn.setTag(position+"");
            holder.huifuBtn.setTag(position+"");
            holder.shanchuBtn.setTag(position+"");
        }else {
            holder = (Item_Adapter_View)convertView.getTag();
            holder.editText.setText("");
            holder.editText.clearFocus();
            holder.shanchuBtn.setEnabled(true);
            holder.zanImageBtn.setEnabled(true);
            holder.shanchuImageBtn.setEnabled(true);
        }
        if(holders.size()<position + 1|| holders.size() == 0)holders.add(holder);
        else holders.set(position,holder);
         /*根据逻辑改变可见度*/
        if(noteItems != null){//改变可见度
            holder.picGridView.setVisibility(View.GONE);
            holder.pinglunImageBtn.setVisibility(View.GONE);
            holder.shanchuImageBtn.setVisibility(View.GONE);
            holder.zanImageBtn.setVisibility(View.GONE);
            if(!(ID == noteItem.getNoteManId() || ID == noteItem.getTargetId())){
                holder.shanchuBtn.setVisibility(View.GONE);
            }
        }else if(twitterItems != null){
            holder.huifuBtn.setVisibility(View.GONE);
            holder.shanchuBtn.setVisibility(View.GONE);
            if(ID != twitterItem.getTalkId()){
                holder.shanchuImageBtn.setVisibility(View.GONE);
            }else{
                holder.shanchuImageBtn.setVisibility(View.VISIBLE);
            }
        }


        LogUtil.e("position",position+"");
        LogUtil.e("size",holders.size()+"");

        //加载头像
        if(twitterItems!=null&&twitterItems.size()>0)
            ImageLoader.getInstance(1).loadImage(Constant.civUrl+twitterItem.getTalkId()+".jpg",holder.perPhotoImageVIew,false);
        else if(noteItems != null&&noteItems.size()>0)
            ImageLoader.getInstance(1).loadImage(Constant.civUrl+noteItem.getNoteManId()+".jpg",holder.perPhotoImageVIew,false);


        if(twitterItems != null){

            /*根据逻辑改变点赞按钮*/
            if(twitterItem.getSupporterId()!=null&&twitterItem.getSupporterId().contains(3)){//这里需要id
                holder.zanImageBtn.setBackground(context.getDrawable(R.mipmap.dongtai_has_zan));
            }else holder.zanImageBtn.setBackground(context.getDrawable(R.mipmap.dongtai_before_zan));

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

               /*添加功能按钮的监听*/


       /* 1.评论按钮 */
            holder.pinglunImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    final int index = Integer.parseInt(v.getTag().toString());
                    holder = holders.get(position);
                    Log.e("Position",position+"");
                    if(twitterItems != null)twitterItem = twitterItems.get(position);
                    else if(noteItems != null)noteItem = noteItems.get(position);
                    holder.editText.requestFocus();
                    showKeyBroad();
                }
            });

        /* 2.删除按钮 */
            holder.shanchuImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.shanchuImageBtn.setEnabled(false);
//                    final int index = Integer.parseInt(v.getTag().toString());
                    holder = holders.get(position);
                    if (twitterItems != null) twitterItem = twitterItems.get(position);
                    final Dialog alertDialog = new AlertDialog.Builder(context).
                            //设置标题
                                    setTitle("提示").
                            //设置内容
                                    setMessage("你确定要删除吗?").
                            //设置按钮事件
                                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = Constant.TalkPub.deleteshuoshuo + twitterItem.getTwitterId();//删除说说
                                    Log.e("URL", url);
                                    HttpUtil.Get(url, new HttpUtil.CallBackListener() {
                                        @Override
                                        public void OnFinish(String result) {
                                            handler.sendEmptyMessage(1);
                                            Message msg = new Message();
                                            Bundle b = new Bundle();
                                            b.putInt("position",position);
                                            msg.what = 7;
                                            msg.setData(b);
                                            handler.sendMessage(msg);
//                                            handler.post(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    ShuoshuoListview.itemList.remove(position);
//                                                    ShuoshuoListview.itemAdapter.notifyDataSetChanged();
//                                                    handler.sendEmptyMessage(3);
//                                                }
//                                            });
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
                    holder.shanchuImageBtn.setEnabled(true);
                }
            });

        /*3. 点赞按钮 */
            holder.zanImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {//点赞按钮
                    Log.e("Click", "OK");
//                    final int index = Integer.parseInt(v.getTag().toString());
                    holder = holders.get(position);
                    v.setEnabled(false);
                    if (twitterItems != null) twitterItem = twitterItems.get(position);
                    String url = Constant.TalkPub.supportshuoshuo+twitterItem.getTwitterId();//点赞
                    HttpUtil.Get(url, new HttpUtil.CallBackListener(){
                        @Override
                        public void OnFinish(String result) {
                            //返回状态
                            Log.e("Tag",result+"");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(twitterItem.getSupporterId().contains(3))twitterItem.getSupporterId().remove(Integer.valueOf(3));
                                    else twitterItem.getSupporterId().add(Integer.valueOf(ID));//这里需要id

                                    handler.sendEmptyMessage(8);

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
        }else if(noteItems != null){
            noteItem = noteItems.get(position);
            holder.wordTextView.setText(noteItem.getNote());
            holder.timeTextView.setText(noteItem.getTime());
            holder.nameTextView.setText(noteItem.getNoteManName());
            NoteRespond_Adapter adapter = new NoteRespond_Adapter(context,
                    R.layout.dongtai_respond_item,noteItem.getComment());//说说回复的数据绑定
            holder.listView.setAdapter(adapter);

            holder.shanchuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.shanchuBtn.setEnabled(false);
//                    final int index = Integer.parseInt(v.getTag().toString());
                    holder = holders.get(position);
                    if(noteItems != null) noteItem = noteItems.get(position);
                    final Dialog alertDialog = new AlertDialog.Builder(context).
                            //设置标题
                                    setTitle("提示").
                            //设置内容
                                    setMessage("你确定要删除吗?").
                            //设置按钮事件
                                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = Constant.Note.deletenote + noteItem.getNoteId();
                                    LogUtil.e("URL", url);
                                    handler.sendEmptyMessage(1);
                                    HttpUtil.Get(url, new HttpUtil.CallBackListener() {
                                        @Override
                                        public void OnFinish(String result) {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
//                                                    ShuoshuoListview.noteList.remove(position);
//                                                    ShuoshuoListview.itemAdapter.notifyDataSetChanged();
                                                    Message msg = new Message();
                                                    Bundle b = new Bundle();
                                                    b.putInt("position",position);
                                                    msg.what = 7;
                                                    msg.setData(b);
                                                    handler.sendMessage(msg);
//                                                    handler.sendEmptyMessage(3);
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
                    holder.shanchuBtn.setEnabled(true);
                }
            });

            holder.huifuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    final int index = Integer.parseInt(v.getTag().toString());
                    holder = holders.get(position);
                    if(noteItems != null)noteItem = noteItems.get(position);
                    holder.editText.requestFocus();
                    showKeyBroad();
                }
            });
        }



        /*ListView的监听*/
        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private TwitterModel twitterModel;
            private NoteModel noteModel;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //回复列表的点击事件
                String URL = null;

                if(twitterItems != null){
                    Log.e("sayItemPosition",sayItemPosition+"");
                    twitterModel = twitterItems.get(sayItemPosition);
                    URL = Constant.TalkPub.shuoshuocomment+"?twitterId=" + twitterModel.getTwitterId()
                            + "&targetId=" + twitterModel.getComment().get(position).getCommenterId()
                            + "&comment=";//评论说说
                    showPopupWindow(URL,twitterModel.getComment().get(position).getCommenterName(),sayItemPosition);
                }else if(noteItems != null){
                    Log.e("sayItemPosition",sayItemPosition+"");
                    noteModel = noteItems.get(sayItemPosition);
                    URL = Constant.Note.notecomment+noteModel.getComment().get(position).getCommenterId()+"&noteId="+noteItem.getNoteId()+
                            "&comment=";//评论留言
                    showPopupWindow(URL,noteModel.getComment().get(position).getCommenterName(), sayItemPosition);
                }
            }
        });

        /*ListView的长按监听*/
        holder.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                holder = holders.get(sayItemPosition);
                if(twitterItems!=null)twitterItem = twitterItems.get(sayItemPosition);
                else noteItem = noteItems.get(sayItemPosition);
                if ((twitterItems!=null && (twitterItem.getTalkId() == ID || twitterItem.getComment().get(position).getCommenterId() == ID))
                        || (noteItems!=null && (noteItem.getTargetId() == ID || noteItem.getComment().get(position).getCommenterId() == ID)) ) {
                    final Dialog alertDialog = new AlertDialog.Builder(context).
                            //设置标题
                                    setTitle("提示").
                            //设置内容
                                    setMessage("你确定要删除吗?").
                            //设置按钮事件
                                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = null;
                                    handler.sendEmptyMessage(1);
                                    if(twitterItems!=null) url = Constant.TalkPub.deletecomment+ twitterItem.getComment().get(position).getCommentId();
                                    else if(noteItems != null) url = Constant.Note.deletecomment+noteItem.getComment().get(position).getCommentId();//需要删除留言评论的id
                                    Log.e("URL", url);
                                    HttpUtil.Get(url, new HttpUtil.CallBackListener() {
                                        @Override
                                        public void OnFinish(String result) {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(twitterItems != null) {
                                                        twitterItem.getComment().remove(position);
                                                    }else if(noteItems != null){
                                                        noteItem.getComment().remove(position);
                                                    }
//                                                    ShuoshuoListview.itemAdapter.notifyDataSetChanged();
                                                    handler.sendEmptyMessage(8);
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
        holder.huifuImageButton.setOnClickListener(new View.OnClickListener() {//编辑框中回复按钮的处理
            @Override
            public void onClick(View v) {
//                final int index = Integer.parseInt(v.getTag().toString());
                holder = holders.get(position);
                if (twitterItems != null) twitterItem = twitterItems.get(position);
                else if(noteItems != null) noteItem = noteItems.get(position);
                String URL = null;
                if(twitterItems != null){
                    URL = Constant.TalkPub.shuoshuocomment+"?twitterId=" + twitterItem.getTwitterId()
                            + "&targetId=" + twitterItem.getTalkId()
                            + "&comment=";
                }else{
                    URL = Constant.Note.notecomment+"?targetId="+ noteItem.getTargetId()+"&noteId="+noteItem.getNoteId()+"&comment=";
                }
                if(!holder.editText.getText().toString().equals("")) {
                    HttpUtil.Get(URL + holder.editText.getText().toString(), new HttpUtil.CallBackListener() {
                        @Override
                        public void OnFinish(String result) {
                            if (twitterItems != null) {
                                final JsonModel<TwitterCommentModel, TwitterCommentModel> jsonModel =
                                        JsonUtil.toModel((String) result, new TypeToken<JsonModel<TwitterCommentModel, TwitterCommentModel>>() {
                                        }.getType());
                                twitterCommentModel = new TwitterCommentModel();
                                twitterCommentModel.setTime(jsonModel.getJsonObject().getTime());
                                twitterCommentModel.setTargetName(twitterItem.getTalkerName());
                                twitterCommentModel.setCommenterName("123");    //这里需要名字
                                twitterCommentModel.setCommenterId(ID);
                                twitterCommentModel.setComment(holder.editText.getText().toString());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        twitterItem.getComment().add(twitterCommentModel);
                                        ((Respond_Adapter) holder.listView.getAdapter()).notifyDataSetChanged();
                                        holder.editText.setText("");
                                    }
                                });
                            } else if (noteItems != null) {
                                final JsonModel<NoteCommentModel, NoteCommentModel> jsonModel =
                                        JsonUtil.toModel((String) result, new TypeToken<JsonModel<NoteCommentModel, NoteCommentModel>>() {
                                        }.getType());
                                noteCommentModel = new NoteCommentModel();
                                noteCommentModel.setTime(jsonModel.getJsonObject().getTime());
                                noteCommentModel.setTargetName(noteItem.getNoteManName());
                                noteCommentModel.setCommenterName("123");   //这里需要名字
                                noteCommentModel.setComment(holder.editText.getText().toString());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        noteItem.getComment().add(noteCommentModel);
                                        ((NoteRespond_Adapter) holder.listView.getAdapter()).notifyDataSetChanged();
                                        holder.editText.setText("");
                                    }
                                });
                            }
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

        return convertView;
    }
    /*要用到的方法*/
    private String getURL(String ip, int twitterId, int position){//获取大图片
        Log.e("Tag",Constant.TalkPub.getbigpicture+twitterId+"_"+(position+1)+".jpg");
        return Constant.TalkPub.getbigpicture+twitterId+"_"+(position+1)+".jpg";
    }
    private void showPopupWindow(final String url, final String targe, final int position) {//弹出框，包含回复功能
        showKeyBroad();

        //设置contentView
        View contentView = LayoutInflater.from(context).inflate(R.layout.popupwindow_layout, null);
        mPopupWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setContentView(contentView);
        //显示PopupWindow
        mPopupWindow.setBackgroundDrawable(context.getDrawable(R.color.colorLiuYanBg));
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAtLocation(contentView,Gravity.CENTER,0,65);
        final EditText huifuEditText = (EditText) contentView.findViewById(R.id.pophuifu_editText);
        huifuEditText.setHighlightColor(Color.argb(12,0,0,0));
        if(twitterItems!=null&&twitterItems.size()>0){
            huifuEditText.setHint("@回复"+targe+":");
        }else if(noteItems!=null && noteItems.size()>0){
            huifuEditText.setHint("@回复"+targe+":");
        }

        ImageButton huifuImageBtn = (ImageButton) contentView.findViewById(R.id.pophuifu_imagebtn);

        huifuImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                if(!huifuEditText.getText().equals("")) {
                    HttpUtil.Get(url + huifuEditText.getText().toString(), new HttpUtil.CallBackListener() {
                        @Override
                        public void OnFinish(String result) {
                            if (twitterItems != null) {
                                final JsonModel<TwitterCommentModel, TwitterCommentModel> jsonModel =
                                        JsonUtil.toModel((String) result, new TypeToken<JsonModel<TwitterCommentModel, TwitterCommentModel>>() {
                                        }.getType());
                                twitterCommentModel = new TwitterCommentModel();
                                twitterCommentModel.setTime(jsonModel.getJsonObject().getTime());
                                twitterCommentModel.setTargetName(targe);
                                twitterCommentModel.setCommenterName("123");    //这里需要名字
                                twitterCommentModel.setCommenterId(ID);
                                twitterCommentModel.setComment(huifuEditText.getText().toString());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        twitterItem = twitterItems.get(position);
                                        twitterItem.getComment().add(twitterCommentModel);
                                        ((Respond_Adapter) holder.listView.getAdapter()).notifyDataSetChanged();
                                        huifuEditText.setText("");
                                    }
                                });
                            } else if (noteItems != null) {
                                final JsonModel<NoteCommentModel, NoteCommentModel> jsonModel =
                                        JsonUtil.toModel((String) result, new TypeToken<JsonModel<NoteCommentModel, NoteCommentModel>>() {
                                        }.getType());
                                noteCommentModel = new NoteCommentModel();
                                noteCommentModel.setTime(jsonModel.getJsonObject().getTime());
                                noteCommentModel.setTargetName(targe);
                                noteCommentModel.setCommenterName("123");   //这里需要名字
                                noteCommentModel.setComment(huifuEditText.getText().toString());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        noteItem = noteItems.get(position);
                                        noteItem.getComment().add(noteCommentModel);
                                        ((NoteRespond_Adapter) holder.listView.getAdapter()).notifyDataSetChanged();
                                        huifuEditText.setText("");
                                    }
                                });
                            }
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

