package com.example.broaf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;


public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceStart) {
        super.onCreate(savedInstanceStart);
        setContentView(R.layout.first);

        // 로그인 버튼을 눌렀을 때
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // 회원가입 버튼을 눌렀을 때
        Button btnRegist = findViewById(R.id.btnRegist);
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}
