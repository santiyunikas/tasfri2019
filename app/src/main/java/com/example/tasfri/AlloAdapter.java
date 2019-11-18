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

public class AlloAdapter extends RecyclerView.Adapter<AlloAdapter.HolderAllo>{
    List<Alokasi> listAllo = new ArrayList<>();
    ItemClickListener itemClickListener;

    DatabaseReference reference;
    FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseDatabase getDb;

    public AlloAdapter(){

    }

    public AlloAdapter(List<Alokasi> listAllo) {
        this.listAllo = listAllo;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        getDb = FirebaseDatabase.getInstance();
        reference = getDb.getReference();
    }

    @Override
    public HolderAllo onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allo_row, parent,false);
        return new HolderAllo(view);
    }

    @Override
    public void onBindViewHolder(final HolderAllo holder, final int position) {
        final int[] isUser = {0};
        final Alokasi alloData = listAllo.get(position);

        if (position==0){
            holder.remove.setVisibility(View.GONE);
        }

        if (auth.getUid()!= null){
            reference = getDb.getReference("user");
            reference.child(user.getUid()).addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users users = dataSnapshot.getValue(Users.class);
                    if(users.getRole().equalsIgnoreCase("user")){
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

        holder.freqBands.setText(alloData.getFreqStartEnd());
        holder.alokasi.setText(alloData.getAllocation());
        holder.footnote.setText(alloData.getFootnote());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position>0 && isUser[0]==0 && auth.getUid() != null){
                    itemClickListener.OnItemClick(position, alloData);
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
                                reference = FirebaseDatabase.getInstance().getReference().child("Data Alokasi");
                                Query a = reference.orderByChild("freqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary").equalTo(listAllo.get(position).getFreqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary());
                                a.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                                            ds.getRef().removeValue();
                                            listAllo.remove(listAllo.indexOf(alloData));
                                            holder.freqBands.setVisibility(View.GONE);
                                            holder.alokasi.setVisibility(View.GONE);
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
        return listAllo.size();
    }

    public static class HolderAllo extends RecyclerView.ViewHolder{

        TextView freqBands, alokasi, footnote;
        Button remove;
        LinearLayout btn;

        public HolderAllo(View itemView) {
            super(itemView);
            freqBands = itemView.findViewById(R.id.col1);
            alokasi = itemView.findViewById(R.id.col2);
            footnote = itemView.findViewById(R.id.col3);
            remove= itemView.findViewById(R.id.btnRemoveAllo);
            btn = itemView.findViewById(R.id.btn);
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public void UpdateData(int position, Alokasi alloData){
        listAllo.remove(position);
        listAllo.add(alloData);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }
}

