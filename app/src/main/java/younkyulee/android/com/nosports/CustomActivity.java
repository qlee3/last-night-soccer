package younkyulee.android.com.nosports;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Younkyu on 2017-11-04.
 * 폰트설정을 위한 커스텀 액티비티
 * 모든 액티비티는 기본적으로 커스텀 액티비티를 상속 받음
 */

public class CustomActivity extends AppCompatActivity {

    @Override protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

}
