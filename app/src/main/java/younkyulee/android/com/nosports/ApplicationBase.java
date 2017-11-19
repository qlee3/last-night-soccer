package younkyulee.android.com.nosports;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by Younkyu on 2017-11-04.
 */

public class ApplicationBase extends Application {

    @Override public void onCreate() {
        super.onCreate();
        // 폰트 정의
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "NanumBarunGothic.otf"))
                .addBold(Typekit.createFromAsset(this, "NanumBarunGothicBold.otf"));
    }
}

