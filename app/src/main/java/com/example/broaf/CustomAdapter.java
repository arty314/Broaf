package com.example.broaf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Alarm> arrayList;
    private Context context;

    public CustomAdapter(ArrayList<Alarm> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.notice_title.setText(arrayList.get(position).getTitle());
        holder.notice_context.setText(arrayList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size(): 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView notice_image;
        TextView notice_title;
        TextView notice_context;
        public CustomViewHolder(@NonNull View itemView) {


            super(itemView);
            this.notice_image = itemView.findViewById(R.id.notice_image);
            this.notice_title = (TextView) itemView.findViewById(R.id.notice_title);
            this.notice_context = (TextView) itemView.findViewById(R.id.notice_context);
        }
    }
}
