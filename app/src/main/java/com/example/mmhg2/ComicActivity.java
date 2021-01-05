package com.example.mmhg2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class ComicActivity extends AppCompatActivity {
    ProgressBar progressBar;
    WebView comicWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic);

        String comicUrl = this.getIntent().getStringExtra("url");

        progressBar = (ProgressBar) findViewById(R.id.loading);
        comicWebView = (WebView) findViewById(R.id.comic_wv);

        comicWebView.getSettings().setJavaScriptEnabled(true);
        comicWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("mmhg2-log", "onPageFinished: Comic page");
                // 选择进入某个漫画，隐藏一些不打必要的元素，以增加观感
                String js = getResources().getString(R.string.js_rm_ad);
                js += getResources().getString(R.string.js_conf);
                js += getResources().getString(R.string.js_rm_topAndBottom);
                js += getResources().getString(R.string.js_comic);
                view.loadUrl(js);

                progressBar.setVisibility(View.GONE);
                comicWebView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("mmhg2-log", "onPageStarted: Comic page ");

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
        comicWebView.loadUrl(comicUrl);
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