package com.ultimustech.cryptowallet.views.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ultimustech.cryptowallet.R;
import com.ultimustech.cryptowallet.controllers.api.RestAPI;
import com.ultimustech.cryptowallet.controllers.database.FirebaseDBController;
import com.ultimustech.cryptowallet.controllers.database.FirebaseDBHelper;
import com.ultimustech.cryptowallet.views.fragments.AccountFragment;
import com.ultimustech.cryptowallet.views.fragments.DashboardFragment;
import com.ultimustech.cryptowallet.views.fragments.ExchangeFragment;
import com.ultimustech.cryptowallet.views.fragments.TransactionsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "Main Activity";
    private String uid = "";

    //firebase AuthStateListener

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mFirebaseAuth;

    private FirebaseDBHelper firebaseDBHelper;
    private FirebaseDBController firebaseDBController;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set firebase persistance caching

        //initialize firebase auth
        mFirebaseAuth  = FirebaseAuth.getInstance();
        firebaseUser = mFirebaseAuth.getCurrentUser();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        firebaseDBController = new FirebaseDBController();
        if(firebaseUser != null) {
            firebaseDBController.updateNotificationToken(refreshToken, firebaseUser.getUid());
        }

        //initialize and call coin history api
        RestAPI restAPI = new RestAPI();
        restAPI.callAPI();
        //initialize Auth State Listener and check if user is logged in
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    //user is signed out
                    //start login activity
                    Intent i = new Intent(getApplication(),LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    //user is signed in get/user id
                    uid = user.getUid();
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference accountRef = rootRef.child("accounts").child(uid);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()) {
                                Intent i  = new Intent(getApplication(), AccountSetupActivity.class);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    accountRef.addListenerForSingleValueEvent(eventListener);
                }
            }
        };



        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setting up fragment manager
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_frame, new DashboardFragment()).commit(); // setting dashboard fragment as main view


    }

    @Override
    protected  void onPause(){
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected  void onResume(){
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            return true;
        } else if (id == R.id.action_logout){
            //signout user
            mFirebaseAuth.signOut();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_dashboard) {
            // Handle the dashboard action
            fragmentManager.beginTransaction().replace(R.id.main_frame, new DashboardFragment()).commit();
            setTitle(R.string.app_name);
        } else if (id == R.id.nav_transactions) {
            // Handle the dashboard action
            fragmentManager.beginTransaction().replace(R.id.main_frame, new TransactionsFragment()).commit();
            setTitle(R.string.nav_transactions);
        } else if (id == R.id.nav_mine) {
            // Handle the dashboard action
            fragmentManager.beginTransaction().replace(R.id.main_frame, new ExchangeFragment()).commit();
            setTitle(R.string.nav_exchanges);
        } else if (id == R.id.nav_account) {
            // Handle the dashboard action
            fragmentManager.beginTransaction().replace(R.id.main_frame, new AccountFragment()).commit();
            setTitle(R.string.nav_account);
        } else if (id == R.id.nav_settings) {
            Intent intent  = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent  = new Intent(getApplicationContext(), HelpActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
