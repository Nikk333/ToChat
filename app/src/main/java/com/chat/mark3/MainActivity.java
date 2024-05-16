package com.chat.mark3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Handler h = new Handler();
    int progress = 0;
    String NAME=null;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setNavigationBarColor(getColor(R.color.white));
        getWindow().setStatusBarColor(getColor(R.color.white));
        auth=FirebaseAuth.getInstance();
        tv=findViewById(R.id.hide_name);
        if(auth.getCurrentUser()!=null) {
            FirebaseDatabase.getInstance().getReference().child("USERLOGIN").child(auth.getUid().toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    NAME = snapshot.child("statusname").getValue(String.class);
                    tv.setText(NAME);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (progress < 100) {
                                progress++;
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (progress == 1) {
                                            if (auth.getCurrentUser() == null) {
                                                startActivity(new Intent(getApplicationContext(), INFOPAGE.class));
                                                finish();
                                            } else {
                                                String abc = "5161c4fdc26ddb8c7bf883f426c6e9baaed587d3913c2b42e38438dfe954a13e";
                                                long appID = 275358039;   // yourAppID
                                                String appSign = abc;
                                                String userID = auth.getUid();
                                                String userName = tv.getText().toString();
                                                ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
                                                ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName, callInvitationConfig);
                                                startActivity(new Intent(getApplicationContext(),FRIENDSCHAT.class));
                                                finish();
                                            }

                                        }
                                    }
                                });
                                try {
                                    Thread.sleep(70);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }).start();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        else
        {
            startActivity(new Intent(getApplicationContext(), INFOPAGE.class));
            finish();
        }

    }
}