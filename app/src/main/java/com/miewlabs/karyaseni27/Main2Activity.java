package com.miewlabs.karyaseni27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.miewlabs.karyaseni27.firebase.UploadImages;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    
    public static final String TAG = "Main2Activity"; 
    private Uri imgPath = null;
    private String uid = null;

    PhotoView imageView;
    Button btnUpload;
    //fbase
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imgPath = Uri.parse(getIntent().getStringExtra("img_path"));
        uid = FirebaseAuth.getInstance().getUid();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        mStorageRef = firebaseStorage.getReference("uploads/"+uid);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("users/"+uid);

        //finding
        imageView = findViewById(R.id.imagev);
        btnUpload = findViewById(R.id.btn_uploadfirebase);

        imageView.setImageURI(imgPath);


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUpload.setEnabled(false);
                uploadFile();
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        Log.d(TAG, "uploadFile: on progres");
        Toast.makeText(this, "Uploading..", Toast.LENGTH_LONG).show();
        if (imgPath != null){
             final StorageReference fileRef = mStorageRef.child( System.currentTimeMillis()+"."+getFileExtension(imgPath) );
             fileRef.putFile(imgPath)
                     .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                         @Override
                         public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             Toast.makeText(Main2Activity.this, "Upload succcess", Toast.LENGTH_SHORT).show();


                         }
                     })
                     .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                             Log.d(TAG, "onComplete: on Completer Upload File but in progres to update data");
                             fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                 @Override
                                 public void onSuccess(Uri uri) {
                                     Uri downloadUrl = uri;

                                     UploadImages upl = new UploadImages("", downloadUrl.toString());

                                     String nameid = mDatabaseRef.push().getKey();
                                     mDatabaseRef.child(nameid).setValue(upl);

                                     Log.d(TAG, "onSuccess: Sukses insert db");
                                     Toast.makeText(Main2Activity.this, "Sukses Upload Img", Toast.LENGTH_SHORT).show();
                                     finish();
                                 }
                             });
                         }
                     })
                     .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(Main2Activity.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                         }
                     })
                     .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                         @Override
                         public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                         }
                     });
        }else {
            Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT).show();
        }
    }

}
