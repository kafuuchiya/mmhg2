package com.example.mmhg2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class ComicActivity extends AppCompatActivity {
    ProgressBar progressBar;
    WebView comicWebView;

    @SuppressLint("AddJavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic);

        String comicUrl = this.getIntent().getStringExtra("url");
        Js2Android jsa = new Js2Android(this);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        comicWebView = (WebView) findViewById(R.id.comic_wv);

        comicWebView.loadUrl(comicUrl);

        comicWebView.getSettings().setJavaScriptEnabled(true);
        comicWebView.addJavascriptInterface(jsa, "toAndroid");
        comicWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("mmhg2-log", "onPageFinished: Comic page");
                if (Build.VERSION.SDK_INT < 19) {
                    // 选择进入某个漫画，隐藏一些不打必要的元素，以增加观感
                    String js = getResources().getString(R.string.js_rm_ad);
                    js += getResources().getString(R.string.js_conf);
                    js += getResources().getString(R.string.js_rm_topAndBottom);
                    js += getResources().getString(R.string.js_comic);
                    view.loadUrl("javascript:" + js);
                }

                progressBar.setVisibility(View.GONE);
                comicWebView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("mmhg2-log", "onPageStarted: Comic page ");

                if (Build.VERSION.SDK_INT >= 19) {
                    String js = jsa.getJsStr("mainJs.js");
                    js += jsa.getJsStr("comicJs.js");
                    comicWebView.evaluateJavascript(js, null);
                }

                // 当观看漫画时，自动转横屏显示
                if (url.indexOf("html") > 0 && url.indexOf("comic") > 0) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏设置
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);//默认设置
                }

                progressBar.setVisibility(View.VISIBLE);
                comicWebView.setVisibility(View.GONE);
            }
        });
        comicWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d("mmhg2-log", "onReceiveValue: " + message);

//                return super.onJsAlert(view, url, message, result);
                AlertDialog.Builder b = new AlertDialog.Builder(ComicActivity.this);
                b.setTitle("Tips:");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, (DialogInterface dialog, int which) -> result.confirm());
                b.setCancelable(false);
                b.create().show();
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && comicWebView.getUrl().indexOf("html") > 0) {
            // 在观看漫画过程点击返回，设置跳回该漫画的目录页面
            int end = comicWebView.getUrl().lastIndexOf("/");
            comicWebView.loadUrl(comicWebView.getUrl().substring(0, end));
            return false;
        } else {
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }
}