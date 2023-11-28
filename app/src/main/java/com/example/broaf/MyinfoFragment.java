package com.example.broaf;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


//그냥 빈 frag 만드니까 이렇게 떴음. 알아서 수정해주세요

public class MyinfoFragment extends Fragment {


    private ImageButton toolbar_myInfo_back;
    private Button btn_to_account_info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myinfo, container, false);
        getActivity().setTitle("내 정보");     //상단 액션바에 표기할 텍스트 내용 정하는 코드

        // FriendListButton 버튼 찾기
        Button friendListButton = view.findViewById(R.id.FriendListButton);

        // FriendListButton에 대한 클릭 이벤트 처리
        friendListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FriendListActivity.class);
                startActivity(intent);
            }
        });


        //계정정보 frag로
        btn_to_account_info = (Button) view.findViewById(R.id.btn_to_account_info);
        btn_to_account_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountInfoFragment accountInfoFragment=new AccountInfoFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frame_layout, accountInfoFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        //여기까지 계정정보 frag





        //뒤로가기 버튼
        toolbar_myInfo_back = (ImageButton) view.findViewById(R.id.toolbar_myInfo_back);
        toolbar_myInfo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment=new HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frame_layout, homeFragment);
                fragmentTransaction.commit();
            }
        });
        //여기까지 뒤로가기
        
        

        return view;
    }


    //액션바 가시성
    @Override
    public void onResume() {
        super.onResume();
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            //ab.hide();      //상단바 숨기기
            ab.show();    //상단바 보이기
        }
    }
    //액션바 가시성 조절 끝


}