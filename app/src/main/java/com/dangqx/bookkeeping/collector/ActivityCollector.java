package com.dangqx.bookkeeping.collector;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dang on 2020-12-21.
 * Time will tell.
 * 管理所有活动的类
 * @description
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finishAll(){
        for (Activity activity : activities) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
