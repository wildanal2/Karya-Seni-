package com.miewlabs.karyaseni27;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.FileInputStream;

public class Main2Activity extends AppCompatActivity {
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        image = findViewById(R.id.imagev);

        Bitmap bitm = BitmapFactory.decodeFile( getIntent().getStringExtra("img_path") );
        image.setImageBitmap(bitm);
    }

}
