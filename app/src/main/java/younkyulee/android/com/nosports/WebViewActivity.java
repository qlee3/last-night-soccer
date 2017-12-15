package younkyulee.android.com.nosports;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.net.URISyntaxException;

public class WebViewActivity extends AppCompatActivity {

    ConstraintLayout ct;
    WebView wv;
    Intent intent;
    String url;
    Button btn;
    private WindowManager.LayoutParams params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        params = getWindow().getAttributes();

        intent = getIntent();
        url = intent.getExtras().getString("url");

        ct = (ConstraintLayout)findViewById(R.id.ct);

        DisplayMetrics dmath=this.getResources().getDisplayMetrics();	// 화면의 가로,세로 길이를 구할 때 사용합니다.

        int SCREEN_WIDTH=dmath.widthPixels;
        int SCREEN_HEIGHT=dmath.heightPixels;

        wv = (WebView)findViewById(R.id.wb);
        WebSettings settings = wv.getSettings();

        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        Log.w("----------------",wv.getSettings().getUserAgentString());
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
        String hi = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";
        String hi2 = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Mobile Safari/537.36";
        settings.setUserAgentString(hi);
        // 줌사용
        settings.setAllowContentAccess(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);




        //3. 웹뷰 클라이언트를 지정(안하면 내장 웹브라우저가 팝업된다.)
        // wv.setWebChromeClient(new WebChromeClient());
//        wv.setWebViewClient(new WebViewClient());
        // 3.1 http 등을 처리하기 위한 핸들러~ : 프로토콜에 따라 클라이언트가선택되는 것으로 파악됨..
//        wv.setWebChromeClient(new WebChromeClient());

        wv.setWebViewClient(new WebViewClient() {

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                final Uri uri = Uri.parse(url);
                return handleUri(uri);
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                final Uri uri = request.getUrl();
                return handleUri(uri);
            }

            private boolean handleUri(final Uri uri) {
                Log.i("---------------", "Uri =" + uri);

                url = uri.toString();

                if (url != null && url.startsWith("intent://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                        if (existPackage != null) {
                            startActivity(intent);
                        } else {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id="+intent.getPackage()));
                            startActivity(marketIntent);
                        }
                        return true;
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (url != null && url.startsWith("market://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent != null) {
                            startActivity(intent);
                        }
                        return true;
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                wv.loadUrl(url);
                return false;

            }
        });
        wv.setWebChromeClient(new CustomWebChromeClient(this,SCREEN_HEIGHT,SCREEN_WIDTH));

        wv.loadUrl(url);

    }

}
