package com.example.mmhg2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    WebView webView;
    final static String targetUrl = "https://m.manhuagui.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.loading);
        webView = (WebView) findViewById(R.id.web_view);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "toAndroid");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("mmhg2-log", "onPageFinished: Main page");
                // 隐藏广告和对一些元素进行修改
                String js = getResources().getString(R.string.js_rm_ad);
                js += getResources().getString(R.string.js_conf);
                view.loadUrl(js);
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("mmhg2-log", "onPageStarted: Main page");
                progressBar.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(targetUrl);
    }

    @JavascriptInterface
    public void toNext(String url){
        Log.d("mmhg2-log", "toNext: " + url);
        // JS 与 Android 之间的交互
        Intent intent = new Intent(this.getApplication(), ComicActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @JavascriptInterface
    public void toErr(String err){
        // 打印JS运行时候的错误
        Log.d("mmhg2-log", "toErr: " + err);
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
            ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    }
            );
            ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            ad.create().show();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }
}