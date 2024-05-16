package com.chat.mark3;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class FRIENDSCHAT extends AppCompatActivity {
    RelativeLayout relativeLayout;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    FriendsAdapter adapter;
    FirebaseDatabase database;
    ArrayList<DBFRIENDS> usersArrayList;
    DatabaseReference databaseReference;
    List<String>stringlist;
    String GETPROFILE,unames;
    EditText SearchBox;

    /***************************[MENU]*************************/
    CircleImageView PROFILE_BTN;
    ImageView STATUS_BTN,CALL_BTN,REQUESTBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendschat);
        /*************************[MENU WORK]************************************/
        PROFILE_BTN=findViewById(R.id.PROFILE_MENU);
        STATUS_BTN=findViewById(R.id.STATUS_MENU);
        CALL_BTN=findViewById(R.id.CALL_MENU);
        SearchBox=findViewById(R.id.SEARCH_FRIENDSCHAT);
        REQUESTBTN=findViewById(R.id.REQUEST_BTN);

        STATUS_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FRIENDSCHAT.this,STATUS.class);
                startActivity(intent);
                finish();
            }
        });
        CALL_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FRIENDSCHAT.this,CALLPAGE.class);
                startActivity(intent);
                finish();
            }
        });
        PROFILE_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FRIENDSCHAT.this,SETTING.class);
                startActivity(intent);
                finish();
            }
        });
        REQUESTBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FRIENDSCHAT.this,HOME.class);
                startActivity(intent);
                finish();

            }
        });

        /***************************[MENU END]**********************************/

        getWindow().setNavigationBarColor(getColor(R.color.white));
        getWindow().setStatusBarColor(getColor(R.color.white));
        relativeLayout=findViewById(R.id.rlayout);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("FRIENDS").child(auth.getUid()).child("fid");
        PROFILE_FUN();
        stringlist=new ArrayList<>();
        usersArrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.USERLISTVIEW);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new FriendsAdapter(FRIENDSCHAT.this,usersArrayList);
        recyclerView.setAdapter(adapter);
        usersArrayList.clear();
        alluser();


        SearchBox.addTextChangedListener(new TextWatcher() {
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
private void PROFILE_FUN()
{
    FirebaseDatabase.getInstance().getReference().child("USERLOGIN").child(auth.getUid().toString()).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            unames=snapshot.child("username").getValue(String.class);
            GETPROFILE=snapshot.child("profileimg").getValue(String.class);
            Picasso.get().load(GETPROFILE).into(PROFILE_BTN);
            call(auth.getUid().toString(),unames);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

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
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void  call(String id,String name)
    {
        String abc="5161c4fdc26ddb8c7bf883f426c6e9baaed587d3913c2b42e38438dfe954a13e";
        Application application = getApplication();
        long appID =275358039 ;
        String appSign =abc;
        String userID =id;
        String userName =name;

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);
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
