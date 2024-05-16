package com.chat.mark3;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.viewholder> {
    FRIENDSCHAT friendschat;
    ArrayList<DBFRIENDS> usersArrayList;
   List<DBFRIENDS> filteredList;
   int a=0;
    public FriendsAdapter(FRIENDSCHAT friendschat, ArrayList<DBFRIENDS> usersArrayList) {
        this.friendschat=friendschat;
        this.usersArrayList=usersArrayList;
        this.filteredList = new ArrayList<>(usersArrayList);

    }
    @NonNull
    @Override
    public FriendsAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(friendschat).inflate(R.layout.friends_list_design,parent,false);
        return new viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.viewholder holder, int position) {
        DBFRIENDS dbfriends;
        if(a==1)
        {
           dbfriends=filteredList.get(position);
        }
        else
        {
            dbfriends=usersArrayList.get(position);
        }

        String senderId= FirebaseAuth.getInstance().getUid();
        String senderRoom=senderId+dbfriends.getFid();
        FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("lastmsg").exists()) {
                    String lastmsg = snapshot.child("lastmsg").getValue(String.class);
                    String time = snapshot.child("lastmsgTime").getValue(String.class);
                    holder.useronoff.setVisibility(View.VISIBLE);
                    holder.userstatus.setText(lastmsg);
                    holder.useronoff.setText(time);
                }
                else
                {
                    holder.userstatus.setText(dbfriends.getFusername());
                    holder.useronoff.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(dbfriends.getSpecialfriend().equals("Yes"))
        {
           Picasso.get().load(dbfriends.getFphoto()).into(holder.userimg);
           holder.username.setText(dbfriends.getFstatusname());
            FirebaseDatabase.getInstance().getReference().child("USERLOGIN").child(dbfriends.getFid()).child("statusonoff").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue(String.class).equals("Ofline"))
                    {
                        holder.ONLINE_IMG.setVisibility(View.GONE);
                    }
                    else
                    {
                        holder.ONLINE_IMG.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            FirebaseDatabase.getInstance().getReference().child("USERLOGIN").child(dbfriends.getFid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    holder.username.setText(snapshot.child("statusname").getValue(String.class));
                    if (snapshot.child("statusonoff").getValue(String.class).equals("Ofline")) {
                        holder.ONLINE_IMG.setVisibility(View.GONE);
                    } else {
                        holder.ONLINE_IMG.setVisibility(View.VISIBLE);

                    }
                    Picasso.get().load(snapshot.child("profileimg").getValue(String.class)).into(holder.userimg);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(friendschat, CHATWINDOW.class);
                intent.putExtra("UID",dbfriends.getFid());
                friendschat.startActivity(intent);
                friendschat.finish();
            }
        });
        holder.userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final Dialog dialog=new Dialog(friendschat);
                dialog.setContentView(R.layout.dailog_profile);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageView imageView=dialog.findViewById(R.id.dialog_profilepic);
                Glide.with(friendschat).load(holder.userimg.getDrawable()).circleCrop().into(imageView);
                dialog.show();

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog=new Dialog(friendschat);
                dialog.setContentView(R.layout.dialog_user);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView deleteuser=dialog.findViewById(R.id.DELETE_USER);
                TextView mute=dialog.findViewById(R.id.MUTE);
                dialog.show();
                deleteuser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().child("FRIENDS").child(FirebaseAuth.getInstance().getUid()).child("fid").child(dbfriends.getFid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    FirebaseDatabase.getInstance().getReference().child("FRIENDS").child(dbfriends.getFid()).child("fid").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isComplete()) {
                                                dialog.dismiss();
                                                Toast.makeText(friendschat, "Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
                FirebaseDatabase.getInstance().getReference("FRIENDS").child(FirebaseAuth.getInstance().getUid()).child("fid").child(dbfriends.getFid()).child("mute").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue(String.class).equals("No"))
                        {
                            mute.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseDatabase.getInstance().getReference("FRIENDS").child(FirebaseAuth.getInstance().getUid()).child("fid").child(dbfriends.getFid()).child("mute").setValue("Yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isComplete())
                                            {
                                                dialog.dismiss();
                                                Toast.makeText(friendschat,"Muted",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                        else
                        {
                            mute.setText("UnMute");
                            mute.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseDatabase.getInstance().getReference("FRIENDS").child(FirebaseAuth.getInstance().getUid()).child("fid").child(dbfriends.getFid()).child("mute").setValue("No").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isComplete())
                                            {
                                                dialog.dismiss();
                                                Toast.makeText(friendschat,"UnMute",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                return false;
            }
        });
    }
    @Override
    public int getItemCount() {
        if(a==1) {
            return filteredList.size();
        }
        else
        {
            return usersArrayList.size();
        }
    }
    public static class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimg,ONLINE_IMG;

        ImageView DELETE;
        TextView username,userstatus,useronoff;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            DELETE=itemView.findViewById(R.id.delete);
            userimg=itemView.findViewById(R.id.profile);
            username=itemView.findViewById(R.id.username);
            userstatus=itemView.findViewById(R.id.userstatus);
            useronoff=itemView.findViewById(R.id.userstatusonoff);
            ONLINE_IMG=itemView.findViewById(R.id.ONLINE);
           // ONLINE_IMG.setVisibility(View.GONE);
            DELETE.setVisibility(View.GONE);
        }
    }
    public  void filter(String query) {

        filteredList.clear();
        if (TextUtils.isEmpty(query)) {
            filteredList.addAll(usersArrayList);
        } else {
            query = query.toLowerCase();
            for (DBFRIENDS user : usersArrayList) {
                if (user.getFstatusname().toLowerCase().contains(query)) {
                    a=1;
                    filteredList.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }
}
