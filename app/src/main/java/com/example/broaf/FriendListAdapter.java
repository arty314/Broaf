//FriendList recyclerview를 위한 Adapter
package com.example.broaf;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.CustomViewHolder> {


    private ArrayList<User> arrayList;
    private Context context;

    public FriendListAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.friend_nickname.setText(arrayList.get(position).getNickname());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size(): 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView friend_nickname;

        public CustomViewHolder(@NonNull View itemView) {

            super(itemView);
            this.friend_nickname = (TextView) itemView.findViewById(R.id.friend_nickname);
        }
    }
}
