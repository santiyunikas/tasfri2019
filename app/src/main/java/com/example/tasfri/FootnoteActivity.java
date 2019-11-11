package com.example.tasfri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FootnoteActivity extends AppCompatActivity {
    private TextView ftnote, inform, txNull;
    private FirebaseAuth auth;
    private DatabaseReference tabel_ft;
    private FirebaseDatabase getDb;
    private EditText ftnoteEd;
    private Button srcFt;
    private LinearLayout data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footnote);

        toolbarSet();
        initView();
    }

    private void initView(){
        ftnoteEd = findViewById(R.id.ftnoteIn);
        auth = FirebaseAuth.getInstance();
        getDb = FirebaseDatabase.getInstance();
        txNull = findViewById(R.id.txResult);
        data = findViewById(R.id.data);
        data.setVisibility(View.GONE);

        srcFt = findViewById(R.id.srcFt);
        srcFt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ftnoteEd.getText().toString().isEmpty()){
                    Toast.makeText(FootnoteActivity.this, "Input footnote code", Toast.LENGTH_SHORT).show();
                }else{
                    data.setVisibility(View.VISIBLE);
                    data.removeAllViews();
                    addData();
                }
            }
        });
    }
    private void addData(){
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.ftnote_row, null);
        data.addView(addView, 0);

        final String txFt = ftnoteEd.getText().toString();
        final int[] i = {1};
        tabel_ft = getDb.getReference("Data footnote");
        tabel_ft.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String footnoteText="", information="";
                boolean state=false;
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (ds.child("footnote_No").getValue().toString().equalsIgnoreCase(txFt)){
                        footnoteText = ds.child("footnote_No").getValue().toString();
                        information = ds.child("footnote_Text").getValue().toString();
                        loadData(footnoteText, information, i[0]);
                        i[0]++;
                        state=true;
                    }
                }
                if(state==false){
                    Toast.makeText(FootnoteActivity.this, "Can't find data", Toast.LENGTH_SHORT).show();
                    txNull.setVisibility(View.VISIBLE);
                    data.setVisibility(View.GONE);
                }else{
                    txNull.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadData(String footnoteText, String information, int i){
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.ftnote_row,null);
        ftnote = addView.findViewById(R.id.col1);
        inform = addView.findViewById(R.id.col2);

        ftnote.setText(footnoteText);
        inform.setText(information);

        data.addView(addView, i);
    }

    @SuppressLint({"NewApi", "ResourceType"})
    private void toolbarSet(){
        Toolbar toolbar = findViewById(R.id.toolbarFt);
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
