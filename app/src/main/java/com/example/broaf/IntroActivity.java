package com.example.broaf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceStart) {
        super.onCreate(savedInstanceStart);
        setContentView(R.layout.activity_intro);

        onCheckPermission();// 권한 확인 함수

        ///**
        //키 해시 얻기
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
        //*키해시얻기:여기까지

        // 로그인 버튼을 눌렀을 때
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
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


        // 사용자 정보 액세스
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }


        //액션바 가시성 조절 (activity버전)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); //보이게 하려면 show
        }
        //여기까지 액션바 가시성 조절
    }

    public void onCheckPermission() // 권한을 확인하는 함수, 근데 권한을 계속 켰다 껏다 하니 동작을 했다 안했다함
    {
        String[] permissions = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.CAMERA,
                    };
        } else {
            permissions = new String[]{android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }

        List<String> denied_permissions = new ArrayList<String>();
        for (String perm : permissions) {
            if (ActivityCompat.checkSelfPermission(this, perm)
                != PackageManager.PERMISSION_GRANTED)
                denied_permissions.add(perm);
        }

        if(denied_permissions.size() != 0){ // 이전버전  < 0 이었음
            String [] deniedPerms = denied_permissions.toArray(new String[denied_permissions.size()]);
            ActivityCompat.requestPermissions(this, deniedPerms, 10);
        }
    }

    @Override // 확인결과에 따른 동작정의
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permission, grantResult);
        switch (requestCode) {
            case 10: // requestCode = 10으로 해놓았음
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) { // 권한이 부여되었을 때
                    Toast.makeText(this, "앱 실행을 위한 권한이 설정 되었습니다", Toast.LENGTH_SHORT).show();
                } else { // 권한이 부여되지 않은 경우 앱이 종료됨(종료하려 했으나 finish(); 를 넣으니 한번 나가지니까 다시 못들어가는 경우 발생)
                    Toast.makeText(this, "앱 실행을 위한 권한이 취소 되었습니다", Toast.LENGTH_SHORT).show();
                    //finish(); // 이거 넣으면 한번 취소하면 계속 토스트메시지 뜨면서 앱이 켜졌다 바로꺼짐
                }
                break;
        }
    }
}
