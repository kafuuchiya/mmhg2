package com.example.mmhg2;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public  class Js2Android {
    Activity activity;

    public Js2Android(Activity a){
        this.activity = a;
    }

    @JavascriptInterface
    public void toNext(String url) {
        Log.d("mmhg2-log", "toNext: " + url);
        // JS 与 Android 之间的交互
        Intent intent = new Intent(activity, ComicActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    @JavascriptInterface
    public void toErr(String err) {
        // 打印JS运行时候的错误
        Log.d("mmhg2-log", "toErr: " + err);
    }

    public String getJsStr(String fname) {
        // 获取Android本地js文件并以String格式返回

        try {
            InputStream in = activity.getAssets().open(fname);
            byte[] buff = new byte[1024];
            ByteArrayOutputStream fromFile = new ByteArrayOutputStream();
            do {
                int numRead = in.read(buff);
                if (numRead <= 0) {
                    break;
                }
                fromFile.write(buff, 0, numRead);
            } while (true);
            String s = fromFile.toString();
            in.close();
            fromFile.close();
            return s;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
