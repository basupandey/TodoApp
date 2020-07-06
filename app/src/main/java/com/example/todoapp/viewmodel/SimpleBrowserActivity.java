package com.example.todoapp.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todoapp.R;
import com.example.todoapp.view.MainActivity;

public class SimpleBrowserActivity extends AppCompatActivity {
    private EditText edtUrl;
    private WebView webView;
    private WebViewClient client;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_browser);
        edtUrl=findViewById(R.id.edt_url);
        webView=findViewById(R.id.web_view);

      WebSettings settings= webView.getSettings();
      settings.setUseWideViewPort(true);
      settings.setLoadWithOverviewMode(true);
      settings.setJavaScriptEnabled(true);

      client=new WebViewClient();
      webView.setWebViewClient(client);

      webView.loadUrl("http://www.google.com");
    }

    public void go(View view) {
        String url="http://"+edtUrl.getText().toString();
        webView.loadUrl(url);
    }

    public void back(View view) {
        if(webView.canGoBack()){
            webView.goBack();
        }
        else{
            Toast.makeText(getApplicationContext(),"cannot go back",Toast.LENGTH_SHORT).show();
        }
    }

    public void forward(View view) {
        if (webView.canGoForward()){
            webView.goForward();
        }
        else{
            Toast.makeText(getApplicationContext(),"cannot go forward",Toast.LENGTH_SHORT).show();
        }

    }

    public void reload(View view) {

        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void clearHistory(View view) {
        webView.clearHistory();
        Toast.makeText(getApplicationContext(),"your history has been cleared.....",Toast.LENGTH_SHORT).show();
    }
}
