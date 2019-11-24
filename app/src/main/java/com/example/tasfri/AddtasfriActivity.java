package com.example.tasfri;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddtasfriActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    //view beginning
    private Spinner spnAdd;
    private LinearLayout lineTasfri, lineApp, lineAss;
    private Button cs;

    //view Add Tasfri
    private EditText freqStartTs, freqEndTs, alloNewTs, appNewTs, ftnoteTs, freqRangeTs;
    private Button addTasfri;
    private Spinner spnTs, spnPriorTs;

    //view Add App
    private EditText freqStartApp, freqEndApp, appNewApp, ftnoteApp;
    private Button addApp;
    private Spinner spnApp;

    //view Add Ass
    private EditText freqStartAss, freqEndAss, appNewAss, insNewAss, startDate, endDate, ketNewAss;
    private Button addAss;
    private Spinner spnAss, spnPriorAss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtasfri);

        initview();
        toolbarSet();
    }

    private void initview(){
        //to set view form
        spnAdd = findViewById(R.id.spnAddWhat);
        lineTasfri = findViewById(R.id.lineTasfri);
        lineApp = findViewById(R.id.lineApp);
        lineAss = findViewById(R.id.lineAss);
        lineTasfri.setVisibility(View.VISIBLE);
        lineApp.setVisibility(View.GONE);
        lineAss.setVisibility(View.GONE);
        cs = findViewById(R.id.cs);
        cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spnAdd.getSelectedItem().toString().equalsIgnoreCase("Add TASFRI")){
                    lineTasfri.setVisibility(View.VISIBLE);
                    lineApp.setVisibility(View.GONE);
                    lineAss.setVisibility(View.GONE);

                    freqStartTs.setText("");
                    freqEndTs.setText("");
                    alloNewTs.setText("");
                    appNewTs.setText("");

                    freqStartApp.setText("");
                    freqEndApp.setText("");
                    appNewApp.setText("");

                    freqStartAss.setText("");
                    freqEndAss.setText("");
                    appNewAss.setText("");
                    insNewAss.setText("");
                    startDate.setText("");
                    endDate.setText("");
                    ketNewAss.setText("");
                } else if(spnAdd.getSelectedItem().toString().equalsIgnoreCase("Add Application")){
                    lineTasfri.setVisibility(View.GONE);
                    lineApp.setVisibility(View.VISIBLE);
                    lineAss.setVisibility(View.GONE);

                    freqStartTs.setText("");
                    freqEndTs.setText("");
                    alloNewTs.setText("");
                    appNewTs.setText("");

                    freqStartApp.setText("");
                    freqEndApp.setText("");
                    appNewApp.setText("");

                    freqStartAss.setText("");
                    freqEndAss.setText("");
                    appNewAss.setText("");
                    insNewAss.setText("");
                    startDate.setText("");
                    endDate.setText("");
                    ketNewAss.setText("");
                } else if(spnAdd.getSelectedItem().toString().equalsIgnoreCase("Add Assignment")) {
                    lineTasfri.setVisibility(View.GONE);
                    lineApp.setVisibility(View.GONE);
                    lineAss.setVisibility(View.VISIBLE);

                    freqStartTs.setText("");
                    freqEndTs.setText("");
                    alloNewTs.setText("");
                    appNewTs.setText("");

                    freqStartApp.setText("");
                    freqEndApp.setText("");
                    appNewApp.setText("");

                    freqStartAss.setText("");
                    freqEndAss.setText("");
                    appNewAss.setText("");
                    insNewAss.setText("");
                    startDate.setText("");
                    endDate.setText("");
                    ketNewAss.setText("");
                }
            }
        });

        //view Add Tasfri
        freqStartTs = findViewById(R.id.txFreqstart);
        freqEndTs = findViewById(R.id.txFreqend);
        alloNewTs = findViewById(R.id.txAlloNew);
        appNewTs = findViewById(R.id.txAppNew);
        ftnoteTs = findViewById(R.id.txFtnoteTs);
        spnPriorTs =findViewById(R.id.spnPriorTasfri);
        spnTs = findViewById(R.id.spnAddTasfri);
        addTasfri = findViewById(R.id.addTasfri);
        addTasfri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ftnoteTs.getText().toString().isEmpty()||freqStartTs.getText().toString().isEmpty()||freqEndTs.getText().toString().isEmpty()||alloNewTs.getText().toString().isEmpty()||appNewTs.getText().toString().isEmpty()||spnPriorTs.getSelectedItem().toString().equalsIgnoreCase("Priority")||spnTs.getSelectedItem().toString().equalsIgnoreCase("Select Unit")){
                    Toast.makeText(AddtasfriActivity.this, "Fill the form completely", Toast.LENGTH_LONG).show();
                }else{
                    if (Double.valueOf(freqStartTs.getText().toString())>Double.valueOf(freqEndTs.getText().toString())){
                        freqStartTs.setError("Must lower than Frequency End");
                        freqEndTs.setError("Must higher than Frequency End");
                    } else {
                        addTasfri();
                    }
                }


            }
        });

        //view Add App
        freqStartApp = findViewById(R.id.txFreqstartApp);
        freqEndApp = findViewById(R.id.txFreqendApp);
        appNewApp = findViewById(R.id.txAppOnlyNew);
        spnApp = findViewById(R.id.spnAddApp);
        ftnoteApp = findViewById(R.id.txFtnoteApp);
        addApp = findViewById(R.id.addApp);
        addApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(freqStartApp.getText().toString().isEmpty()||freqEndApp.getText().toString().isEmpty()||appNewApp.getText().toString().isEmpty()||spnApp.getSelectedItem().toString().equalsIgnoreCase("Select Unit")){
                    Toast.makeText(AddtasfriActivity.this, "Fill the form completely", Toast.LENGTH_LONG).show();
                }else{
                    if (Double.valueOf(freqStartApp.getText().toString())>Double.valueOf(freqEndApp.getText().toString())){
                        freqStartApp.setError("Must lower than Frequency End");
                        freqEndApp.setError("Must higher than Frequency End");
                    } else {
                        addApp();
                    }
                }
            }
        });

        //view Add Ass
        freqStartAss = findViewById(R.id.txFreqstartAss);
        freqEndAss = findViewById(R.id.txFreqendAss);
        appNewAss = findViewById(R.id.txAppAssNew);
        insNewAss = findViewById(R.id.txInsAssNew);
        startDate = findViewById(R.id.txStartdateAssNew);
        endDate = findViewById(R.id.txDateendAssNew);
        ketNewAss = findViewById(R.id.txInformAssNew);
        spnAss = findViewById(R.id.spnSatAss);
        spnPriorAss = findViewById(R.id.spnPriorAss);
        addAss = findViewById(R.id.addAss);
        addAss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(freqStartAss.getText().toString().isEmpty()||freqEndAss.getText().toString().isEmpty()|| appNewAss.getText().toString().isEmpty()|| insNewAss.getText().toString().isEmpty()|| startDate.getText().toString().isEmpty()|| endDate.getText().toString().isEmpty()|| ketNewAss.getText().toString().isEmpty()|| spnAss.getSelectedItem().toString().equalsIgnoreCase("select unit")||spnPriorAss.getSelectedItem().toString().equalsIgnoreCase("priority")){
                    Toast.makeText(AddtasfriActivity.this, "Fill the form completely", Toast.LENGTH_LONG).show();
                }else{
                    if (Double.valueOf(freqStartAss.getText().toString())>Double.valueOf(freqEndAss.getText().toString())){
                        freqStartAss.setError("Must lower than Frequency End");
                        freqEndAss.setError("Must higher than Frequency End");
                    } else {
                        addAss();
                    }
                }
            }
        });

    }

    private void addTasfri(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Data Alokasi");
        int randomId = (int) ((Math.random()*100000)+1544);
//        myRef.child(String.valueOf(randomId)).setValue(new Alokasi(
//                alloNewTs.getText().toString().trim(),
//                appNewTs.getText().toString().trim(),
//                freqStartTs.getText().toString().trim(),
//                freqEndTs.getText().toString().trim(),
//                freqStartTs.getText().toString().trim()+"-"+freqEndTs.getText().toString().trim(),
//                spnPriorTs.getSelectedItem().toString().trim(),
//                spnTs.getSelectedItem().toString().trim(),
//                ftnoteTs.getText().toString().trim(),
//                String.valueOf(Double.valueOf(freqEndTs.getText().toString().trim())-Double.valueOf(freqStartTs.getText().toString().trim())),
//                freqStartTs.getText().toString().trim()+"-"+freqEndTs.getText().toString().trim()+"_"+spnTs.getSelectedItem().toString().trim()+"_"+alloNewTs.getText().toString().trim().toUpperCase()+"_"+appNewTs.getText().toString().trim()+"_"+ftnoteTs.getText().toString().trim()+"_"+spnPriorTs.getSelectedItem().toString().trim()));
        if (spnPriorTs.getSelectedItem().toString().trim().equalsIgnoreCase("primary")){
            myRef.child(String.valueOf(randomId)).setValue(new Alokasi(
                    alloNewTs.getText().toString().trim().toUpperCase(),
                    appNewTs.getText().toString().trim(),
                    freqStartTs.getText().toString().trim(),
                    freqEndTs.getText().toString().trim(),
                    freqStartTs.getText().toString().trim()+"-"+freqEndTs.getText().toString().trim(),
                    spnPriorTs.getSelectedItem().toString().trim(),
                    spnTs.getSelectedItem().toString().trim(),
                    ftnoteTs.getText().toString().trim(),
                    String.valueOf(Double.valueOf(freqEndTs.getText().toString().trim())-Double.valueOf(freqStartTs.getText().toString().trim())),
                    freqStartTs.getText().toString().trim()+"-"+freqEndTs.getText().toString().trim()+"_"+spnTs.getSelectedItem().toString().trim()+"_"+alloNewTs.getText().toString().trim().toUpperCase()+"_"+appNewTs.getText().toString().trim()+"_"+ftnoteTs.getText().toString().trim()+"_"+spnPriorTs.getSelectedItem().toString().trim()));
        } else {
            myRef.child(String.valueOf(randomId)).setValue(new Alokasi(
                    alloNewTs.getText().toString().trim(),
                    appNewTs.getText().toString().trim(),
                    freqStartTs.getText().toString().trim(),
                    freqEndTs.getText().toString().trim(),
                    freqStartTs.getText().toString().trim()+"-"+freqEndTs.getText().toString().trim(),
                    spnPriorTs.getSelectedItem().toString().trim(),
                    spnTs.getSelectedItem().toString().trim(),
                    ftnoteTs.getText().toString().trim(),
                    String.valueOf(Double.valueOf(freqEndTs.getText().toString().trim())-Double.valueOf(freqStartTs.getText().toString().trim())),
                    freqStartTs.getText().toString().trim()+"-"+freqEndTs.getText().toString().trim()+"_"+spnTs.getSelectedItem().toString().trim()+"_"+alloNewTs.getText().toString().trim()+"_"+appNewTs.getText().toString().trim()+"_"+ftnoteTs.getText().toString().trim()+"_"+spnPriorTs.getSelectedItem().toString().trim()));
        }

        Log.d("idTasfri", String.valueOf(randomId));
        freqStartTs.setText("");
        freqEndTs.setText("");
        alloNewTs.setText("");
        appNewTs.setText("");
        ftnoteTs.setText("");

        Toast.makeText(AddtasfriActivity.this, "Data is Added", Toast.LENGTH_LONG).show();
    }

    private void addApp(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Data Aplikasi");
        int randomId = (int) ((Math.random()*100000)+117);
        myRef.child(String.valueOf(randomId)).setValue(new Aplikasi(
                appNewApp.getText().toString(),
                freqStartApp.getText().toString(),
                freqEndApp.getText().toString(),
                freqStartApp.getText().toString()+"-"+freqEndApp.getText().toString(),
                String.valueOf(Double.valueOf(freqEndApp.getText().toString())-Double.valueOf(freqStartApp.getText().toString())),
                spnApp.getSelectedItem().toString(),
                ftnoteApp.getText().toString(),
                freqStartApp.getText().toString()+"-"+freqEndApp.getText().toString()+"_"+spnApp.getSelectedItem().toString()+"_"+appNewApp.getText().toString()+"_"+ftnoteApp.getText().toString()));

        Log.d("idApp", String.valueOf(randomId));
        freqStartApp.setText("");
        freqEndApp.setText("");
        appNewApp.setText("");
        ftnoteApp.setText("");

        Toast.makeText(AddtasfriActivity.this, "Data is Added", Toast.LENGTH_LONG).show();
    }

    private void addAss(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Data Assignment");
        int randomId = (int) ((Math.random()*100000));
        myRef.child(String.valueOf(randomId)).setValue(new Assignment(freqStartAss.getText().toString(),
                freqEndAss.getText().toString(),
                freqStartAss.getText().toString()+"-"+freqEndAss.getText().toString(),
                appNewAss.getText().toString(),
                insNewAss.getText().toString(),
                startDate.getText().toString(),
                endDate.getText().toString(),
                spnPriorAss.getSelectedItem().toString(),
                spnAss.getSelectedItem().toString(),
                ketNewAss.getText().toString(),
                freqStartAss.getText().toString()+"-"+freqEndAss.getText().toString()+"_"+spnAss.getSelectedItem().toString()+"_"+appNewAss.getText().toString()+"_"+insNewAss.getText().toString()+"_"+startDate.getText().toString()+"_"+endDate.getText().toString()+"_"+spnPriorAss.getSelectedItem().toString()+"_"+ketNewAss.getText().toString()));
        Log.d("idAss", String.valueOf(randomId));
        freqStartAss.setText("");
        freqEndAss.setText("");
        appNewAss.setText("");
        insNewAss.setText("");
        startDate.setText("");
        endDate.setText("");
        ketNewAss.setText("");

        Toast.makeText(AddtasfriActivity.this, "Data is Added", Toast.LENGTH_LONG).show();
    }

    @SuppressLint({"NewApi", "ResourceType"})
    private void toolbarSet(){
        Toolbar toolbar = findViewById(R.id.toolbarAdd);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(AddtasfriActivity.this, HomeActivity.class));
            }
        });
        toolbar.inflateMenu(R.menu.logout);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddtasfriActivity.this);
                        builder.setTitle(R.string.app_name);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setMessage("Do you want to exit?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        auth.getInstance().signOut();
                                        startActivity(new Intent(AddtasfriActivity.this, OptionActivity.class));
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

