package com.example.ricco.others;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ricco.qgzone.R;

/**
 * TopBar
 */
public class TopBar extends RelativeLayout{
    //1.声明导航栏中包含以下子控件
    private TextView midTextView;
    private Button leftButton;
    private Button rightButton;
    private TopBarClickListener listener;

    public interface TopBarClickListener{
        public void LeftClick(View view);
        public void RightClick(View view);
    }

    public void setOnTopBarClickListener(TopBarClickListener listener){
        this.listener = listener;
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context,attrs);
        //2.初始化子控件
        midTextView = new TextView(context);
        leftButton = new Button(context);
        rightButton = new Button(context);

        //3.给子控件添加自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);

        midTextView.setText(ta.getString(R.styleable.TopBar_title));

        leftButton.setText(ta.getString(R.styleable.TopBar_leftText));
        leftButton.setBackground(ta.getDrawable(R.styleable.TopBar_leftBackground));
        if(ta.getBoolean(R.styleable.TopBar_leftIsVisability,true)){
            leftButton.setVisibility(VISIBLE);
        }else{
            leftButton.setVisibility(INVISIBLE);
        }

        rightButton.setText(ta.getString(R.styleable.TopBar_rightText));
        rightButton.setBackground(ta.getDrawable(R.styleable.TopBar_rightBackground));
        if(ta.getBoolean(R.styleable.TopBar_RightIsVisability,true)){
            rightButton.setVisibility(VISIBLE);
        }else{
            rightButton.setVisibility(INVISIBLE);
        }

        ta.recycle();

        //4.给子控件添加固定样式(未完善)
        midTextView.setGravity(Gravity.CENTER);
        this.setBackgroundColor(Color.TRANSPARENT);

        //5.将子控件以LayoutParams形式加入到ViewGroup中
        LayoutParams MidParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        MidParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(midTextView, MidParams);

        LayoutParams LeftParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        LeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        LeftParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        addView(leftButton, LeftParams);

        LayoutParams RightParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        RightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        LeftParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        addView(rightButton, RightParams);

        //6.接口回调，实现用户的具体行为
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.LeftClick(v);
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.RightClick(v);
            }
        });
    }

    public void setTitle(String title) {
        midTextView.setText(title);
    }

    public void setLeftText(String leftText) {
        leftButton.setText(leftText);
    }

    public void setRightText(String rightText) {
        rightButton.setText(rightText);
    }


    //左按钮隐藏或显示
    public void setLeftIsVisable(boolean flag){
        if(flag){
            leftButton.setVisibility(VISIBLE);
        }else{
            leftButton.setVisibility(INVISIBLE);
        }
    }

    //右按钮隐藏或显示
    public void setRightIsVisable(boolean flag){
        if(flag){
            rightButton.setVisibility(VISIBLE);
        }else{
            rightButton.setVisibility(INVISIBLE);
        }
    }




}
