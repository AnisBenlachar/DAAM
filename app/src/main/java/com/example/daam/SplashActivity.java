package com.example.daam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TOTAL_MS = 2500;
    private static final long ANIM_DURATION_MS = 800;
    private static final long BOUNCE_MS = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo_icon);

        logo.setAlpha(0f);
        logo.setScaleX(0.85f);
        logo.setScaleY(0.85f);

        logo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(ANIM_DURATION_MS)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> {
                    logo.animate()
                            .scaleX(1.05f)
                            .scaleY(1.05f)
                            .setDuration(BOUNCE_MS)
                            .withEndAction(() ->
                                    logo.animate()
                                            .scaleX(1f)
                                            .scaleY(1f)
                                            .setDuration(BOUNCE_MS)
                            );
                });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, SPLASH_TOTAL_MS);
    }
}
