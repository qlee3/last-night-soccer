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

    WebView wv;
    WebSettings settings;
    Intent intent;
    String url;
    WindowManager.LayoutParams params;
    int SCREEN_WIDTH;
    int SCREEN_HEIGHT;
    String mobileHeader = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        init();
        setWv(wv);

    }

    private void init() {

        //화면 사이즈를 영상 view에 보내기, 현재 사용하지 않고 있음
        params = getWindow().getAttributes();
        DisplayMetrics dmath = this.getResources().getDisplayMetrics();    // 화면의 가로,세로 길이를 구할 때 사용합니다.
        SCREEN_WIDTH = dmath.widthPixels;
        SCREEN_HEIGHT = dmath.heightPixels;

        //영상 url 받기
        intent = getIntent();
        url = intent.getExtras().getString("url");

        wv = (WebView) findViewById(R.id.wb);

    }

    private void setWv(final WebView wv) {

        settings = wv.getSettings();

        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString(mobileHeader);

        // 줌사용
        settings.setAllowContentAccess(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);

        //웹뷰 naverApp 연결 딥링크 관련 세팅
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
                            marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                            startActivity(marketIntent);
                        }
                        return true;
                    } catch (Exception e) {
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
        wv.setWebChromeClient(new CustomWebChromeClient(this, SCREEN_HEIGHT, SCREEN_WIDTH));

        wv.loadUrl(url);
    }


}
