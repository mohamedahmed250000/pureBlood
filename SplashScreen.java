package elteam.pureblood.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import elteam.pureblood.R;

import static elteam.pureblood.activities.HomeActivity.loadLocale;

public class SplashScreen extends AppCompatActivity {

    private ImageView Logo , progressbar;
    private Animation animationImage, animationProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        setContentView(R.layout.splash_screen);

        Logo = findViewById(R.id.logo);
        progressbar = findViewById(R.id.progress_bar);

        animationImage = AnimationUtils.loadAnimation(this, R.anim.splash_screen_image);
        animationProgressBar = AnimationUtils.loadAnimation(this, R.anim.splash_screen_progress_bar);

        Logo.setAnimation(animationImage);
        progressbar.setAnimation(animationProgressBar);

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
