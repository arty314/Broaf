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
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyinfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyinfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btn_close_viewer;

    public MyinfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyinfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyinfoFragment newInstance(String param1, String param2) {
        MyinfoFragment fragment = new MyinfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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

        btn_close_viewer= view.findViewById(R.id.btn_close_viewer);
        btn_close_viewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_close_viewer= (Button) view.findViewById(R.id.btn_close_viewer);
                btn_close_viewer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn_close_viewer.setOnClickListener((view)->{
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            HomeFragment homeFragment=new HomeFragment();
                            fragmentTransaction.replace(R.id.frame_layout,homeFragment);
                            fragmentTransaction.commit();
                        });
                    }
                });

            }
        });

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