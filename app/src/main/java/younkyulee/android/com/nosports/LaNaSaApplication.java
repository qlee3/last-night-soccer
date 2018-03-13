package younkyulee.android.com.nosports;

/**
 * Created by Younkyu on 2017-11-18.
 * 폰트 설정
 * 트래커설정
 */

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.tsengvn.typekit.Typekit;

import java.util.ArrayList;

public class LaNaSaApplication extends Application {

    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;
    public static ArrayList<Match> mDatas = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "NanumBarunGothic.otf"))
                .addBold(Typekit.createFromAsset(this, "NanumBarunGothicBold.otf"));

        sAnalytics = GoogleAnalytics.getInstance(this);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }
}
