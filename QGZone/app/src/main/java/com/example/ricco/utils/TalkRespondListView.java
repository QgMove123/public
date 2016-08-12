package com.example.ricco.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Mr_Do on 2016/8/9.
 * 重写listview用于避免回复不能显示
 */
public class TalkRespondListView extends ListView {
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
                super.onMeasure(widthMeasureSpec,expandSpec);
    }

    public TalkRespondListView(Context context) {
        super(context);
    }

    public TalkRespondListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TalkRespondListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
