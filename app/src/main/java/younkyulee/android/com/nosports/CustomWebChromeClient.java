package younkyulee.android.com.nosports;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

/**
 * Created by Younkyu on 2017-11-26.
 */

public class CustomWebChromeClient extends WebChromeClient {
    private View mCustomView;
    private Activity mActivity;
    private int w,h;
    private int mOriginalOrientation;
    private FullscreenHolder mFullscreenContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCollback;

    public CustomWebChromeClient(Activity activity,int h, int w) {
        this.mActivity = activity;
        this.w = w;
        this.h = h;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        result.confirm();
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {

        if (mCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }

        mOriginalOrientation = mActivity.getRequestedOrientation();
        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();

        mFullscreenContainer = new FullscreenHolder(mActivity);
        mFullscreenContainer.addView(view, ViewGroup.LayoutParams.MATCH_PARENT);
        decor.addView(mFullscreenContainer, ViewGroup.LayoutParams.MATCH_PARENT);

        mActivity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        mCustomView = view;
        mCustomViewCollback = callback;
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }


    @Override
    public void onHideCustomView() {
        super.onHideCustomView();
        if (mCustomView == null) {
            return;
        }

        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
        decor.removeView(mFullscreenContainer);
        mFullscreenContainer = null;
        mCustomView = null;
        mCustomViewCollback.onCustomViewHidden();

        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR   );
    }

    public void setRequestOrientation(int orientation){
        mActivity.setRequestedOrientation(orientation);
    }

    private static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }
}