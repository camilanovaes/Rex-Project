package com.rex.rex_project_v03;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class actSplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Main();
            }
        }, 3000);
    }

        private void Main() {
            Intent it = new Intent(actSplashScreen.this, actMain.class);
            startActivity(it);
            finish();
        }
    }