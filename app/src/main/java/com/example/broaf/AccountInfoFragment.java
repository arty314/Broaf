package com.example.broaf;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


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
                getActivity().onBackPressed();
            }
        });

        Button acInfo_makerEmail = view.findViewById(R.id.acInfo_makerEmail);
        acInfo_makerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("makerEmail", "amaranth950927@gmail.com");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "제작자의 이메일이 복사되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        Button app_version_btn = view.findViewById(R.id.acInfo_AppVersion);
        app_version_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Broaf, 1.0.1", Toast.LENGTH_SHORT).show();
            }
        });







        return view;
    }
}

