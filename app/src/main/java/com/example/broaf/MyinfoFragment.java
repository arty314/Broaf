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



        //일단 db 대신 쓸 post 객체 생성. 수정 시 HomeFragment에 있는 것도 같이 수정해주세요..
        PostLabel postLabel0=new PostLabel(10001,1, null,"Post내용물입니당",false,
                35.831272, 128.755840, 0,6,"2080123140042",
                "헨젤","-NkVlvAINDhIXRBZ1hHL","202311300042");
        PostLabel postLabel1=new PostLabel(10002,8, "qwerty","Post내용물입니당2",false,
                35.832645, 128.757779, 0,43,"2080123140042",
                "나주","10001","202311260213");;
        PostLabel postLabel2=new PostLabel(10003,13, null,"Post내용물입니당3",false,
                35.839907, 128.755500, 0,2,"2080123140042",
                "헨젤","-NkVlvAINDhIXRBZ1hHL","202311261147");
        PostLabel postLabel3=new PostLabel(10004,4, null,"Post내용물입니당4",false,
                35.832735, 128.753172, 0,11,"2080123140042",
                "오리너구리","10004","202311261922");
        PostLabel postLabel4=new PostLabel(10005,3, null,"Post내용물입니당5",false,
                35.838979, 128.754296, 0,0,"2080123140042",
                "헨젤","-NkVlvAINDhIXRBZ1hHL","202311270136");
        PostLabel postLabel5=new PostLabel(10001,7, null,"Post내용물입니당",false,
                35.831272, 128.759159, 0,6,"2080123140042",
                "David","10005","202311270851");
        PostLabel postLabel6=new PostLabel(10002,8, "qwerty","Post내용물입니당2",false,
                35.826150, 128.755304, 0,43,"2080123140042",
                "치즈","10006","202311271605");;
        PostLabel postLabel7=new PostLabel(10003,13, null,"Post내용물입니당3",false,
                35.826756, 128.759264, 0,2,"2080123140042",
                "캔따개","10007","202311280019");
        PostLabel postLabel8=new PostLabel(10004,1, null,"Post내용물입니당4",false,
                35.827336, 128.759925, 0,11,"2080123140042",
                "wowo","-NkCD1GOweVWBk7mhJJ9","202311280733");
        PostLabel postLabel9=new PostLabel(10005,1, null,"Post내용물입니당5",false,
                35.830263,128.750771 , 0,0,"2080123140042",
                "wowo","-NkCD1GOweVWBk7mhJJ9","202311281448");
        PostLabel postLabel10=new PostLabel(10002,10, "qwerty","Post내용물입니당2",false,
                35.831140, 128.753568, 0,43,"2080123140042",
                "wowo","-NkCD1GOweVWBk7mhJJ9","202311282202");;
        PostLabel postLabel11=new PostLabel(10003,13, null,"Post내용물입니당3",false,
                35.834808, 128.755337, 0,2,"2080123140042",
                "good","-Nk5C6FKxdEHbn5hxDo0","202311290516");
        PostLabel postLabel12=new PostLabel(10004,4, null,"Post내용물입니당4",false,
                35.838494, 128.759727, 0,11,"2080123140042",
                "good","-Nk5C6FKxdEHbn5hxDo0","202311291231");
        PostLabel postLabel13=new PostLabel(10005,9, null,"Post내용물입니당5",false,
                35.826276, 128.758904, 0,0,"2080123140042",
                "리치","-NkVnLvZ0TdAMTwY0w96","202311291945");
        PostLabel postLabel14=new PostLabel(10002,6, "qwerty","Post내용물입니당2",false,
                35.834563, 128.752793, 0,43,"2080123140042",
                "리치","-NkVnLvZ0TdAMTwY0w96","202311300259");;
        PostLabel postLabel15=new PostLabel(10003,1, null,"Post내용물입니당3",false,
                35.835961, 755500, 0,2,"2080123140042",
                "리치","-NkVnLvZ0TdAMTwY0w96","202311301014");
        PostLabel postLabel16=new PostLabel(10004,2, null,"Post내용물입니당4",false,
                35.822753, 128.758879, 0,11,"2080123140042",
                "리치","-NkVnLvZ0TdAMTwY0w96","202311301728");
        PostLabel postLabel17=new PostLabel(10005,3, null,"Post내용물입니당5",false,
                35.837781, 128.754831, 0,0,"2080123140042",
                "나주","10001","202311302342");
        PostLabel postLabel18=new PostLabel(10003,4, null,"Post내용물입니당3",false,
                35.823499, 128.754017, 0,2,"2080123140042",
                "나주","10001","202312010657");
        PostLabel postLabel19=new PostLabel(10004,5, null,"Post내용물입니당4",false,
                35.826888, 128.752606, 0,11,"2080123140042",
                "나주","10001","202312011411");
        PostLabel postLabel20=new PostLabel(10005,6, null,"Post내용물입니당5",false,
                35.831330, 128.750808, 0,0,"2080123140042",
                "감귤","10002","202312012125");
        PostLabel postLabel21=new PostLabel(10002,7, "qwerty","Post내용물입니당2",false,
                35.839653, 128.753539, 0,43,"2080123140042",
                "감귤","10002","202311260528");;
        PostLabel postLabel22=new PostLabel(10003,8, null,"Post내용물입니당3",false,
                35.822492, 128.757284, 0,2,"2080123140042",
                "감귤","10002","202311261342");
        PostLabel postLabel23=new PostLabel(10004,9, null,"Post내용물입니당4",false,
                35.823360, 128.753468, 0,11,"2080123140042",
                "영석","10003","202311262157");
        PostLabel postLabel24=new PostLabel(10005,10, null,"Post내용물입니당5",false,
                35.828453, 128.754673, 0,0,"2080123140042",
                "영석","10003","202311270611");
        PostLabel postLabel25=new PostLabel(10003,11, null,"Post내용물입니당3",false,
                35.833135, 128.759240, 0,2,"2080123140042",
                "오리너구리","10004","202311271425");
        PostLabel postLabel26=new PostLabel(10004,12, null,"Post내용물입니당4",false,
                35.834984, 128.752745, 0,11,"2080123140042",
                "오리너구리","10004","202311272240");
        PostLabel postLabel27=new PostLabel(10005,3, null,"Post내용물입니당5",false,
                35.837526, 128.758078, 0,0,"2080123140042",
                "오리너구리","10004","202311281508");
        PostLabel postLabel28=new PostLabel(10002,14, "qwerty","Post내용물입니당2",false,
                35.833029, 128.759484, 0,43,"2080123140042",
                "David","10005","202311282323");;
        PostLabel postLabel29=new PostLabel(10003,2, null,"Post내용물입니당3",false,
                35.839327, 128.754154, 0,2,"2080123140042",
                "치즈","10006","202311300042");
        PostLabel postLabel30=new PostLabel(10004,14, null,"Post내용물입니당4",false,
                35.832735, 128.752636, 0,11,"2080123140042",
                "캔따개","10007","202311290737");
        PostLabel postLabel31=new PostLabel(10005,1, null,"Post내용물입니당5",false,
                35.838979, 128.757078, 0,0,"2080123140042",
                "캔따개","10007","202312010052");








        return view;
    }
}