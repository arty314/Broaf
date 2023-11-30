package com.example.broaf;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.broaf.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private DatabaseReference database;

    private Button button1;
    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance().getReference();

        try { //인텐트로 PostBody객체가 도착하지 않으면 무시함
            Intent recivepostbody = getIntent();
            NormalPost normalPost = new NormalPost((CreatePostFragment.PostBody)
                    recivepostbody.getSerializableExtra("newpostbody"));
            Toast.makeText(this, "Normalpost 생성 완료", Toast.LENGTH_SHORT).show();
            final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( this, new String[] {
                        android.Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
            }
            else {
                // 가장최근 위치정보 가져오기
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    normalPost.setpLatitude(location.getLatitude());
                    normalPost.setpLongitude(location.getLongitude());
                }
            }
            normalPost.setPID("testpid");
            normalPost.setWriterName("테스트닉네임");
            database.child("Post").child("NormalPost").child(normalPost.getPID()).setValue(normalPost);

        }
        catch (Exception e){ }

        //처음엔 HomeFragment가 표시됨
        replaceFragment(new HomeFragment());

        //여기부터 '지도로 돌아가기 버튼'
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //하단바 관련.
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
        binding.naviToHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ // 일단 홈 프레그먼트에서 바로 게시글작성 프레그먼트 호촐하도록 만들었음, 다른탭일경우 지도로 돌아감
                for (Fragment fragment: getSupportFragmentManager().getFragments()){
                    if(fragment.isVisible()){
                        if(fragment instanceof HomeFragment){
                            replaceFragment(new CreatePostFragment());
                            return;
                        }
                    }
                }
                replaceFragment(new HomeFragment());
            }
            //여기까지 '지도로 돌아가기 버튼'
        });

    }


    //flagment 전환 메소드
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null); //프레그먼트간 백스택 저장 다른탭에서 뒤로가기 누르면 지도로 돌아온다.
        fragmentTransaction.commit();
    }


    //선택한 메뉴 아이템에 따라 replaceFragment(선택 flagment) 호출
    private boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.notice) {
            replaceFragment(new NoticeFragment());
        }
        else if (item.getItemId()== R.id.myinfo) {
            replaceFragment(new MyinfoFragment());

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void closePostFrag(){
        replaceFragment(new HomeFragment());
        FloatingActionButton fab = findViewById(R.id.navi_to_home);
    }

}