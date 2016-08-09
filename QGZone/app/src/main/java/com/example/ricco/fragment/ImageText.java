package com.example.ricco.fragment;

/**
 * Created by Ricco on 2016/8/9.
 */
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ricco.constant.Constant;
import com.example.ricco.qgzone.R;


public class ImageText extends LinearLayout{
    private Context mContext = null;
    private ImageView mImageView = null;
    private TextView mTextView = null;
    private final static int DEFAULT_IMAGE_WIDTH = 64;
    private final static int DEFAULT_IMAGE_HEIGHT = 64;
    private int CHECKED_COLOR = Color.BLACK; //选中黑色
    private int UNCHECKED_COLOR = Color.GRAY;   //自然灰色

    public ImageText(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    public ImageText(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parentView = inflater.inflate(R.layout.image_text_layout, this, true);
        mImageView = (ImageView)findViewById(R.id.image_iamge_text);
        mTextView = (TextView)findViewById(R.id.text_iamge_text);
    }

    public void setImage(int id){
        if(mImageView != null){
            mImageView.setImageResource(id);
            setImageSize(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
        }
    }

    public void setText(String str){
        if (str.equals("HIDE")) {
            mTextView.setVisibility(View.GONE);
        } else if (mTextView != null){
            mTextView.setText(str);
            mTextView.setTextColor(UNCHECKED_COLOR);
        }
    }

    //用于限制是否允许点击事件向下传递，这里返回true表示，点击事件在这里就会被消化，不会传递
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return true;
    }

    private void setImageSize(int w, int h){
        if(mImageView != null){
            ViewGroup.LayoutParams params = mImageView.getLayoutParams();
            params.width = w;
            params.height = h;
            mImageView.setLayoutParams(params);
        }
    }

    public void setChecked(int itemID){
        if(mTextView != null){
            mTextView.setTextColor(CHECKED_COLOR);
        }
        int checkDrawableId = -1;
        switch (itemID){
            case Constant.BTN_FLAG_DONGTAI:
//                checkDrawableId = R.drawable.message_selected;
                checkDrawableId = R.mipmap.ic_launcher;
                break;
            case Constant.BTN_FLAG_ABOUTME:
//                checkDrawableId = R.drawable.contacts_selected;
                checkDrawableId = R.mipmap.ic_launcher;
                break;
            case Constant.BTN_FLAG_SEND:
//                checkDrawableId = R.drawable.news_selected;
                checkDrawableId = R.mipmap.ic_launcher;
                break;
            case Constant.BTN_FLAG_ZONE:
//                checkDrawableId = R.drawable.setting_selected;
                checkDrawableId = R.mipmap.ic_launcher;
                break;
            case Constant.BTN_FLAG_FRIEND:
                checkDrawableId = R.mipmap.ic_launcher;
                break;
            default:break;
        }

        if(mImageView != null){
            mImageView.setImageResource(checkDrawableId);
        }
    }
}
