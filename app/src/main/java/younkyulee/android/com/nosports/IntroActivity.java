package younkyulee.android.com.nosports;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import static younkyulee.android.com.nosports.LaNaSaApplication.mDatas;

public class IntroActivity extends CustomActivity {

    private Intent intent;
    private final int SPLASHTIME = 1000;
    private ImageView iv;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ValueEventListener mValueEventListener;
    private DatabaseReference isFix;
    public static boolean isFixed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFixed = false;
        setUi();
        init();
        setListener();

    }

    private void init() {
        // firebase 연결
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("MatchNew");
        //데이터 초기화

        if(mDatas == null) {
            mDatas = new ArrayList<>();
        }

        mDatas.clear();
    }

    //Ui 설정
    private void setUi() {
        // 가로로 안 뉘어지도록 설정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_intro);

        // 랜딩이미지 디바이스별 크기 조절
        iv = (ImageView) findViewById(R.id.iv_randing);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if ((double) width / height < (double) 1440 / 2392) {
            Glide.with(this).load(R.drawable.landing_long).into(iv);
        }

        //전체 화면 설정
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
    }


    private void setListener() {

        //이벤트 리스터 생성
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //데이터 존재 여부 확인
                if (dataSnapshot.exists() || dataSnapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        final Match match = postSnapshot.getValue(Match.class);
                        //크롤링 실패하여 이상한 데이터가 들어온 경우 예외 처리
                        if (match.getMatchUrl() != null) {
                            mDatas.add(match);
                        }
                    }
                    ;
                    //데이터 정렬
                    Collections.sort(mDatas, new MatchHitComparator());
                }
                isFixed();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                isFixed();
            }
        };

        mDatabaseReference.addListenerForSingleValueEvent(mValueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mValueEventListener != null) {
            mDatabaseReference.removeEventListener(mValueEventListener);
        }
    }

    public void isFixed() {
        //현재 서버점검 모드인지 확인
        isFix = mFirebaseDatabase.getReference("isFix");
        isFix.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("true")) {
                    isFixed = true;
                }
                goNextPage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                goNextPage();
            }
        });
    }

    public void goNextPage() {
        Intent mainIntent = new Intent(IntroActivity.this, MainActivity.class);
        IntroActivity.this.startActivity(mainIntent);
        IntroActivity.this.finish();
    }

    //데이터 조회수 순 정렬
    class MatchHitComparator implements Comparator<Match> {
        @Override
        public int compare(Match o1, Match o2) {
            return Integer.parseInt(o2.getHit()) - Integer.parseInt(o1.getHit());
        }
    }

}
