package com.huyaoyu.testauthenticator;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginWebActivity extends AppCompatActivity {

    private WebView mWebView;
    private String mIntentUriUrl;

    private String mUserName;
    private String mEmail;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_web);

        mWebView = (WebView) findViewById(R.id.login_web_view);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new tokenWebViewClient());

        Intent intent = getIntent();
        mIntentUriUrl = intent.getData().toString();

        mUserName = intent.getExtras().getString("UserName");
        mEmail    = intent.getExtras().getString("Email");
        mPassword = intent.getExtras().getString("Password");
    }

    @Override
    protected void onResume() {
        super.onResume();

        mWebView.loadUrl(mIntentUriUrl);
    }

    private class tokenWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return super.shouldOverrideUrlLoading(view, url);
            if ( Uri.parse(url).getScheme().equals("http") ) {
                return false;
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.putExtra("UserName", mUserName);
                intent.putExtra("Email", mEmail);
                intent.putExtra("Password", mPassword);
                startActivity(intent);
                finish();
                return true;
            }
        }
    }
}
