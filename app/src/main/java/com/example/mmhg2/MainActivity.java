package com.example.mmhg2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterInputStream;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    WebView webView;
    final static String targetUrl = "https://m.manhuagui.com/";

    @SuppressLint("AddJavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Js2Android jsa = new Js2Android(this);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        webView = (WebView) findViewById(R.id.web_view);

        webView.loadUrl(targetUrl);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(jsa, "toAndroid");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("mmhg2-log", "onPageFinished: Main page");
                // loadUrl没有安卓版本限制，但是效率慢，返回值处理比较麻烦
                if (Build.VERSION.SDK_INT < 19) {
                    // 隐藏广告和对一些元素进行修改
                    String js = getResources().getString(R.string.js_rm_ad);
                    js += getResources().getString(R.string.js_conf);
                    view.loadUrl("javascript:" + js);
                }
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Log.d("mmhg2-log", "onPageStarted: Main page");
                // evaluateJavascript需要安卓4.4（即API等级要19以上）。效率更高
                if (Build.VERSION.SDK_INT >= 19) {
                    String js = jsa.getJsStr("mainJs.js");
                    // 这里不需要回调值，所以null了
                    webView.evaluateJavascript(js, null);
                }
                progressBar.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        String url_japan = "https://m.manhuagui.com/list/japan/update.html";
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.getUrl().equals(url_japan)) {
            // 由于漫画大全页面后台js锁定到 "日本" 且 "最近更新" 两个条件，所以返回上一页会跳转到默认条件页面
            // 首页 → 漫画大全（"日本" 且 "最近更新"）→ 返回上一页（漫画大全，默认条件）
            // 故基于用户习惯，应该跳回首页比较合理
            webView.loadUrl(targetUrl);
            return false;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && webView.getUrl().equals(targetUrl)) {
            // 当处于首页点击返回时，弹出窗口询问是否退出APP
            AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
            ad.setTitle("退出应用");
            ad.setMessage("是否退出应用？");
            ad.setPositiveButton("确认", (DialogInterface dialog, int which) -> System.exit(0));
            ad.setNegativeButton("取消", (DialogInterface dialog, int which) -> dialog.dismiss());
            ad.create().show();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }


}
