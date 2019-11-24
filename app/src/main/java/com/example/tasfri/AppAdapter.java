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

        if (position==0){
            holder.remove.setVisibility(View.GONE);
        }

        if (auth.getUid()!= null){
            reference = getDb.getReference("user");
            reference.child(user.getUid()).addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users user = dataSnapshot.getValue(Users.class);
                    if(user.getRole().equalsIgnoreCase("user")){
                        holder.btn.setVisibility(View.GONE);
                        isUser[0] = 1;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            holder.btn.setVisibility(View.GONE);
        }

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

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Are you sure to delete data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                reference = FirebaseDatabase.getInstance().getReference().child("Data Aplikasi");
                                Query a = reference.orderByChild("freqStartEnd_satuan_application_footnote").equalTo(listApp.get(position).getFreqStartEnd_satuan_application_footnote());
                                a.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                                            ds.getRef().removeValue();
                                            listApp.remove(listApp.indexOf(appData));
                                            holder.freqBands.setVisibility(View.GONE);
                                            holder.aplikasi.setVisibility(View.GONE);
                                            holder.footnote.setVisibility(View.GONE);
                                            holder.btn.setVisibility(View.GONE);
                                            holder.remove.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                notifyDataSetChanged();

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
            remove = itemView.findViewById(R.id.btnRemoveApp);
            btn = itemView.findViewById(R.id.btn);
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public void UpdateData(int position, Aplikasi appData){
        listApp.remove(position);
        listApp.add(appData);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }

}
