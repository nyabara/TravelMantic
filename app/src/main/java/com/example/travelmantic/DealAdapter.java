package com.example.travelmantic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {
    ArrayList<TravelDeal> mDeals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private Context mContext;
    public DealAdapter(){
        FirebaseUtil.openFirebaseRefence("travel", (ListActivity) mContext);
        mFirebaseDatabase=FirebaseUtil.sFirebaseDatabase;
        mDatabaseReference=FirebaseUtil.sDatabaseReference;
        mDeals=FirebaseUtil.sDeals;
        mChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TravelDeal td = snapshot.getValue(TravelDeal.class);
                Log.d("Deal:",td.getTitle());
                td.setId(snapshot.getKey());
                mDeals.add(td);
                notifyItemInserted(mDeals.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);

    }
    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        View itemView= LayoutInflater.from(context).inflate(R.layout.item_list,parent,false);
        return new DealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        TravelDeal deal=mDeals.get(position);
        holder.currentposition=position;
        holder.bind(deal);

    }

    @Override
    public int getItemCount() {
        return mDeals.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder {
        TextView dealTitle,dealDescription,dealPrice;
        public int currentposition;

        public DealViewHolder(@NonNull View itemView) {

            super(itemView);
            dealTitle=itemView.findViewById(R.id.dealTitle);
            dealDescription=itemView.findViewById(R.id.dealDescription);
            dealPrice=itemView.findViewById(R.id.dealPrice);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(),DealActivity.class);
                    TravelDeal selectedDeal=mDeals.get(currentposition);
                    Log.d("click", String.valueOf(selectedDeal));
                    intent.putExtra("Deal",selectedDeal);
                    v.getContext().startActivity(intent);
                }
            });
        }
        public void bind(TravelDeal deal)
        {
            dealTitle.setText(deal.getTitle());
            dealDescription.setText(deal.getDescription());
            dealPrice.setText(deal.getPrice());
        }
    }
}
