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
    private EditText fromApp, toApp, freqstartEd,freqEndEd, applicationEd, footnoteEd;
    private Spinner satuanEd;
    private String fromAppText, toAppText;
    private Button srcApp, ftnote, update, cancel;
    private Spinner spSatuan;
    private RecyclerView data;

    private FirebaseDatabase getDb;
    DatabaseReference reference;
    FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference tabel_app;

    String freqStartTx, freqEndTx, satuanTx, aplikasiTx, footnoteTx, freqRangeTx, keyTx;

    com.example.tasfri.AppAdapter adapter;
    List<com.example.tasfri.Aplikasi> listApp = new ArrayList<>();
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
        adapter = new com.example.tasfri.AppAdapter(listApp);
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

        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClick(int position, Assignment appData) {
            }

            @Override
            public void OnItemClick(int position, com.example.tasfri.Aplikasi appData) {
                builder = new AlertDialog.Builder(ApplicationActivity.this);
                builder.setTitle("Update Application Info");
                builder.setCancelable(false);
                View view = LayoutInflater.from(ApplicationActivity.this).inflate(R.layout.update_app,null,false);
                InitUpdateDialog(position, view, appData);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
            }

            @Override
            public void OnItemClick(int position, Alokasi alloData) {

            }
        });

    }

    private void addData(){
        final com.example.tasfri.Aplikasi[] appData = {new com.example.tasfri.Aplikasi()};
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

                        appData[0] = new com.example.tasfri.Aplikasi();

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

    private void InitUpdateDialog(final int position, View view, final com.example.tasfri.Aplikasi appDataIn) {
        freqstartEd  = view.findViewById(R.id.ed_update_freqStart_app);
        freqEndEd = view.findViewById(R.id.ed_update_freqEnd_app);
        satuanEd = view.findViewById(R.id.ed_update_satuan_app);
        applicationEd  = view.findViewById(R.id.ed_update_apl_app);
        footnoteEd = view.findViewById(R.id.ed_update_footnote_app);

        update = view.findViewById(R.id.btn_update_app);
        cancel = view.findViewById(R.id.btn_cancelUpd_app);

        freqstartEd.setText(appDataIn.getFreqStart());
        freqEndEd.setText(appDataIn.getFreqEnd());
        applicationEd.setText(appDataIn.getApplication());
        footnoteEd.setText(appDataIn.getFootnote());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Double.valueOf(freqstartEd.getText().toString())>Double.valueOf(freqEndEd.getText().toString())){
                    freqEndEd.setError("Must higher than Frequency Start");
                    freqstartEd.setError("Must lower than Frequency End");
                }else{
                    if(footnoteEd.getText().toString().isEmpty()||freqstartEd.getText().toString().isEmpty()||freqEndEd.getText().toString().isEmpty()||applicationEd.getText().toString().isEmpty()||satuanEd.getSelectedItem().toString().equalsIgnoreCase("Select Unit")){
                        Toast.makeText(ApplicationActivity.this, "Fill the form completely", Toast.LENGTH_LONG).show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationActivity.this);
                        builder.setTitle(R.string.app_name);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setMessage("Are you sure you make changes ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        freqStartTx = freqstartEd.getText().toString();
                                        freqEndTx=freqEndEd.getText().toString();
                                        satuanTx=satuanEd.getSelectedItem().toString();
                                        aplikasiTx=applicationEd.getText().toString();
                                        footnoteTx=footnoteEd.getText().toString();
                                        freqRangeTx=String.valueOf(Double.valueOf(freqEndEd.getText().toString())-Double.valueOf(freqEndEd.getText().toString()));
                                        keyTx=freqstartEd.getText().toString()+"-"+freqEndEd.getText().toString()+"_"+satuanEd.getSelectedItem().toString()+"_"+applicationEd.getText().toString()+"_"+footnoteEd.getText().toString();

                                        reference = FirebaseDatabase.getInstance().getReference().child("Data Aplikasi");
                                        Query a = reference.orderByChild("freqStartEnd_satuan_application_footnote").equalTo(appDataIn.getFreqStartEnd_satuan_application_footnote());
                                        a.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot ds: dataSnapshot.getChildren()){
                                                    ds.getRef().child("freqStart").setValue(freqStartTx);
                                                    ds.getRef().child("freqEnd").setValue(freqEndTx);
                                                    ds.getRef().child("freqStartEnd").setValue(freqStartTx+"-"+freqEndTx);
                                                    ds.getRef().child("application").setValue(aplikasiTx);
                                                    ds.getRef().child("satuan").setValue(satuanTx);
                                                    ds.getRef().child("footnote").setValue(footnoteTx);
                                                    ds.getRef().child("freqRange").setValue(freqRangeTx);
                                                    ds.getRef().child("freqStartEnd_satuan_application_footnote").setValue(keyTx);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        com.example.tasfri.Aplikasi appData = new com.example.tasfri.Aplikasi();

                                        appData.setFreqStartEnd(freqStartTx+"-"+freqEndTx);
                                        appData.setApplication(aplikasiTx);
                                        appData.setSatuan(satuanTx);
                                        appData.setFreqStart(freqStartTx);
                                        appData.setFreqEnd(freqEndTx);
                                        appData.setFreqRange(freqRangeTx);
                                        appData.setFreqStartEnd_satuan_application_footnote(keyTx);
                                        appData.setFootnote(footnoteTx);

                                        adapter.UpdateData(position, appData);
                                        Toast.makeText(ApplicationActivity.this,"Application Updated..",Toast.LENGTH_SHORT).show();

                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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
                                        startActivity(new Intent(ApplicationActivity.this, OptionActivity.class));
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
