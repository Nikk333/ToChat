package com.chat.mark3;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.viewholder> {
    STATUS status;
    ArrayList<DBSTATUS> statusArrayList;
    List<DBSTATUS> filteredList;
    int a = 0;

    public StatusAdapter(STATUS status, ArrayList<DBSTATUS> statusArrayList) {
        this.status = status;
        this.statusArrayList = statusArrayList;
        this.filteredList = new ArrayList<>(statusArrayList);
    }

    @NonNull
    @Override
    public StatusAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(status).inflate(R.layout.moments_list_design, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        DBSTATUS dbstatus;
        if (a == 1) {
            dbstatus = filteredList.get(position);
        } else {
            dbstatus = statusArrayList.get(position);
        }

        holder.textView1.setText("" + dbstatus.getStatusname());
        Picasso.get().load(dbstatus.getUserphoto()).into(holder.profile1);
        Glide.with(status).load(dbstatus.getStatus()).into(holder.imageView1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(status, MOMENTPAGE.class);
                intent.putExtra("PHOTO", dbstatus.getStatus());
                status.startActivity(intent);
                status.finish();
            }
        });
    }


    @Override
    public int getItemCount() {
        if (a == 1) {
            return filteredList.size();
        } else {
            return statusArrayList.size();
        }

    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView profile1, profile2;
        ImageView imageView1, imageView2;
        TextView textView1, textView2;
        LinearLayout LAYOUT1, LAYOUT2;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            profile1 = itemView.findViewById(R.id.CIRCLE1);
            imageView1 = itemView.findViewById(R.id.IMAGE1);
            textView1 = itemView.findViewById(R.id.text1);
            profile2 = itemView.findViewById(R.id.CIRCLE2);
            imageView2 = itemView.findViewById(R.id.IMAGE2);
            textView2 = itemView.findViewById(R.id.text2);
            LAYOUT1 = itemView.findViewById(R.id.LAYOUT1);
            LAYOUT2 = itemView.findViewById(R.id.LAYOUT2);
            LAYOUT2.setVisibility(View.GONE);
        }
    }

    public void filter(String query) {

        filteredList.clear();
        if (TextUtils.isEmpty(query)) {
            filteredList.addAll(statusArrayList);
        } else {
            query = query.toLowerCase();
            for (DBSTATUS user : statusArrayList) {
                if (user.getUsername().toLowerCase().contains(query) || user.getStatusname().toLowerCase().contains(query)) {
                    a = 1;
                    filteredList.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }
}
