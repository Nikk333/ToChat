package com.chat.mark3;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {
    HOME home;
    ArrayList<DBLOGIN> usersArrayList;
    List<DBLOGIN> filteredList;
    int a=0;
    public UserAdapter(HOME home, ArrayList<DBLOGIN> usersArrayList) {
        this.home=home;
        this.usersArrayList=usersArrayList;
        this.filteredList = new ArrayList<>(usersArrayList);
    }

    @NonNull
    @Override
    public UserAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(home).inflate(R.layout.user_list_design,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.viewholder holder, int position) {
       DBLOGIN dblogin;

      if(a==1)
        {
            dblogin=filteredList.get(position);
            FirebaseDatabase.getInstance().getReference("FRIENDS").child(FirebaseAuth.getInstance().getUid().toString()).child("fid").child(dblogin.getUID().toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        holder.layout.setVisibility(View.GONE);
                    }
                    else
                    {
                        holder.username.setText(dblogin.getSTATUSNAME());
                        holder.userstatus.setText(dblogin.getBIO());
                        Picasso.get().load(dblogin.getPROFILEIMG()).into(holder.userimg);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            dblogin=usersArrayList.get(position);
                 FirebaseDatabase.getInstance().getReference("FRIENDS").child(FirebaseAuth.getInstance().getUid()).child("fid").child(dblogin.getUID().toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        holder.layout.setVisibility(View.GONE);
                    }
                    else
                    {
                        FirebaseDatabase.getInstance().getReference("REQUEST").child(dblogin.getUID()).child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    holder.layout.setVisibility(View.GONE);
                                }
                                else {
                                    holder.username.setText(dblogin.getSTATUSNAME());
                                    holder.userstatus.setText(dblogin.getBIO());
                                    Picasso.get().load(dblogin.getPROFILEIMG()).into(holder.userimg);
                                }
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        holder.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth auth=FirebaseAuth.getInstance();
                FirebaseDatabase database;
                database=FirebaseDatabase.getInstance();
                holder.addtext.setText("Friend");
                String key=auth.getUid();
                DatabaseReference databaseReference1=database.getReference().child("REQUEST").child(dblogin.getUID().toString()).child(key);
                DBREQUEST dbrequest=new DBREQUEST(auth.getUid().toString(),key,dblogin.getUSERNAME().toString(),dblogin.getPROFILEIMG().toString());
                databaseReference1.setValue(dbrequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete())
                        {
                            FirebaseDatabase.getInstance().getReference("USERLOGIN").child(auth.getUid()).child("statusname").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String uname=snapshot.getValue(String.class);
                                    sendNotification(uname,dblogin.getUID());
                                    holder.layout.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }
                });
            }
        });
      }
    }
    @Override
    public int getItemCount()
    {
        if(a==1) {
            return filteredList.size();
        }
        else
        {
            return usersArrayList.size();
        }
    }
    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimg;
        CardView addbtn;
        TextView username,userstatus,addtext;
        LinearLayout layout;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimg=itemView.findViewById(R.id.PROFILEPIC);
            username=itemView.findViewById(R.id.NAMETEXT);
            userstatus=itemView.findViewById(R.id.BIOTEXT);
            addbtn=itemView.findViewById(R.id.ADDBTN);
            addtext=itemView.findViewById(R.id.addtext);
            layout=itemView.findViewById(R.id.thay_to);
        }
    }
    public  void filter(String query) {

        filteredList.clear();
        if (TextUtils.isEmpty(query)) {
            filteredList.addAll(usersArrayList);
        } else {
            query = query.toLowerCase();
            for (DBLOGIN user : usersArrayList) {
                if (user.getSTATUSNAME().toLowerCase().contains(query)||user.getUSERNAME().toLowerCase().contains(query)) {
                    a=1;
                    filteredList.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }




    public void sendNotification(String USERNAME,String ID)
    {


        FirebaseDatabase.getInstance().getReference("USERLOGIN").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DBLOGIN usertoken=snapshot.getValue(DBLOGIN.class);
                FirebaseDatabase.getInstance().getReference("USERLOGIN").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            DBLOGIN dblogin=snapshot.getValue(DBLOGIN.class);

                            JSONObject jsonObject=new JSONObject();
                            JSONObject notificationobj=new JSONObject();
                            notificationobj.put("title",USERNAME);
                            notificationobj.put("body",USERNAME+" has sent you request");
                            notificationobj.put("icon","https://firebasestorage.googleapis.com/v0/b/mark4-c3fbe.appspot.com/o/user.png?alt=media&token=9f461808-7343-43e9-ac65-ff350cbfcbdc");
                            JSONObject dataobj=new JSONObject();
                            dataobj.put("userId",dblogin.getUID());
                            jsonObject.put("notification",notificationobj);
                            jsonObject.put("data",dataobj);
                            jsonObject.put("to",usertoken.getNOTIFICATIONTOKEN());
                            callApi(jsonObject);
                        }
                        catch (Exception e)
                        {}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void  callApi(JSONObject jsonObject)
    {
        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String url="https://fcm.googleapis.com/fcm/send";
        RequestBody body=RequestBody.create(jsonObject.toString(),JSON);
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization","Bearer AAAAogMUOjc:APA91bH7prNMYCcPWS2ziMEVo4wzZFQLl1D9R6oD_u4tQx2IGmLWOrcYNNyWF3_UULd7Zzq4Na4yxKKGHSDaSH49kbK7froa-U4fb816oOlzCljnPtnH-8uOEEvFVimv7tOd8xWEpvAu")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }
}
