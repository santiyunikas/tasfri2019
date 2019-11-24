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
import android.util.Log;
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

public class AssignmentActivity extends AppCompatActivity {
    private TextView txNull;
    private EditText fromAss, toAss, freqStartEd,freqEndEd, aplikasiEd, instansiEd, startDateEd, endDateEd, ketEd;
    private String fromAssText, toAssText;
    private Button srcAss, update,cancel;
    private Spinner spSatuan, satuanEd, priorityEd;
    private RecyclerView data;
    private FirebaseDatabase getDb;
    DatabaseReference reference;
    FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference tabel_ass;

    String freqStartTx, freqEndTx, satuanTx, aplikasiTx, instansiTx, startDateTx, endDateTx, priorityTx, ketTx, keyTx;

    AssAdapter adapter;
    List<Assignment> listAss = new ArrayList<>();
    AlertDialog.Builder builder;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        toolbarSet();
        initView();
    }

    private void initView(){
        data = (RecyclerView) findViewById(R.id.data);
        data.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AssAdapter(listAss);
        data.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        getDb = FirebaseDatabase.getInstance();
        txNull = findViewById(R.id.txResult);

        toAss = findViewById(R.id.toAss);
        fromAss = findViewById(R.id.fromAss);

        spSatuan = findViewById(R.id.spAss);
        data = findViewById(R.id.data);
        data.setVisibility(View.GONE);

        user = auth.getCurrentUser();
        getDb = FirebaseDatabase.getInstance();
        reference = getDb.getReference();

        srcAss = findViewById(R.id.srcAss);
        srcAss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toAss.getText().toString().isEmpty() || fromAss.getText().toString().isEmpty()) {
                    Toast.makeText(AssignmentActivity.this, "Input frequency range", Toast.LENGTH_SHORT).show();
                }else if (Double.valueOf(fromAss.getText().toString())>Double.valueOf(toAss.getText().toString())){
                    toAss.setError("Must higher than Frequency From");
                    fromAss.setError("Must lower than Frequency To");
                } else {
                    addData();
                }
            }});


        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClick(int position, Assignment assData) {
                builder = new AlertDialog.Builder(AssignmentActivity.this);
                builder.setTitle("Update Assignment Info");
                builder.setCancelable(false);
                View view = LayoutInflater.from(AssignmentActivity.this).inflate(R.layout.update_ass,null,false);
                InitUpdateDialog(position, view, assData);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
            }

            @Override
            public void OnItemClick(int position, Aplikasi appData) {

            }

            @Override
            public void OnItemClick(int position, Alokasi alloData) {

            }
        });

    }

    private void addData(){
        final Assignment[] assData = {new Assignment()};
        listAss.removeAll(listAss);
        fromAssText = fromAss.getText().toString();
        toAssText = toAss.getText().toString();

        assData[0].setFreqStartEnd("Frequency Bands");
        assData[0].setAplikasi("Application");
        assData[0].setSatuan("");
        assData[0].setInstansi("Agency");
        assData[0].setPrimarySecondary("Priority");
        assData[0].setStartDate("Start Date");
        assData[0].setEndDate("End Date");
        assData[0].setFreqStart("");
        assData[0].setFreqEnd("");
        assData[0].setKeterangan("");
        assData[0].setFreqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan("");
        listAss.add(assData[0]);

        tabel_ass = getDb.getReference("Data Assignment");
        tabel_ass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean state = false;
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (Double.valueOf(ds.child("freqStart").getValue().toString()) >= Double.valueOf(fromAssText) && Double.valueOf(ds.child("freqEnd").getValue().toString()) <= Double.valueOf(toAssText) && ds.child("satuan").getValue().toString().equalsIgnoreCase(spSatuan.getSelectedItem().toString())){
                        freqStartTx = ds.child("freqStart").getValue().toString();
                        freqEndTx = ds.child("freqEnd").getValue().toString();
                        satuanTx = ds.child("satuan").getValue().toString();
                        aplikasiTx = ds.child("aplikasi").getValue().toString();
                        instansiTx = ds.child("instansi").getValue().toString();
                        startDateTx = ds.child("startDate").getValue().toString();
                        endDateTx = ds.child("endDate").getValue().toString();
                        priorityTx = ds.child("primarySecondary").getValue().toString();
                        ketTx=ds.child("keterangan").getValue().toString();
                        keyTx = ds.child("freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan").getValue().toString();

                        assData[0] = new Assignment();

                        assData[0].setFreqStartEnd(freqStartTx+"-"+freqEndTx);
                        assData[0].setAplikasi(aplikasiTx);
                        assData[0].setSatuan(satuanTx);
                        assData[0].setInstansi(instansiTx);
                        assData[0].setPrimarySecondary(priorityTx);
                        assData[0].setStartDate(startDateTx);
                        assData[0].setEndDate(endDateTx);
                        assData[0].setFreqStart(freqStartTx);
                        assData[0].setFreqEnd(freqEndTx);
                        assData[0].setKeterangan(ketTx);
                        assData[0].setFreqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan(keyTx);
                        listAss.add(assData[0]);
                        state=true;
                    }

                }

                if(state==false){
                    Toast.makeText(AssignmentActivity.this, "Can't find data", Toast.LENGTH_SHORT).show();
                    txNull.setVisibility(View.VISIBLE);
                    data.setVisibility(View.GONE);
                }else{
                    txNull.setVisibility(View.GONE);
                    data.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void InitUpdateDialog(final int position, View view, final Assignment assDataIn) {
        freqStartEd  = view.findViewById(R.id.ed_update_freqStart_ass);
        freqEndEd = view.findViewById(R.id.ed_update_freqEnd_ass);
        satuanEd = view.findViewById(R.id.ed_update_satuan_ass);
        aplikasiEd = view.findViewById(R.id.ed_update_apl_ass);
        instansiEd= view.findViewById(R.id.ed_update_instansi_ass);
        startDateEd= view.findViewById(R.id.ed_update_startDate_ass);
        endDateEd= view.findViewById(R.id.ed_update_endDate_ass);
        priorityEd= view.findViewById(R.id.ed_update_prior_ass);
        ketEd= view.findViewById(R.id.ed_update_ket_ass);

        update = view.findViewById(R.id.btn_update_ass);
        cancel = view.findViewById(R.id.btn_cancelUpd_ass);

        freqStartEd.setText(assDataIn.getFreqStart());
        freqEndEd.setText(assDataIn.getFreqEnd());
        aplikasiEd.setText(assDataIn.getAplikasi());
        instansiEd.setText(assDataIn.getInstansi());
        startDateEd.setText(assDataIn.getStartDate());
        endDateEd.setText(assDataIn.getEndDate());
        ketEd.setText(assDataIn.getKeterangan());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Double.valueOf(freqStartEd.getText().toString())>Double.valueOf(freqEndEd.getText().toString())){
                    freqEndEd.setError("Must higher than Frequency Start");
                    freqStartEd.setError("Must lower than Frequency End");
                }else{
                    if(ketEd.getText().toString().isEmpty()||endDateEd.getText().toString().isEmpty()||startDateEd.getText().toString().isEmpty()||instansiEd.getText().toString().isEmpty()||freqStartEd.getText().toString().isEmpty()||freqEndEd.getText().toString().isEmpty()||aplikasiEd.getText().toString().isEmpty()||satuanEd.getSelectedItem().toString().equalsIgnoreCase("Select Unit")){
                        Toast.makeText(AssignmentActivity.this, "Fill the form completely", Toast.LENGTH_LONG).show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentActivity.this);
                        builder.setTitle(R.string.app_name);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setMessage("Are you sure you make changes ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        freqStartTx = freqStartEd.getText().toString();
                                        freqEndTx=freqEndEd.getText().toString();
                                        satuanTx=satuanEd.getSelectedItem().toString();
                                        aplikasiTx=aplikasiEd.getText().toString();
                                        instansiTx=instansiEd.getText().toString();
                                        startDateTx=startDateEd.getText().toString();
                                        endDateTx=endDateEd.getText().toString();
                                        priorityTx=priorityEd.getSelectedItem().toString();
                                        ketTx=ketEd.getText().toString();
                                        keyTx=freqStartEd.getText().toString()+"-"+freqEndEd.getText().toString()+"_"+satuanEd.getSelectedItem().toString()+"_"+aplikasiEd.getText().toString()+"_"+instansiEd.getText().toString()+"_"+startDateEd.getText().toString()+"_"+endDateEd.getText().toString()+"_"+priorityEd.getSelectedItem().toString()+"_"+ketEd.getText().toString();

                                        reference = FirebaseDatabase.getInstance().getReference().child("Data Assignment");
                                        Query a = reference.orderByChild("freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan").equalTo(assDataIn.getFreqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan());
                                        a.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot ds: dataSnapshot.getChildren()){
                                                    Log.d("masuk ga", assDataIn.getFreqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan());
                                                    ds.getRef().child("freqStart").setValue(freqStartTx);
                                                    ds.getRef().child("freqEnd").setValue(freqEndTx);
                                                    ds.getRef().child("freqStartEnd").setValue(freqStartTx+"-"+freqEndTx);
                                                    ds.getRef().child("aplikasi").setValue(aplikasiTx);
                                                    ds.getRef().child("instansi").setValue(instansiTx);
                                                    ds.getRef().child("satuan").setValue(satuanTx);
                                                    ds.getRef().child("primarySecondary").setValue(priorityTx);
                                                    ds.getRef().child("startDate").setValue(startDateTx);
                                                    ds.getRef().child("endDate").setValue(endDateTx);
                                                    ds.getRef().child("keterangan").setValue(ketTx);
                                                    ds.getRef().child("freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan").setValue(keyTx);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        Assignment assData = new Assignment();

                                        assData.setFreqStartEnd(freqStartTx+"-"+freqEndTx);
                                        assData.setAplikasi(aplikasiTx);
                                        assData.setSatuan(satuanTx);
                                        assData.setInstansi(instansiTx);
                                        assData.setPrimarySecondary(priorityTx);
                                        assData.setStartDate(startDateTx);
                                        assData.setEndDate(endDateTx);
                                        assData.setFreqStart(freqStartTx);
                                        assData.setFreqEnd(freqEndTx);
                                        assData.setKeterangan(ketTx);
                                        assData.setFreqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan(keyTx);

                                        adapter.UpdateData(position, assData);
                                        Toast.makeText(AssignmentActivity.this,"Assignment Updated..",Toast.LENGTH_SHORT).show();

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
        Toolbar toolbar = findViewById(R.id.toolbarAss);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(AssignmentActivity.this, HomeActivity.class));
            }
        });
        toolbar.inflateMenu(R.menu.logout);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentActivity.this);
                        builder.setTitle(R.string.app_name);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setMessage("Do you want to exit?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        auth.getInstance().signOut();
                                        startActivity(new Intent(AssignmentActivity.this, OptionActivity.class));
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
