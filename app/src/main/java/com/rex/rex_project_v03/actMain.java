package com.rex.rex_project_v03;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class actMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            //Adicionar o fragmento inicial
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frgContainer, new frgVolume())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btn_sair) {
            /*SAIR DA CONTA E IR PARA A PÁG DE LOGIN*/
            setContentView(R.layout.frg_slashcontainer);
            Fragment nf = new frgLogin();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragContainerTest, nf).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        int id = item.getItemId();

        if (id == R.id.nav_volume) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frgContainer, new frgVolume())
                    .commit();

        } else if (id == R.id.nav_consumo) {
            Context context = getApplicationContext();
            CharSequence text = "Em breve!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        } else if (id == R.id.nav_perfil) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frgContainer, new frgPerfil())
                    .commit();

        } else if (id == R.id.nav_manual) {
            Context context4 = getApplicationContext();
            CharSequence text = "Utilize a tag NFC para acessar o manual! Atualizações em breve!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context4, text, duration);
            toast.show();

        } else if (id == R.id.nav_config) {
            Context context3 = getApplicationContext();
            CharSequence text = "Em breve!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context3, text, duration);
            toast.show();

        } else if (id == R.id.nav_feedback) {
            Context context4 = getApplicationContext();
            CharSequence text = "Em breve!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context4, text, duration);
            toast.show();

        } else if (id == R.id.nav_sobre) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frgContainer, new frgSobre())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
