package com.example.ricco.others;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.ricco.qgzone.R;

/**
 * PopupWindow
 */
public class mPopupWindow extends PopupWindow {

    private Activity context;

    public mPopupWindow(Activity context, View root, int width, int height, boolean animated) {
        super(root, width, height);
        this.context = context;

        //设置常用属性
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        if (animated) {
            //设置动画效果
            setAnimationStyle(R.style.popupWindowAnim);
        }
        //设置popup消失事件
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

        @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        setBackgroundAlpha(0.3f);
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setBackgroundAlpha(0.3f);
        super.showAtLocation(parent, gravity, x, y);
    }

     private  void setBackgroundAlpha(float alpha) {
         Window window = context.getWindow();
         WindowManager.LayoutParams Ip = window.getAttributes();
         Ip.alpha = alpha;
         window.setAttributes(Ip);
    }
}
