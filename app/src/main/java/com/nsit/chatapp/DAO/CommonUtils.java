package com.nsit.chatapp.DAO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommonUtils {

    public static FirebaseDatabase getFirebaseDatabase(){
        return FirebaseDatabase.getInstance();
    }

    public static FirebaseAuth getFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

}
