package com.miewlabs.karyaseni27;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int IMAGE_REQUEST = 1;
    public static final String TAG = "MainActivity";
    private Uri mImageUri;
    private FloatingActionButton btnCam;
    private String action = null;

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
                final BottomSheetDialog dialog = new BottomSheetDialog( MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.menu_camera_layout);

                RelativeLayout btnCam = dialog.findViewById(R.id.btn_opncamera);
                RelativeLayout btnGalery = dialog.findViewById(R.id.btn_opngalery);

                btnCam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        action = "cam";
                        dialog.dismiss();
                        checkCameraHardware(MainActivity.this);
                        // create intent cam
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        if (cameraIntent.resolveActivity(getPackageManager()) != null){
                            Log.i(TAG, "on resolve not null img null");
                            File imageFile = null;

                            try {
                                imageFile = getImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (imageFile != null){
                                mImageUri = FileProvider.getUriForFile(MainActivity.this,"com.miewlabs.karyaseni27.fileprovider",imageFile);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                                Log.i(TAG, "onClick: img not null");

                                startActivityForResult(cameraIntent, IMAGE_REQUEST);
                            }
                        }

                        //END
                        Toast.makeText(MainActivity.this,"Sukses ", Toast.LENGTH_SHORT).show();
                    }
                });
                btnGalery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        action = "file";
                        dialog.dismiss();
                        openFileChooser();
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
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode:"+requestCode+" | resultCode:"+resultCode);
        Log.d(TAG, "onActivityResult: aksi:"+action);
        //ifsisa && data != null && data.getData()!=null
        if (requestCode == IMAGE_REQUEST && resultCode==RESULT_OK ){
            // jika aksi dari pilihan file images
            if (action == "file"){
                mImageUri = data.getData();
            }

            Log.i(TAG, "onActivityResult| result:"+resultCode+" |reqcod:"+requestCode+" |data:"+mImageUri.toString() );

            Intent in = new Intent(getApplicationContext(),Main2Activity.class);
            in.putExtra("img_path",mImageUri.toString());
            startActivity(in);
        }
    }


    private File getImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String img_name = "IMG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imgFile = File.createTempFile(img_name,".jpg",storageDir);
        return imgFile;
    }

    //open image
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }
}
