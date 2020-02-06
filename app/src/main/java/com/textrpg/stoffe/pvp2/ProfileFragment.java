package com.textrpg.stoffe.pvp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    Button btnDeleteUser,btnLogout;
    TextView username,lvlTxt,statsTxt;
    View view;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    private FirebaseDatabase databse;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stats,container,false);

        btnDeleteUser =(Button)view.findViewById(R.id.deleteBtn);
        btnLogout =(Button)view.findViewById(R.id.cikis_yap);
        username = view.findViewById(R.id.usernameTxt);
        lvlTxt = view.findViewById(R.id.lvlTxt);
        statsTxt = view.findViewById(R.id.statsTxt);
        firebaseAuth= FirebaseAuth.getInstance();;
        FirebaseUser user = firebaseAuth.getCurrentUser();

        username.setText(user.getDisplayName());
        databse = FirebaseDatabase.getInstance();
        myRef = databse.getReference("users/" + user.getUid());
        myRef.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                JSONObject json = new JSONObject(map);
                try {
                    lvlTxt.setText("LVL:" + json.getString("lvl"));
                    statsTxt.setText("STR:" + json.getString("str") + " AGI:" + json.getString("agi")+
                            " INT:"+json.getString("intl"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("jeff", "Failed to read value.", error.toException());
            }
        });
        lvlTxt.setText(user.getDisplayName());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(),LoginActivity.class));
            }
        });

        return view;
    }
}
