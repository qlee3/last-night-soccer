package younkyulee.android.com.nosports;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Younkyu on 2017-11-04.
 */

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomViewHolder> {

    ArrayList<Match>  datas = new ArrayList<Match>();
    // 리스트 각 행에서 사용되는 레이아웃 xml의 아이디
    int itemLayout;
    Context context;
    Intent intent;
    Tracker mTracker;


    public CustomRecyclerViewAdapter(ArrayList<Match> datas, int itemLayout, Tracker mTracker ) {
        this.datas = datas;
        this.itemLayout = itemLayout;
        this.mTracker = mTracker;
    }

    @Override
    public CustomRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vursurs_card, parent, false);
        CustomViewHolder cvh = new CustomViewHolder(view);
        context = parent.getContext();
        return cvh;
    }

    // listView에서의 getview를 대체하는 함수(새로 만든 것)
    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        final Match match = datas.get(position);
        holder.tv_home_team.setText(match.getHomeTeam() + "");
        holder.tv_away_team.setText(match.getAwayTeam() + "");
        final String url = match.getMatchUrl();

        holder.tv_match_label.setText(match.getMatchLabel() + " " + match.getMatchRound());
        holder.tv_match_time.setText(match.getMatchTime());

        imgSetter(match.getHomeTeamCode(), holder.iv_home);
        imgSetter(match.getAwayTeamCode(), holder.iv_away);
        holder.iv_medal.setVisibility(View.INVISIBLE);
        if(position < 3) {
            holder.iv_medal.setVisibility(View.VISIBLE);
            switch (position) {
                case 0 : Glide.with(context).load(R.drawable.medal_gold).into(holder.iv_medal); break;
                case 1 : Glide.with(context).load(R.drawable.medal_silver).into(holder.iv_medal); break;
                case 2 : Glide.with(context).load(R.drawable.medal_bronze).into(holder.iv_medal); break;

            }

        }


        holder.img_hihighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("경기별")
                        .setAction(match.getHomeTeam()+" vs "+match.getAwayTeam())
                        .setLabel(String.valueOf(position))
                        .build());

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("팀별")
                        .setAction(match.getHomeTeam())
                        .setLabel(String.valueOf(position))
                        .build());

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("팀별")
                        .setAction(match.getAwayTeam())
                        .setLabel(String.valueOf(position))
                        .build());

                intent = new Intent(context,WebViewActivity.class);
                intent.putExtra("url",url);
                v.getContext().startActivity(intent);

            }
        });
    }

    // 데이터 총개수;
    @Override
    public int getItemCount() {
        return datas.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tv_home_team,tv_away_team,tv_match_time,tv_match_label;
        CardView cd;
        ImageView iv_home,iv_away,img_hihighlight,iv_medal;
        String url;

        public CustomViewHolder(View itemView) {
            super(itemView);

            // 생성자가 호출되는 순간(즉 new하는 순간) 내부의 위젯을 생성해서 담아둔다.
            iv_medal = (ImageView)itemView.findViewById(R.id.iv_medal);
            tv_home_team = (TextView) itemView.findViewById(R.id.tv_home_team);
            tv_away_team = (TextView) itemView.findViewById(R.id.tv_away_team);
            tv_match_time = (TextView) itemView.findViewById(R.id.tv_match_time);
            tv_match_label = (TextView) itemView.findViewById(R.id.tv_match_label);
            iv_home = (ImageView)itemView.findViewById(R.id.iv_home);
            iv_away = (ImageView)itemView.findViewById(R.id.iv_away);
            img_hihighlight = (ImageView)itemView.findViewById(R.id.img_hihighlight);
            cd = (CardView) itemView.findViewById(R.id.vursurs_card);
        }

    }


    public void imgSetter(String teamCode, ImageView iv) {

        switch (teamCode) {
            case "4" : Glide.with(context).load(R.drawable.chelsea).into(iv); break;
            case "23" : Glide.with(context).load(R.drawable.bournemouth).into(iv); break;
            case "43" : Glide.with(context).load(R.drawable.westham_united).into(iv); break;
            case "5" : Glide.with(context).load(R.drawable.crystal_palace).into(iv); break;
            case "38" : Glide.with(context).load(R.drawable.stoke_city).into(iv); break;
            case "41" : Glide.with(context).load(R.drawable.watford).into(iv); break;
            case "8" : Glide.with(context).load(R.drawable.everton).into(iv); break;
            case "29" : Glide.with(context).load(R.drawable.leicester_city).into(iv); break;
            case "18" : Glide.with(context).load(R.drawable.southampton).into(iv); break;
            case "6795" : Glide.with(context).load(R.drawable.brighton_hove_albion).into(iv); break;
            case "31" : Glide.with(context).load(R.drawable.newcastle_united).into(iv); break;
            case "70" : Glide.with(context).load(R.drawable.burnley).into(iv); break;
            case "65" : Glide.with(context).load(R.drawable.swansea_city).into(iv); break;
            case "9" : Glide.with(context).load(R.drawable.liverpool).into(iv); break;
            case "12" : Glide.with(context).load(R.drawable.manchester_united).into(iv); break;
            case "1006" : Glide.with(context).load(R.drawable.arsenal).into(iv); break;
            case "11" : Glide.with(context).load(R.drawable.manchester_city).into(iv); break;
            case "42" : Glide.with(context).load(R.drawable.west_bromwich_albion).into(iv); break; //웨스트브롬
            case "56" : Glide.with(context).load(R.drawable.huddersfield_town).into(iv); break;
            case "19" : Glide.with(context).load(R.drawable.tottenham_hotspur).into(iv); break;

            case "38295" : Glide.with(context).load(R.drawable.villarreal).into(iv); break;
            case "26305" : Glide.with(context).load(R.drawable.atletico_madrid).into(iv); break;
            case "26300" : Glide.with(context).load(R.drawable.barcelona).into(iv); break;
            case "26313" : Glide.with(context).load(R.drawable.athletic_bilbao).into(iv); break;
            case "26303" : Glide.with(context).load(R.drawable.real_madrid).into(iv); break;
            case "27832" : Glide.with(context).load(R.drawable.girona).into(iv); break;
            case "37454" : Glide.with(context).load(R.drawable.levante).into(iv); break;
            case "37452" : Glide.with(context).load(R.drawable.eibar).into(iv); break;
            case "26308" : Glide.with(context).load(R.drawable.real_sociedad).into(iv); break;
            case "37459" : Glide.with(context).load(R.drawable.getafe).into(iv); break;
            case "26302" : Glide.with(context).load(R.drawable.celta_de_vigo).into(iv); break;
            case "27826" : Glide.with(context).load(R.drawable.malaga).into(iv); break;
            case "27812" : Glide.with(context).load(R.drawable.leganes).into(iv); break;
            case "27821" : Glide.with(context).load(R.drawable.sevilla).into(iv); break;
            case "26309" : Glide.with(context).load(R.drawable.deportivo_la_coruna).into(iv); break;
            case "27804" : Glide.with(context).load(R.drawable.las_palmas).into(iv); break;
            case "26316" : Glide.with(context).load(R.drawable.valencia).into(iv); break;
            case "26314" : Glide.with(context).load(R.drawable.real_betis).into(iv); break;
            case "26476" : Glide.with(context).load(R.drawable.deportivo_alaves).into(iv); break;
            case "26306" : Glide.with(context).load(R.drawable.espanyol).into(iv); break;

            case "6894" : Glide.with(context).load(R.drawable.genoa).into(iv); break;
            case "26368" : Glide.with(context).load(R.drawable.ac_milan).into(iv); break;
            case "26357" : Glide.with(context).load(R.drawable.as_roma).into(iv); break;
            case "27038" : Glide.with(context).load(R.drawable.torino).into(iv); break;
            case "26359" : Glide.with(context).load(R.drawable.juventus).into(iv); break;
            case "26360" : Glide.with(context).load(R.drawable.udinese_calcio).into(iv); break;
            case "27648" : Glide.with(context).load(R.drawable.hellas_verona).into(iv); break;
            case "26364" : Glide.with(context).load(R.drawable.atalanta).into(iv); break;
            case "26361" : Glide.with(context).load(R.drawable.sampdoria).into(iv); break;
            case "6136" : Glide.with(context).load(R.drawable.inter_milan).into(iv); break;
            case "26370" : Glide.with(context).load(R.drawable.napoli).into(iv); break;
            case "27611" : Glide.with(context).load(R.drawable.chievo_verona).into(iv); break;
            case "26474" : Glide.with(context).load(R.drawable.cagliari_calcio).into(iv); break;
            case "113755" : Glide.with(context).load(R.drawable.spal).into(iv); break; //스팔
            case "38624" : Glide.with(context).load(R.drawable.crotone).into(iv); break;
            case "26362" : Glide.with(context).load(R.drawable.lazio).into(iv); break;
            case "26366" : Glide.with(context).load(R.drawable.fiorentina).into(iv); break;
            case "2759" : Glide.with(context).load(R.drawable.benevento_calcio).into(iv); break;
            case "26371" : Glide.with(context).load(R.drawable.bologna).into(iv); break;
            case "12345" : Glide.with(context).load(R.drawable.sassuolo_calcio).into(iv); break;


        }

    }

}
