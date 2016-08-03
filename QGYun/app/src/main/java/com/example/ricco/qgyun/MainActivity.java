package com.example.ricco.qgyun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.ricco.util.DataUtil;
import com.example.ricco.util.ListAdapter;

import java.util.List;
import java.util.Map;

/**
 * @author yason
 * 主界面
 */
public class MainActivity extends Activity {
    private EditText edi_search;
    private ImageButton imgbtn_clear;
    private ImageButton imgbtn_back;
    private Button btn_info;
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
        imgbtn_clear = (ImageButton) findViewById(R.id.imgbtn_clear);
        imgbtn_back = (ImageButton) findViewById(R.id.imgbtn_back);
        btn_info = (Button) findViewById(R.id.person_info);
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
                //将文件类型、文件id传给下载页面
                Map<String,Object> map = (Map<String,Object>)sip.getItem(position);
                ItemInfoActivity.actionStart(MainActivity.this,
                        map.get("ResourceName").toString(), map.get("ResourceId").toString());

                //此打开页面方式作废
//                intent.putExtra("file", (Serializable) map);
//                startActivity(intent);

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
                if(s.length()>0) {

                    imgbtn_clear.setVisibility(View.VISIBLE);
                } else {
                    imgbtn_clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //设置清除按钮点击时间
        imgbtn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edi_search.setText("");
            }
        });

        //设置返回按钮监听事件
        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //设置个人中心按钮监听事件
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PersonInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
