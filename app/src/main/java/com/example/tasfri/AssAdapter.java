package com.example.tasfri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class AssAdapter extends RecyclerView.Adapter<AssAdapter.HolderAss>{
    List<Assignment> listAss = new ArrayList<>();
    ItemClickListener itemClickListener;

    DatabaseReference reference;
    FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseDatabase getDb;

    public AssAdapter(){

    }

    public AssAdapter(List<Assignment> listAss) {
        this.listAss = listAss;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        getDb = FirebaseDatabase.getInstance();
        reference = getDb.getReference();
    }

    @Override
    public HolderAss onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ass_row, parent,false);
        return new HolderAss(view);
    }

    @Override
    public void onBindViewHolder(final HolderAss holder, final int position) {
        final Assignment assData = listAss.get(position);

        holder.btn.setVisibility(View.GONE);

        holder.freqBands.setText(assData.getFreqStartEnd());
        holder.aplikasi.setText(assData.getAplikasi());
        holder.instansi.setText(assData.getInstansi());
        holder.priority.setText(assData.getPrimarySecondary());
        holder.startDate.setText(assData.getStartDate());
        holder.endDate.setText(assData.getEndDate());
    }

    @Override
    public int getItemCount() {
        return listAss.size();
    }

    public static class HolderAss extends RecyclerView.ViewHolder{

        TextView freqBands, aplikasi, instansi, startDate, endDate, priority;
        Button remove;
        LinearLayout btn;

        public HolderAss(View itemView) {
            super(itemView);
            freqBands = itemView.findViewById(R.id.col1);
            aplikasi = itemView.findViewById(R.id.col2);
            instansi= itemView.findViewById(R.id.col3);
            startDate= itemView.findViewById(R.id.col4);
            endDate= itemView.findViewById(R.id.col5);
            priority= itemView.findViewById(R.id.col6);
            btn = itemView.findViewById(R.id.btn);
        }
    }
}

