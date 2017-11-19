package younkyulee.android.com.nosports;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class IntroActivity extends CustomActivity {

    private Intent intent;
    private final int SPLASHTIME = 1000;
    private ImageView iv;


    public static ArrayList<Match> mDatas = new ArrayList<>();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ValueEventListener mValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        iv = (ImageView)findViewById(R.id.iv_randing);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if((double) width / height < (double) 1440 / 2392) {
            Glide.with(this).load(R.drawable.landing_long).into(iv);
        }


        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off. ");
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.");
        }
// 몰입 모드를 꼭 적용해야 한다면 아래의 3가지 속성을 모두 적용시켜야 합니다
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Match");


        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Match match = postSnapshot.getValue(Match.class);
                    mDatas.add(match);
                };
                Collections.sort(mDatas, new MatchHitComparator());
                goNext();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                goNext();
            }
        };

        mDatabaseReference.addValueEventListener(mValueEventListener);

//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                /* 메뉴액티비티를 실행하고 로딩화면을 죽인다.*/
//                Intent mainIntent = new Intent(IntroActivity.this,MainActivity.class);
//                IntroActivity.this.startActivity(mainIntent);
//                IntroActivity.this.finish();
//            }
//        }, SPLASHTIME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mValueEventListener != null) {
            mDatabaseReference.removeEventListener(mValueEventListener);
        }
    }

    public void goNext() {
        Intent mainIntent = new Intent(IntroActivity.this,MainActivity.class);
        IntroActivity.this.startActivity(mainIntent);
        IntroActivity.this.finish();
    }


    class MatchHitComparator implements Comparator<Match> {
        @Override
        public int compare(Match o1, Match o2) {
            return Integer.parseInt(o2.getHit()) - Integer.parseInt(o1.getHit());
        }
    }

}
