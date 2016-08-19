package com.example.ricco.qgzone;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ricco.constant.Constant;
import com.example.ricco.others.CircleImageVIew;
import com.example.ricco.others.ImageLoader;
import com.example.ricco.others.InfoItem;
import com.example.ricco.others.PassItem;
import com.example.ricco.others.TopBar;
import com.example.ricco.utils.EditProblemItem;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.SelectPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改个人信息
 * Created by chenyi on 2016/8/15.
 */
public class EditInfoActivity extends BaseActivity implements View.OnClickListener {

    private List<InfoItem> infoArry = new ArrayList<InfoItem>();
    private CircleImageVIew userPic;
    private TopBar tb;
    private TextView nickname;
    private TextView problem;
    private TextView password;
    private SelectPopupWindow popupWindow;
    String path;
    Map<String, String> model = new HashMap<>();

    /**
     * 启动EditInfoActivity
     * @param context
     * @param message
     */
    public static void actionStart(Context context, String message) {
        Intent intent = new Intent(context, EditInfoActivity.class);
        intent.putExtra("message", message);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info_layout);

        //初始化控件
        initInfo();
        userPic = (CircleImageVIew) findViewById(R.id.user_pic);
        tb = (TopBar) findViewById(R.id.topBar);
        nickname = (TextView) findViewById(R.id.nickname);
        password = (TextView) findViewById(R.id.password);
        problem = (TextView) findViewById(R.id.problem);
        userPic.setOnClickListener(this);
        nickname.setOnClickListener(this);
        password.setOnClickListener(this);
        problem.setOnClickListener(this);

        //从个人信息中获取信息并将信息存入控件中
        setMessage();

