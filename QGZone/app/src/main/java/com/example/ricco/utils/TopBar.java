package com.example.ricco.utils;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.PublicKey;

/**
 * 导航栏
 */
public class TopBar extends RelativeLayout{
    //1.声明导航栏中包含以下子控件
    private Button leftButton;
    private Button rightButton;
    private TextView midTextView;
    private TopBarClickListener listener;

    public interface TopBarClickListener{
        public void LeftClick();
        public void RightClick();
    }

    public void setOnTopBarClickListener(TopBarClickListener listener){
        this.listener = listener;
    }

    public TopBar(Context context) {
        super(context);
        //2.初始化子控件
        leftButton = new Button(context);
        rightButton = new Button(context);
        midTextView = new Button(context);

        //3.将子控件以LayoutParams形式加入到ViewGroup中
        LayoutParams LeftParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        LeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(leftButton, LeftParams);

        LayoutParams RightParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        RightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        addView(rightButton, RightParams);

        LayoutParams MidParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        MidParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(midTextView, MidParams);

        //4.接口回调，实现用户的具体行为
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.LeftClick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.RightClick();
            }
        });
    }

    public void setMidText(String str){

        midTextView.setText(str);

    }

    public void setLeftIsVisable(boolean flag){
        if(flag){
            leftButton.setVisibility(VISIBLE);
        }else{
            leftButton.setVisibility(INVISIBLE);
        }
    }

    public void setRightIsVisable(boolean flag){
        if(flag){
            rightButton.setVisibility(VISIBLE);
        }else{
            rightButton.setVisibility(INVISIBLE);
        }
    }


}
