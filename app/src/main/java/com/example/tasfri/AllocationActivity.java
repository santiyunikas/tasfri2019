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

public class AllocationActivity extends AppCompatActivity {
    private TextView txNull;
    private EditText fromAllo, toAllo;
    private String fromAlloText, toAlloText;
    private Button srcAllo, ftnote;
    private Spinner spSatuan;
    private RecyclerView data;

    private FirebaseDatabase getDb;
    DatabaseReference reference;
    FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference tabel_allo;

    AlloAdapter adapter;
    List<Alokasi> listAllo = new ArrayList<>();

    String allocationTx, aplikasiTx, footnoteTx, freqEndTx, freqRangeTx, freqStartTx, freqStartEndTx, priorityTx, satuanTx, keyTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocation);
        toolbarSet();
        initView();
    }

    private void initView(){
        data = (RecyclerView) findViewById(R.id.data);
        data.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlloAdapter(listAllo);
        data.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        getDb = FirebaseDatabase.getInstance();
        txNull = findViewById(R.id.txResult);

        toAllo = findViewById(R.id.toAllo);
        fromAllo = findViewById(R.id.fromAllo);

        spSatuan = findViewById(R.id.spAllo);
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
                startActivity(new Intent(AllocationActivity.this, FootnoteActivity.class));
            }
        });

        srcAllo = findViewById(R.id.srcAllo);
        srcAllo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toAllo.getText().toString().isEmpty() || fromAllo.getText().toString().isEmpty()) {
                    Toast.makeText(AllocationActivity.this, "Input frequency range", Toast.LENGTH_SHORT).show();
                }else if (Double.valueOf(fromAllo.getText().toString())>Double.valueOf(toAllo.getText().toString())){
                    toAllo.setError("Must higher than Frequency From");
                    fromAllo.setError("Must lower than Frequency To");
                } else {
                    addData();
                }
            }});

    }

    private void addData(){
        final Alokasi[] alloData = {new Alokasi()};
        listAllo.removeAll(listAllo);
        fromAlloText = fromAllo.getText().toString();
        toAlloText = toAllo.getText().toString();

        alloData[0].setFreqStartEnd("Frequency Bands");
        alloData[0].setAplikasi("");
        alloData[0].setSatuan("");
        alloData[0].setAllocation("Allocations");
        alloData[0].setPrimarySecondary("");
        alloData[0].setFreqStart("");
        alloData[0].setFreqEnd("");
        alloData[0].setFootnote("Footnotes");
        alloData[0].setFreqRange("");
        alloData[0].setFreqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary("");
        listAllo.add(alloData[0]);

        tabel_allo = getDb.getReference("Data Alokasi");
        tabel_allo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean state = false;
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (Double.valueOf(ds.child("freqStart").getValue().toString()) >= Double.valueOf(fromAlloText) && Double.valueOf(ds.child("freqEnd").getValue().toString()) <= Double.valueOf(toAlloText) && ds.child("satuan").getValue().toString().equalsIgnoreCase(spSatuan.getSelectedItem().toString())){
                        allocationTx = ds.child("allocation").getValue().toString();
                        aplikasiTx = ds.child("aplikasi").getValue().toString();
                        footnoteTx = ds.child("footnote").getValue().toString();
                        freqStartTx = ds.child("freqStart").getValue().toString();
                        freqEndTx = ds.child("freqEnd").getValue().toString();
                        freqRangeTx = ds.child("freqRange").getValue().toString();
                        freqStartEndTx = ds.child("freqStartEnd").getValue().toString();
                        priorityTx = ds.child("primarySecondary").getValue().toString();
                        satuanTx = ds.child("satuan").getValue().toString();
                        keyTx = ds.child("freqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary").getValue().toString();

                        alloData[0] = new Alokasi();

                        alloData[0].setAllocation(allocationTx);
                        alloData[0].setAplikasi(aplikasiTx);
                        alloData[0].setFootnote(footnoteTx);
                        alloData[0].setFreqEnd(freqEndTx);
                        alloData[0].setFreqRange(freqRangeTx);
                        alloData[0].setFreqStart(freqStartTx);
                        alloData[0].setFreqStartEnd(freqStartTx +" - "+ freqEndTx +" "+ satuanTx);
                        alloData[0].setFreqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary(keyTx);
                        alloData[0].setPrimarySecondary(priorityTx);
                        alloData[0].setSatuan(satuanTx);

                        listAllo.add(alloData[0]);
                        state=true;
                    }

                }

                if(state==false){
                    //Toast.makeText(AllocationActivity.this, "Can't find data", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AllocationActivity.this);
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
                    adapter.notifyDataSetChanged();
                    ftnote.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint({"NewApi", "ResourceType"})
    private void toolbarSet(){
        Toolbar toolbar = findViewById(R.id.toolbarAllo);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(AllocationActivity.this, HomeActivity.class));
            }
        });
        toolbar.inflateMenu(R.menu.logout);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(AllocationActivity.this);
                        builder.setTitle(R.string.app_name);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setMessage("Do you want to exit?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        auth.getInstance().signOut();
                                        startActivity(new Intent(AllocationActivity.this, HomeActivity.class));
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

