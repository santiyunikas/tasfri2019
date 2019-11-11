package com.example.tasfri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth auth;
    Button btnFreq, btnAllo, btnApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUi();
        initToolbar();

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnected()){
            this.startActivity(new Intent(Settings.ACTION_SETTINGS));
            Toast.makeText(this, "Activated your internet connection", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            onStart();
        }
    }


    private void initUi(){
        btnFreq = findViewById(R.id.btnFreq);
        btnAllo = findViewById(R.id.btnAllo);
        btnApp = findViewById(R.id.btnApp);


        btnFreq.setOnClickListener(this);
        btnAllo.setOnClickListener(this);
        btnApp.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    private void initToolbar (){
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setActionBar(toolbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnFreq :
                startActivity(new Intent(HomeActivity.this, FreqActivity.class));
                break;
            case R.id.btnAllo :
                startActivity( new Intent(HomeActivity.this, AllocationActivity.class));
                break;
            case R.id.btnApp :
                startActivity( new Intent(HomeActivity.this, ApplicationActivity.class));
                break;
        }
    }
}
