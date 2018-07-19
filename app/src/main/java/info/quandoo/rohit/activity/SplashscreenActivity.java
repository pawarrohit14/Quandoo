package info.quandoo.rohit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import info.quandoo.rohit.R;

/**
 * Created by Rohit Pawar on 18-07-2018.
 */

public class SplashscreenActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        hideTitle();
        LaunchNextActivity();
    }

    // Launch Customer Activity after 3 seconds
    private void LaunchNextActivity() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashscreenActivity.this, CustomerActivity.class);
                startActivity(i);

                finish();
            }
        }, 3000);
    }


}
