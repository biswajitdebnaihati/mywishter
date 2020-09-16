package com.exnovation.wishster.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.exnovation.wishster.R;

public class ActivityWebView extends AppCompatActivity {
    WebView webView;

    static String url="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Intent i = getIntent();
        if (i != null) {
            url=i.getStringExtra("url");
        //url="https://www.ebay.com/itm/Patanjali-Spirulina-60-Capsules-With-Moringa-Superfood-Rich-in-Protein/202739238915";
            webView = findViewById(R.id.webview);
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setAppCachePath(getCacheDir().getAbsolutePath() + "/webViewCache");
            webView.getSettings().setGeolocationDatabasePath(getCacheDir().getAbsolutePath() + "/webViewDatabase");
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
            });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                getWindow().setTitle(title); //Set Activity tile to page title.
            }
        });

            webView.loadUrl(url);
           }else {
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
