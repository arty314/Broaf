package com.example.broaf;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyInfoNormalPostAdapter extends RecyclerView.Adapter<MyInfoNormalPostAdapter.PostViewHolder>{

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
    public MyInfoNormalPostAdapter(Context context, ArrayList<ReceiveNormalPost> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListSearched = new ArrayList<>(arrayList); // 검색 결과 리스트 초기화
    }
    //실제 list가 연결될 다음 처음으로 ViewHolder를 만들어 낸다.
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_mypost,parent, false);
        PostViewHolder holder = new PostViewHolder(view);

        ImageButton more_btn = view.findViewById(R.id.morebutton);
        more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setItems(R.array.edit_del, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        switch(i){
                            case 0:
                                //수정하는부분
                                Toast.makeText(view.getContext(), "수정인텐트 오류가 있습니다",Toast.LENGTH_SHORT).show();
                                ReceiveNormalPost normalPost = arrayList.get(holder.getBindingAdapterPosition());
                                Intent intent = new Intent(view.getContext(), PostEditerActivity.class);
                                intent.putExtra("edit_post", normalPost);
                                //view.getContext().startActivity(intent);
                                break;
                            case 1:
                                AlertDialog.Builder innerb = new AlertDialog.Builder(view.getContext());
                                innerb.setTitle("삭제하시갰습니까?");
                                innerb.setMessage("한번 삭제한 내용은 영원히 사라집니다.");
                                innerb.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //삭제수행
                                        String del_pid = arrayList.get(holder.getBindingAdapterPosition()).getPid();
                                        Query qy = db.child("Post").child("NormalPost").orderByKey().equalTo(del_pid);
                                        qy.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                try{for (DataSnapshot ds : snapshot.getChildren()) {
                                                    String del_pid = ds.child("pid").getValue(String.class);
                                                    if (del_pid != null) {
                                                        db.child("Post").child("NormalPost").child(del_pid).setValue(null);
                                                        Toast.makeText(view.getContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }} catch (Exception e) {Toast.makeText(view.getContext(), "존재하지않는 게시글입니다.", Toast.LENGTH_SHORT).show();}
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(view.getContext(), "게시글을 삭제하는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                innerb.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) { }//아무것도 안함
                                });
                                innerb.create().show();
                        }
                    }
                });
                builder.create().show();
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        //holder.attachImageURL.setText(arrayList.get(position).getAttachImageURL());
        holder.content.setText(arrayList.get(position).getContents());
        // holder.isHide.setText(arrayList.get(position).getIsHide()); //현재 position에 있는 것을 가져와서
        //holder.latitude.setText(arrayList.get(position).getLatitude());
//        holder.likeCount.setText(arrayList.get(position).getLikeCount());
        //holder.longitude.setText(arrayList.get(position).getLongitude());
        switch (arrayList.get(position).getOpenRange()) {
            case "1":
                holder.openRange.setText("전체 공개");
                break;
            case "2":
                holder.openRange.setText("친구 공개");
                break;
            case "3":
                holder.openRange.setText("비공개");
                break;
            default:
                holder.openRange.setText("시스템 오류입니다.");
        }
        //holder.openRange.setText(arrayList.get(position).getOpenRange()); String형태 그대로 받아들일 때
        //holder.openToDateTime.setText(arrayList.get(position).getOpenToDateTime());
        //holder.writerName.setText(arrayList.get(position).getWriterName());
        //holder.writerUID.setText(arrayList.get(position).getWriterUID());
        //holder.writtenDateTime.setText(arrayList.get(position).getWrittenDateTime());
        String exisingText = arrayList.get(position).getWriterName();
        String writetime = arrayList.get(position).getWriteTime();
        //얻은 시간을 년,월,일,시간,분으로 쪼갠다.
        String year = writetime.substring(0, 4);
        String month = writetime.substring(4, 6);
        String date = writetime.substring(6, 8);
        String hour = writetime.substring(8, 10);
        String minute = writetime.substring(10, 12);
        if (Integer.parseInt(hour) >= 0 && Integer.parseInt(hour) <= 12) { // 0~12시 사이면
            hour = "오전 " + hour; //오전+시간
        } else if (Integer.parseInt(hour) > 12 && Integer.parseInt(hour) <= 23) { //13~23시 사이면
            hour = "오후 " + String.valueOf(Integer.parseInt(hour) - 12); //오후(hour-12)
        }
        String newText = year + "." + month + "." + date + ". " + hour + ":" + minute;
        holder.writerName_And_writtenDateTime.setText(exisingText + "  |  " + newText);

////////////////////////
        //Add Data to listView
       /* ListView listView = new ListView(context);
        //Add data to Array List
        //List<String> data = new ArrayList<>();
        //추가
        ArrayList<String> category = new ArrayList<>();

        category.add("수정");
        category.add("삭제");

        //Create Array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, category);
        listView.setAdapter(adapter);

        //Now we add list View to alert bpx
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setView(listView);
        final AlertDialog dialog = builder.create();

        // do action to Edit text
        holder.morebutton.setOnClickListener(new View.OnClickListener() { //클릭이되면
            @Override
            public void onClick(View v) {
                dialog.show();
            } //dialog를 보여준다.
        });

        //add action to listView to select date to Edit text
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss(); // 다이얼로그 닫기
                if (position == 0) { // 수정
                    ReceiveNormalPost selectedItem = arrayListSearched.get(holder.getAdapterPosition());
                    Log.e("MyInfoClick", "수정: " + selectedItem.getContents());
                    // 선택한 항목의 내용을 표시하거나 전달하는 등의 작업을 수행합니다.

                    // PostEditerActivity로 데이터를 전달하는 Intent 생성


                } else if (position == 1) { // 삭제
                    ReceiveNormalPost selectedItem = arrayListSearched.get(holder.getAdapterPosition());
                    Log.e("MyInfoClick", "삭제: " + selectedItem.getContents());
                    // 선택한 항목의 내용을 표시하거나 전달하는 등의 작업을 수행합니다.
                }
            }
        });*/

//////////////////////////////
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView attachImageURL,content,isHide,latitude,likeCount,longitude,openRange,openToDateTime,writerName, writerUID,writtenDateTime;
        TextView writerName_And_writtenDateTime;
        ImageButton morebutton;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            //this.attachImageURL = itemView.findViewById(R.id.attachImageURL);
            this.content = itemView.findViewById(R.id.content);
            //this.isHide = itemView.findViewById(R.id.isHide);
            //this.latitude = itemView.findViewById(R.id.latitude);
//          //this.likeCount = itemView.findViewById(R.id.likeCount);
            //this.longitude = itemView.findViewById(R.id.longitude);
            this.openRange = itemView.findViewById(R.id.openRange);
            //this.openToDateTime = itemView.findViewById(R.id.openToDateTime);
            //this.writerName = itemView.findViewById(R.id.writerName);
            //this.writerUID = itemView.findViewById(R.id.writerUID);
            //this.writtenDateTime = itemView.findViewById(R.id.writtenDateTime);
            this.writerName_And_writtenDateTime = itemView.findViewById(R.id.writerName_And_writtenDateTime);
            this.morebutton = itemView.findViewById(R.id.morebutton);
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