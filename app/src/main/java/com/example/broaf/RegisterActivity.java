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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//회원가입
public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText editNickname, editID, editemail, editPW, editPWcheck;
    private Button btnRegi;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        editNickname = findViewById(R.id.editNickname);
        editID = findViewById(R.id.editID);
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
                String ID = editID.getText().toString();
                String PW = editPW.getText().toString();
                String PWcheck = editPWcheck.getText().toString();

                if((email != null) && !email.isEmpty()
                && (Nickname != null) && !Nickname.isEmpty()
                && (ID != null) && !ID.isEmpty()
                && (PW != null) && !PW.isEmpty()
                && (PWcheck != null) && !PWcheck.isEmpty()) {
                    mFirebaseAuth.createUserWithEmailAndPassword(ID, PW)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // 회원가입 성공 시의 동작
                                        Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, FirstActivity.class));
                                        finish();
                                    } else {
                                        // 회원가입 실패 시의 동작
                                        Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();

                                        editemail.setText(email);
                                        editNickname.setText(Nickname);
                                        editID.setText(ID);
                                        editPW.setText("");
                                        editPWcheck.setText("");
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "모든 정보를 입력해주세요", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
