package com.chat.mark3;

import android.content.Intent;
import android.os.Bundle;
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

public class REQUESTPAGE extends AppCompatActivity {
    RelativeLayout relativeLayout;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    RequestAdapter adapter;
    FirebaseDatabase database;
    ArrayList<DBREQUEST> dbrequestArrayList;
    DatabaseReference databaseReference,databaseReference1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestpage);
        relativeLayout=findViewById(R.id.relative);
        auth=FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("REQUEST").child(auth.getUid());
        databaseReference1=database.getReference("USERLOGIN");
        dbrequestArrayList =new ArrayList<>();
        recyclerView=findViewById(R.id.reuestcycle);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new RequestAdapter(REQUESTPAGE.this, dbrequestArrayList);
        recyclerView.setAdapter(adapter);
        alluser();

        getWindow().setNavigationBarColor(getColor(R.color.whitelight));
        getWindow().setStatusBarColor(getColor(R.color.white));
    }

    public void alluser()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbrequestArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DBREQUEST dbrequest=dataSnapshot.getValue(DBREQUEST.class);
                    dbrequestArrayList.add(dbrequest);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(REQUESTPAGE.this,FRIENDSCHAT.class);
        startActivity(intent);
        finish();
    }
}