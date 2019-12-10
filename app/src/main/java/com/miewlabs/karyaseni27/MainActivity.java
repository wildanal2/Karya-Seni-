package com.miewlabs.karyaseni27;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btnCam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView botNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        botNav.setOnNavigationItemSelectedListener(navListener);

        btnCam = findViewById(R.id.fab);
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog dialog = new BottomSheetDialog( MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.menu_camera_layout);

                RelativeLayout btnCam = dialog.findViewById(R.id.btn_opncamera);
                RelativeLayout btnGalery = dialog.findViewById(R.id.btn_opngalery);

                btnCam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkCameraHardware(MainActivity.this);

                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i,0);

                        Toast.makeText(MainActivity.this, "Camera clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                btnGalery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "Gallery clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFrag = null; //Fragment Yang akan ditampilkan

                    switch (menuItem.getItemId()){
                        case R.id.nav_dasboard:
                            selectedFrag = new DasboardFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_home,selectedFrag).commit();
                            break;
                        case R.id.nav_setting:
                            selectedFrag = new SettingFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_home,selectedFrag).commit();
                            break;
                    }

                    return true;
                }
            };


    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bit = (Bitmap) data.getExtras().get("data");

    }
}
