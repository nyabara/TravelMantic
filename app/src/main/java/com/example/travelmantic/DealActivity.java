package com.example.travelmantic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {
    EditText editTitle,editPrice,editDescription;
    Button btnSubmit;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    TravelDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_deal);
        editTitle=findViewById(R.id.editTitle);
        editPrice=findViewById(R.id.editPrice);
        editDescription=findViewById(R.id.editDescription);

        FirebaseUtil.openFirebaseRefence("travel");
        mFirebaseDatabase=FirebaseUtil.sFirebaseDatabase;
        mDatabaseReference=FirebaseUtil.sDatabaseReference;
        TravelDeal deal= (TravelDeal) getIntent().getSerializableExtra("Deal");
        if (deal==null)
        {
            deal=new TravelDeal();
        }
        this.deal=deal;
        editTitle.setText(deal.getTitle());
        editDescription.setText(deal.getDescription());
        editPrice.setText(deal.getPrice());



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itsave:
                saveDeal();
                Toast.makeText(DealActivity.this, "Deal saved", Toast.LENGTH_SHORT).show();
                backToList();
                clean();
                return true;
            case R.id.deleteDeal:
                deleteDeal();
                Toast.makeText(this, "Deal deleted", Toast.LENGTH_SHORT).show();
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void saveDeal() {
        deal.setTitle(editTitle.getText().toString());
        deal.setPrice(editPrice.getText().toString());
        deal.setDescription(editDescription.getText().toString());

        if (deal.getId()==null)
        {
            mDatabaseReference.push().setValue(deal);
        }
        else {
            mDatabaseReference.child(deal.getId()).setValue(deal);
        }

    }
    private void clean() {
        editDescription.setText("");
        editPrice.setText("");
        editTitle.setText("");
    }
    private void deleteDeal(){
        if (deal.getId()==null){
            Toast.makeText(this, "save deal before deleting", Toast.LENGTH_SHORT).show();
            return;
        }
        mDatabaseReference.child(deal.getId()).removeValue();

    }
    private void backToList(){
        startActivity(new Intent(DealActivity.this,ListActivity.class));
    }


}
