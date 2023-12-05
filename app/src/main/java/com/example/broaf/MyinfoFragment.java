package com.example.broaf;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


//그냥 빈 frag 만드니까 이렇게 떴음. 알아서 수정해주세요

public class MyinfoFragment extends Fragment {


    private ImageButton toolbar_myInfo_back;
    private Button btn_to_account_info;
    //
    private TextView myinfo_email;
    //recyclerView
    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    //mAuth import
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    // 현재 이용자 UID를 받을 변수,
    String currentUserEmail,userNickname;
    private ArrayList<ReceiveNormalPost> arrayList;
    private ArrayList<ReceiveNormalPost> arrayListSearched;
    TextView myinfo_textView; //내 게시글 개수를 나타낼 TextView
    TextView myinfo_nickName; //myinfo에서 닉네임 표시하는 부분

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myinfo, container, false);

        //중앙 버튼 이미지 설정
        FloatingActionButton fab = getActivity().findViewById(R.id.navi_to_home);
        fab.setImageResource(R.drawable.back_to_map);
        //


        // FriendListButton 버튼 찾기
        Button friendListButton = view.findViewById(R.id.FriendListButton);

        // FriendListButton에 대한 클릭 이벤트 처리
        friendListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FriendListActivity.class);
                startActivity(intent);
            }
        });


        //계정정보 frag로
        btn_to_account_info = (Button) view.findViewById(R.id.btn_to_account_info);
        btn_to_account_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountInfoFragment accountInfoFragment=new AccountInfoFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frame_layout, accountInfoFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        //여기까지 계정정보 frag





        //뒤로가기 버튼
        toolbar_myInfo_back = (ImageButton) view.findViewById(R.id.toolbar_myInfo_back);
        toolbar_myInfo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        //여기까지 뒤로가기

//이메일 가져오지
        myinfo_email = view.findViewById(R.id.myinfo_email);
        myinfo_email.setText(currentUser.getEmail());

//recyclerview를 위한 추가
        recyclerview = view.findViewById(R.id.myinfo_recyclerview);
        myinfo_textView = view.findViewById(R.id.myinfo_textView);
        myinfo_nickName = view.findViewById(R.id.myinfo_nickName);

        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList = new ArrayList<>();
        arrayListSearched = new ArrayList<>();


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://broaf-72e4c-default-rtdb.firebaseio.com/");
        // 아이디 찾기
        //여기다 사용자 이름을 받는다.
        if (currentUser != null) {
            currentUserEmail = currentUser.getEmail();
            database.getReference("User").orderByChild("email").equalTo(currentUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        userNickname = userSnapshot.child("nickname").getValue(String.class);
                        Log.e("MainActivity", userNickname.toString());
                        myinfo_nickName.setText(userNickname);
                        // 여기서 userNickname을 사용하여 필요한 데이터를 필터링하고 표시합니다.
                        // Post->NormalPost에서 writerName이 찾은 Nickname과 같은 것만
                        database.getReference("Post").child("NormalPost").orderByChild("writerName").equalTo(userNickname).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // ArrayList에 넣고,
                                arrayList.clear();
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    ReceiveNormalPost receiveNormalPost = postSnapshot.getValue(ReceiveNormalPost.class);
                                    Log.e("MainActivity", receiveNormalPost.getContents().toString());
                                    // 필요한 필터링 작업 수행
                                    arrayList.add(receiveNormalPost);
                                }
                                myinfo_textView.setText("작성한 게시글 (" + String.valueOf(arrayList.size()) +")");
                                adapter = new ReceiveNormalPostAdapter(getContext(), arrayList);
                                recyclerview.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                // RecyclerView에 filteredPosts를 사용하여 데이터를 표시합니다.
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // 데이터베이스 읽기 실패 시 처리
                            }
                        });

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 데이터베이스 읽기 실패 시 처리
                }
            });

        }

        /// 아이디 찾기


/*
        //recyclerView 구현부
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();
                arrayListSearched.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    ReceiveNormalPost receiveNormalPost = snapshot.getValue(ReceiveNormalPost.class);
                    //arrayList.add(post);
                    arrayListSearched.add(receiveNormalPost);
                }
                //arrayListSearched.addAll(arrayList);
                arrayList.addAll(arrayListSearched);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", String.valueOf(databaseError.toException()));
            }

        };
        database.getReference("Post").child("NormalPost").addValueEventListener(postListener);
        //adapter = new PostAdapter(arrayList);
        adapter = new ReceiveNormalPostAdapter(getContext(), arrayListSearched);
        recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // 변경된 데이터셋을 RecyclerView에 알림

        ///
*/
        //한번만 검색 되는 것
/*
        adapter = new ReceiveNormalPostAdapter(getContext(), arrayListSearched);
        recyclerview.setAdapter(adapter);


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = input_text_search_Result.getText().toString().toLowerCase();
                //arrayList.clear(); // 검색 결과 리스트 초기화
                arrayListSearched.clear();

                //for (Post post : arrayListSearched) {
                for (ReceiveNormalPost receiveNormalPost : arrayList) {
                    if (receiveNormalPost.getContents().toLowerCase().contains(searchText)) {
                        // arrayList.add(post); // 검색된 데이터를 arrayListSearched에 추가
                        arrayListSearched.add(receiveNormalPost);
                    }
                }

                //arrayList.clear(); // 기존 arrayList 비우기
                //arrayList.addAll(arrayListSearched); // 필터링된 데이터로 arrayList 업데이트

                search_input.setText("'" + searchText + "'의 검색 결과(" + arrayListSearched.size() + ")");
                adapter.notifyDataSetChanged(); // 변경된 데이터셋을 RecyclerView에 알림
            }
        });
        */

        return view;
    }
}