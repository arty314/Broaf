package com.example.broaf;

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


public class NoticeFragment extends Fragment {

    private ImageButton toolbar_notice_back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        getActivity().setTitle("알림");   //상단 액션바에 표기할 텍스트 내용 정하는 코드








        //뒤로가기 버튼
        toolbar_notice_back = (ImageButton) view.findViewById(R.id.toolbar_notice_back);
        toolbar_notice_back.setOnClickListener(new View.OnClickListener() {
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