        //设置控件的监听
        setListener();
    }

    public void setMessage() {
        Intent intent = getIntent();
        try {

            //从intent中获取message
            String message = intent.getStringExtra("message");
            JSONObject dataJson = new JSONObject(message);

            //将message存入view中
            for (InfoItem ii : infoArry) {
                ii.setEditText(dataJson.getString(ii.getTextView()));
                if (!ii.getTextView().equals("account")) {
                    ii.setOnClickListener(this);
                }
            }
            nickname.setText(dataJson.getString("userName"));
            ImageLoader.getInstance(1).loadImage(Constant.civUrl + dataJson.getString("userImage"),
                    userPic, false);

            //更新message的model
            model = JsonUtil.toModel(message, HashMap.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setListener() {

        //头像的监听
        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化SelectPicPopupWindow
                popupWindow = new SelectPopupWindow(EditInfoActivity.this, itemsOnClick);
                popupWindow.initBtnName("拍照", "相册");
                //显示窗口   设置layout在PopupWindow中显示的位置
                popupWindow.showAtLocation(findViewById(R.id.edit_info_activity), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        //设置TopBar的监听
        tb.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void LeftClick(View view) {
                finish();
            }

            @Override
            public void RightClick(View view) {
                for(InfoItem ii: infoArry) {
                    model.put(ii.getTextView(), ii.getEditText());
                }
                HttpUtil.Get(Constant.Account.MessageChange+"?jsonObject="+JsonUtil.toJson(model), callBackListener);
            }
        });
    }

    /**
     * 为弹出窗口实现监听类
     */
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            popupWindow.dismiss();
            path = null;

            Intent intent;
            switch (v.getId()) {
                case R.id.button:
                    // 拍照
                    //设置图片的保存路径,作为全局变量
                    path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/image_pic.jpg";
                    File temp = new File(path);
                    //获取文件的Uri
                    Uri imageFileUri = Uri.fromFile(temp);
                    // 跳转到相机Activity
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 告诉相机拍摄完毕输出图片到指定的Uri
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                    startActivityForResult(intent, 1);
                    break;
                case R.id.button2:
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, 2);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<String> paths = new ArrayList<>();
        switch(requestCode){
            case 1:
                paths.add(path);
                Log.e("onActivityResult: paths", paths+"");
                HttpUtil.Post(Constant.Account.UserUploadImage, null, paths, callBackListener);
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    // 获得图片的uri
                    Uri originalUri = data.getData();

                    //获取图片的路径：
                    String[] proj = { MediaStore.Images.Media.DATA };

                    //android多媒体数据库的封装接口
                    @SuppressWarnings("deprecation")
                    Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                    //获得用户选择的图片的索引值
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                    cursor.moveToFirst();
                    // 最后根据索引值获取图片路径
                    path = cursor.getString(column_index);
                    paths.add(path);
                    Log.e("onActivityResult: paths", paths+"");
                    HttpUtil.Post(Constant.Account.UserUploadImage, null, paths, callBackListener);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.problem:
                NewPassDialog();
                break;
            case R.id.password:
                problemdialog();
                break;
            case R.id.nickname:
                editdialog("昵称", "[a-z0-9A-Z一-龥]{1,15}", R.id.nickname);
                break;
            case R.id.sex:
                sexdialog("性别", R.id.sex);
                break;
            case R.id.birth:
                datedialog("生日", R.id.birth);
                break;
            case R.id.place:
                editdialog("地址", "[a-z0-9A-Z一-龥]{1,15}", R.id.place);
                break;
            case R.id.phone:
                editdialog("电话", "[1][358]\\d{9}", R.id.phone);
                break;
            case R.id.email:
                editdialog("邮箱", "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]"+
                        "*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$", R.id.email);
                break;
        }
    }

    //设置对话框用于输入新密码
    private void NewPassDialog() {
        //对话框的自定义样式
        final PassItem passItem = new PassItem(EditInfoActivity.this);
        passItem.setPass1("旧密码");
        passItem.setPass2("新密码");
        //添加对话框
        AlertDialog.Builder show = new AlertDialog.Builder(EditInfoActivity.this);
        show.setTitle("修改密码");
        show.setView(passItem);
        show.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, Object> jsonObjiect = new HashMap<>();
                jsonObjiect.put("oldPassword", passItem.getPass1());
                jsonObjiect.put("newPassword", passItem.getPass2());
                HttpUtil.Get(Constant.Account.UserChangePassword+"?jsonObject="+ JsonUtil.toJson(jsonObjiect), callBackListener);
            }
        });
        show.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                canCloseDialog(dialog, true);
            }
        });
        show.create().show();
    }

    //设置对话框用于输入新密保
    public void problemdialog () {
        //对话框的自定义样式
        final EditProblemItem dialogItem = new EditProblemItem(EditInfoActivity.this);
        //添加对话框
        AlertDialog.Builder show = new AlertDialog.Builder(EditInfoActivity.this);
        show.setTitle("修改密保");
        show.setView(dialogItem);
        show.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, Object> jsonObjiect = new HashMap<>();
                jsonObjiect.put("oldSecretId", dialogItem.getOldId());
                jsonObjiect.put("oldAnswer", dialogItem.getOldAnswer());
                jsonObjiect.put("newSecretId", dialogItem.getNewId());
                jsonObjiect.put("newAnswer", dialogItem.getNewAnswer());
                HttpUtil.Get(Constant.Account.UserChangeSecret+"?jsonObject="+JsonUtil.toJson(jsonObjiect), callBackListener);
            }
        });
        show.setNegativeButton("取消", null);
        show.create().show();
    }

    //设置对话框用于设置性别
    public void sexdialog (String title, final int id) {
        final String items[]={"男","女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("新"+title); //设置标题
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((InfoItem)findViewById(id)).setEditText(items[which]);
            }
        });
        builder.setPositiveButton("确定", null);
        builder.create().show();
    }

    //设置对话框用于输入新生日
    public void datedialog (String title, final int id) {
        //用来获取日期和时间的
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditInfoActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ((InfoItem)findViewById(id)).setEditText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setTitle("新"+title);
        datePickerDialog.show();
    }

    //设置对话框用于输入新信息
    public void editdialog (final String title, final String pattern, final int id) {
        final EditText et = new EditText(this);
        //添加对话框
        AlertDialog.Builder show = new AlertDialog.Builder(this);
        show.setTitle("新"+title);
        show.setView(et);
        show.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = et.getText().toString();
                if (str.matches(pattern)) {
                    canCloseDialog(dialog, true);
                    if(id != R.id.nickname) {
                        ((InfoItem)findViewById(id)).setEditText(str);
                    } else {
                        ((TextView)findViewById(id)).setText(str);
                    }

                } else {
                    canCloseDialog(dialog, false);
                    et.setError(title+"格式错误或太长");
                    View focusView = et;
                    focusView.requestFocus();
                }
            }
        });
        show.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                canCloseDialog(dialog, true);
            }
        });
        show.create().show();
    }

    private void canCloseDialog(DialogInterface dialogInterface, boolean close) {
        try {
            Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialogInterface, close);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化个人信息的表格
     */
    public void initInfo() {
        InfoItem account = (InfoItem) findViewById(R.id.account);
        account.setTextView("帐号");
        infoArry.add(account);
        InfoItem sex = (InfoItem) findViewById(R.id.sex);
        sex.setTextView("性别");
        infoArry.add(sex);
        InfoItem birthday = (InfoItem) findViewById(R.id.birth);
        birthday.setTextView("生日");
        infoArry.add(birthday);
        InfoItem place = (InfoItem) findViewById(R.id.place);
        place.setTextView("地址");
        infoArry.add(place);
        InfoItem phone = (InfoItem) findViewById(R.id.phone);
        phone.setTextView("电话");
        infoArry.add(phone);
        InfoItem email = (InfoItem) findViewById(R.id.email);
        email.setTextView("邮箱");
        infoArry.add(email);
    }

    private HttpUtil.CallBackListener callBackListener = new HttpUtil.CallBackListener() {
        @Override
        public void OnFinish(String result) {
            Message msg = new Message();
            try {
                //通过JSONObject取出服务器传回的状态和信息
                JSONObject dataJson = new JSONObject(result);
                Log.e("OnFinish: result", result);
                msg.what = Integer.valueOf(dataJson.getString("state"));
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                mHandler.sendMessage(msg);
            }
        }

        @Override
        public void OnError(Exception e) {

        }
    };

    /**
     * 获取个人信息
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 171:
                    ImageLoader.getInstance(1).loadImage(path, userPic, true);
                case 131:
                case 141:
                case 151:
                    Toast.makeText(EditInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    break;
                case 132:
                case 142:
                case 152:
                case 172:
                    Toast.makeText(EditInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    break;
                case 173:
                    Toast.makeText(EditInfoActivity.this, "文件格式错误", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    };
}
