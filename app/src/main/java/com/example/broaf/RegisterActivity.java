
/*
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
*/
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

import com.example.broaf.DBUser;
import com.example.broaf.IntroActivity;
import com.example.broaf.R;
import com.example.broaf.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//회원가입
public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText editNickname, editID, editemail, editPW, editPWcheck;
    private Button btnRegi,btnDupl;
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
        btnDupl = findViewById(R.id.btnDupl);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://broaf-72e4c-default-rtdb.firebaseio.com/");
        //여기서 중복확인 버튼을 눌렀을 때 중복여부를 확인하는 코드를 작성해준다.

        btnDupl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Nickname = editNickname.getText().toString();
                database.getReference("User").orderByChild("nickname").equalTo(Nickname).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {

                            Toast.makeText(getApplicationContext(),"이미 존재하는 닉네임입니다. 다른 닉네임을 사용해주세요.",Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"사용 가능한 닉네임입니다.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
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

                    database.getReference("User").orderByChild("nickname").equalTo(Nickname).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                Toast.makeText(getApplicationContext(),"이미 존재하는 닉네임입니다. 다른 닉네임을 사용해주세요.",Toast.LENGTH_SHORT).show();
                            }
                            else if(!PW.equals(PWcheck)) {
                                Toast.makeText(getApplicationContext(),"비밀번호 확인이 비밀번호와 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                            }
                            else {
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
                                                }
                                                else {
                                                    // 회원가입 실패 시의 동작
                                                    Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();

                                                    editemail.setText(email);
                                                    editNickname.setText(Nickname);
                                                    editPW.setText("");
                                                    editPWcheck.setText("");
                                                }
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    //

                    // 다 입력 되었다면

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