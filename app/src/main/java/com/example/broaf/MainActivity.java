package com.example.broaf;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.broaf.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //여기부터 '지도로 돌아가기 버튼'
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());        //처음엔 HomeFragment가 표시됨
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

        NormalPost normalPost;

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
        if (item.getItemId()== R.id.notice)
            replaceFragment(new NoticeFragment());
        else if (item.getItemId()== R.id.myinfo)
            replaceFragment(new MyinfoFragment());
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
    }
}