package com.miewlabs.karyaseni27;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;


public class SettingFragment extends Fragment {
    public static final String TAG = "SettingFragment";
    Button logout;
    View v;
    TextView uNamed,uMail;
    ImageView imgProf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_setting, container, false);

        //find
        uNamed = v.findViewById(R.id.id_user);
        uMail = v.findViewById(R.id.txv_mail);
        imgProf = v.findViewById(R.id.imageView3);

        setListener();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // check Status Login Fb
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (user != null){
            Log.d(TAG, "onCreateView: : Google id detected "+user);
            uNamed.setText( user.getDisplayName() );
            uMail.setText( user.getEmail() );

            Picasso.get().load(user.getPhotoUrl()).into(imgProf);
        }
        else if (isLoggedIn == true){
            Log.d(TAG, "onCreateView: Fb Detected :"+accessToken.getToken());

            try {
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                try {
                                    Log.v("FACEBOOK LOGIN", response.toString());
                                    String fb_id = object.getString("id");   //FaceBook User ID
                                    String fb_name = object.getString("name");
                                    String fb_email = object.getString("email");
                                    String profilePicUrl = "https://graph.facebook.com/" + fb_id + "/picture?width=200&height=200";
                                    Log.d(TAG, "onCompleted:");

                                    uNamed.setText( fb_name );
                                    uMail.setText( fb_email );
                                    Picasso.get().load(profilePicUrl).into(imgProf);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture.type(small)");
                request.setParameters(parameters);
                request.executeAsync();

            } catch (Exception e) {
                Log.d("ERROR", e.toString());
            }
        }

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
