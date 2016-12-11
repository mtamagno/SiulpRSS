package com.rss.siulp.siulp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsDetails extends AppCompatActivity {
WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        Bundle bundle = getIntent().getExtras();
        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings =  webView.getSettings();
        webView.getSettings().setJavaScriptEnabled (true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows (false);
        webView.getSettings().setSupportZoom (false);
        webView.setVerticalScrollBarEnabled (false);
        webView.setHorizontalScrollBarEnabled (false);
        webView.loadUrl(bundle.getString("Link"));

        webView.setWebViewClient(new WebViewClient() {
            @ Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("vnd.youtube")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }
                else {
                    return false;
                }
            }
        });
    }
}
