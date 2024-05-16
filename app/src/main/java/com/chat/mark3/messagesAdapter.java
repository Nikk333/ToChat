package com.chat.mark3;

import static com.chat.mark3.R.drawable.reciver_chat_design;
import static com.chat.mark3.R.drawable.sender_chat_design;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class messagesAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private int isDarkMode;

    Context context;
    ArrayList<DBMESSAGE> DBMESSAGEArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;
    String senderRoom;
    String reciverRoom;
    String ID;
    public messagesAdapter(Context context,ArrayList<DBMESSAGE> DBMESSAGEArrayList,String senderRoom,String reciverRoom,String ID)
    {
        inflater=LayoutInflater.from(context);
        int currentmode=context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(currentmode==Configuration.UI_MODE_NIGHT_YES)
        {
            isDarkMode=1;
        }
        else
        {
            isDarkMode=0;
        }
        this.context=context;
        this.DBMESSAGEArrayList = DBMESSAGEArrayList;
        this.senderRoom=senderRoom;
        this.reciverRoom=reciverRoom;
        this.ID=ID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.senderchatdesign_layout,parent,false);
            return new senderViewHolder(view);
        }
        else
        {
            View view=LayoutInflater.from(context).inflate(R.layout.reciverchatdesign_layout,parent,false);
            return new reciverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DBMESSAGE DBMESSAGE = DBMESSAGEArrayList.get(position);
            int reactions[] = new int[]{
                    R.drawable.thumbs,
                    R.drawable.love,
                    R.drawable.laugh,
                    R.drawable.wow,
                    R.drawable.sad,
                    R.drawable.angry};
            ReactionsConfig config = new ReactionsConfigBuilder(context).withReactions(reactions).build();
                ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
                    if(pos>=0) {
                        if (holder.getClass() == senderViewHolder.class) {
                            senderViewHolder viewHolder = (senderViewHolder) holder;
                            viewHolder.senderreact.setImageResource(reactions[pos]);
                            viewHolder.senderreact.setVisibility(View.VISIBLE);
                            /******************************************************/
                            if(reactions[pos]==0) {
                                sendNotification(DBMESSAGE.getMessage(), ID, "\uD83D\uDC4D");
                            }


                            /******************************************************/
                        } else {
                            reciverViewHolder viewHolder = (reciverViewHolder) holder;
                            viewHolder.reciverreact.setImageResource(reactions[pos]);
                            viewHolder.reciverreact.setVisibility(View.VISIBLE);
                        }
                    }
                    else {

                        /*null for react not crash*/
                    }
                    DBMESSAGE.setReact(pos);
                    FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("message").child(DBMESSAGE.getMessageid()).setValue(DBMESSAGE);
                    FirebaseDatabase.getInstance().getReference().child("chats").child(reciverRoom).child("message").child(DBMESSAGE.getMessageid()).setValue(DBMESSAGE);
                    return true;
                });




