package com.example.myapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

/*
在不进行任何设置的情况下，https类型的会使用系统浏览器打开，而http类型的会使用内嵌浏览器
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private WebView webView;
    private Button refresh;
    private Button back;
    private TextView web_title;
    private TextView text_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        web_setting();
    }

    private void init() {
        webView = (WebView) findViewById(R.id.webview);
        refresh = (Button) findViewById(R.id.refresh);
        back = (Button) findViewById(R.id.back);
        web_title = (TextView) findViewById(R.id.web_title);
        text_error = (TextView) findViewById(R.id.text_error);

        refresh.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void web_setting() {
//        webView.loadUrl("http://www.apk3.com/uploads/soft/201504/bdsjzs.apk");
        webView.loadUrl("https://www.baidu.com/");
//        webView.loadUrl("http://www.51job.com/");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                web_title.setText(title);
                super.onReceivedTitle(view, title);
            }
        });
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //也可以使用这种方式加载一个本地的错误码   view.loadUrl();
                Log.i("ln","进入onReceivedError");
                text_error.setText("error 404");
                webView.setVisibility(View.GONE);
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Log.i("ln", "进入downloadlistener");
                if (url.endsWith(".apk")) {
                    new httpThread(url).start();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh:
                Log.i("ln", "进行刷新");
                text_error.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.reload();
                break;

            case R.id.back:
                finish();
                break;
        }
    }
}
