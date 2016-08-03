package com.example.ricco.qgyun;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    private EditText edi_search;
    private ImageButton imgbtn_search;
    private Button btn_upload;
    private ListView lv;
    private List<Map<String,Object>> dataList;
    private SimpleAdapter sip;

    private final String url = "http://192.168.199.200:8080/Server/ResourceGet?page=";
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        //控件初始化
        edi_search = (EditText) findViewById(R.id.edi_search);
        imgbtn_search = (ImageButton) findViewById(R.id.imgbtn_search);
        btn_upload = (Button)findViewById(R.id.button_upload);
/*        lv = (ListView) findViewById(R.id.list_view);
        Log.e("tag",lv+"");
        //显示ListView列表
        dataList = DataUtil.getData(url+(page++));
        sip = new SimpleAdapter(this,dataList,R.layout.list_item,
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

        //设置搜索按钮点击事件
        imgbtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        //设置上传按钮事件
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//用于指定文件类型
                try {
                    startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), 1);
                } catch (android.content.ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path;
                    if ("content".equalsIgnoreCase(uri.getScheme())) {//获取在某一文件夹下的文件
                        String[] projection = { "_data" };
                        Cursor cursor = null;

                        try {
                            cursor = this.getContentResolver().query(uri, projection,null, null, null);
                            int column_index = cursor.getColumnIndexOrThrow("_data");
                            if (cursor.moveToFirst()) {
                                path = cursor.getString(column_index);
                                Log.e("TAG",path);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    else if ("file".equalsIgnoreCase(uri.getScheme())) {//获取不在某文件夹下的文件
                        path = uri.getPath();
                        Log.e("TAG",path);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
