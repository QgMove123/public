//package com.example.ricco.qgzone;
//
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.example.ricco.entity.MessageModel;
//import com.example.ricco.utils.DialogItem;
//import com.example.ricco.utils.EditProblemItem;
//import com.example.ricco.utils.InfoItem;
//import com.example.ricco.utils.JsonUtil;
//import com.example.ricco.others.TopBar;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 修改个人信息
// * Created by chenyi on 2016/8/15.
// */
//public class EditInfoActivity extends BaseActivity implements View.OnClickListener {
//
//    private List<InfoItem> infoArry = new ArrayList<InfoItem>();
//    private TopBar tb;
//    private TextView nickname;
//    private TextView problem;
//    private TextView password;
//    private MessageModel mm;
//    Map<String, Object> model = new HashMap<>();
//
//    public static void actionStart(Context context, String message) {
//        Intent intent = new Intent(context, EditInfoActivity.class);
//        intent.putExtra("message", message);
//        context.startActivity(intent);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.edit_info_layout);
//
//        initInfo();
//        tb = (TopBar) findViewById(R.id.topBar);
//        nickname = (TextView) findViewById(R.id.nickname);
//        password = (TextView) findViewById(R.id.password);
//        problem = (TextView) findViewById(R.id.problem);
//        nickname.setOnClickListener(this);
//        password.setOnClickListener(this);
//        problem.setOnClickListener(this);
//
//        Intent intent = getIntent();
//        try {
//            String message = intent.getStringExtra("message");
//            JSONObject dataJson = new JSONObject(message);
//            for (InfoItem ii: infoArry) {
//                ii.setEditText(dataJson.getString(ii.getTextView()));
//                if(!ii.getTextView().equals("account")) {
//                    ii.setOnClickListener(this);
//                }
//            }
////            model = JsonUtil.toObject(message, HashMap<>.class);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        tb.setOnTopBarClickListener(new TopBar.TopBarClickListener() {
//            @Override
//            public void LeftClick(View view) {
//                finish();
//            }
//
//            @Override
//            public void RightClick(View view) {
//
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.problem:
//                NewPassDialog();
//                break;
//            case R.id.password:
//                problemdialog();
//                break;
//            case R.id.nickname:
//                editdialog("昵称", "[a-z0-9A-Z一-龥]{1,15}", R.id.nickname);
//                break;
//            case R.id.sex:
//                sexdialog("性别", R.id.sex);
//                break;
//            case R.id.birth:
//                datedialog("生日", R.id.birth);
//                break;
//            case R.id.place:
//                editdialog("地址", "[u4e00-u9fa5]{0,}$", R.id.place);
//                break;
//            case R.id.phone:
//                editdialog("电话", "^[0-9]*${11}", R.id.phone);
//                break;
//            case R.id.email:
//                editdialog("邮箱", "^w+([-+.]w+)*@w+([-.]w+)*.w+([-.]w+)*$", R.id.email);
//                break;
//        }
//    }
//
//    //设置对话框用于输入新密码
//    private void NewPassDialog() {
//        //对话框的自定义样式
//        final DialogItem dialogItem = new DialogItem(EditInfoActivity.this);
//        dialogItem.setPass1("旧密码");
//        dialogItem.setPass1("新密码");
//        //添加对话框
//        AlertDialog.Builder show = new AlertDialog.Builder(EditInfoActivity.this);
//        show.setTitle("修改密码");
//        show.setView(dialogItem);
//        show.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Map<String, Object> jsonObjiect = new HashMap<>();
//                jsonObjiect.put("oldPassword", dialogItem.getPass1());
//                jsonObjiect.put("newPassword", dialogItem.getPass2());
//            }
//        });
//        show.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                canCloseDialog(dialog, true);
//            }
//        });
//        show.create().show();
//    }
//
//    public void problemdialog () {
//        //对话框的自定义样式
//        final EditProblemItem dialogItem = new EditProblemItem(EditInfoActivity.this);
//        //添加对话框
//        AlertDialog.Builder show = new AlertDialog.Builder(EditInfoActivity.this);
//        show.setTitle("修改密保");
//        show.setView(dialogItem);
//        show.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Map<String, Object> jsonObjiect = new HashMap<>();
//                jsonObjiect.put("oldSecretId", dialogItem.getOldId());
//                jsonObjiect.put("oldAnswer", dialogItem.getOldAnswer());
//                jsonObjiect.put("newSecretId", dialogItem.getNewId());
//                jsonObjiect.put("newAnswer", dialogItem.getNewAnswer());
//            }
//        });
//        show.setNegativeButton("取消", null);
//        show.create().show();
//    }
//
//    public void sexdialog (String title, final int id) {
//        final String items[]={"男","女"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
//        builder.setTitle("新"+title); //设置标题
//        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ((InfoItem)findViewById(id)).setEditText(items[which]);
//            }
//        });
//        builder.setPositiveButton("取消", null);
//        builder.create().show();
//    }
//
//    public void datedialog (String title, final int id) {
//        DatePickerDialog datePickerDialog = new DatePickerDialog(EditInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                ((InfoItem)findViewById(id)).setEditText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//            }
//        }, 2015, 1, 1);
//
//        datePickerDialog.setTitle("新"+title);
//        datePickerDialog.show();
//    }
//
//    public void editdialog (final String title, final String pattern, final int id) {
//        final EditText et = new EditText(this);
//        //添加对话框
//        AlertDialog.Builder show = new AlertDialog.Builder(this);
//        show.setTitle("新"+title);
//        show.setView(et);
//        show.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String str = et.getText().toString();
//                if (str.matches(pattern)) {
//                    canCloseDialog(dialog, true);
//                    ((InfoItem)findViewById(id)).setEditText(str);
//                } else {
//                    canCloseDialog(dialog, false);
//                    et.setError(title+"格式错误或太长");
//                    View focusView = et;
//                    focusView.requestFocus();
//                }
//            }
//        });
//        show.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                canCloseDialog(dialog, true);
//            }
//        });
//        show.create().show();
//    }
//
//    private void canCloseDialog(DialogInterface dialogInterface, boolean close) {
//        try {
//            Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
//            field.setAccessible(true);
//            field.set(dialogInterface, close);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 初始化个人信息的表格
//     */
//    public void initInfo() {
//        InfoItem account = (InfoItem) findViewById(R.id.account);
//        account.setTextView("帐号");
//        infoArry.add(account);
//        InfoItem sex = (InfoItem) findViewById(R.id.sex);
//        sex.setTextView("性别");
//        infoArry.add(sex);
//        InfoItem birthday = (InfoItem) findViewById(R.id.birth);
//        birthday.setTextView("生日");
//        infoArry.add(birthday);
//        InfoItem place = (InfoItem) findViewById(R.id.place);
//        place.setTextView("地址");
//        infoArry.add(place);
//        InfoItem phone = (InfoItem) findViewById(R.id.phone);
//        phone.setTextView("电话");
//        infoArry.add(phone);
//        InfoItem email = (InfoItem) findViewById(R.id.email);
//        email.setTextView("邮箱");
//        infoArry.add(email);
//    }
//}
