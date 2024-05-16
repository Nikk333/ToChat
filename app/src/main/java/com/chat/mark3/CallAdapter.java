package com.chat.mark3;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.viewholder> {
    CALLPAGE callpage;
    ArrayList<DBFRIENDS> usersArrayList;
    List<DBFRIENDS> filteredList;
    int a=0;
    String NAME="user";
    public CallAdapter(CALLPAGE callpage, ArrayList<DBFRIENDS> usersArrayList) {
        this.callpage=callpage;
        this.usersArrayList=usersArrayList;
        this.filteredList = new ArrayList<>(usersArrayList);
    }
    @NonNull
    @Override
    public CallAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(callpage).inflate(R.layout.call_list_design,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallAdapter.viewholder holder, int position) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        DBFRIENDS dbfriends;
        if(a==1)
        {
            dbfriends=filteredList.get(position);
        }
        else
        {
            dbfriends=usersArrayList.get(position);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(callpage,"fd",Toast.LENGTH_SHORT).show();
            }
        });
        FirebaseDatabase.getInstance().getReference("FRIENDS").child(auth.getUid()).child("fid").child(dbfriends.getFid()).child("specialfriend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue(String.class).equals("Yes"))
                {
                    holder.username.setText(dbfriends.getFstatusname());
                    holder.userstatus.setText(dbfriends.getFstatusname());
                    Picasso.get().load(dbfriends.getFphoto()).into(holder.userimg);
                }
               else
                {
                    FirebaseDatabase.getInstance().getReference().child("USERLOGIN").child(dbfriends.getFid().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            holder.username.setText(snapshot.child("statusname").getValue(String.class));
                            holder.userstatus.setText(snapshot.child("username").getValue(String.class));
                            Picasso.get().load(snapshot.child("profileimg").getValue(String.class)).into(holder.userimg);
                            NAME=snapshot.child("statusname").getValue(String.class);

                          }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String targetUserID,targetUserName;
        targetUserID = dbfriends.getFid().toString();
       targetUserName=dbfriends.getFstatusname().toString();
        btnVoicecall.setIsVideoCall(false);
        btnVoicecall.setResourceID("zego_uikit_call");
        btnVoicecall.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID, targetUserName)));
        btnVideocall.setIsVideoCall(true);
        btnVideocall.setResourceID("zego_uikit_call");
        btnVideocall.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID,targetUserName)));






    }
    @Override
    public int getItemCount() {
        if(a==1)
        {
            return filteredList.size();
        }
        else {
            return usersArrayList.size();
        }
    }
    ZegoSendCallInvitationButton btnVoicecall,btnVideocall;

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimg;
        TextView username,userstatus;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimg=itemView.findViewById(R.id.PROFILEPIC);

            username=itemView.findViewById(R.id.NAMETEXT);
            userstatus=itemView.findViewById(R.id.BIOTEXT);
            btnVoicecall=itemView.findViewById(R.id.voicecallbtn);
            btnVideocall=itemView.findViewById(R.id.videocallbtn);

        }


    }
    public  void filter(String query) {

        filteredList.clear();
        if (TextUtils.isEmpty(query)) {
            filteredList.addAll(usersArrayList);
        } else {
            query = query.toLowerCase();
            for (DBFRIENDS user : usersArrayList) {
                if (user.getFstatusname().toLowerCase().contains(query)||user.getFusername().toLowerCase().contains(query)) {
                    a=1;
                    filteredList.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

}


