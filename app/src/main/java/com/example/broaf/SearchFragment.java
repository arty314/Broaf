package com.example.broaf;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
//알아서 수정해주세요
//홈에서 내용 끌어오는건 구현함

public class SearchFragment extends Fragment {
    View view;

    String search_uid = "11111";    //11111이면 에러란 뜻.
    String searchKeyword = "ERROR";   //입력된 검색어를 저장하는 변수
    TextView search_uid_view;   //uid 띄울 layout 창
    ImageButton btn_back_search_to_home;    //뒤로가기 버튼 (== 홈으로 돌아가기 버튼)

    //검색창 내부로와서 text와 버튼

    TextView input_text_search_Result;    //입력된 검색어 띄울 layout 창
    ImageButton btn_search;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        input_text_search_Result = view.findViewById(R.id.input_text_search_Result);



        if(getArguments() != null) { //받아온 값이 빈값이 아닐 때 실행해라
            searchKeyword = getArguments().getString("fromHomeFrag");
            input_text_search_Result.setText(searchKeyword);
        }

        //recyclerview
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://broaf-72e4c-default-rtdb.firebaseio.com/");

        //메인화면으로 돌아가기 버튼 뺐어요. 이유: Searchfragment에서는 휴대폰 뒤로가기가 잘먹힘. 그리고 하단바의 메인 지도 버튼 눌러도 되기에.

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