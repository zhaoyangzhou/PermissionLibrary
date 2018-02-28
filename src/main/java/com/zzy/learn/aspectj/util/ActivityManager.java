package com.zzy.learn.aspectj.util;

import android.app.Activity;

import java.lang.ref.SoftReference;

/**
 * Package: com.zzy.learn.aspectj.util
 * Class: ActivityManager
 * Description: Activity管理类，记录、获取当前显示的视图
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/18 13:02
 */
public class ActivityManager {
    private static ActivityManager sInstance = new ActivityManager();
    private SoftReference<Activity> sCurrentActivityWeakRef;

    private ActivityManager() {

    }

    public static ActivityManager getInstance() {
        return sInstance;
    }

    /**
     * Method: setCurrentActivity
     * Description: 获取当前显示的视图引用
     * @return  SoftReference<Activity>
     */
    public SoftReference<Activity> getCurrentActivity() {
        return sCurrentActivityWeakRef;
    }

    /**
     * Method: setCurrentActivity
     * Description: 记录当前显示的视图引用
     * @param activity
     * @return  void
     */
    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new SoftReference<Activity>(activity);
    }
}
