package com.chat.mark3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.viewholder> {
    REQUESTPAGE requestpage;
    ArrayList<DBREQUEST> usersArrayList;
    String OWNUSERNAME;

    public RequestAdapter(REQUESTPAGE requestpage, ArrayList<DBREQUEST> usersArrayList) {
        this.requestpage=requestpage;
        this.usersArrayList=usersArrayList;

    }
    @NonNull
    @Override
    public RequestAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(requestpage).inflate(R.layout.request_list_design,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.viewholder holder, int position) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        DBREQUEST dbrequest=usersArrayList.get(position);
        holder.userstatus.setText(dbrequest.getRequests());
        FirebaseDatabase.getInstance().getReference().child("USERLOGIN").child(dbrequest.getRequests().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.username.setText(snapshot.child("statusname").getValue(String.class));
                holder.userstatus.setText(snapshot.child("username").getValue(String.class));
                Picasso.get().load(snapshot.child("profileimg").getValue(String.class)).into(holder.userimg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database1=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=database1.getReference().child("USERLOGIN").child(dbrequest.getRequests().toString());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        OWNUSERNAME = snapshot.child("username").getValue(String.class);
                        String OWNPHOTO=snapshot.child("profileimg").getValue(String.class);
                        String OWNSTATUSNAME=snapshot.child("statusname").getValue(String.class);
                        String FID = dbrequest.getRequests().toString();
                        DBFRIENDS DB = new DBFRIENDS(FID, OWNUSERNAME,OWNPHOTO,"No",OWNSTATUSNAME,"No");
                       FirebaseDatabase.getInstance().getReference().child("USERLOGIN").child(auth.getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               String FSTATUSNAME=snapshot.child("statusname").getValue(String.class);
                               FirebaseDatabase.getInstance().getReference().child("FRIENDS").child(auth.getUid()).child("fid").child(FID).setValue(DB);
                               String FID2 = auth.getUid().toString();
                               String FUSERNAME = dbrequest.getFusername().toString();
                               String FPHOTO=dbrequest.getFphoto().toString();
                               DBFRIENDS DB1 = new DBFRIENDS(FID2, FUSERNAME,FPHOTO,"No",FSTATUSNAME,"No");
                               FirebaseDatabase.getInstance().getReference().child("FRIENDS").child(dbrequest.getRequests().toString()).child("fid").child(FID2).setValue(DB1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isComplete())
                                       {
                                           sendNotification(FSTATUSNAME, dbrequest.getRequestid()," has accepted your request");
                                       }
                                   }
                               });

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
                FirebaseDatabase.getInstance().getReference().child("REQUEST").child(auth.getUid()).child(dbrequest.getRequestid().toString()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        holder.accepttext.setText("done");
                    }
                });
            }
        });
        holder.rejectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("REQUEST").child(auth.getUid()).child(dbrequest.getRequestid().toString()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        holder.accepttext.setText("done");
                        FirebaseDatabase.getInstance().getReference("USERLOGIN").child(auth.getUid()).child("statusname").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String OWNNAME=snapshot.getValue(String.class);
                                sendNotification(OWNNAME,dbrequest.getRequestid()," has rejected your request");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }



    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimg;
        CardView acceptbtn,rejectbtn;
        TextView username,userstatus,accepttext;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimg=itemView.findViewById(R.id.PROFILEPIC);
            username=itemView.findViewById(R.id.NAMETEXT);
            userstatus=itemView.findViewById(R.id.BIOTEXT);
            accepttext=itemView.findViewById(R.id.accepttext);
            acceptbtn=itemView.findViewById(R.id.ACCEPTBTN);
            rejectbtn=itemView.findViewById(R.id.REJECTBTN);
        }
    }

    public void sendNotification(String USERNAME,String ID,String MESSAGE)
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
                            notificationobj.put("title",dblogin.getSTATUSNAME());
                            notificationobj.put("body",USERNAME+MESSAGE);
                            notificationobj.put("image_url","https://firebasestorage.googleapis.com/v0/b/mark4-c3fbe.appspot.com/o/user.png?alt=media&token=9f461808-7343-43e9-ac65-ff350cbfcbdc");
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


