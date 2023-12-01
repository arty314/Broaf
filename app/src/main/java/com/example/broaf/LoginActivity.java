package com.example.broaf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText getloginemail, getloginpw;
    private Button btnlogin;

    private Button btnAutoInput; //개발중에 편하게 로그인하기 위한 버튼. : 기능) 누르면 test1@test.com이랑 test1password가 자동입력됨
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        getloginemail=findViewById(R.id.getloginemail);
        getloginpw=findViewById(R.id.getloginpw);
        btnlogin=findViewById(R.id.btnlogin);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getloginemail.getText().toString();
                String pw = getloginpw.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email, pw)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "아이디나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        //개발 중에 임시로 쓸 계정 정보 자동 입력 로그인 버튼

        btnAutoInput=findViewById(R.id.btnAutoInput);
        btnAutoInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "goodman@good.com";
                String pw = "asdf123";
                firebaseAuth.signInWithEmailAndPassword(email, pw)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, "아이디나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        //여기까지 임시 자동 로그인 버튼. 릴리즈 시 삭제.


        //액션바 가시성 조절 (activity버전)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); //보이게 하려면 show
        }
        //여기까지 액션바 가시성 조절
    }

}