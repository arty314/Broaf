package com.example.broaf;


import static androidx.core.content.PermissionChecker.checkCallingPermission;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class CreatePostFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PICTURE_OK = 2;
    private ImageView attach_img_view;
    private Button del_img_btn;
    private PostBody postBody;
    private String currentFilePath;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        SeekBar open_time_ratio = view.findViewById(R.id.open_time_ratio); // 시크바 토글에 사용하기 위해 위로 땡김
        postBody = new PostBody();
        TextView open_time = view.findViewById(R.id.open_time_view);
        // 공개범위 토글스위치 세팅
        ToggleButton[] open_range = new ToggleButton[3];
        open_range[0] = view.findViewById(R.id.toggle_open_range_public);
        open_range[1] = view.findViewById(R.id.toggle_open_range_friends);
        open_range[2] = view.findViewById(R.id.toggle_open_range_onlyme);

        EditText content = view.findViewById(R.id.post_content);
        TextView textcnt = view.findViewById(R.id.text_cnt);
        content.addTextChangedListener(new TextWatcher() {
            String str;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                str = content.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (content.length() > 1000){
                    content.setText(str);
                    Toast.makeText(view.getContext(), "최대 1000자까지 입력할 수 있습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                textcnt.setText(content.length() + " /1000");
            }
        });

        open_range[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ToggleButton t : open_range) {
                    t.setChecked(false);
                }
                open_range[0].setChecked(true);
                postBody.setOpenRange(1);
                int n = open_time_ratio.getProgress();
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
                int n = open_time_ratio.getProgress();
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
                int n = open_time_ratio.getProgress();
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

        ImageView icon = view.findViewById(R.id.icon_view); // 아이콘 뷰
        GridLayout icon_box = view.findViewById(R.id.icon_box); // 아이콘 박스
        ImageButton close_box = view.findViewById(R.id.close_icon_box_btn); // 아이콘 박스 닫는버튼
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon_box.setVisibility(View.VISIBLE);
                close_box.setVisibility(View.VISIBLE);
            }
        }); // 아이콘 뷰 클릭시 아이콘 박스 표시

        close_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        }); // 닫기버튼 클릭시 닫기

        ImageView[] icon_set = new ImageView[14];
        icon_set[0] = view.findViewById(R.id.icon1);
        icon_set[1] = view.findViewById(R.id.icon2);
        icon_set[2] = view.findViewById(R.id.icon3);
        icon_set[3] = view.findViewById(R.id.icon4);
        icon_set[4] = view.findViewById(R.id.icon5);
        icon_set[5] = view.findViewById(R.id.icon6);
        icon_set[6] = view.findViewById(R.id.icon7);
        icon_set[7] = view.findViewById(R.id.icon8);
        icon_set[8] = view.findViewById(R.id.icon9);
        icon_set[9] = view.findViewById(R.id.icon10);
        icon_set[10] = view.findViewById(R.id.icon11);
        icon_set[11] = view.findViewById(R.id.icon12);
        icon_set[12] = view.findViewById(R.id.icon13);
        icon_set[13] = view.findViewById(R.id.icon14); // 아이콘 세트 배열 정의

        icon_set[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon1);
                postBody.setIcon(1);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon2);
                postBody.setIcon(2);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon3);
                postBody.setIcon(3);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon4);
                postBody.setIcon(4);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon5);
                postBody.setIcon(5);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon6);
                postBody.setIcon(6);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon7);
                postBody.setIcon(7);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon8);
                postBody.setIcon(8);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon8);
                postBody.setIcon(9);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon10);
                postBody.setIcon(10);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon11);
                postBody.setIcon(11);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon12);
                postBody.setIcon(12);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[12].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon13);
                postBody.setIcon(13);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[13].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon14);
                postBody.setIcon(14);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });

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
                        if (checkSelfPermission(view.getContext(), Manifest.permission.READ_MEDIA_IMAGES) ==
                        PermissionChecker.PERMISSION_GRANTED &&
                        checkSelfPermission(view.getContext(), Manifest.permission.CAMERA) ==
                        PermissionChecker.PERMISSION_GRANTED) {
                            switch (i) {
                                case 0:
                                    //Toast.makeText(getActivity(),i + "번째 누름", Toast.LENGTH_SHORT).show();
                                    pickImageFromGallery();
                                    break; //갤러리 열어서 하는거 추가
                                case 1:
                                    Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if(cameraintent.resolveActivity(getActivity().getPackageManager()) != null){
                                        File pictureFile = null;
                                        try{pictureFile = createImageFile(view.getContext());}
                                        catch (IOException ex){Toast.makeText(getActivity(), "파일 생성 실패",Toast.LENGTH_SHORT).show();}
                                        if (pictureFile != null){
                                            try { // 현재 카메라 구동안됨
                                            Uri pictureUri = FileProvider.getUriForFile(view.getContext(),
                                                    "com.example.broaf.fileprovider", pictureFile);
                                                cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                                                startActivityForResult(cameraintent, TAKE_PICTURE_OK);
                                            }catch(Exception e){Toast.makeText(getActivity(), "URI 구동 구동실패",Toast.LENGTH_SHORT).show();}
                                        }
                                    }

                                    //카메라 열어서 하는거 추가
                            }
                        }
                        else{
                            requestPermissions(new String[]{
                                    Manifest.permission.READ_MEDIA_IMAGES,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA }, 10);
                        }

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // 공개시간 SeekBar동작 정의

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
                switch(postBody.getOpenRange()) {
                    case 1:
                        postBody.setOpenratio((1 + seekBar.getProgress()/3));
                        break;
                    case 2:
                        postBody.setOpenratio((1 + seekBar.getProgress()/2));
                        break;
                    case 3:
                        postBody.setOpenratio(1 + seekBar.getProgress());
                }
            }
        });
        // 저장버튼 동작 정의 및 데이터객체 내보내기
        Button save_btn = view.findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postBody.setText(String.valueOf(content.getText()));
                if (!postBody.getText().trim().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("등록");
                    builder.setMessage("작성하신 내용을 등록하시겠습니까?");
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (postBody.getOpenRange() == 3 && open_time_ratio.getProgress() == 18){
                                Calendar c = Calendar.getInstance();
                                c.add(Calendar.YEAR, 100); // 무제한일 시 100년동안 열람가능
                                Date dt = new Date(c.getTimeInMillis());
                                postBody.setOpentilldate(dt);
                            } else {
                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.DATE, postBody.getOpenratio());
                                Date d = new Date(cal.getTimeInMillis());
                                postBody.setOpentilldate(d);
                            }
                            Intent intent = new Intent(view.getContext(), MainActivity.class); // 또는 getActivity()사용
                            intent.putExtra("newpostbody", postBody);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    builder.show();
                }
                else{
                    Toast.makeText(view.getContext(), "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
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
        /*else if (requestCode == TAKE_PICTURE_OK && resultCode == Activity.RESULT_OK) {
            File file = new File(currentFilePath);
            Bitmap bitmap;
            if (Build.VERSION.SDK_INT >= 29) {
                ImageDecoder.Source source = ImageDecoder.createSource(getcontentResolver(), Uri.fromFile(file));
                try {
                    bitmap = ImageDecoder.decodeBitmap(source);
                    if (bitmap != null) { attach_img_view.setImageBitmap(bitmap); }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.fromFile(file));
                    if (bitmap != null) { attach_img_view.setImageBitmap(bitmap); }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/ // 오류로인한 카메라 이미지 처리부분 주석처리
    }
    // 선택한 이미지 처리를 위한 메서드
    private void handleSelectedImage(Uri imageUri) {
        // 이미지 처리 로직을 구현
        attach_img_view.setImageURI(imageUri);
        postBody.setImgurl(imageUri);
        del_img_btn.setVisibility(View.VISIBLE);
    }

    private void handleTakePicture(Bitmap bitmap){
        if (bitmap != null){
            attach_img_view.setImageBitmap(bitmap);
        }
    }

    private File createImageFile(Context context) throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentFilePath = image.getAbsolutePath();
        return image;
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

    public static class PostBody implements Serializable {
        private String text;
        private Date writeTime;
        private int icon; // 아이콘 구분자는 뭘로할지 몰라서 일단 주석처리
        private Date opentilldate;
        private String imguri;
        private int openRange;
        private int openratio;

        PostBody(){
            this.text = "";
            this.writeTime = new Date();
            this.imguri = "";
            this.openRange = 1;
            this.opentilldate = new Date();
            this.openratio = 9;
            this.icon = 1;
        }

        public String getText(){return text;}
        public void setText(String str){this.text = str;}
        public String getImgurl(){return imguri;}
        public void setImgurl(Uri uri){this.imguri = uri.toString();}
        public int getOpenRange(){return openRange;}
        public void setOpenRange(int n){this.openRange = n;}
        public Date getOpentilldate(){return opentilldate;}
        public void setOpentilldate(Date date){this.opentilldate = date;}
        public int getOpenratio(){return openratio;}
        public void setOpenratio(int n){this.openratio = n;}
        public int getIcon(){return this.icon;}
        public void setIcon(int n) {this.icon = n;}
        public Date getWriteTime(){return this.writeTime;}
    }

}
