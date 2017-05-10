package cctv.cn.jiyuntesting.base;

import android.app.Activity;
import android.app.Application;

/**
 * Created by 张志远 on 2017/5/5.
 */

public class App extends Application{

    public static Activity activity;
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
