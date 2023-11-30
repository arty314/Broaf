package com.example.broaf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FriendListActivity extends AppCompatActivity {

    private EditText getNickname;
    //Auth 데이터를 가져오기 위한 변수
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    private FirebaseDatabase database;
    //
    private DatabaseReference databaseReference;

    private ArrayList<User> arrayList;

    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView.Adapter adapter;

    private View view;


    private ImageButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        getNickname = findViewById(R.id.getFriendNickname);
        add = findViewById(R.id.Iaddfriend);

        recyclerView = (RecyclerView)findViewById(R.id.friendlist_view); // 뷰가져오기
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능항상
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager); //레이아웃 연결

        if (currentUser != null) {
            String currentEmail = currentUser.getEmail();

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("User");

            usersRef.orderByChild("email").equalTo(currentEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String currentUid = userSnapshot.getKey(); // 현재 로그인한 사용자의 UID (A0)

                        database = FirebaseDatabase.getInstance(); // 데이터베이스에서 데이터가져오기 시작
                        databaseReference = database.getReference("User").child(currentUid).child("friendlist");

                        arrayList = new ArrayList<>();

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                arrayList.clear();
                                for(DataSnapshot friendSnapshot : snapshot.getChildren()) {
                                    User user = friendSnapshot.getValue(User.class);
                                    arrayList.add(user);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("FriendListActivity", String.valueOf(error.toException()));
                            }
                        });

                        adapter = new FriendListAdapter(arrayList, FriendListActivity.this);
                        recyclerView.setAdapter(adapter); // 어뎁터 연결
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 쿼리가 취소된 경우 또는 에러가 발생한 경우 처리
                }
            });
        }




        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        
        //닉네임 기반 친구 추가
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FriendListActivity.this, "이미지 버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show(); //버튼 클릭 확인용. 나중에 지울꺼임
                String FNickname = getNickname.getText().toString(); // 입력한 닉네임 저장


                if (currentUser != null) {
                    String currentEmail = currentUser.getEmail();

                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("User");

                    // 현재 로그인한 사용자의 email을 사용하여 해당 사용자 찾기
                    usersRef.orderByChild("email").equalTo(currentEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                String currentUid = userSnapshot.getKey(); // 현재 로그인한 사용자의 UID (A)

                                Log.d("User UID", currentUid);

                                // 검색창에 입력한 닉네임을 가진 사용자의 UID 찾기
                                usersRef.orderByChild("nickname").equalTo(FNickname).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot asdfSnapshot : dataSnapshot.getChildren()) {
                                            String asdfUid = asdfSnapshot.getKey(); // 검색창 닉네임을 가진 사용자의 UID (B)

                                            Log.d("Friend UID", asdfUid);

                                            // A의 friendlist에 B의 UID 추가
                                            DatabaseReference currentFriendlistRef = usersRef.child(currentUid).child("friendlist").push();
                                            currentFriendlistRef.setValue(asdfUid);

                                            // B의 정보 가져오기
                                            String asdfEmail = asdfSnapshot.child("email").getValue(String.class);
                                            String asdfNickname = asdfSnapshot.child("nickname").getValue(String.class);
                                            String asdfPw = asdfSnapshot.child("pw").getValue(String.class);

                                            // A의 friendlist에 B의 email, nickname, pw 값 추가
                                            currentFriendlistRef.child("email").setValue(asdfEmail);
                                            currentFriendlistRef.child("nickname").setValue(asdfNickname);
                                            currentFriendlistRef.child("pw").setValue(asdfPw);

                                            arrayList.add(new User(asdfEmail, asdfNickname, asdfPw));
                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // 쿼리가 취소된 경우 또는 에러가 발생한 경우 처리
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // 쿼리가 취소된 경우 또는 에러가 발생한 경우 처리
                        }
                    });
                }
            }
        });
    }
}