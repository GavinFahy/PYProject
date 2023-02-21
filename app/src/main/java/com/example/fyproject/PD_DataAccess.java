package com.example.fyproject;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PD_DataAccess {
    private DatabaseReference DBReference;
    private FirebaseAuth mAuth;
    //method used to gain access to the database
    public PD_DataAccess()
    {
        mAuth = FirebaseAuth.getInstance();
        DBReference = FirebaseDatabase.getInstance().getReference();
    }
    //method used to update teh data
    public Task<Void> update(String userId, HashMap<String, Object> data) {
        return DBReference.child("PDHandler").child(userId).child("personalDetails").updateChildren(data);
    }
}
