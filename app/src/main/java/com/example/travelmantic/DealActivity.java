package com.example.travelmantic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.Resource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DealActivity extends AppCompatActivity {
    EditText editTitle,editPrice,editDescription;
    Button btnUpload;
    ImageView image;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    TravelDeal deal;
    private static final int REQUEST_PICTURES=42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_deal);
        editTitle=findViewById(R.id.editTitle);
        editPrice=findViewById(R.id.editPrice);
        editDescription=findViewById(R.id.editDescription);
        image=findViewById(R.id.image);
        btnUpload=findViewById(R.id.btnimage);

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
        showImage(deal.getImageUrl());

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);//only data that is on the device
                startActivityForResult(Intent.createChooser(intent,"insert Pictures"),REQUEST_PICTURES);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        if (FirebaseUtil.isadmin){
            menu.findItem(R.id.itsave).setVisible(true);
            menu.findItem(R.id.deleteDeal).setVisible(true);
            enableEditTexts(true);
        }
        else {
            menu.findItem(R.id.itsave).setVisible(false);
            menu.findItem(R.id.deleteDeal).setVisible(false);
            enableEditTexts(false);
        }
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
    private void enableEditTexts(boolean isEnabled){
        editPrice.setEnabled(isEnabled);
        editDescription.setEnabled(isEnabled);
        editTitle.setEnabled(isEnabled);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_PICTURES&&resultCode==RESULT_OK){
            Uri imageUrl=data.getData();
            final StorageReference picRef=FirebaseUtil.sStorageReference.child(imageUrl.getLastPathSegment());
            picRef.putFile(imageUrl);
            picRef.getDownloadUrl().addOnSuccessListener(this, new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url=uri.toString();
                    deal.setImageUrl(url);
                    showImage(url);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Error:",e.getMessage());

                }
            });
//           picRef.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    String url=taskSnapshot.getStorage().getDownloadUrl().toString();
//                    deal.setImageUrl(url);
//                    showImage(url);
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d("error:",e.getMessage());
//
//                }
//            });
        }
    }

    private void showImage(String url) {
        if (url!=null&&!url.isEmpty()){
            int width= Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(url).resize(width,width*2/3).centerCrop().into(image);
        }
    }
}
