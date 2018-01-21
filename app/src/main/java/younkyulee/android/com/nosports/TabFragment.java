package younkyulee.android.com.nosports;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;

import java.util.ArrayList;

import static younkyulee.android.com.nosports.IntroActivity.isFixed;
import static younkyulee.android.com.nosports.MainActivity.mTracker;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragment extends Fragment {

    ArrayList<Match> datas;
    private OnFragmentInteractionListener mListener;
    RecyclerView rv_match_list;
    ImageView iv_empty;
    FrameLayout fm_empty;

    public TabFragment() {
        // Required empty public constructor
    }


    public static TabFragment newInstance() {
        TabFragment fragment = new TabFragment();
        return fragment;
    }

    public void setMatchs(ArrayList<Match> datas) {
        this.datas = datas;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_zero, container, false);

        rv_match_list = (RecyclerView) view.findViewById(R.id.rv_match_list);

        if (datas.size() <= 0 || isFixed) {

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("경기 없음")
                    .setAction("")
                    .build());

            rv_match_list.setVisibility(View.GONE);
            fm_empty = (FrameLayout) view.findViewById(R.id.fm_empty);
            iv_empty = (ImageView) view.findViewById(R.id.iv_empty);
            fm_empty.setVisibility(View.VISIBLE);

            DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            if ((double) width / height < (double) 1440 / 2392) {
                Glide.with(this).load(R.drawable.empty_daily).into(iv_empty);
            } else {
                Glide.with(this).load(R.drawable.empty_daily).into(iv_empty);
            }

            return view;
        }

        if(!isFixed) {
            //2. 아답터생성하기
            CustomRecyclerViewAdapter rca = new CustomRecyclerViewAdapter(datas, R.layout.vursurs_card, mTracker);

            //3. 리사이클러 뷰에 아답터 세팅하기
            rv_match_list.setAdapter(rca);

            //4. 리사이클러 뷰 매니저 등록하기(뷰의 모양을 결정 : 그리드, 일반리스트, 비대칭그리드)
            rv_match_list.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
