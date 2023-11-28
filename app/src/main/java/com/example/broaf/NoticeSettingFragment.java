package com.example.broaf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class NoticeSettingFragment extends Fragment {

    private ImageButton toolbar_noticeSetting_back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice_setting, container, false);



        //뒤로가기 버튼
        toolbar_noticeSetting_back = (ImageButton) view.findViewById(R.id.toolbar_noticeSetting_back);
        toolbar_noticeSetting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoticeFragment noticeFragment=new NoticeFragment(); //notice로 돌아감
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frame_layout, noticeFragment);
                fragmentTransaction.commit();
            }
        });
        //여기까지 뒤로가기

        return view;
    }
}