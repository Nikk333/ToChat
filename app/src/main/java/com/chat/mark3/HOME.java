package com.chat.mark3;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HOME extends AppCompatActivity {
    RelativeLayout relativeLayout;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<DBLOGIN> usersArrayList;
    Handler h = new Handler();
    int progress = 0,i=0;
    Resources rs;
    DatabaseReference databaseReference;
    ArrayList<String> friendlist;
    EditText SEARCHBOX;
    ImageView REQUEST_BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        relativeLayout=findViewById(R.id.rlayout);
        getWindow().setNavigationBarColor(getColor(R.color.whitelight));
        getWindow().setStatusBarColor(getColor(R.color.white));
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("USERLOGIN");
        usersArrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.USERLISTVIEW);
        SEARCHBOX=findViewById(R.id.SEARCH_HOME);
        REQUEST_BTN=findViewById(R.id.REQUEST_BTN);
        REQUEST_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HOME.this,REQUESTPAGE.class);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new UserAdapter(HOME.this,usersArrayList);
        recyclerView.setAdapter(adapter);
        alluser();
        friendlist=new ArrayList<>();
    }
    public void alluser()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    DBLOGIN dblogin = dataSnapshot.getValue(DBLOGIN.class);
                    usersArrayList.add(dblogin);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        SEARCHBOX.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(HOME.this,FRIENDSCHAT.class);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {

        super.onRestart();
    }

    @Override
    protected void onStart() {
       USERACTIVE.ONOFF_FUN("Online");
        super.onStart();
    }
    @Override
    protected void onPause() {
        USERACTIVE.ONOFF_FUN("Ofline");
        super.onPause();
    }


}
