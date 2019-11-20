package com.example.tasfri;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class OptionActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnGuest, btnUser, btnAdmin;
   // ImageView btnAdmin;
    private FirebaseAuth auth;
    private Intent intent;
    private int actionTake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
     //   actionTake = 1;
        btnAdmin = findViewById(R.id.btn_admin);
        btnGuest = findViewById(R.id.btn_guest);
        btnUser = findViewById(R.id.btn_user);


        btnAdmin.setOnClickListener(this);
        btnGuest.setOnClickListener(this);
        btnUser.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        if (auth.getUid()!= null){
            intent = new Intent(OptionActivity.this, HomeActivity.class);
            finish();
            startActivity(intent);
            Log.d("auth.getUid()!= null","no");
        }
        Log.d("auth.getUid()!= null","yes");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.btn_admin :
            case R.id.btn_admin :
              //  actionTake++;
               // if(actionTake>=10){
                    if (auth.getUid()!= null){
                        intent = new Intent(OptionActivity.this, HomeActivity.class);
                        finish();
                    }else{
                        intent = new Intent(OptionActivity.this, AdminsignActivity.class);
                        finish();
                    }
                    startActivity(intent);
                //}
                break;
            case R.id.btn_guest :
                intent = new Intent(OptionActivity.this, HomeActivity.class);
                finish();
                startActivity(intent);
                break;
            case R.id.btn_user :
                if (auth.getUid()!= null){
                    intent = new Intent(OptionActivity.this, HomeActivity.class);
                    finish();
                }else {
                    intent = new Intent(OptionActivity.this, SignupActivity.class);
                    finish();
                }
                startActivity(intent);
                break;
        }
    }
}