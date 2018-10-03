package com.example.android.jecrc;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends AppCompatActivity {
    private WebView mywebview;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout =(SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Loadweb();
                //mywebview.goForward();
            }
        });

        Loadweb();
    }

    public void Loadweb(){
        if(!isConnected(MainActivity.this)) {
            buildDialog(MainActivity.this).show();
        }
        else {
           // setContentView(R.layout.activity_main);
            mywebview = (WebView) findViewById(R.id.webview);
            WebSettings webSettings = mywebview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAppCacheEnabled(true);
            mywebview.loadUrl("https://jecrcuniversity.edu.in");
            swipeRefreshLayout.setRefreshing(true);
            mywebview.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    mywebview.loadUrl("");
                    buildDialog(MainActivity.this).show();
                }

                public void onPageFinished(WebView view, String url) {

                    //hide swip refresh layout
                    swipeRefreshLayout.setRefreshing(false);
                }
            }); //opens  url in the app
        }
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c){
        AlertDialog.Builder builder=new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Turn on Mobile Data or WiFi. Press OK to Exit");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        return builder;
    }
    public void onBackPressed() {
        if(mywebview.canGoBack()){
            mywebview.goBack();
        }
        else{
        super.onBackPressed();
        }
    }
}

