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
    private ImageButton rm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        getNickname = findViewById(R.id.getFriendNickname);
        add = findViewById(R.id.Iaddfriend);
        rm = findViewById(R.id.Irmfriend);

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
                        String currentUid = userSnapshot.getKey(); // 현재 로그인한 사용자의 realtime-base UID

                        database = FirebaseDatabase.getInstance(); 
                        databaseReference = database.getReference("User").child(currentUid).child("friendlist"); //현재 로그인한 사용자의 friendlist 데이터 가져오기

                        arrayList = new ArrayList<>();
                        
                        //가져온 데이터의 child 데이터들을 User 클래스의 정의에 맞게 정리 후 리스트에 추가
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
                                        if (dataSnapshot.getChildrenCount() > 0) {

                                        for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                                            String fUid = friendSnapshot.getKey(); // 검색창 닉네임을 가진 사용자의 UID (B)

                                            Log.d("Friend UID", fUid);

                                            // A의 friendlist에 B의 UID 추가
                                            DatabaseReference currentFriendlistRef = usersRef.child(currentUid).child("friendlist").push();
                                            currentFriendlistRef.setValue(fUid);

                                            // B의 정보 가져오기
                                            String fEmail = friendSnapshot.child("email").getValue(String.class);
                                            String fNickname = friendSnapshot.child("nickname").getValue(String.class);
                                            String fPw = friendSnapshot.child("pw").getValue(String.class);

                                            // A의 friendlist에 B의 email, nickname, pw 값 추가
                                            currentFriendlistRef.child("email").setValue(fEmail);
                                            currentFriendlistRef.child("nickname").setValue(fNickname);
                                            currentFriendlistRef.child("pw").setValue(fPw);

                                            // 리스트에도 추가 후 갱신
                                            arrayList.add(new User(fEmail, fNickname, fPw));
                                            adapter.notifyDataSetChanged();

                                            Toast.makeText(FriendListActivity.this, "친구가 추가되었습니다.", Toast.LENGTH_SHORT).show(); //버튼 클릭 확인용. 나중에 지울꺼임
                                            }
                                        } else {
                                            Toast.makeText(FriendListActivity.this, "해당하는 닉네임의 사용자가 없습니다.", Toast.LENGTH_SHORT).show(); //버튼 클릭 확인용. 나중에 지울꺼임
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
        // 닉네임 기반 사용자 삭제
        rm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                                // 현재 로그인한 사용자의 FrindList에서 검색창에 입력한 닉네임을 가진 사용자의 UID 찾기
                                usersRef.child(currentUid).child("friendlist").orderByChild("nickname").equalTo(FNickname).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getChildrenCount() > 0) {

                                            for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                                                String fUid = friendSnapshot.getKey(); // 검색창 닉네임을 가진 사용자의 UID (B)

                                                Log.d("Friend UID", fUid);

                                                String fNickname = friendSnapshot.child("nickname").getValue(String.class);

                                                // A의 friendlist에서 B의 UID 삭제(Realtime-database)
                                                usersRef.child(currentUid).child("friendlist").child(fUid).removeValue();

                                                // 리스트에서도 제거 후 갱신
                                                for (User user : arrayList) {
                                                    if (user.getNickname().equals(fNickname)) {
                                                        arrayList.remove(user);
                                                        break;
                                                    }
                                                }
                                                adapter.notifyDataSetChanged();

                                                Toast.makeText(FriendListActivity.this, "친구가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(FriendListActivity.this, "해당하는 닉네임의 친구가 없습니다.", Toast.LENGTH_SHORT).show();
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