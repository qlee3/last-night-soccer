package younkyulee.android.com.nosports;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static younkyulee.android.com.nosports.IntroActivity.isFixed;
import static younkyulee.android.com.nosports.LaNaSaApplication.mDatas;

public class MainActivity extends CustomActivity {

//    RecyclerView rv_match_list;
    LinearLayout li;
    ImageView iv_empty;
    FrameLayout fm_empty;
    public static Tracker mTracker;
    TabLayout tabLayout, nalzeTab;
    int today= 0;
    ViewPager vp;
    final int TAB_COUNT = 7;
    TabFragment tabFragment1;
    TabFragment tabFragment2;
    TabFragment tabFragment3;
    TabFragment tabFragment4;
    TabFragment tabFragment5;
    TabFragment tabFragment6;
    TabFragment tabFragment7;

    ArrayList<Match> data1 = new ArrayList<>();
    ArrayList<Match> data2 = new ArrayList<>();
    ArrayList<Match> data3 = new ArrayList<>();
    ArrayList<Match> data4 = new ArrayList<>();
    ArrayList<Match> data5 = new ArrayList<>();
    ArrayList<Match> data6 = new ArrayList<>();
    ArrayList<Match> data7 = new ArrayList<>();

    // 1     2     3     4     5     6     7
    final String[] week = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
//    final String[] week = { "일", "월", "화", "수", "목", "금", "토" };
    Calendar oCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 앱이 튕겼을 경우 재시작
        if(mDatas == null) {
            Intent mainIntent = new Intent(MainActivity.this, IntroActivity.class);
            this.startActivity(mainIntent);
            this.finish();
        }


        //트래커 초기화
        LaNaSaApplication application = (LaNaSaApplication) getApplication();
        mTracker = application.getDefaultTracker();
        setDay();
        setData();
        initElement();
//        initRecyclerView();

    }

    private void setData() {

        String date;
        Match match;
        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd hh:mm");

        for(int i = 0 ; i < mDatas.size() ; i ++) {
            match = mDatas.get(i);
            date = match.getYear()+"/"+match.getMatchTime();
            try {
                Date dts = dt.parse(date);
                Calendar day = Calendar.getInstance();
                day.setTime(dts);
                day.add(Calendar.HOUR,3);
                switch ( (day.get(Calendar.DAY_OF_WEEK)-1 + (7-today) ) %7 )  {
                    case 0 : data7.add(match); break;
                    case 1 : data1.add(match); break;
                    case 2 : data2.add(match); break;
                    case 3 : data3.add(match); break;
                    case 4 : data4.add(match); break;
                    case 5 : data5.add(match); break;
                    case 6 : data6.add(match); break;
                }

            } catch (ParseException e) {
                continue;
            }
        }

    }

    private void initElement() {
        li = (LinearLayout) findViewById(R.id.li_main);
        vp = (ViewPager) findViewById(R.id.viewPager);

        tabFragment1 = TabFragment.newInstance(); tabFragment1.setMatchs(data1);
        tabFragment2 = TabFragment.newInstance(); tabFragment2.setMatchs(data2);
        tabFragment3 = TabFragment.newInstance(); tabFragment3.setMatchs(data3);
        tabFragment4 = TabFragment.newInstance(); tabFragment4.setMatchs(data4);
        tabFragment5 = TabFragment.newInstance(); tabFragment5.setMatchs(data5);
        tabFragment6 = TabFragment.newInstance(); tabFragment6.setMatchs(data6);
        tabFragment7 = TabFragment.newInstance(); tabFragment7.setMatchs(data7);

        // 아답터 생성
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        // 1. 페이저 리스너 - 페이저가 변경되었을 때 탭을 바꿔주는 리스너
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(nalzeTab));
        // 2. 탭이 변경되었을 때 페이지를 바꿔주는 리스너
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp));
        nalzeTab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp));

        TabLayout.Tab tab = tabLayout.getTabAt(6);
        tab.select();

    }

    private void setDay() {

        oCalendar = Calendar.getInstance();  // 현재 날짜/시간 등의 각종 정보 얻기
        today = oCalendar.get(Calendar.DAY_OF_WEEK) - 1;

        nalzeTab = (TabLayout)findViewById(R.id.nalzaTab);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        nalzeTab.setTabTextColors(ContextCompat.getColor(this, R.color.your_nonselected_text_color),
                ContextCompat.getColor(this, R.color.your_selected_text_color));

        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.your_nonselected_text_color),
                ContextCompat.getColor(this, R.color.your_selected_text_color));


        nalzeTab.getTabAt(0).setText(week[(today+1)%7]);
        nalzeTab.getTabAt(1).setText(week[(today+2)%7]);
        nalzeTab.getTabAt(2).setText(week[(today+3)%7]);
        nalzeTab.getTabAt(3).setText(week[(today+4)%7]);
        nalzeTab.getTabAt(4).setText(week[(today+5)%7]);
        nalzeTab.getTabAt(5).setText(week[(today+6)%7]);
        nalzeTab.getTabAt(6).setText(week[today]);

        Date date = oCalendar.getTime();
        String nalza =  new SimpleDateFormat("dd").format(date);

        tabLayout.getTabAt(6).setText(nalza);

        oCalendar.add(Calendar.DAY_OF_MONTH,-1);
        date = oCalendar.getTime();
        nalza =  new SimpleDateFormat("dd").format(date);

        tabLayout.getTabAt(5).setText(nalza);

        oCalendar.add(Calendar.DAY_OF_MONTH,-1);
        date = oCalendar.getTime();
        nalza =  new SimpleDateFormat("dd").format(date);

        tabLayout.getTabAt(4).setText(nalza);

        oCalendar.add(Calendar.DAY_OF_MONTH,-1);
        date = oCalendar.getTime();
        nalza =  new SimpleDateFormat("dd").format(date);

        tabLayout.getTabAt(3).setText(nalza);

        oCalendar.add(Calendar.DAY_OF_MONTH,-1);
        date = oCalendar.getTime();
        nalza =  new SimpleDateFormat("dd").format(date);
        tabLayout.getTabAt(2).setText(nalza);

        oCalendar.add(Calendar.DAY_OF_MONTH,-1);
        date = oCalendar.getTime();
        nalza =  new SimpleDateFormat("dd").format(date);

        tabLayout.getTabAt(1).setText(nalza);

        oCalendar.add(Calendar.DAY_OF_MONTH,-1);
        date = oCalendar.getTime();
        nalza =  new SimpleDateFormat("dd").format(date);

        tabLayout.getTabAt(0).setText(nalza);

    }


