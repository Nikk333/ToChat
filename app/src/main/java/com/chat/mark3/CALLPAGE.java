package com.chat.mark3;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

import java.util.ArrayList;
import java.util.List;

public class CALLPAGE extends AppCompatActivity {
    RelativeLayout relativeLayout;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    CallAdapter adapter;
    FirebaseDatabase database;
    ArrayList<DBFRIENDS> usersArrayList;
    DatabaseReference databaseReference;
    List<String>stringlist;
    String uid,unames;
    EditText SEARCHBOX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callpage);
        getWindow().setNavigationBarColor(getColor(R.color.whitelight));
        getWindow().setStatusBarColor(getColor(R.color.white));
        relativeLayout=findViewById(R.id.rlayout);
        auth=FirebaseAuth.getInstance();
        stringlist=new ArrayList<>();
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("FRIENDS").child(auth.getUid()).child("fid");
        usersArrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.USERLISTVIEW);
        SEARCHBOX=findViewById(R.id.SEARCH_CALLPAGE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CallAdapter(CALLPAGE.this,usersArrayList);
        recyclerView.setAdapter(adapter);
        alluser();
    }
    public void alluser()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DBFRIENDS dbfriends =dataSnapshot.getValue(DBFRIENDS.class);
                    usersArrayList.add(dbfriends);
                }
                callid(auth.getUid().toString(),unames);
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

    public void callid(String uid,String uname)
    {
        String abc="5161c4fdc26ddb8c7bf883f426c6e9baaed587d3913c2b42e38438dfe954a13e";
        Application application = getApplication();
        long appID =275358039 ;   // yourAppID
        String appSign =abc;
        String userID =uid;
        String userName = uname;

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(CALLPAGE.this,FRIENDSCHAT.class);
        startActivity(intent);
        super.onBackPressed();
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