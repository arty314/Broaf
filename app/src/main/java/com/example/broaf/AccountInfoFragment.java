package com.example.broaf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class AccountInfoFragment extends Fragment {

    private ImageButton toolbar_accountInfo_back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_info, container, false);




        //뒤로가기 버튼
        toolbar_accountInfo_back = (ImageButton) view.findViewById(R.id.toolbar_accountInfo_back);
        toolbar_accountInfo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyinfoFragment myinfoFragment=new MyinfoFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frame_layout, myinfoFragment);
                fragmentTransaction.commit();
            }
        });
        //여기까지 뒤로가기

        return view;
    }
}

