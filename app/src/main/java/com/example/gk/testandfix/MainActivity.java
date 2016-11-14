package com.example.gk.testandfix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gk.testandfix.app.MyApp;
import com.example.gk.testandfix.bean.CheckVersion;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_1;
    private Button btn_2;
    private List<String> list = new ArrayList<>();
    private String helloWorld = "new";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
    }

    private void initEvent() {
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        Log.e("test", helloWorld);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                error();
                Toast.makeText(getApplicationContext(), "btn_1点击了", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_2:
                Toast.makeText(getApplicationContext(), "检查是否有补丁", Toast.LENGTH_SHORT).show();
                checkPatch();
                break;
        }
    }

    //错误的方法
    private void error() {
//        list.get(0);
    }

    public static final String checkUrl = "http://192.168.1.111:8080/andfix/check.json";//检查版本
    public static final String patchName = "newpatch.apatch";//补丁名字

    /**
     * 检查是否有补丁
     */
    private void checkPatch() {
        OkHttpUtils.get().url(checkUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("test", "onError");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("test", response);
                CheckVersion checkVersion = new Gson().fromJson(response, CheckVersion.class);
                if (null != checkVersion) {
                    if (checkVersion.isHavepatch()) {
                        Toast.makeText(getApplicationContext(), "发现补丁,去安装", Toast.LENGTH_SHORT).show();
                        File filePath = new File(getExternalCacheDir().getAbsolutePath(), "andfix");
                        if (!filePath.exists()) {
                            filePath.mkdir();
                        }
                        File patchFile = new File(filePath.getAbsolutePath(), patchName);
                        if (patchFile.exists()) {
                            Log.e("test", "补丁文件已经存在,删除");
                            patchFile.delete();
                        }
                        downloadPatch(checkVersion.getPath(), filePath.getAbsolutePath(), patchName);
                    } else {
                        Toast.makeText(getApplicationContext(), "没有补丁", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 下载补丁
     *
     * @param url      补丁路径
     * @param filePath 补丁存放路径
     * @param fileName 补丁名字
     */
    private void downloadPatch(String url, String filePath, String fileName) {
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(filePath, fileName)//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getApplicationContext(), "补丁下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Toast.makeText(getApplicationContext(), "补丁下载成功,安装ing", Toast.LENGTH_SHORT).show();
                        if (null != response && response.exists()) {
                            try {
                                MyApp.getmPatchManager().addPatch(response.getAbsolutePath());
                                Toast.makeText(getApplicationContext(), "补丁安装成功", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "补丁安装失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
