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

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
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
    ImageButton btn_search, btn_search_inner;


////////////////
//데이터 추가를 위한 것

    //recyclerView
    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ReceiveNormalPost> arrayList;
    private ArrayList<ReceiveNormalPost> arrayListSearched;
    //검색(String), 개수
    private TextView search_input;


    //검색을 위한 변수
    private Button searchButton;//검색어 입력버튼
    private EditText editTextSearch; //검색어 입력창

    ///////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        //중앙 버튼 이미지 설정
        FloatingActionButton fab = getActivity().findViewById(R.id.navi_to_home);
        fab.setImageResource(R.drawable.back_to_map);
        //
        input_text_search_Result = view.findViewById(R.id.input_text_search_Result);

        if (getArguments() != null) { //받아온 값이 빈값이 아닐 때 실행해라
            searchKeyword = getArguments().getString("fromHomeFrag");
            input_text_search_Result.setText(searchKeyword);
        }


        btn_search = view.findViewById(R.id.btn_search);
//recyclerview를 위한 추가
        recyclerview = view.findViewById(R.id.recyclerView);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList = new ArrayList<>();
        arrayListSearched = new ArrayList<>();

        //검색
        search_input = view.findViewById(R.id.search_input);


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://broaf-72e4c-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Post");
        //DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("message");


        //recyclerView 구현부
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();
                arrayListSearched.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    ReceiveNormalPost receiveNormalPost = snapshot.getValue(ReceiveNormalPost.class);
                    //arrayList.add(post);
                    arrayListSearched.add(receiveNormalPost);
                }
                //arrayListSearched.addAll(arrayList);
                arrayList.addAll(arrayListSearched);
                //adapter.notifyDataSetChanged();

                if(TextUtils.isEmpty(searchKeyword)) {
                    Toast.makeText(getActivity().getApplicationContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
                    arrayListSearched.clear();
                    adapter = new ReceiveNormalPostAdapter(getContext(), arrayListSearched);
                    recyclerview.setAdapter(adapter);
                    search_input.setText("검색어를 입력해주세요");
                    adapter.notifyDataSetChanged(); // 변경된 데이터셋을 RecyclerView에 알림

                }
                else if (searchKeyword.length()<2) {
                    Toast.makeText(getActivity().getApplicationContext(), "최소 2글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                    arrayListSearched.clear();
                    adapter = new ReceiveNormalPostAdapter(getContext(), arrayListSearched);
                    recyclerview.setAdapter(adapter);
                    search_input.setText("두 글자 이상 검색해 주세요");
                    adapter.notifyDataSetChanged(); // 변경된 데이터셋을 RecyclerView에 알림

                }
                else {
                    /// 리스트에 불러온 후에 검색할 수 있도록 한다.(따로두면 ArrayList에 담기전에 먼저 실행된다.)앞에거 검색
                    String searchText = getArguments().getString("fromHomeFrag").toString().toLowerCase();
                    //arrayList.clear(); // 검색 결과 리스트 초기화
                    arrayListSearched.clear();

                    //for (Post post : arrayListSearched) {
                    for (ReceiveNormalPost receiveNormalPost : arrayList) {
                        if (receiveNormalPost.getContents().toLowerCase().contains(searchText)) {
                            // arrayList.add(post); // 검색된 데이터를 arrayListSearched에 추가
                            arrayListSearched.add(receiveNormalPost);
                        }
                    }

                    //arrayList.clear(); // 기존 arrayList 비우기
                    //arrayList.addAll(arrayListSearched); // 필터링된 데이터로 arrayList 업데이트
                    adapter = new ReceiveNormalPostAdapter(getContext(), arrayListSearched);
                    recyclerview.setAdapter(adapter);
                    search_input.setText("'" + searchText + "'의 검색 결과(" + arrayListSearched.size() + ")");
                    adapter.notifyDataSetChanged(); // 변경된 데이터셋을 RecyclerView에 알림
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", String.valueOf(databaseError.toException()));
            }

        };

        //arrayListSearched.addAll(arrayList);
        //myRef.child("NormalPost").addValueEventListener(postListener);
        FirebaseDatabase.getInstance("https://broaf-72e4c-default-rtdb.firebaseio.com/").getReference("Post").child("NormalPost").addValueEventListener(postListener);
        //adapter = new PostAdapter(arrayList);
        //adapter = new ReceiveNormalPostAdapter(getContext(), arrayListSearched);
        //recyclerview.setAdapter(adapter);
        ///여기까지 data받아오기

        //arrayList.clear(); // 기존 arrayList 비우기
        //arrayList.addAll(arrayListSearched); // 필터링된 데이터로 arrayList 업데이트
        //adapter.notifyDataSetChanged(); // 변경된 데이터셋을 RecyclerView에 알림


        //한번만 검색 되는 것

        //adapter = new PostAdapter(arrayList);


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = input_text_search_Result.getText().toString().toLowerCase();
                //arrayList.clear(); // 검색 결과 리스트 초기화
                if(TextUtils.isEmpty(searchText)) { //아무것ㅅ도 없이 검색하면
                    Toast.makeText(getActivity().getApplicationContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
                    arrayListSearched.clear();
                    adapter = new ReceiveNormalPostAdapter(getContext(), arrayListSearched);
                    recyclerview.setAdapter(adapter);
                    search_input.setText("검색어를 입력해주세요");
                    adapter.notifyDataSetChanged(); // 변경된 데이터셋을 RecyclerView에 알림

                }
                else if (searchText.length()<2) {
                    Toast.makeText(getActivity().getApplicationContext(), "최소 2글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                    arrayListSearched.clear();
                    adapter = new ReceiveNormalPostAdapter(getContext(), arrayListSearched);
                    recyclerview.setAdapter(adapter);
                    search_input.setText("두 글자 이상 검색해주세요");
                    adapter.notifyDataSetChanged(); // 변경된 데이터셋을 RecyclerView에 알림

                }
                else {
                    arrayListSearched.clear();
                    //for (Post post : arrayListSearched) {
                    for (ReceiveNormalPost receiveNormalPost : arrayList) {
                        if (receiveNormalPost.getContents().toLowerCase().contains(searchText)) {
                            // arrayList.add(post); // 검색된 데이터를 arrayListSearched에 추가
                            arrayListSearched.add(receiveNormalPost);

                        }
                    }
                    //arrayList.clear(); // 기존 arrayList 비우기
                    //arrayList.addAll(arrayListSearched); // 필터링된 데이터로 arrayList 업데이트
                    adapter = new ReceiveNormalPostAdapter(getContext(), arrayListSearched);
                    recyclerview.setAdapter(adapter);
                    search_input.setText("'" + searchText + "'의 검색 결과(" + arrayListSearched.size() + ")");
                    adapter.notifyDataSetChanged(); // 변경된 데이터셋을 RecyclerView에 알림

                }


            }
        });
        return view;
    }
}