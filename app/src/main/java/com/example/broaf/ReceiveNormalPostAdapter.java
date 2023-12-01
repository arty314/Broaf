package com.example.broaf;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReceiveNormalPostAdapter extends RecyclerView.Adapter<ReceiveNormalPostAdapter.PostViewHolder>{

    private Context context;
    private ArrayList<ReceiveNormalPost> arrayList;
    private ArrayList<ReceiveNormalPost> arrayListSearched; // 검색 결과를 보관할 리스트
    /*
        public void setFilter(ArrayList<Post> filteredList) {
            arrayListSearched.clear(); // arrayListSearched 초기화
            arrayListSearched.addAll(filteredList); // 필터링된 리스트를 arrayListSearched에 추가
            notifyDataSetChanged(); // RecyclerView 갱신
        }
    */
    public ReceiveNormalPostAdapter(Context context, ArrayList<ReceiveNormalPost> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListSearched = new ArrayList<>(arrayList); // 검색 결과 리스트 초기화
    }
    //실제 list가 연결될 다음 처음으로 ViewHolder를 만들어 낸다.
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post,parent, false);
        PostViewHolder holder = new PostViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        //holder.attachImageURL.setText(arrayList.get(position).getAttachImageURL());
        holder.content.setText(arrayList.get(position).getContents());
        // holder.isHide.setText(arrayList.get(position).getIsHide()); //현재 position에 있는 것을 가져와서
        //holder.latitude.setText(arrayList.get(position).getLatitude());
        holder.likeCount.setText(arrayList.get(position).getLikeCount());
        //holder.longitude.setText(arrayList.get(position).getLongitude());
        holder.openRange.setText(arrayList.get(position).getOpenRange());
        //holder.openToDateTime.setText(arrayList.get(position).getOpenToDateTime());
        //holder.writerName.setText(arrayList.get(position).getWriterName());
        //holder.writerUID.setText(arrayList.get(position).getWriterUID());
        //holder.writtenDateTime.setText(arrayList.get(position).getWrittenDateTime());
        String exisingText = arrayList.get(position).getWriterName();
        String newText = arrayList.get(position).getWriteTime();
        holder.writerName_And_writtenDateTime.setText(exisingText + " | " + newText);


    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView attachImageURL,content,isHide,latitude,likeCount,longitude,openRange,openToDateTime,writerName, writerUID,writtenDateTime;
        TextView writerName_And_writtenDateTime;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            //this.attachImageURL = itemView.findViewById(R.id.attachImageURL);
            this.content = itemView.findViewById(R.id.content);
            //this.isHide = itemView.findViewById(R.id.isHide);
            //this.latitude = itemView.findViewById(R.id.latitude);
            this.likeCount = itemView.findViewById(R.id.likeCount);
            //this.longitude = itemView.findViewById(R.id.longitude);
            this.openRange = itemView.findViewById(R.id.openRange);
            //this.openToDateTime = itemView.findViewById(R.id.openToDateTime);
            //this.writerName = itemView.findViewById(R.id.writerName);
            //this.writerUID = itemView.findViewById(R.id.writerUID);
            //this.writtenDateTime = itemView.findViewById(R.id.writtenDateTime);
            this.writerName_And_writtenDateTime = itemView.findViewById(R.id.writerName_And_writtenDateTime);
            //내게시글 모아보기 intent로 PostEditerActivity로 이동(NormalPost전체전보를 전달)

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        ReceiveNormalPost receiveNormalPost = arrayList.get(pos);

                        MainActivity activity = (MainActivity)context;
                        activity.replaceFragment(new ViewPostFragment(receiveNormalPost));
                    }
                }
            });
        }
    }
}