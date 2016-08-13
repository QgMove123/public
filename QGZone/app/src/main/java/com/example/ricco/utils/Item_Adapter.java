package com.example.ricco.utils;

/**
 * Created by Mr_Do on 2016/8/10.
 * 用于加载首页的说说
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ricco.entity.TwitterCommentModel;
import com.example.ricco.entity.TwitterModel;
import com.example.ricco.qgzone.R;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mr_Do on 2016/8/8.
 */
public class Item_Adapter extends BaseAdapter implements View.OnClickListener {
    private List<Item_Adapter_View> holders = new ArrayList<Item_Adapter_View>();
    private Item_Adapter_View holder = null;//自定义的一个类用来缓存convertview
    private LayoutInflater inflater;
    private ArrayList<TwitterModel> items;
    private Context context;
    private int resourceId;

    public Item_Adapter(ArrayList<TwitterModel> items, Context context){
        this.items = items;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public int getCount() {
        //在实际应用中，此处的返回值是由从数据库中查询出来的数据的总条数
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView ==null) {
            holder = new Item_Adapter_View();
            convertView =inflater.inflate(R.layout.dongtai_say_item, null);
            holder.wordTextView =(TextView)convertView.findViewById(R.id.say_textview);//说说内容
            holder.nameTextView =(TextView)convertView.findViewById(R.id.dongtai_name_text); //姓名文本框
            holder.timeTextView = (TextView) convertView.findViewById(R.id.dongtai_time_text); //时间文本框
            holder.editText = (EditText) convertView.findViewById(R.id.respond_EditText);//用于回复留言或说说的编辑框

            holder.listView =(ListView) convertView.findViewById(R.id.respond_listview);//评论列表
            holder.picGridView=(GridView) convertView.findViewById(R.id.dongtai_shuos_gridview);//图片列表


            //下面是三个小图标的按钮
            holder.pinglunImageBtn = (ImageButton) convertView.findViewById(R.id.dongtai_pinglun);//评论按钮（首页）
            holder.pinglunImageBtn.setTag(position+"");
            holder.shanchuImageBtn = (ImageButton) convertView.findViewById(R.id.dongtai_shangchu);//删除按钮
            holder.shanchuImageBtn.setTag(position+"");
            holder.zanImageBtn = (ImageButton)convertView. findViewById(R.id.dongtai_zan);//点赞按钮(首页)
            holder.zanImageBtn.setTag(position+"");

            //下面是两个留言的回复和删除按钮
            holder.huifuBtn = (Button)convertView.findViewById(R.id.liuyan_huifu_btn);//留言的回复按钮
            holder.huifuBtn.setTag(position+"");
            holder.shanchuBtn = (Button)convertView.findViewById(R.id.liuyan_shanchu_btn);//留言的删除按钮
            holder.shanchuBtn.setTag(position+"");

            holder.huifuImageButton = (ImageButton)convertView.findViewById(R.id.respond_ImageButton);//回复发送按钮
            holder.huifuImageButton.setTag(position+"");

            holders.add(holder);
            convertView.setTag(holder);
        }else {
            holder = ( Item_Adapter_View)convertView.getTag();
        }
        TwitterModel item = items.get(position);
        holder.wordTextView.setText(item.getTwitterWord());
        holder.timeTextView.setText(item.getTime());
        holder.nameTextView.setText(item.getTalkerName());
        Respond_Adapter adapter = new Respond_Adapter(context,
                R.layout.dongtai_respond_item, item.getComment());//说说回复的数据绑定
        holder.listView.setAdapter(adapter);

        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //说说列表的点击事件
            }
        });
        holder.picGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //图片列表的点击事件
            }
        });
        return convertView;
    }
    public void onClick(View view){
        int index = Integer.parseInt(view.getTag().toString());
        holder = holders.get(index);
        switch (view.getId()){
            case R.id.dongtai_pinglun:{
                //评论按钮的功能，焦点跳转到编辑框上,弹出软键盘
                holder.editText.requestFocus();
                break;
            }
            case R.id.dongtai_shangchu:{
                //删除说说
            }
            case  R.id.dongtai_zan:{
                //给说说点赞
            }
            case R.id.liuyan_huifu_btn:{
                //留言板中留言的回复
            }
            case R.id.liuyan_shanchu_btn:{
                //留言板中留言的删除
            }
            case R.id.respond_ImageButton:{
                //发送留言
            }
            default: break;
        }
    }
}

