//User클래스 정보를 데이터베이스에 넣기 위한 클래스

package com.example.broaf;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DBUser {

    private DatabaseReference databaseReference;

    DBUser() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName());
    }

    //등록
    public Task<Void> add(User user) {
        return databaseReference.push().setValue(user);
    }

    public void updateFriendList(String userId, List<String> friendList) {
        DatabaseReference userReference = databaseReference.child(userId).child("user_friendlist");
        userReference.setValue(friendList);
    }

    public void getUserFriendList(String userId, OnSuccessListener<List<String>> successListener, OnFailureListener failureListener) {
        DatabaseReference userReference = databaseReference.child(userId).child("user_friendlist");

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> friendList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String friendId = snapshot.getValue(String.class);
                        friendList.add(friendId);
                    }
                    successListener.onSuccess(friendList);
                } else {
                    // 사용자의 친구 목록이 존재하지 않는 경우 빈 목록을 반환
                    successListener.onSuccess(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                failureListener.onFailure(databaseError.toException());
            }
        });
    }
}
