package com.rex.rex_project_v03;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.logging.Handler;


/**
 * A simple {@link Fragment} subclass.
 */
public class frgSplashScreen extends Fragment {


    public frgSplashScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frg_splash_screen, container, false);


        return view;
    }

    private void callLogin(){
        Intent it = new Intent(getActivity(), actMain.class);
        startActivity(it);
    }

}
