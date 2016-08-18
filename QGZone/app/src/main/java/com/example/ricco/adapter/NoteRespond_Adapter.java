package com.example.ricco.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ricco.entity.NoteCommentModel;
import com.example.ricco.entity.NoteModel;
import com.example.ricco.qgzone.R;

import java.util.List;

/**
 * Created by Mr_Do on 2016/8/13.
 */
public class NoteRespond_Adapter extends ArrayAdapter<NoteCommentModel>{
    private  int resourceId;
    public NoteRespond_Adapter(Context context, int textViewResourceId, List<NoteCommentModel> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        NoteCommentModel respond = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView textView = (TextView) view.findViewById(R.id.respond_text);
        textView.setText(respond.getCommenterName()+" say to "+respond.getTargetName()
                +": "+respond.getComment()+respond.getTime());
        return view;
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
}
