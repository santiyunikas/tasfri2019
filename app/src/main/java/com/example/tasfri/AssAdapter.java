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
        final int[] isUser = {0};
        final Assignment assData = listAss.get(position);

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


        holder.freqBands.setText(assData.getFreqStartEnd());
        holder.aplikasi.setText(assData.getAplikasi());
        holder.instansi.setText(assData.getInstansi());
        holder.priority.setText(assData.getPrimarySecondary());
        holder.startDate.setText(assData.getStartDate());
        holder.endDate.setText(assData.getEndDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position>0 && isUser[0]==0 && auth.getUid() != null){
                    itemClickListener.OnItemClick(position, assData);
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

                                reference = FirebaseDatabase.getInstance().getReference().child("Data Assignment");
                                Query a = reference.orderByChild("freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan").equalTo(listAss.get(position).getFreqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan());
                                a.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                                            ds.getRef().removeValue();
                                            listAss.remove(listAss.indexOf(assData));
                                            holder.freqBands.setVisibility(View.GONE);
                                            holder.remove.setVisibility(View.GONE);
                                            holder.endDate.setVisibility(View.GONE);
                                            holder.startDate.setVisibility(View.GONE);
                                            holder.priority.setVisibility(View.GONE);
                                            holder.instansi.setVisibility(View.GONE);
                                            holder.aplikasi.setVisibility(View.GONE);
                                            holder.btn.setVisibility(View.GONE);
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
            remove= itemView.findViewById(R.id.btnRemoveAss);
            btn = itemView.findViewById(R.id.btn);
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public void UpdateData(int position, Assignment assData){
        listAss.remove(position);
        listAss.add(assData);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }
}

