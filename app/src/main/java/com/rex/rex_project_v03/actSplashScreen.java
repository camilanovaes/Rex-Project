package com.rex.rex_project_v03;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;

public class actSplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash_screen);

        Handler handler = new Handler();
        if (checkInfo()) {

            Intent intent = new Intent(getApplicationContext(), actMain.class);
            startActivity(intent);
            finish();

        } else {
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (savedInstanceState == null) {
                        //Adicionar o fragmento inicial
                        setContentView(R.layout.frg_slashcontainer);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.fragContainerTest, new frgLogin())
                                .commit();
                    }
                }
            }, 3000);
        }

    }

    //Checa a informação do usuário
    public boolean checkInfo(){
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String check_numSerie   = sharedPref.getString("key_numSerie", "");
        String check_recipiente = sharedPref.getString("key_recipiente", "");

        if (Objects.equals(check_numSerie, "R3X") && (Objects.equals(check_recipiente, "0") || Objects.equals(check_recipiente, "1") || Objects.equals(check_recipiente, "2"))){
            return true;
        } else {
            return false;
        }

    }

}