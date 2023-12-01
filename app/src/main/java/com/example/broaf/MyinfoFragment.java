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

import com.google.android.material.floatingactionbutton.FloatingActionButton;


//그냥 빈 frag 만드니까 이렇게 떴음. 알아서 수정해주세요

public class MyinfoFragment extends Fragment {


    private ImageButton toolbar_myInfo_back;
    private Button btn_to_account_info;
    String myUID = "-NkVlvAINDhIXRBZ1hHL";  //"henzel@gmail.com", 헨젤, 00001111 계정

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myinfo, container, false);

        //중앙 버튼 이미지 설정
        FloatingActionButton fab = getActivity().findViewById(R.id.navi_to_home);
        fab.setImageResource(R.drawable.back_to_map);
        //


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
}