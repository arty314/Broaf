package com.example.broaf;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//회원가입
public class
RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText editNickname, editID, editemail, editPW, editPWcheck;
    private Button btnRegi;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        editNickname = findViewById(R.id.editNickname);
        editemail = findViewById(R.id.editemail);
        editPW = findViewById(R.id.editPW);
        editPWcheck = findViewById(R.id.editPWcheck);
        btnRegi = findViewById(R.id.btnRegi);

        btnRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // register
                String email = editemail.getText().toString();
                String Nickname = editNickname.getText().toString();
                String PW = editPW.getText().toString();
                String PWcheck = editPWcheck.getText().toString();



                if((email != null) && !email.isEmpty()
                        && (Nickname != null) && !Nickname.isEmpty()
                        && (PW != null) && !PW.isEmpty()
                        && (PWcheck != null) && !PWcheck.isEmpty()) {
                    // 다 입력 되었다면
                    mFirebaseAuth.createUserWithEmailAndPassword(email, PW)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // 회원가입 성공 시의 동작
                                        // 회원가입시 작성한 데이터 realtime-database에도 작성
                                        DBUser dao = new DBUser();

                                        User user = new User(email, Nickname, PW, null,null,null);

                                        dao.add(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                        //
                                        Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, IntroActivity.class));
                                        finish();
                                    } else {
                                        // 회원가입 실패 시의 동작
                                        Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();

                                        editemail.setText(email);
                                        editNickname.setText(Nickname);
                                        editPW.setText("");
                                        editPWcheck.setText("");
                                    }
                                }
                            });
                } else {
                    // 입력이 다 되어있지 않은 경우
                    Toast.makeText(RegisterActivity.this, "모든 정보를 입력해주세요", Toast.LENGTH_LONG).show();
                }
            }
        });

        //액션바 가시성 조절 (activity버전)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); //보이게 하려면 show
        }
        //여기까지 액션바 가시성 조절
    }
}
