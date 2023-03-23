package com.example.fyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fyproject.DataAccess.CI_DataAccess;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MedicalHistory extends AppCompatActivity {

    private CI_DataAccess CIDataAccess;
    private DatabaseReference reference;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);

        CIDataAccess = new CI_DataAccess();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final EditText EnterCurrent = findViewById(R.id.Current);
        final EditText EnterHistory = findViewById(R.id.History);
        Button UpdateCurrent = findViewById(R.id.UpdateCurrent);
        Button UpdateHistory = findViewById(R.id.UpdateHistory);

        //updating current Illness
        UpdateCurrent.setOnClickListener(V -> {
            HashMap<String, Object> data = new HashMap<>();
            data.put("Current", EnterCurrent.getText().toString());
            CIDataAccess.update(currentUserId, data)
                    .addOnSuccessListener(suc -> {
                        //notify the user on update success
                        Toast.makeText(this, "Successfully updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(error -> {
                        //notify the user on update failure
                        Toast.makeText(this, "Failed to update" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        //updating History
        UpdateHistory.setOnClickListener(V -> {
            HashMap<String, Object> data = new HashMap<>();
            data.put("History", EnterHistory.getText().toString());
            CIDataAccess.update(currentUserId, data)
                    .addOnSuccessListener(suc -> {
                        //if update is successful, notify the user as so
                        Toast.makeText(this, "Successfully updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(error -> {
                        //else notify the user that the update failed
                        Toast.makeText(this, "Failed to update" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        //references the firebase for the contents of History
        reference = FirebaseDatabase.getInstance().getReference("CIHandler").child(currentUserId).child("history");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    //if contents exist in the fields it gets the value
                    String currentIllness = snapshot.child("Current").getValue(String.class);
                    String medicalHistory = snapshot.child("History").getValue(String.class);
                    //sets the values of the text fields to what is found in the firebase
                    EnterCurrent.setText(currentIllness);
                    EnterHistory.setText(medicalHistory);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MedicalHistory.this, "Something Wrong Happened", Toast.LENGTH_LONG).show();
            }
        });
    }
}