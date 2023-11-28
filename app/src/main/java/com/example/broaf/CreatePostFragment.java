package com.example.broaf;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;


public class CreatePostFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView attach_img_view;
    private Button del_img_btn;
    private PostBody postBody;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        postBody = new PostBody();
        TextView open_time = view.findViewById(R.id.open_time_view);
        // 공개범위 토글스위치 세팅
        ToggleButton[] open_range = new ToggleButton[3];
        open_range[0] = view.findViewById(R.id.toggle_open_range_public);
        open_range[1] = view.findViewById(R.id.toggle_open_range_friends);
        open_range[2] = view.findViewById(R.id.toggle_open_range_onlyme);
        EditText content = view.findViewById(R.id.post_content);
        open_range[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ToggleButton t : open_range) {
                    t.setChecked(false);
                }
                open_range[0].setChecked(true);
                postBody.setOpenRange(1);
                int n = postBody.getOpenratio();
                String t = (1 + n / 3) + " 일";
                open_time.setText(t);
            }
        });
        open_range[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ToggleButton t : open_range) {
                    t.setChecked(false);
                }
                open_range[1].setChecked(true);
                postBody.setOpenRange(2);
                int n = postBody.getOpenratio();
                String t = (1 + n / 2) + " 일";
                open_time.setText(t);
            }
        });
        open_range[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ToggleButton t : open_range) {
                    t.setChecked(false);
                }
                open_range[2].setChecked(true);
                postBody.setOpenRange(3);
                int n = postBody.getOpenratio();
                if (n == 18)
                    open_time.setText("무제한");
                else {
                    String t = (1 + n) + " 일";
                    open_time.setText(t);
                }
            }
        });
        // ------여기까지 토글스위치 세팅 openrange도 설정함

        //상단 뒤로가기 버튼 액션지정
        ImageButton back_btn = view.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = String.valueOf(content.getText());
                if (text.trim().isEmpty()) {
                    closefrag();
                } else {
                    isclosedialog();
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
                    isclosedialog();
                }
            }
        });// -------여기까지 하단 취소버튼 지정

        del_img_btn = view.findViewById(R.id.delete_img_btn);
        del_img_btn.setVisibility(View.GONE); // 처음에는 숨겨져있음
        del_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //첨부된 이미지를 지우는 코드
                del_img_btn.setVisibility(View.GONE);
                postBody.setImgurl(Uri.parse(""));
                attach_img_view.setImageURI(Uri.parse(""));
            }
        });

        attach_img_view = view.findViewById(R.id.attach_img_view);
        ImageView attach_img_btn = view.findViewById(R.id.add_img_btn);
        attach_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("사진첨부 방법 선택");
                builder.setItems(R.array.attach_img, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                //Toast.makeText(getActivity(),i + "번째 누름", Toast.LENGTH_SHORT).show();
                                pickImageFromGallery();
                                break; //갤러리 열어서 하는거 추가
                            case 1:

                                //카메라 열어서 하는거 추가
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // 공개시간 SeekBar동작 정의
        SeekBar open_time_ratio = view.findViewById(R.id.open_time_ratio);
        open_time_ratio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch(postBody.getOpenRange()) {
                    case 1:
                        String t1 = (1 + i/3) + " 일";
                        open_time.setText(t1);
                        break;
                    case 2:
                        String t2 = (1 + i/2) + " 일";
                        open_time.setText(t2);
                        break;
                    case 3:
                        String t3;
                        if (i == 18){
                            open_time.setText("무제한");
                        }
                        else{
                            t3 = (1 + i ) + " 일";
                            open_time.setText(t3);
                        }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                postBody.setOpenratio(seekBar.getProgress());
            }
        });










        return view;
    }

    // 갤러리에서 이미지를 선택하기 위한 메서드 호출
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // 갤러리에서 선택한 이미지를 처리하기 위한 콜백
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // 선택한 이미지의 URI 가져오기
                Uri selectedImageUri = data.getData();
                // URI를 사용하여 이미지 처리
                handleSelectedImage(selectedImageUri);
            }
        }
    }
    // 선택한 이미지 처리를 위한 메서드
    private void handleSelectedImage(Uri imageUri) {
        // 이미지 처리 로직을 구현
        attach_img_view.setImageURI(imageUri);
        postBody.setImgurl(imageUri);
        del_img_btn.setVisibility(View.VISIBLE);
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

    public void isclosedialog() {
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

    public int openrangeis(ToggleButton[] t){
        for (int i = 0; i < 3; i++){
            if (t[i].isChecked())
                return i;
        }
        return -1;
    }

    private static class PostBody{
        private String content;
        private Calendar writeTime;
        //private int? badge; // 이모티콘 구분자는 뭘로할지 몰라서 일단 주석처리
        private Calendar opentilldate;
        private Uri imguri;
        private int openRange;
        private int openratio;

        PostBody(){
            this.content = "";
            this.writeTime = Calendar.getInstance();
            this.imguri = null;
            this.openRange = 1;
            this.opentilldate = Calendar.getInstance();
            this.openratio = 9;
        }

        public String getContent(){return content;}
        public void setContent(String str){this.content = str;}
        public Uri getImgurl(){return imguri;}
        public void setImgurl(Uri uri){this.imguri = uri;}
        public int getOpenRange(){return openRange;}
        public void setOpenRange(int n){this.openRange = n;}
        public Calendar getOpentilldate(){return opentilldate;}
        public void setOpentilldate(Calendar calendar){this.opentilldate = calendar;}
        public int getOpenratio(){return openratio;}
        public void setOpenratio(int n){this.openratio = n;}
    }

}