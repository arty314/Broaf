package com.example.broaf;

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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//어플 시작 시 뜨는 로그인 화면
public class LoginActivity extends AppCompatActivity {

    //메인 acti넘어가게할 때 쓸 버튼. activity_login.xml의 동명의 버튼과 연결
    Button btn_loginActi_to_mainActi; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //메인 acti로 넘어가기. (메인 홈 화면 구현을 위해 임시로 만들어둔 거니 적당히 수정해서 써주세요)
        btn_loginActi_to_mainActi=findViewById(R.id.btn_loginActi_to_mainActi);
        btn_loginActi_to_mainActi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
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