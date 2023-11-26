package com.example.broaf;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class CreatePostFragment extends Fragment {

    //NormalPost normalPost;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        //normalPost = new NormalPost();

        // 공개범위 토글스위치 세팅
        ToggleButton[] open_range = new ToggleButton[3];
        open_range[0] = view.findViewById(R.id.toggle_open_range_public);
        open_range[1] = view.findViewById(R.id.toggle_open_range_friends);
        open_range[2] = view.findViewById(R.id.toggle_open_range_onlyme);

        EditText content = view.findViewById(R.id.post_content);
        for (ToggleButton toggleButton : open_range) {
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (ToggleButton t : open_range) {
                        t.setChecked(false);
                    }
                    toggleButton.setChecked(true);
                }
            });
        }// ------여기까지 토글스위치 세팅

        //상단 뒤로가기 버튼 액션지정
        ImageButton back_btn = view.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = String.valueOf(content.getText());
                if (text.trim().isEmpty()) {
                    closefrag();
                } else {
                    isclosedialog(text);
                }
            }
        });// --------여기까지 상단 뒤로가기 버튼 지정

        // 하단 취소버튼 지정
        Button cancel_btn = view.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = String.valueOf(content.getText());
                if (text.trim().isEmpty()) {
                    closefrag();
                } else {
                    isclosedialog(text);
                }
            }
        });// -------여기까지 하단 취소버튼 지정

        Button del_img_btn = view.findViewById(R.id.delete_img_btn); // 현재 이게 문제가 좀 있음
        del_img_btn.setVisibility(View.VISIBLE); // 처음에는 숨겨져있음
        del_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("사진첨부 방법 선택");
                builder.setItems(R.array.attach_img, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 1: break; //갤러리 열어서 하는거 추가
                            case 2: //카메라 열어서 하는거 추가
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.hide();      //상단바 숨기기
            //ab.show();    //상단바 보이기
        }
        BottomAppBar bab = requireActivity().findViewById(R.id.bottomAppBar);
        bab.setVisibility(View.GONE);
        FloatingActionButton fab = requireActivity().findViewById(R.id.navi_to_home);
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        BottomAppBar bab = requireActivity().findViewById(R.id.bottomAppBar);
        bab.setVisibility(View.VISIBLE);
        FloatingActionButton fab = requireActivity().findViewById(R.id.navi_to_home);
        fab.setVisibility(View.VISIBLE);
    }

    public void closefrag() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).closePostFrag();
        }
    }

    public void isclosedialog(String text) {
        if (text.trim().isEmpty()) {
            closefrag();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("작성취소");
            builder.setMessage("작성을 취소하고 뒤로 돌아가시겠습니까?");
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    closefrag();
                }
            });
            builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //아무것도 하지않고 창을 닫음
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
}