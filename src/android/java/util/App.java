package com.skytech.wps.util;
import android.app.Application;
import android.content.Context;
import com.lzy.okgo.OkGo;

/**
 * Created by Administrator on 2017/10/10.
 */

public class App extends Application {
    private static Context instance;
    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;

        OkGo.getInstance().init(this);

    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context getInstance(){
        return instance;
    }
}
