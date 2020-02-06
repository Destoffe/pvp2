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

public class MessageFragment extends Fragment implements View.OnClickListener {
    View view;
    TextView currentPlace;
    Button goUpBtn,goDownBtn,goLeftBtn,goRightBtn;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    DatabaseReference worldPosition;
    DatabaseReference positionRefY,positionRefX,positionRefXReader,worldReader;
    private FirebaseDatabase databse;
    FirebaseUser user;
    Player player;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message,container,false);

/*
        goUpBtn = (Button) view.findViewById(R.id.goUpBtn);
        goUpBtn.setOnClickListener(this);

        goDownBtn = (Button) view.findViewById(R.id.goDownBtn);
        goDownBtn.setOnClickListener(this);
*/
        goLeftBtn = (Button) view.findViewById(R.id.goLeftBtn);
        goLeftBtn.setOnClickListener(this);

        goRightBtn = (Button) view.findViewById(R.id.goRightBtn);
        goRightBtn.setOnClickListener(this);



        currentPlace = (TextView) view.findViewById(R.id.currentPlace);
        firebaseAuth= FirebaseAuth.getInstance();;
        user = firebaseAuth.getCurrentUser();
        databse = FirebaseDatabase.getInstance();
        positionRefY = databse.getReference("users/" + user.getUid()+"/posY");
        positionRefX = databse.getReference("users/" + user.getUid()+"/posX");
        positionRefXReader = databse.getReference("users/" + user.getUid()+"/posX");


        positionRefXReader.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Data",dataSnapshot.getValue().toString());
                String currentPosition = dataSnapshot.getValue().toString();
                currentPlace.setText("Currentplace: " + "[" + currentPosition + "]");
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                worldReader = databse.getReference("worldmap/");
                worldReader.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        //Map<String, Object> map = (Map<String, Object>) snap.getValue();
                        //JSONObject json = new JSONObject(map);
                       // Log.d("worldmap",json.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

                    //WorldMap wM = new WorldMap()
                   // Log.d("player",player.giveMeString());
                    currentPlace.setText("Currentplace: " + "[" + currentPosition + "]");// + "[" + json.getString("posY") +"]");


            }

            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("jeff", "Failed to read value.", error.toException());
            }
        });

        myRef = databse.getReference("users/" + user.getUid());
        myRef.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                JSONObject json = new JSONObject(map);
                try {
                    player = new Player(json.getString("username"),json.getInt("lvl"),json.getInt("str"),json.getInt("agi"),json.getInt("intl"),json.getInt("posX"),json.getInt("posY"));
                    Log.d("player",player.giveMeString());
                    //currentPlace.setText("Currentplace: " + "[" + json.getString("posX") + "]");// + "[" + json.getString("posY") +"]");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("jeff", "Failed to read value.", error.toException());
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
/*
            case R.id.goUpBtn:
                Log.d("button","go up");
                Log.d("player",player.getString());
                movePlayerY(1);
                break;

 */
            case R.id.goLeftBtn:
                Log.d("button","go left");
                movePlayerX(-1);
                break;
            case R.id.goRightBtn:
                Log.d("button","go right");
                movePlayerX(1);
                break;
/*
            case R.id.goDownBtn:
                Log.d("button","go down");
                movePlayerY(1);
                break;
 */
        }
    }

    private void movePlayerY(int direction){
        // TODO Can you move this way? Does this path even exist?


        positionRefY.setValue(player.getPosY() +direction, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError != null){
                    Log.w("jeff",databaseError.getMessage());

                }else{
                    // positionRef.setValue()
                }
            }
        });
    }

    private void movePlayerX(final int direction){
        worldReader.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final int tempDirection = player.getPosX();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Log.d("direction",data+ " data2");
                    if (data.getKey().equals(Integer.toString(player.getPosX()))) {
                       // JSONObject json = new JSONObject();
                       Log.d("direction",tempDirection + " exists");
                        Log.d("direction",data+ " data");
                    } else {
                        Log.d("direction", tempDirection + " doesnt exist");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        positionRefX.setValue(player.getPosX() +direction, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError != null){
                    Log.w("jeff",databaseError.getMessage());

                }else{
                    // positionRef.setValue()
                }
            }
        });
    }

}
