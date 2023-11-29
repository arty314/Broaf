package com.example.broaf;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuInflater;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//그냥 빈 frag 만드니까 이렇게 떴음. 알아서 수정해주세요
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoticeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Alarm> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private View view;

    // 각각의 스태틱 변수 선언

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NoticeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoticeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoticeFragment newInstance(String param1, String param2) {
        NoticeFragment fragment = new NoticeFragment();
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
        view = inflater.inflate(R.layout.fragment_notice, container, false);
        getActivity().setTitle("알림");   //상단 액션바에 표기할 텍스트 내용 정하는 코드
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView)view.findViewById(R.id.alarm_view); // 뷰가져오기
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능항상
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager); //레이아웃 연결
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance(); // 데이터베이스에서 데이터가져오기 시작
        databaseReference = database.getReference("Post").child("AnnouncementPost");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot noticeSnapshot : snapshot.getChildren()){
                    Alarm alarm = noticeSnapshot.getValue(Alarm.class);
                    arrayList.add(alarm);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("NoticeFragment", String.valueOf(error.toException()));
            }
        });

        adapter = new CustomAdapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter); // 어뎁터 연결

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.alarm_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getActivity(), AlarmSettingsActivity.class);
        startActivity(intent);
        return true;
    }


}