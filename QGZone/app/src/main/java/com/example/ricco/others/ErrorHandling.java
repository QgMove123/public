package com.example.ricco.others;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.ricco.utils.ToastUtil;


/**
 * @author yason
 * 异常处理
 */
public class ErrorHandling {

    private ProgressDialog progressDialog;

    private boolean isFirstError = true;
    private long Timeout = 0;

    public interface CallBackListener {
        public void onHandle();
    }

    public void handleError(Context context,CallBackListener listener) {
        //只执行一次
        if(isFirstError){
            progressDialog = ProgressDialog.show(context, null, "网络异常...");
            progressDialog.setCancelable(true);
            Timeout = System.currentTimeMillis() + 60000;
            isFirstError = false;
        }
        //尝试重新连接服务器，8秒内
        if (System.currentTimeMillis() <= Timeout) {
            listener.onHandle();
        }else {
            progressDialog.dismiss();
            ToastUtil.showLong(context,"连接服务器失败");
            isFirstError = true;
        }
    }

    public void setSuccess(){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
