package com.example.tasfri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;



public class ForgotpassActivity extends AppCompatActivity {
    private EditText emailEd;
    private Button emailRs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        toolbarSet();
        initView();
    }

    private void initView(){
        emailEd = findViewById(R.id.edEmail);
        emailRs = findViewById(R.id.btnReset);
        emailRs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPass();
            }
        });

    }

    private void forgotPass(){
        if (emailEd.getText().toString()!=null){
            FirebaseAuth.getInstance().sendPasswordResetEmail(emailEd.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Email sent", "Email sent.");
                                Toast.makeText(ForgotpassActivity.this, "Reset email send, check your mailbox", Toast.LENGTH_SHORT).show();
                                emailEd.setText("");
                            }
                        }
                    });
        }else{
            emailEd.setError("Fill email address");
        }
    }

    @SuppressLint({"NewApi", "ResourceType"})
    private void toolbarSet(){
        Toolbar toolbar = findViewById(R.id.toolbarPass);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

