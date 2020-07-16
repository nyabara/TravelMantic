package com.example.travelmantic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    TextView txtfetch;
    ArrayList<TravelDeal>mDeals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    RecyclerView rvDeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_list);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_activity_menu,menu);
        MenuItem insertmenu=menu.findItem(R.id.editDeal);
        if (FirebaseUtil.isadmin){
            insertmenu.setVisible(true);
        }
        else {
            insertmenu.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.editDeal:
                startActivity(new Intent(ListActivity.this,DealActivity.class));
                return true;
            case R.id.logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                                Log.d("logout","logged out");
                                FirebaseUtil.attachAuthListener();
                            }
                        });
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtil.openFirebaseRefence("travel",this);
        rvDeals=findViewById(R.id.idrecyclev);
        final DealAdapter dealAdapter=new DealAdapter();
        rvDeals.setAdapter(dealAdapter);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ListActivity.this,LinearLayoutManager.VERTICAL,false);
        rvDeals.setLayoutManager(linearLayoutManager);
        dealAdapter.notifyDataSetChanged();
        FirebaseUtil.attachAuthListener();

    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachAuthListener();
    }
    public void showMenu(){
        invalidateOptionsMenu();
    }
}
