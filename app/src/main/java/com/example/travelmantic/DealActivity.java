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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_deal);
        editTitle=findViewById(R.id.editTitle);
        editPrice=findViewById(R.id.editPrice);
        editDescription=findViewById(R.id.editDescription);
        btnSubmit=findViewById(R.id.btnSubmit);





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
                clean();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void saveDeal() {
        String title=editTitle.getText().toString();
        String price=editPrice.getText().toString();
        String description=editDescription.getText().toString();
        TravelDeal travelDeal=new TravelDeal(title,price,description);
        FirebaseUtil.openFirebaseRefence("travel");
        mFirebaseDatabase=FirebaseUtil.sFirebaseDatabase;
        mDatabaseReference=FirebaseUtil.sDatabaseReference;
        mDatabaseReference.push().setValue(travelDeal);
    }
    private void clean() {
        editDescription.setText("");
        editPrice.setText("");
        editTitle.setText("");
    }


}
