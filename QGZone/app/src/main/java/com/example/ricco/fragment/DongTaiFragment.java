package com.example.ricco.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import com.example.ricco.constant.Constant;
import com.example.ricco.entity.TwitterModel;
import com.example.ricco.qgzone.MainActivity;
import com.example.ricco.qgzone.MsgBoardActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.qgzone.TalkPubActivity;
import com.example.ricco.utils.*;
import java.util.ArrayList;

/**
 * 首页的说说列表
 * Created by Ricco on 2016/8/9.
 */
public class DongTaiFragment extends BaseFragment {

    private ArrayList<TwitterModel> itemList = new ArrayList<TwitterModel>();
    private ArrayList<Respond> responds1 =new ArrayList<Respond>();
    private ArrayList<Respond> responds2 =new ArrayList<Respond>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_dongtai, container, false);
        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_DONGTAI;;
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //还需要数据的加载
        //
        Item_Adapter adapter = new Item_Adapter(itemList, getActivity());
        ListView lv = (ListView)getActivity().findViewById(R.id.lv);
        View header = View.inflate(getActivity(), R.layout.dongtai_title_layout, null);//头部内容
        lv.setAdapter(null);
        lv.addHeaderView(header, null, true);//添加头部
        lv.setAdapter(adapter);
        ImageButton shuoShuo = (ImageButton) header.findViewById(R.id.dongtai_say);
        ImageButton liuYan = (ImageButton) header.findViewById(R.id.dongtai_liuyan);
        ImageButton picture = (ImageButton) header.findViewById(R.id.dongtai_picture);
        shuoShuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首页的说说图片按钮
                Intent intent = new Intent(getActivity(), TalkPubActivity.class);
                startActivity(intent);
            }
        });
        liuYan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首页的留言按钮
                Intent intent = new Intent(getActivity(), MsgBoardActivity.class);
                startActivity(intent);
            }
        });
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首页的相册按钮

            }
        });
        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_DONGTAI;
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        View say_Item = LayoutInflater.from(getActivity()).inflate(R.layout.dongtai_say_item, null);
        say_Item.requestFocus();
        say_Item.findViewById(R.id.respond_EditText).requestFocus();
        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_DONGTAI;
    }


}