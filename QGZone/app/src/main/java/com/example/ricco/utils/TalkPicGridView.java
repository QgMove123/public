package com.example.ricco.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Mr_Do on 2016/8/12.
 * 重写GridView，避免说说的图片不能显示
 */
public class TalkPicGridView extends GridView {
    public TalkPicGridView(Context context){
        super(context);
    }
    public TalkPicGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
