package younkyulee.android.com.nosports;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    WebView wv;
    Intent intent;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        intent = getIntent();
        url = intent.getExtras().getString("url");

        wv = (WebView)findViewById(R.id.wb);
        wv.getSettings().setJavaScriptEnabled(true);
        // 줌사용
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(true);

        //3. 웹뷰 클라이언트를 지정(안하면 내장 웹브라우저가 팝업된다.)
        // wv.setWebChromeClient(new WebChromeClient());
        wv.setWebViewClient(new WebViewClient());
        // 3.1 http 등을 처리하기 위한 핸들러~ : 프로토콜에 따라 클라이언트가선택되는 것으로 파악됨..
        wv.setWebChromeClient(new WebChromeClient());
        wv.loadUrl(url);

    }
    
    private class MyWebViewClient extend WebViewClient {
        @Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //TODO : implement...
        }
    }

}
