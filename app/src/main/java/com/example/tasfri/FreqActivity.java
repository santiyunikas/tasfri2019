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

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FreqActivity extends AppCompatActivity {
    private TextView txNull;
    private EditText fromFreq, toFreq;
    private String fromFreqText, toFreqText;
    private Button srcFreq, ftnote;
    private Spinner spSatuan;
    private RecyclerView data;

    private FirebaseDatabase getDb;
    DatabaseReference reference;
    private DatabaseReference tabel_allo;

    String  allocationTx, aplikasiTx, freqStartTx, freqEndTx, freqStartEndTx, primarySecondaryTx, satuanTx, footnoteTx, freqRangeTx;

    FreqAdapter adapter;
    List<Alokasi> listAlo = new ArrayList<>();
    AlertDialog.Builder builder;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freq);
        toolbarSet();
        initView();
    }

    private void initView(){
        data = (RecyclerView) findViewById(R.id.data);
        data.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FreqAdapter(listAlo);
        data.setAdapter(adapter);

        txNull = findViewById(R.id.txResultFreq);

        toFreq = findViewById(R.id.toFreq);
        fromFreq = findViewById(R.id.fromFreq);

        spSatuan = findViewById(R.id.spFreq);
        data = findViewById(R.id.data);
        data.setVisibility(View.GONE);


        getDb = FirebaseDatabase.getInstance();
        reference = getDb.getReference();

        ftnote = findViewById(R.id.btnFootnote);
        ftnote.setVisibility(View.GONE);
        ftnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FreqActivity.this, FootnoteActivity.class));
            }
        });

        srcFreq = findViewById(R.id.srcFreq);
        srcFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toFreq.getText().toString().isEmpty() || fromFreq.getText().toString().isEmpty()) {
                    Toast.makeText(FreqActivity.this, "Input frequency range", Toast.LENGTH_SHORT).show();
                }else if (Double.valueOf(fromFreq.getText().toString())>Double.valueOf(toFreq.getText().toString())){
                    toFreq.setError("Must higher than Frequency From");
                    fromFreq.setError("Must lower than Frequency To");
                } else {
                    addData();
                }
            }
        });
    }

    private void addData(){
        final Alokasi[] aloData = {new Alokasi()};
        listAlo.removeAll(listAlo);
        fromFreqText = fromFreq.getText().toString();
        toFreqText = toFreq.getText().toString();

        aloData[0].setFreqRange("");
        aloData[0].setPrimarySecondary("");
        aloData[0].setAllocation("Allocations");
        aloData[0].setAplikasi("Applications");
        aloData[0].setFreqStart("");
        aloData[0].setFreqEnd("");
        aloData[0].setSatuan("");
        aloData[0].setFreqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary("");
        aloData[0].setFreqStartEnd("Frequency Bands");
        aloData[0].setFootnote("Footnotes");
        listAlo.add(aloData[0]);

        tabel_allo = getDb.getReference("Data Alokasi");
        tabel_allo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean state = false;
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (Double.valueOf(ds.child("freqStart").getValue().toString()) >= Double.valueOf(fromFreqText) && Double.valueOf(ds.child("freqEnd").getValue().toString()) <= Double.valueOf(toFreqText) && ds.child("satuan").getValue().toString().equalsIgnoreCase(spSatuan.getSelectedItem().toString())){
                        freqStartTx = ds.child("freqStart").getValue().toString();
                        freqEndTx = ds.child("freqEnd").getValue().toString();
                        allocationTx = ds.child("allocation").getValue().toString();
                        aplikasiTx = ds.child("aplikasi").getValue().toString();
                        freqStartEndTx = ds.child("freqStartEnd").getValue().toString();
                        primarySecondaryTx = ds.child("primarySecondary").getValue().toString();
                        satuanTx = ds.child("satuan").getValue().toString();
                        footnoteTx = ds.child("footnote").getValue().toString();

                        aloData[0] = new Alokasi();

                        aloData[0].setFreqStartEnd(freqStartTx+" - "+freqEndTx+" "+ satuanTx);
                        aloData[0].setAplikasi(aplikasiTx);
                        aloData[0].setAllocation(allocationTx);
                        aloData[0].setSatuan(satuanTx);
                        aloData[0].setFootnote(footnoteTx);
                        listAlo.add(aloData[0]);
                        state=true;
                    }
                }

                if(state==false){
                    //Toast.makeText(FreqActivity.this, "Can't find data", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(FreqActivity.this);
                    builder.setTitle(R.string.app_name);
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setMessage(R.string.freq_cant_find_data)
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

    @SuppressLint("NewApi")
    private void toolbarSet(){
        Toolbar toolbar = findViewById(R.id.toolbarFreq);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(FreqActivity.this);
                        builder.setTitle(R.string.app_name);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setMessage("Do you want to exit?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(new Intent(FreqActivity.this, HomeActivity.class));
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
