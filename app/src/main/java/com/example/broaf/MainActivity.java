package com.example.broaf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //메뉴바 구현 방향.
        //알림,내정보보기 누르면 현재 띄워진 모든 frag 지우고 frame layout을 noti frag 혹은 myinfo frag로 replace

        //중앙 버튼은 홈 frag가 단독으로 켜져있을 때만 '게시글 작성 버튼'임 (즉, top frag == 홈 frag일 시)
        //게시글 작성 버튼을 누르면 포스트 에디터 acti가 켜지며, 이 때 포스트 에디터의 acti.xml는 normal 버전.
        // => 즉, 포스트 에디터 acti에게 '게시글 작성 버튼을 눌러 들어왔음'을 알리는 상수를 포스트 에디터 acti에게 함께 전달해야 됨. 그러면 포스트 에디터는 해당 상수에 맞는 posteditor_(버전 상수).xml을 호출.
                                //상수 대신 기존의 top frag를 전달해서, 기존의 top frag == 홈 frag일 시에만 normal.xml이 호출되도록 할 수도?

        //중앙 버튼은 <홈 frag 단독 case>를 제외한 case에선 '지도로 돌아가기 버튼'임 (즉, top frag /= 홈 frag)
        //지도로 돌아가기 버튼을 누르면, 현재 켜져 있던 모든 fragment stack를 빼낸 뒤 홈 fragment를 호출



    }
}