/****************************************************************[load messages from here]*************************************************************************/
            if (holder.getClass() == senderViewHolder.class) {
                senderViewHolder viewHolder = (senderViewHolder) holder;
                if (DBMESSAGE.getReact() >= 0) {

                   // viewHolder.DELETE.setVisibility(View.GONE);
                    viewHolder.senderreact.setImageResource(reactions[DBMESSAGE.getReact()]);
                    viewHolder.senderreact.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.senderreact.setVisibility(View.GONE);
                }

                viewHolder.msgtxt.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popup.onTouch(v, event);
                        return false;
                    }
                });
                viewHolder.SEND_DELETE_LAYOUT.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        viewHolder.SEND_DELETE_LAYOUT.setBackgroundColor(context.getColor(R.color.lightpurple));
                        final Dialog dialog=new Dialog(context);
                        dialog.setContentView(R.layout.dialog_delete);
                        dialog.setCancelable(true);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        TextView MY=dialog.findViewById(R.id.DELETE_MY_CHAT);
                        TextView EVERYONE=dialog.findViewById(R.id.DELTE_EVERY_CHAT);
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                viewHolder.SEND_DELETE_LAYOUT.setBackgroundColor(context.getColor(android.R.color.transparent));

                            }
                        });
                        MY.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("message").child(DBMESSAGE.getMessageid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isComplete()) {
                                            dialog.dismiss();
                                            viewHolder.SEND_DELETE_LAYOUT.setBackgroundColor(context.getColor(android.R.color.transparent));

                                        }

                                    }
                                });
                            }
                        });
                        EVERYONE.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("message").child(DBMESSAGE.getMessageid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isComplete()) {
                                            FirebaseDatabase.getInstance().getReference().child("chats").child(reciverRoom).child("message").child(DBMESSAGE.getMessageid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    dialog.dismiss();
                                                    viewHolder.SEND_DELETE_LAYOUT.setBackgroundColor(context.getColor(android.R.color.transparent));
                                                }
                                            });
                                        }

                                    }
                                });

                            }
                        });



                        return false;
                    }
                });

                /**********************[sender message load]*************************/
                if(DBMESSAGE.getMessage().equals("nullmessage"))
                {
                    viewHolder.msgtxt.setText(DBMESSAGE.getMessage());
                    viewHolder.sendertime.setText(DBMESSAGE.getSendertime());
                    viewHolder.msgtxt.setVisibility(View.GONE);
                    viewHolder.sendertime.setVisibility(View.VISIBLE);
                    viewHolder.SENDERIMAGES.setVisibility(View.VISIBLE);
                    Glide.with(context).load(DBMESSAGE.getImages().toString()).into(((senderViewHolder) holder).SENDERIMAGES);
                }
                else
                {
                    if(DBMESSAGE.getImages().equals("nullimage"))
                    {
                        viewHolder.SENDERIMAGES.setVisibility(View.GONE);
                        viewHolder.msgtxt.setVisibility(View.VISIBLE);
                        viewHolder.sendertime.setVisibility(View.VISIBLE);
                        viewHolder.msgtxt.setText(DBMESSAGE.getMessage());
                        viewHolder.sendertime.setText(DBMESSAGE.getSendertime());
                    }
                    else
                    {
                        viewHolder.SENDERIMAGES.setVisibility(View.VISIBLE);
                        viewHolder.msgtxt.setVisibility(View.VISIBLE);
                        viewHolder.sendertime.setVisibility(View.VISIBLE);
                        viewHolder.msgtxt.setText(DBMESSAGE.getMessage());
                        viewHolder.sendertime.setText(DBMESSAGE.getSendertime());
                        Glide.with(context).load(DBMESSAGE.getImages().toString()).into(((senderViewHolder) holder).SENDERIMAGES);
                    }
                }
                viewHolder.SENDERIMAGES.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, CHAT_PHOTO.class);
                        intent.putExtra("CHATPHOTO",DBMESSAGE.getImages().toString());
                        context.startActivity(intent);
                    }
                });
            }
            else
            {
                    reciverViewHolder viewHolder = (reciverViewHolder) holder;
                    if (DBMESSAGE.getReact() >= 0)
                    {
                        viewHolder.reciverreact.setImageResource(reactions[DBMESSAGE.getReact()]);
                        viewHolder.reciverreact.setVisibility(View.VISIBLE);
                    }
                    else {
                        viewHolder.reciverreact.setVisibility(View.GONE);
                    }
                    viewHolder.msgtxt.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popup.onTouch(v, event);
                        return false;
                    }


                });
                    viewHolder.RECIVEIMAGES.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(context, CHAT_PHOTO.class);
                            intent.putExtra("CHATPHOTO",DBMESSAGE.getImages().toString());
                            context.startActivity(intent);
                        }
                    });
                viewHolder.RECIVE_DELETE_LAYOUT.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        viewHolder.RECIVE_DELETE_LAYOUT.setBackgroundColor(context.getColor(R.color.lightpurple));
                        final Dialog dialog=new Dialog(context);
                        dialog.setContentView(R.layout.dialog_delete);
                        dialog.setCancelable(true);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        TextView MY=dialog.findViewById(R.id.DELETE_MY_CHAT);
                        TextView EVERYONE=dialog.findViewById(R.id.DELTE_EVERY_CHAT);
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                viewHolder.RECIVE_DELETE_LAYOUT.setBackgroundColor(context.getColor(android.R.color.transparent));

                            }
                        });
                        MY.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FirebaseDatabase.getInstance().getReference().child("chats").child(reciverRoom ).child("message").child(DBMESSAGE.getMessageid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isComplete()) {
                                            dialog.dismiss();
                                            viewHolder.RECIVE_DELETE_LAYOUT.setBackgroundColor(context.getColor(android.R.color.transparent));

                                        }

                                    }
                                });
                            }
                        });
                        EVERYONE.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("message").child(DBMESSAGE.getMessageid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isComplete()) {
                                            FirebaseDatabase.getInstance().getReference().child("chats").child(reciverRoom).child("message").child(DBMESSAGE.getMessageid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    dialog.dismiss();
                                                    viewHolder.RECIVE_DELETE_LAYOUT.setBackgroundColor(context.getColor(android.R.color.transparent));
                                                }
                                            });
                                        }

                                    }
                                });

                            }
                        });



                        return false;
                    }
                });

                /*************************[recever load from here]***************************/
                if(DBMESSAGE.getMessage().equals("nullmessage"))
                {
                    viewHolder.msgtxt.setText(DBMESSAGE.getMessage());
                    viewHolder.recivetime.setText(DBMESSAGE.getSendertime());
                    viewHolder.msgtxt.setVisibility(View.GONE);
                    viewHolder.recivetime.setVisibility(View.VISIBLE);
                    viewHolder.RECIVEIMAGES.setVisibility(View.VISIBLE);
                    Glide.with(context).load(DBMESSAGE.getImages().toString()).into(((reciverViewHolder) holder).RECIVEIMAGES);
                }
                else
                {
                    if(DBMESSAGE.getImages().equals("nullimage"))
                    {
                        viewHolder.msgtxt.setVisibility(View.VISIBLE);
                        viewHolder.recivetime.setVisibility(View.VISIBLE);
                        viewHolder.msgtxt.setText(DBMESSAGE.getMessage());
                        viewHolder.recivetime.setText(DBMESSAGE.getSendertime());
                        viewHolder.RECIVEIMAGES.setVisibility(View.GONE);
                    }
                    else
                    {
                        viewHolder.msgtxt.setVisibility(View.VISIBLE);
                        viewHolder.recivetime.setVisibility(View.VISIBLE);
                        viewHolder.RECIVEIMAGES.setVisibility(View.VISIBLE);
                        viewHolder.msgtxt.setText(DBMESSAGE.getMessage());
                        viewHolder.recivetime.setText(DBMESSAGE.getSendertime());
                        Glide.with(context).load(DBMESSAGE.getImages().toString()).into(((reciverViewHolder) holder).RECIVEIMAGES);
                    }
                }
            }
    }

    @Override
    public int getItemCount() {
        return DBMESSAGEArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
       DBMESSAGE DBMESSAGE = DBMESSAGEArrayList.get(position);
       if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(DBMESSAGE.getSenderid()))
       {
           return ITEM_SEND;
       }
       else
       {
           return ITEM_RECIVE;
       }
    }

    class senderViewHolder extends RecyclerView.ViewHolder
    {
        TextView msgtxt,sendertime;
        ImageView senderreact,SENDERIMAGES;
        LinearLayout SEND_DELETE_LAYOUT;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            SENDERIMAGES=itemView.findViewById(R.id.SENDERIMAGE);
            senderreact=itemView.findViewById(R.id.senderreact);
            msgtxt=itemView.findViewById(R.id.sendertextset);
            sendertime=itemView.findViewById(R.id.sendertime);
            SEND_DELETE_LAYOUT =itemView.findViewById(R.id.SEND_HOLD_DELL);
            if(isDarkMode==1)
            {
                sendertime.setTextColor(context.getColor(R.color.whitehighlight));
                msgtxt.setTextColor(context.getColor(R.color.blacklight));
                msgtxt.setBackground(context.getDrawable(sender_chat_design));
                // Toast.makeText(context,"(dark)",Toast.LENGTH_LONG).show();
            }
            else
            {
               // Toast.makeText(context,"(light)",Toast.LENGTH_LONG).show();
            }

        }
    }
    class reciverViewHolder extends RecyclerView.ViewHolder {
        TextView msgtxt, recivetime;
        ImageView reciverreact, RECIVEIMAGES;
        LinearLayout RECIVE_DELETE_LAYOUT;

        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);

            RECIVEIMAGES = itemView.findViewById(R.id.RECIVEIMAGE);
            reciverreact = itemView.findViewById(R.id.recivereact);
            msgtxt = itemView.findViewById(R.id.recivertextset);
            recivetime = itemView.findViewById(R.id.recivertime);
            RECIVE_DELETE_LAYOUT=itemView.findViewById(R.id.RECIVE_HOLD_DELL);
            if (isDarkMode == 1) {
                recivetime.setTextColor(context.getColor(R.color.whitehighlight));
                msgtxt.setTextColor(context.getColor(R.color.whitelight));
                msgtxt.setBackground(context.getDrawable(reciver_chat_design));

            }

        }

    }
    public void sendNotification(String message,String ID,String Reaction)
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
                            notificationobj.put("body",message+Reaction);
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
