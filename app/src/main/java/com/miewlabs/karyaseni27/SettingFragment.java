package com.miewlabs.karyaseni27;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;


public class SettingFragment extends Fragment {

    Button logout;
    View v;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_setting, container, false);
        setListener();

        return v;
    }

    public void setListener(){
        logout = v.findViewById(R.id.btn_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    public void signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut(); //Firebase
        LoginManager.getInstance().logOut();  //FB

        // [END auth_sign_out]
        startActivity(new Intent( getActivity() ,LoginActivity.class));
        getActivity().finish();
    }

}
