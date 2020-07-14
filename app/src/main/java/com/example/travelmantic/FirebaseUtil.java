package com.example.travelmantic;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtil {
    private static FirebaseUtil sFirebaseUtil;
    public static FirebaseDatabase sFirebaseDatabase;
    public static DatabaseReference sDatabaseReference;
    public static ArrayList<TravelDeal> sDeals;
    public static void openFirebaseRefence(String ref){
        if (sFirebaseUtil==null)
        {
            sFirebaseUtil=new FirebaseUtil();
            sFirebaseDatabase=FirebaseDatabase.getInstance();
        }
        sDeals=new ArrayList<>();
        sDatabaseReference=sFirebaseDatabase.getReference().child(ref);
    }

}
