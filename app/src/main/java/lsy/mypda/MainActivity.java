package lsy.mypda;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView webview;
    private ImageView imageView;
    private TextView tv_refresh,tv_setting,tv_logout;
    private ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    String url;
    String url_slice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//   继承的不是activity,导致隐藏标题栏失效
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        webview = (WebView) findViewById(R.id.webview);
        //  开启JavaScript脚本支持
        webview.getSettings().setJavaScriptEnabled(true);
        imageView = (ImageView) findViewById(R.id.imgbtn_back);
        tv_refresh = (TextView) findViewById(R.id.tv_refresh );
        tv_setting = (TextView) findViewById(R.id.tv_setting );
        tv_logout = (TextView) findViewById(R.id.tv_logout);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        url=sharedPreferences.getString("lastUrl","gl-test");
        url_slice = "http://bds." + url + ".com/mobile/login/index";
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.goBack();
            }
        });
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                webview.reload();
            }
        });
        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,EditUrlActivity.class);
                intent.putExtra("currentUrl",url);
                MainActivity.this.startActivityForResult(intent,1);
            }
        });
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //webview.goBack();
                //String LogoutUrl = "http://erp.hkbluelans.com:801/system/index.php/mob/managers/logout";
//                String LogoutUrl = url + "/system/index.php/mob/managers/logout";
                url_slice = "http://bds." + url + ".com/mobile/login/index";
                //  清除缓存cookie
                webview.clearCache(true);
                CookieManager.getInstance().removeAllCookie();
                webview.loadUrl(url_slice);
            }
        });
        openUrl(url_slice);
    }

    private void saveUrl(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastUrl",url);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 3) {
            String tempUrl = data.getStringExtra("url");
            url_slice = "http://bds." + tempUrl + ".com/mobile/login/index";
            System.out.println("tempUrl="+tempUrl);
//            if(Util.isVaildUrl(tempUrl)){
                url=tempUrl;
                saveUrl();

                openUrl(url_slice);
//            }

        }

    }

    private void openUrl(String url){
        if(!Util.isVaildUrl(url)){
            Toast.makeText(this,"请输入有效的网址!",Toast.LENGTH_SHORT).show();
        }else if(url.startsWith("https")){
            setWebView2(url);
        }else if(url.startsWith("http")){
            indata(url);
        }
    }

    public void indata(String url) {
        //String url = "http://192.16m8.88.64/syste/index.php/mob";
        webview.setWebViewClient(new webViewClient());
        webview.setWebChromeClient(new webChromeClient());
        //WebView加载web资源
        webview.loadUrl(url);
        //设置Web视图
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setWebView1(String url){
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.cancel(); // Android默认的处理方式
                handler.proceed();  // 接受所有网站的证书
                //handleMessage(Message msg); // 进行其他处理
            }
        });
        webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webview.loadUrl(url);
    }

    private void setWebView2(String url) {
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.cancel(); // Android默认的处理方式
                handler.proceed();  // 接受所有网站的证书
                //handleMessage(Message msg); // 进行其他处理
            }
        });
        //  隐藏加载条
        webview.setWebChromeClient(new webChromeClient());
        webview.loadUrl(url);
    }

//    @Override
//    //设置回退
//    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
//            webview.goBack(); //goBack()表示返回WebView的上一页面
//            return true;
//        }
//        finish();//结束退出程序
//        return false;
//    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class webChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview.clearCache(true);
    }
}
