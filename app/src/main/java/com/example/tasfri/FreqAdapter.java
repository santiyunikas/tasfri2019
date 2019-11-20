package com.example.tasfri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class FreqAdapter extends RecyclerView.Adapter<FreqAdapter.HolderFreq> {

    List<Alokasi> listAlo = new ArrayList<>();
    ItemClickListener itemClickListener;

    DatabaseReference reference;
    FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseDatabase getDb;

    public FreqAdapter(){}

    public FreqAdapter(List<Alokasi> listAlo) {
        this.listAlo = listAlo;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        getDb = FirebaseDatabase.getInstance();
        reference = getDb.getReference();
    }

    @Override
    public FreqAdapter.HolderFreq onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.freq_row, parent,false);
        return new FreqAdapter.HolderFreq(view);
    }

    @Override
    public void onBindViewHolder(final FreqAdapter.HolderFreq holder, final int position) {
        final int[] isUser = {0};
        final Alokasi aloData = listAlo.get(position);

        holder.freqBands.setText(aloData.getFreqStartEnd());
        holder.aplikasi.setText(aloData.getAplikasi());
        holder.alokasi.setText(aloData.getAllocation());
        holder.footnote.setText(aloData.getFootnote());

    }

    @Override
    public int getItemCount() {
        return listAlo.size();
    }

    public static class HolderFreq extends RecyclerView.ViewHolder{

        TextView freqBands, aplikasi, footnote, alokasi;

        public HolderFreq(View itemView) {
            super(itemView);
            freqBands = itemView.findViewById(R.id.col1);
            aplikasi = itemView.findViewById(R.id.col2);
            alokasi = itemView.findViewById(R.id.col3);
            footnote = itemView.findViewById(R.id.col4);
        }
    }

}
