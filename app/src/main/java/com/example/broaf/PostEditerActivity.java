package com.example.broaf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//게시글 편집화면 액티비티
public class PostEditerActivity extends AppCompatActivity {

    //메인 acti넘어가게할 때 쓸 버튼. activity_post_editer.xml의 동명의 버튼과 연결
    Button btn_postEditerActi_to_mainActi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_editer);

        //메인 acti로 돌아가기. (=activity 뒤로가기(임시))
        btn_postEditerActi_to_mainActi=findViewById(R.id.btn_postEditerActi_to_mainActi);
        btn_postEditerActi_to_mainActi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //여기까지 메인 acti로 돌아가기

    }
}