package com.example.broaf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//어플 시작 시 뜨는 로그인 화면
public class LoginActivity extends AppCompatActivity {

    //메인 acti넘어가게할 때 쓸 버튼. activity_login.xml의 동명의 버튼과 연결
    private EditText getloginid, getloginpw;
    private Button btnlogin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        getloginid=findViewById(R.id.getloginid);
        getloginpw=findViewById(R.id.getloginpw);
        btnlogin=findViewById(R.id.btnlogin);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getloginid.getText().toString();
                String pw = getloginpw.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(id, pw)
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
        //여기까지 메인 acti로 넘어가기.

        ///**
        //키 해시 얻기 (저희 팀 멤버들의 모든 개발환경 키 해시를 얻고 나면 삭제할 코드
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("키해시는 :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //*여기까지 키 해시 얻기*/

    }
}