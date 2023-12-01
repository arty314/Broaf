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
import android.provider.ContactsContract;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private Button button1;
    private TextView txtResult;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser curuser = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //database = FirebaseDatabase.getInstance().getReference();

        try { //인텐트로 PostBody객체가 도착하지 않으면 무시함
            Intent receivepostbody = getIntent();
            NormalPost normalPost = new NormalPost((CreatePostFragment.PostBody)
                    receivepostbody.getSerializableExtra("newpostbody"));
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
            String uemail = curuser.getEmail(); // 로그인한 유저이메일을 갖고와서
            Thread pidTh = new Thread(){
                @Override
                public void run() {
                    super.run();
                    makePID(new pidCallback() {
                        @Override
                        public void onPidResult(int pid) {
                            ++pid;
                            String newpid = String.valueOf(pid);
                            normalPost.setPID(newpid);
                            Toast.makeText(getApplicationContext(), newpid, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
            pidTh.start();
            try{ pidTh.join();} catch (Exception e) {}
            if (normalPost.getPID().isEmpty()) {normalPost.setPID("10001");}
            Thread dataTh = new Thread(){ // 파이어베이스 쿼리가 비동기 동기화
                @Override
                public void run() {
                    super.run();
                    searchname(uemail, new searchCallback() {
                        @Override
                        public void onSearchResult(String nickname) {
                            normalPost.setWriterName(nickname);
                            database.child("Post").child("NormalPost").child(normalPost.getPID()).setValue(normalPost);
                            //Toast.makeText(getApplicationContext(), "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
            dataTh.start();
            try{
                dataTh.join();
            } catch (Exception e){}
        }
        catch (Exception e) { }

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

    public void replaceFF(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.frame_layout, fragment).commit();
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

    public interface searchCallback{
        void onSearchResult(String nickname);
    }

    public void searchname(String uemail, searchCallback callback){
        Query qy = database.child("User").orderByChild("email").equalTo(uemail);
        qy.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{for (DataSnapshot ds : snapshot.getChildren()) {
                    //Toast.makeText(getApplicationContext(), "닉네임을 찾음 " + name, Toast.LENGTH_SHORT).show();
                    String name =  ds.child("nickname").getValue(String.class);
                    callback.onSearchResult(name);
                }} catch (Exception e) {}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "닉네임 찾기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface pidCallback{
        void onPidResult(int pid);
    }

    public void makePID(pidCallback callback){
        database.child("Post").child("NormalPost").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int maxpid = 0;
                try{
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String pid = ds.child("pid").getValue(String.class);
                        int intpid = Integer.parseInt(pid);
                        if (maxpid < intpid){
                            maxpid = intpid;
                            callback.onPidResult(maxpid);
                        }
                    }
                } catch (Exception e) {}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}