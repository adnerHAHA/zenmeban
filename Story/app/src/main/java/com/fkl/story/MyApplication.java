package com.fkl.story;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by adner on 2016/10/25.
 */
public class MyApplication extends Application {
    private List<Activity> mList=new LinkedList();
    private static MyApplication instace;

    private MyApplication() {
    }
    public synchronized static MyApplication getInstace(){
        if (null==instace){
            instace=new MyApplication();
        }
        return instace;
    }
    public void addActivity(Activity activity){
        mList.add(activity);
    }
    public void exit(){
        try {
            for (Activity activity:mList) {
                if (null!=activity){
                    activity.finish();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.exit(0);
        }
    }
    public void onLowMemory(){
        super.onLowMemory();
        System.gc();
    }
}
