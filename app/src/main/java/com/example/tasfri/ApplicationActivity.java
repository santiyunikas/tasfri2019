package com.example.tasfri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ApplicationActivity extends AppCompatActivity {
    private TextView txNull;
    private EditText fromApp, toApp;
    private String fromAppText, toAppText;
    private Button srcApp, ftnote;
    private Spinner spSatuan;
    private RecyclerView data;

    private FirebaseDatabase getDb;
    DatabaseReference reference;
    FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference tabel_app;

    String freqStartTx, freqEndTx, satuanTx, aplikasiTx, footnoteTx, freqRangeTx, keyTx;

    AppAdapter adapter;
    List<Aplikasi> listApp = new ArrayList<>();
    AlertDialog.Builder builder;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        toolbarSet();
        initView();
    }

    private void initView(){
        data = (RecyclerView) findViewById(R.id.data);
        data.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppAdapter(listApp);
        data.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        getDb = FirebaseDatabase.getInstance();
        txNull = findViewById(R.id.txResult);

        toApp = findViewById(R.id.toApp);
        fromApp = findViewById(R.id.fromApp);

        spSatuan = findViewById(R.id.spApp);
        data = findViewById(R.id.data);
        data.setVisibility(View.GONE);

        user = auth.getCurrentUser();
        getDb = FirebaseDatabase.getInstance();
        reference = getDb.getReference();

        ftnote = findViewById(R.id.btnFootnote);
        ftnote.setVisibility(View.GONE);
        ftnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApplicationActivity.this, FootnoteActivity.class));
            }
        });

        srcApp = findViewById(R.id.srcApp);
        srcApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toApp.getText().toString().isEmpty() || fromApp.getText().toString().isEmpty()) {
                    Toast.makeText(ApplicationActivity.this, "Input frequency range", Toast.LENGTH_SHORT).show();
                }else if (Double.valueOf(fromApp.getText().toString())>Double.valueOf(toApp.getText().toString())){
                    toApp.setError("Must higher than Frequency From");
                    fromApp.setError("Must lower than Frequency To");
                } else {
                    addData();
                }
            }
        });

    }

    private void addData(){
        final Aplikasi[] appData = {new Aplikasi()};
        listApp.removeAll(listApp);
        fromAppText = fromApp.getText().toString();
        toAppText = toApp.getText().toString();

        appData[0].setFreqStartEnd("Frequency Bands");
        appData[0].setSatuan("");
        appData[0].setFreqStart("");
        appData[0].setFreqEnd("");
        appData[0].setApplication("Applications");
        appData[0].setFootnote("Footnotes");
        listApp.add(appData[0]);

        tabel_app = getDb.getReference("Data Aplikasi");
        tabel_app.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean state = false;
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (Double.valueOf(ds.child("freqStart").getValue().toString()) >= Double.valueOf(fromAppText) && Double.valueOf(ds.child("freqEnd").getValue().toString()) <= Double.valueOf(toAppText) && ds.child("satuan").getValue().toString().equalsIgnoreCase(spSatuan.getSelectedItem().toString())){
                        freqStartTx = ds.child("freqStart").getValue().toString();
                        freqEndTx = ds.child("freqEnd").getValue().toString();
                        satuanTx = ds.child("satuan").getValue().toString();
                        aplikasiTx = ds.child("application").getValue().toString();
                        footnoteTx = ds.child("footnote").getValue().toString();
                        freqRangeTx = ds.child("freqRange").getValue().toString();
                        keyTx = ds.child("freqStart").getValue().toString()+"-"+ds.child("freqEnd").getValue().toString()+"_"+ds.child("satuan").getValue().toString()+"_"+ds.child("application").getValue().toString()+"_"+ds.child("footnote").getValue().toString();

                        appData[0] = new Aplikasi();

                        appData[0].setFreqStartEnd(freqStartTx +" - "+ freqEndTx +" "+ satuanTx);
                        appData[0].setApplication(aplikasiTx);
                        appData[0].setSatuan(satuanTx);
                        appData[0].setFreqStart(freqStartTx);
                        appData[0].setFreqEnd(freqEndTx);
                        appData[0].setFreqRange(freqRangeTx);
                        appData[0].setFootnote(footnoteTx);
                        appData[0].setFreqStartEnd_satuan_application_footnote(keyTx);
                        listApp.add(appData[0]);
                        state=true;
                    }

                }

                if(state==false){
                    // Toast.makeText(ApplicationActivity.this, "Can't find data", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationActivity.this);
                    builder.setTitle(R.string.app_name);
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setMessage(R.string.app_cant_find_data)
                            .setCancelable(false)
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                    txNull.setVisibility(View.VISIBLE);
                    data.setVisibility(View.GONE);
                }else{
                    txNull.setVisibility(View.GONE);
                    data.setVisibility(View.VISIBLE);
                    ftnote.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @SuppressLint({"NewApi", "ResourceType"})
    private void toolbarSet(){
        Toolbar toolbar = findViewById(R.id.toolbarApp);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.logout);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationActivity.this);
                        builder.setTitle(R.string.app_name);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setMessage("Do you want to exit?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        auth.getInstance().signOut();
                                        startActivity(new Intent(ApplicationActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                }

                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
