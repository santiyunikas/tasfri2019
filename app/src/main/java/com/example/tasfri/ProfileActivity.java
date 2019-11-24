package com.example.tasfri;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseUser user;
    private FirebaseAuth auth;
    private TextView name, email, agency;
    private LinearLayout lineName, lineEmail, lineAgency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbarSet();
        initView();
    }

    private void initView(){
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        agency = findViewById(R.id.agency);

        lineName = findViewById(R.id.lineName);
        lineEmail = findViewById(R.id.lineEmail);
        lineAgency = findViewById(R.id.lineAgency);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (auth.getUid()!= null){
            reference = FirebaseDatabase.getInstance().getReference("user");
            reference.child(user.getUid()).addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users users = dataSnapshot.getValue(Users.class);
                    if(users.getRole().equalsIgnoreCase("user")){
                        name.setText(users.getName());
                        email.setText(users.getEmail());
                        agency.setText(users.getInstansi());
                    }else{
                        lineAgency.setVisibility(View.GONE);
                        name.setText(users.getName());
                        email.setText(users.getEmail());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @SuppressLint({"NewApi", "ResourceType"})
    private void toolbarSet(){
        Toolbar toolbar = findViewById(R.id.toolbarProfile);
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
