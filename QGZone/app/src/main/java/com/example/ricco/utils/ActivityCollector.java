package com.example.ricco.utils;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();
    //添加活动进入活动管理器
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }
    //移出活动
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }
    //退出程序，结束所有活动
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
