package younkyulee.android.com.nosports;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import static younkyulee.android.com.nosports.IntroActivity.mDatas;

public class MainActivity extends CustomActivity {

    CustomRecyclerView rv_match_list;
    LinearLayout li;
    ImageView iv_empty;
    FrameLayout fm_empty;
    ArrayList<Match> datas = new ArrayList<>();
    LayoutInflater vi;
    View v;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ValueEventListener mValueEventListener;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        initElement();
//        initData();
        initRecyclerView();

    }

    private void initElement() {
        li = (LinearLayout)findViewById(R.id.li_main);
    }

    private void initData() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Match");


        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Match match = postSnapshot.getValue(Match.class);
                    datas.add(match);
                };

                Collections.sort(datas, new MatchHitComparator());
                initRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                initRecyclerView();
            }
        };

        mDatabaseReference.addValueEventListener(mValueEventListener);
    }

    private void initRecyclerView() {

        RecyclerView rv_match_list = (RecyclerView) findViewById(R.id.rv_match_list);

        if(mDatas.size() <= 0) {

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("경기 없음")
                    .setAction("")
                    .build());

            rv_match_list.setVisibility(View.GONE);
            fm_empty = (FrameLayout)findViewById(R.id.fm_empty);
            iv_empty = (ImageView)findViewById(R.id.iv_empty);
            fm_empty.setVisibility(View.VISIBLE);

            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            if((double) width / height < (double) 1440 / 2392) {
                Glide.with(this).load(R.drawable.empty_long).into(iv_empty);
            } else {
                Glide.with(this).load(R.drawable.empty).into(iv_empty);
            }


            return;
        }

        //2. 아답터생성하기
        CustomRecyclerViewAdapter rca = new CustomRecyclerViewAdapter(mDatas, R.layout.vursurs_card, mTracker);

        //3. 리사이클러 뷰에 아답터 세팅하기
        rv_match_list.setAdapter(rca);

        //4. 리사이클러 뷰 매니저 등록하기(뷰의 모양을 결정 : 그리드, 일반리스트, 비대칭그리드)
        rv_match_list.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mValueEventListener != null) {
            mDatabaseReference.removeEventListener(mValueEventListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //using tracker variable to set Screen Name
        mTracker.setScreenName("메인액티비티");
        //sending the screen to analytics using ScreenViewBuilder() method
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    class MatchHitComparator implements Comparator<Match> {
        @Override
        public int compare(Match o1, Match o2) {
            return Integer.parseInt(o2.getHit()) - Integer.parseInt(o1.getHit());
        }
    }

}


