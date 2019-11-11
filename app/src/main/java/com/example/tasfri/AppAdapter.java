package com.example.tasfri;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.HolderApp>{

    List<Aplikasi> listApp = new ArrayList<>();
    ItemClickListener itemClickListener;

    DatabaseReference reference;
    FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseDatabase getDb;

    public AppAdapter(){}

    public AppAdapter(List<Aplikasi> listApp) {
        this.listApp = listApp;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        getDb = FirebaseDatabase.getInstance();
        reference = getDb.getReference();
    }

    @Override
    public AppAdapter.HolderApp onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_row, parent,false);
        return new HolderApp(view);
    }

    @Override
    public void onBindViewHolder(final HolderApp holder, final int position) {
        final int[] isUser = {0};
        final Aplikasi appData = listApp.get(position);

        holder.btn.setVisibility(View.GONE);

        holder.freqBands.setText(appData.getFreqStartEnd());
        holder.aplikasi.setText(appData.getApplication());
        holder.footnote.setText(appData.getFootnote());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position>0 && isUser[0]==0 && auth.getUid() != null){
                    itemClickListener.OnItemClick(position, appData);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listApp.size();
    }

    public static class HolderApp extends RecyclerView.ViewHolder{

        TextView freqBands, aplikasi, footnote;
        Button remove;
        LinearLayout btn;

        public HolderApp(View itemView) {
            super(itemView);
            freqBands = itemView.findViewById(R.id.col1);
            aplikasi = itemView.findViewById(R.id.col2);
            footnote = itemView.findViewById(R.id.col3);
            btn = itemView.findViewById(R.id.btn);
        }
    }

}
