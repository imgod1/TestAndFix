package com.example.gk.testandfix.app;

import android.app.Application;

import com.alipay.euler.andfix.patch.PatchManager;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 项目名称：TestAndFix
 * 类描述：
 * 创建人：gk
 * 创建时间：2016/11/14 15:41
 * 修改人：gk
 * 修改时间：2016/11/14 15:41
 * 修改备注：
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initLib();
    }

    private void initLib() {
        initOkHttpUtils();
        initAndFix();
    }

    private void initOkHttpUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public static PatchManager mPatchManager;

    /**
     * 初始化andfix
     */
    private void initAndFix() {
        // 初始化patch管理类
        mPatchManager = new PatchManager(this);
        // 初始化patch版本
        mPatchManager.init("1.0");
        // 加载已经添加到PatchManager中的patch
        mPatchManager.loadPatch();
    }

    /**
     * 获取管理对象
     *
     * @return
     */
    public static PatchManager getmPatchManager() {
        return mPatchManager;
    }
}
