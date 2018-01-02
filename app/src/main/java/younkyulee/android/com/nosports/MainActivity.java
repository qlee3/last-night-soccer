package younkyulee.android.com.nosports;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import static younkyulee.android.com.nosports.IntroActivity.isFixed;
import static younkyulee.android.com.nosports.IntroActivity.mDatas;

public class MainActivity extends CustomActivity {

    RecyclerView rv_match_list;
    LinearLayout li;
    ImageView iv_empty;
    FrameLayout fm_empty;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //트래커 초기화
        LaNaSaApplication application = (LaNaSaApplication) getApplication();
        mTracker = application.getDefaultTracker();

        initElement();
        initRecyclerView();

    }

    private void initElement() {
        li = (LinearLayout) findViewById(R.id.li_main);
    }

    private void initRecyclerView() {

        rv_match_list = (RecyclerView) findViewById(R.id.rv_match_list);

        if (isFixed) {

            rv_match_list.setVisibility(View.GONE);
            fm_empty = (FrameLayout) findViewById(R.id.fm_empty);
            iv_empty = (ImageView) findViewById(R.id.iv_empty);
            fm_empty.setVisibility(View.VISIBLE);

            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            if ((double) width / height < (double) 1440 / 2392) {
                Glide.with(this).load(R.drawable.fix_long).into(iv_empty);
            } else {
                Glide.with(this).load(R.drawable.fix).into(iv_empty);
            }

            return;
        }

        if (mDatas.size() <= 0) {

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("경기 없음")
                    .setAction("")
                    .build());

            rv_match_list.setVisibility(View.GONE);
            fm_empty = (FrameLayout) findViewById(R.id.fm_empty);
            iv_empty = (ImageView) findViewById(R.id.iv_empty);
            fm_empty.setVisibility(View.VISIBLE);

            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            if ((double) width / height < (double) 1440 / 2392) {
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
    protected void onResume() {
        super.onResume();
        //using tracker variable to set Screen Name
        mTracker.setScreenName("메인액티비티");
        //sending the screen to analytics using ScreenViewBuilder() method
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

}