//    private void initRecyclerView() {
//
//        rv_match_list = (RecyclerView) findViewById(R.id.rv_match_list);
//
//        if (isFixed) {
//
//            rv_match_list.setVisibility(View.GONE);
//            fm_empty = (FrameLayout) findViewById(R.id.fm_empty);
//            iv_empty = (ImageView) findViewById(R.id.iv_empty);
//            fm_empty.setVisibility(View.VISIBLE);
//
//            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
//            int width = dm.widthPixels;
//            int height = dm.heightPixels;
//            if ((double) width / height < (double) 1440 / 2392) {
//                Glide.with(this).load(R.drawable.fix_long).into(iv_empty);
//            } else {
//                Glide.with(this).load(R.drawable.fix).into(iv_empty);
//            }
//
//            return;
//        }
//
//        if (mDatas.size() <= 0) {
//
//            mTracker.send(new HitBuilders.EventBuilder()
//                    .setCategory("경기 없음")
//                    .setAction("")
//                    .build());
//
//            rv_match_list.setVisibility(View.GONE);
//            fm_empty = (FrameLayout) findViewById(R.id.fm_empty);
//            iv_empty = (ImageView) findViewById(R.id.iv_empty);
//            fm_empty.setVisibility(View.VISIBLE);
//
//            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
//            int width = dm.widthPixels;
//            int height = dm.heightPixels;
//            if ((double) width / height < (double) 1440 / 2392) {
//                Glide.with(this).load(R.drawable.empty_long).into(iv_empty);
//            } else {
//                Glide.with(this).load(R.drawable.empty).into(iv_empty);
//            }
//
//
//            return;
//        }
//
//        //2. 아답터생성하기
//        CustomRecyclerViewAdapter rca = new CustomRecyclerViewAdapter(mDatas, R.layout.vursurs_card, mTracker);
//
//        //3. 리사이클러 뷰에 아답터 세팅하기
//        rv_match_list.setAdapter(rca);
//
//        //4. 리사이클러 뷰 매니저 등록하기(뷰의 모양을 결정 : 그리드, 일반리스트, 비대칭그리드)
//        rv_match_list.setLayoutManager(new LinearLayoutManager(this));
//
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        //using tracker variable to set Screen Name
        mTracker.setScreenName("메인액티비티");
        //sending the screen to analytics using ScreenViewBuilder() method
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }


    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0 : fragment = tabFragment1; break;
                case 1 : fragment = tabFragment2; break;
                case 2 : fragment = tabFragment3; break;
                case 3 : fragment = tabFragment4; break;
                case 4 : fragment = tabFragment5; break;
                case 5 : fragment = tabFragment6; break;
                case 6 : fragment = tabFragment7; break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }

}


