package com.textrpg.stoffe.pvp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayNameActivity extends AppCompatActivity {

    EditText displayName;
    Button submitBtn;
    private FirebaseAuth.AuthStateListener  authStateListener;
    private FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth firebaseAuth;
    UserProfileChangeRequest profileUpdates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayname);


        displayName = (EditText) findViewById(R.id.displayName);
        submitBtn = (Button) findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(displayName.getText().toString()).build();
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("users");
                firebaseAuth = FirebaseAuth.getInstance();


                authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if(user == null){
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }
                    }
                };
                final FirebaseUser user  = firebaseAuth.getCurrentUser();

                myRef.child("usernames").child(displayName.getText().toString()).runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        if (mutableData.getValue() == null) {


                            mutableData.setValue(user.getUid());
                            return Transaction.success(mutableData);
                        }else{
                            Toast.makeText(getApplicationContext(),"Username already exists",Toast.LENGTH_SHORT).show();
                            return Transaction.abort();
                        }


                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                            if(b){
                                Player player = new Player(displayName.getText().toString(),1,1,1,1,1,1);
                                myRef.child(user.getUid()).setValue(player);
                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            finish();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(),"Username already exists",Toast.LENGTH_SHORT).show();
                            }
                    }

                });
            }
        });
    }

}
