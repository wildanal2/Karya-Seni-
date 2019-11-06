package com.miewlabs.karyaseni27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LupaPasswordActivity extends AppCompatActivity {
    String TAG = "LupaPasswordActivity";

    FirebaseAuth mAuth;
    EditText etEmail;
    Button btnForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        btnForgot = (Button) findViewById(R.id.btn_resetpwd_forgot);
        etEmail = (EditText) findViewById(R.id.et_email_forgot);
        mAuth = FirebaseAuth.getInstance();

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etEmail.getText().toString().isEmpty()){
                    etEmail.setError("isi Email Terlebih dahulu");
                    return;
                }else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(etEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                        Toast.makeText(LupaPasswordActivity.this, "reset Password Sukses Cek Email", Toast.LENGTH_SHORT).show();

                                        finish();
                                    }else{
                                        Toast.makeText(LupaPasswordActivity.this, "Terjadi Kesalahan "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
    }
}
