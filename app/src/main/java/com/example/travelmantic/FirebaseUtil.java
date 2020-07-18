package com.example.travelmantic;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    private static FirebaseUtil sFirebaseUtil;
    public static FirebaseDatabase sFirebaseDatabase;
    public static DatabaseReference sDatabaseReference;
    public static ArrayList<TravelDeal> sDeals;
    public static FirebaseAuth sFirebaseAuth;
    public static FirebaseStorage sStorage;
    public static StorageReference sStorageReference;
    public static FirebaseAuth.AuthStateListener sAuthStateListener;
    private static ListActivity caller;
    public static boolean isadmin;
    public static final int RC_SIGN_IN=123;
    public static void openFirebaseRefence(String ref, final ListActivity callerActivity){
        if (sFirebaseUtil==null)
        {
            sFirebaseUtil=new FirebaseUtil();
            sFirebaseDatabase=FirebaseDatabase.getInstance();
            sFirebaseAuth=FirebaseAuth.getInstance();
            caller=callerActivity;

            sAuthStateListener=new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser()==null){
                        FirebaseUtil.SignIn();
                    }
                    else {
                        String userid=firebaseAuth.getUid();
                        checkisAdmin(userid);
                    }
                    Toast.makeText(callerActivity, "welcome back", Toast.LENGTH_SHORT).show();


                }
            };
            connectStorage();
        }
        
        sDeals=new ArrayList<>();
        sDatabaseReference=sFirebaseDatabase.getReference().child(ref);

    }
    private static void connectStorage(){
        sStorage=FirebaseStorage.getInstance();
        sStorageReference=sStorage.getReference().child("dealsPicture");
    }
    private static void checkisAdmin(String userid) {
        FirebaseUtil.isadmin=false;
        DatabaseReference ref=sFirebaseDatabase.getReference().child("adminstrators").child(userid);
        ChildEventListener listener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FirebaseUtil.isadmin=true;
                caller.showMenu();
                Log.d("admin","youre an administrator");
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
        ref.addChildEventListener(listener);

    }

    private static void SignIn() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public static void attachAuthListener(){
        sFirebaseAuth.addAuthStateListener(sAuthStateListener);
    }
    public static void detachAuthListener(){
        sFirebaseAuth.removeAuthStateListener(sAuthStateListener);
    }

}
