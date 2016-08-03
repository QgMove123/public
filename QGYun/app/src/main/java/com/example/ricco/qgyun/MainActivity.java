package com.example.ricco.qgyun;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;

import com.example.ricco.util.DataUtil;
import com.example.ricco.util.ListAdapter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author yason
 * 主界面
 */
public class MainActivity extends Activity {
    private EditText edi_search;
    private ListView lv;
    private List<Map<String,Object>> dataList;
    private ListAdapter sip;

    private final String url = "http://192.168.199.200:8080/Server/ResourceGet?page=";
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        //控件初始化
        edi_search = (EditText) findViewById(R.id.edi_search);
        lv = (ListView) findViewById(R.id.list_view);

        //显示ListView列表
        dataList = DataUtil.getData(url+(page++));
        sip = new ListAdapter(this,dataList,R.layout.list_item,
                new String[]{"ResourceType","ResourceName","ResourceUploadTime","uploader"},
                new int[]{R.id.image_type,R.id.file_name,R.id.file_time,R.id.file_man});
        lv.setAdapter(sip);

        //设置列表项点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ItemInfoActivity.class);
                //将用户名字、文件类型、文件id传给下载页面
                Map<String,Object> map = (Map<String,Object>)sip.getItem(position);
                intent.putExtra("file", (Serializable) map);
                startActivity(intent);
            }
        });
        //设置滚动监听事件
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
             @Override
             public void onScrollStateChanged(AbsListView view, int scrollState) {
                 if (scrollState == SCROLL_STATE_IDLE) {
                     if (view.getLastVisiblePosition() == view.getCount() - 1) {
                         dataList = DataUtil.getData(url+(page++));
                         sip.notifyDataSetChanged();
                     }
                 }
             }

             @Override
             public void onScroll(AbsListView absListView, int i, int i1, int i2) {

             }
        });

        //给EditText设置内容更改监听
        edi_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sip.getFilter().filter(s);
//                if(s.length()>0) {
//                    button_clear.setVisibility(View.VISIBLE);
//                } else {
//                    button_clear.setVisibility(View.GONE);
//                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}
