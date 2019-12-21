package com.hz.rxbus.demo;

import android.app.Application;

import com.didichuxing.doraemonkit.DoraemonKit;

/**
 * 描述：
 *
 * @author fzJiang
 * @date 2019-12-18 9:38.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化DoraemonKit
        DoraemonKit.install(this);
    }
}
