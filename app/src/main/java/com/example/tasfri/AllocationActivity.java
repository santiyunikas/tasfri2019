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
    private EditText fromAllo, toAllo, alloEd, appEd, ftnoteEd, freqEndEd, freqStartEd;
    private Spinner priorEd, satuanEd;
    private String fromAlloText, toAlloText;
    private Button srcAllo, update,cancel, ftnote;
    private Spinner spSatuan;
    private RecyclerView data;

    private FirebaseDatabase getDb;
    DatabaseReference reference;
    FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference tabel_allo;

    AlloAdapter adapter;
    List<Alokasi> listAllo = new ArrayList<>();
    AlertDialog.Builder builder;
    AlertDialog dialog;

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


        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClick(int position, Alokasi alloData) {
                builder = new AlertDialog.Builder(AllocationActivity.this);
                builder.setTitle("Update Allocation Info");
                builder.setCancelable(false);
                View view = LayoutInflater.from(AllocationActivity.this).inflate(R.layout.update_allo,null,false);
                InitUpdateDialog(position, view, alloData);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
            }

            @Override
            public void OnItemClick(int position, Aplikasi appData) {

            }

            @Override
            public void OnItemClick(int position, Assignment alloData) {

            }
        });

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

    private void InitUpdateDialog(final int position, View view, final Alokasi alloDataIn) {
        alloEd = view.findViewById(R.id.ed_update_alokasi_allo);
        appEd = view.findViewById(R.id.ed_update_aplikasi_allo);
        ftnoteEd = view.findViewById(R.id.ed_update_footnote_allo);
        freqEndEd = view.findViewById(R.id.ed_update_freqEnd_allo);
        freqStartEd = view.findViewById(R.id.ed_update_freqStart_allo);
        priorEd = view.findViewById(R.id.ed_update_prior_allo);
        satuanEd = view.findViewById(R.id.ed_update_satuan_allo);

        update = view.findViewById(R.id.btn_update_allo);
        cancel = view.findViewById(R.id.btn_cancelUpd_allo);

        alloEd.setText(alloDataIn.getAllocation());
        appEd.setText(alloDataIn.getAplikasi());
        ftnoteEd.setText(alloDataIn.getFootnote());
        freqEndEd.setText(alloDataIn.getFreqEnd());
        freqStartEd.setText(alloDataIn.getFreqStart());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Double.valueOf(freqStartEd.getText().toString())>Double.valueOf(freqEndEd.getText().toString())){
                    freqEndEd.setError("Must higher than Frequency Start");
                    freqStartEd.setError("Must lower than Frequency End");
                }else{
                    if(ftnoteEd.getText().toString().isEmpty()||freqStartEd.getText().toString().isEmpty()||freqEndEd.getText().toString().isEmpty()||alloEd.getText().toString().isEmpty()||appEd.getText().toString().isEmpty()||priorEd.getSelectedItem().toString().equalsIgnoreCase("Priority")||satuanEd.getSelectedItem().toString().equalsIgnoreCase("Select Unit")){
                        Toast.makeText(AllocationActivity.this, "Fill the form completely", Toast.LENGTH_LONG).show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(AllocationActivity.this);
                        builder.setTitle(R.string.app_name);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setMessage("Are sure to make changes ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (priorEd.getSelectedItem().toString().equalsIgnoreCase("primary")){
                                            allocationTx = alloEd.getText().toString().toUpperCase();
                                        }else{
                                            allocationTx = alloEd.getText().toString();
                                        }
                                        aplikasiTx = appEd.getText().toString();
                                        footnoteTx = ftnoteEd.getText().toString();
                                        freqEndTx = freqEndEd.getText().toString();
                                        freqRangeTx = String.valueOf(Double.valueOf(freqStartTx)-Double.valueOf(freqEndTx));
                                        freqStartTx = freqStartEd.getText().toString();
                                        freqStartEndTx =freqStartEd.getText().toString()+"-"+freqEndEd.getText().toString();
                                        satuanTx = satuanEd.getSelectedItem().toString();
                                        priorityTx = priorEd.getSelectedItem().toString();
                                        keyTx = freqStartEndTx+"_"+satuanTx+"_"+allocationTx+"_"+aplikasiTx+"_"+footnoteTx+"_"+priorityTx;

                                        reference = FirebaseDatabase.getInstance().getReference().child("Data Alokasi");
                                        Query a = reference.orderByChild("freqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary").equalTo(alloDataIn.getFreqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary());
                                        a.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot ds: dataSnapshot.getChildren()){
                                                    ds.getRef().child("allocation").setValue(allocationTx);
                                                    ds.getRef().child("aplikasi").setValue(aplikasiTx);
                                                    ds.getRef().child("footnote").setValue(footnoteTx);
                                                    ds.getRef().child("freqEnd").setValue(freqEndTx);
                                                    ds.getRef().child("freqRange").setValue(freqRangeTx);
                                                    ds.getRef().child("freqStart").setValue(freqStartTx);
                                                    ds.getRef().child("freqStartEnd").setValue(freqStartEndTx);
                                                    ds.getRef().child("satuan").setValue(satuanTx);
                                                    ds.getRef().child("primarySecondary").setValue(priorityTx);
                                                    ds.getRef().child("freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan").setValue(keyTx);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        Alokasi alloData = new Alokasi();

                                        alloData.setAllocation(allocationTx);
                                        alloData.setAplikasi(aplikasiTx);
                                        alloData.setFootnote(footnoteTx);
                                        alloData.setFreqEnd(freqEndTx);
                                        alloData.setFreqRange(freqRangeTx);
                                        alloData.setFreqStart(freqStartTx);
                                        alloData.setFreqStartEnd(freqStartEndTx);
                                        alloData.setPrimarySecondary(priorityTx);
                                        alloData.setSatuan(satuanTx);
                                        alloData.setFreqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary(keyTx);

                                        adapter.UpdateData(position, alloData);
                                        Toast.makeText(AllocationActivity.this,"Allocation Updated..", Toast.LENGTH_SHORT).show();

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
                                        startActivity(new Intent(AllocationActivity.this, OptionActivity.class));
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

