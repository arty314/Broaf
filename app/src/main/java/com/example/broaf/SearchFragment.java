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
import android.widget.ImageButton;
import android.widget.TextView;
//알아서 수정해주세요
//홈에서 내용 끌어오는건 구현함

public class SearchFragment extends Fragment {
    View view;

    String search_uid = "11111";    //11111이면 에러란 뜻.
    String searchKeyword = "ERROR";   //입력된 검색어를 저장하는 변수
    TextView search_uid_view;   //uid 띄울 layout 창
    TextView searchKeyword_textview;    //입력된 검색어 띄울 layout 창
    ImageButton btn_back_search_to_home;    //뒤로가기 버튼 (== 홈으로 돌아가기 버튼)


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

//        search_uid=getArguments().getInt("uid");
//        search_uid_view.setText("search_uid");    //이 부분에서 에러남
//        //홈으로부터 검색 결과 받아오기
//        searchKeyword_textview = view.findViewById(R.id.searchKeyword_textview);
//        if(getArguments()!=null){
//            searchKeyword=getArguments().getString("searchKeyword_input");
//            searchKeyword_textview.setText(searchKeyword);
//            search_uid=getArguments().getInt("uid");
//            search_uid_view.setText(search_uid);
//        }
        //여기까지 홈으로부터 검색 결과 받아오기

//        //메인화면으로 돌아가기 버튼
//        btn_back_search_to_home=(ImageButton) view.findViewById(R.id.btn_back_search_to_home);
//        btn_back_search_to_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btn_back_search_to_home.setOnClickListener((view)->{
//                    FragmentManager fragmentManager = getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    HomeFragment homeFragment=new HomeFragment();
//                    fragmentTransaction.replace(R.id.frame_layout,homeFragment);
//                    fragmentTransaction.commit();
//                });
//            }
//        });
//        //여기까지 메인화면으로 돌아가기 버튼

        return view;
    }


    //액션바 가시성
    @Override
    public void onResume() {
        super.onResume();
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.hide();      //상단바 숨기기
            //ab.show();    //상단바 보이기
        }
    }
    //액션바 가시성 조절 끝
}