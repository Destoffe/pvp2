package com.textrpg.stoffe.pvp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    TextView textView;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener  authStateListener;
    private FirebaseDatabase databse;
    DatabaseReference myRef;
    private TextView usernameTxt,playerLvl;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationerViewer = findViewById(R.id.nav_view);
        navigationerViewer.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
/* Default fragment
    if(savedInstanceState==null){
        navigationerViewer.setCheckedItem(R.id.nav_message);
    }
*/
       // textView = (TextView) findViewById(R.id.textView1);
        usernameTxt = (TextView) navigationerViewer.getHeaderView(0).findViewById(R.id.navUsername);
        playerLvl = (TextView) navigationerViewer.getHeaderView(0).findViewById(R.id.playerLvl);


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
        if(user != null ){
            checkUsername();
        }

        databse = FirebaseDatabase.getInstance();
        WorldMap wM = new WorldMap("Mysterious Forest",2);
        DatabaseReference testRef;
        testRef = databse.getReference("worldmap/" );
        testRef.child(Integer.toString(wM.getPosition())).setValue(wM);
        if(user == null){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }else {
            String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            myRef = databse.getReference("users/" + user.getUid());
            myRef.addValueEventListener(new ValueEventListener() {

                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    JSONObject json = new JSONObject(map);
                    try {
                        Log.d("jeff", "Value is: " + json.getString("str"));
                        Log.d("jeff", "User token: " + user.getUid());
                        playerLvl.setText("LVL:" + json.getString("lvl"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("jeff", "Failed to read value.", error.toException());
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    private void checkUsername(){

        String username = firebaseAuth.getCurrentUser().getDisplayName();
        if(username == null  || username.isEmpty()){
            Log.d("stoffe","username is null");
            //textView.setText("Hi " + user.getEmail());
            startActivity(new Intent(getApplicationContext(),DisplayNameActivity.class));
            finish();
        }else{
            Log.d("123","123");
            usernameTxt.setText(username);
            usernameTxt.setText(username);
        }
    }

    @Override
    protected void onStart() {
        if(firebaseAuth.getCurrentUser() != null){
            checkUsername();
        }

        //textView.setText("No username set, set username below.");
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_message:
                if(drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.closeDrawer(GravityCompat.START);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment()).commit();
                break;
            case R.id.nav_logout:
                if(drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.closeDrawer(GravityCompat.START);
                }
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
                break;
            case R.id.nav_profile:
                if(drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.closeDrawer(GravityCompat.START);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
        }
        return true;
    }
}