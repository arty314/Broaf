package com.example.broaf;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//게시글 편집화면 액티비티
public class PostEditerActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PICTURE_OK = 2;
    private ImageView attach_img_view;
    private Button del_img_btn;
    private String currentFilePath;
    NormalPost editedPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_editer);

        Intent receiveEditPost = getIntent(); // ReceiveNormalPost객체를 받아서 NormalPost에 대입하기
        editedPost = new NormalPost ((ReceiveNormalPost) receiveEditPost.getSerializableExtra("edit_post"));
        String origintext = editedPost.getContents();

        EditText contents = findViewById(R.id.post_content); // 글 내용 세팅
        contents.setText(editedPost.getContents());
        SeekBar seekBar = findViewById(R.id.open_time_ratio); // 공개시간은 변경 못하도록
        //seekBar.setEnabled(false);

        ToggleButton[] open_range = new ToggleButton[3];
        open_range[0] = findViewById(R.id.toggle_open_range_public);
        open_range[1] = findViewById(R.id.toggle_open_range_friends);
        open_range[2] = findViewById(R.id.toggle_open_range_onlyme);

        open_range[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ToggleButton t : open_range) {
                    t.setChecked(false);
                }
                open_range[0].setChecked(true);
                editedPost.setOpenRange(1);
            }
        });
        open_range[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ToggleButton t : open_range) {
                    t.setChecked(false);
                }
                open_range[1].setChecked(true);
                editedPost.setOpenRange(2);
            }
        });
        open_range[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ToggleButton t : open_range) {
                    t.setChecked(false);
                }
                open_range[2].setChecked(true);
                editedPost.setOpenRange(3);
            }
        });

        for (int i = 0; i < 3; i++){
            open_range[i].setChecked(false);
        }
        open_range[editedPost.intGetOpenRange()].setChecked(true); // 받은 포스트의 공개범위 선택되어있게
        // 나중에 해당 이모티콘이 표시회어있고 만약에 사진 있으면 사진을 표시하는(구현못할듯)기능도 추가하기

        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edittext = String.valueOf(contents.getText()).trim();
                if (origintext.equals(edittext)) {
                    // 메인액티비티 또는 내 게시글 모아보기로 이동
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("수정 취소");
                    builder.setMessage("게시글 편집을 취소하겠습니까?");
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                }
            }
        });// --------여기까지 상단 뒤로가기 버튼 지정

        // 하단 취소버튼 지정
        Button cancel_btn = findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edittext = String.valueOf(contents.getText()).trim();
                if (origintext.equals(edittext)) {
                    // 메인액티비티 또는 내 게시글 모아보기로 이동
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("수정 취소");
                    builder.setMessage("게시글 편집을 취소하겠습니까?");
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
                }
            }
        });// -------여기까지 하단 취소버튼 지정

        ImageView icon = findViewById(R.id.icon_view); // 아이콘 뷰
        GridLayout icon_box = findViewById(R.id.icon_box); // 아이콘 박스
        ImageButton close_box = findViewById(R.id.close_icon_box_btn); // 아이콘 박스 닫는버튼
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
        icon_set[0] = findViewById(R.id.icon1);
        icon_set[1] = findViewById(R.id.icon2);
        icon_set[2] = findViewById(R.id.icon3);
        icon_set[3] = findViewById(R.id.icon4);
        icon_set[4] = findViewById(R.id.icon5);
        icon_set[5] = findViewById(R.id.icon6);
        icon_set[6] = findViewById(R.id.icon7);
        icon_set[7] = findViewById(R.id.icon8);
        icon_set[8] = findViewById(R.id.icon9);
        icon_set[9] = findViewById(R.id.icon10);
        icon_set[10] = findViewById(R.id.icon11);
        icon_set[11] = findViewById(R.id.icon12);
        icon_set[12] = findViewById(R.id.icon13);
        icon_set[13] = findViewById(R.id.icon14); // 아이콘 세트 배열 정의

        icon_set[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon1);
                editedPost.setIcon(0);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon2);
                editedPost.setIcon(1);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon3);
                editedPost.setIcon(2);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon4);
                editedPost.setIcon(3);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon5);
                editedPost.setIcon(4);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon6);
                editedPost.setIcon(5);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon7);
                editedPost.setIcon(6);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon8);
                editedPost.setIcon(7);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon8);
                editedPost.setIcon(8);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon10);
                editedPost.setIcon(9);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon11);
                editedPost.setIcon(10);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon12);
                editedPost.setIcon(11);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[12].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon13);
                editedPost.setIcon(12);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });
        icon_set[13].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon.setImageResource(R.drawable.posticon14);
                editedPost.setIcon(13);
                icon_box.setVisibility(View.GONE);
                close_box.setVisibility(View.GONE);
            }
        });

        del_img_btn = findViewById(R.id.delete_img_btn);
        del_img_btn.setVisibility(View.GONE); // 처음에는 숨겨져있음
        del_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //첨부된 이미지를 지우는 코드
                del_img_btn.setVisibility(View.GONE);
                editedPost.setImgurl("");
                attach_img_view.setImageURI(Uri.parse(""));
            }
        });

        del_img_btn = findViewById(R.id.delete_img_btn);
        del_img_btn.setVisibility(View.GONE); // 처음에는 숨겨져있음
        del_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //첨부된 이미지를 지우는 코드
                del_img_btn.setVisibility(View.GONE);
                editedPost.setImgurl("");
                attach_img_view.setImageURI(Uri.parse(""));
            }
        });

        attach_img_view = findViewById(R.id.attach_img_view);
        ImageView attach_img_btn = findViewById(R.id.add_img_btn);
        attach_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("사진첨부 방법 선택");
                builder.setItems(R.array.attach_img, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) ==
                                PackageManager.PERMISSION_GRANTED &&
                                checkSelfPermission(android.Manifest.permission.CAMERA) ==
                                        PackageManager.PERMISSION_GRANTED) {
                            switch (i) {
                                case 0:
                                    //Toast.makeText(getActivity(),i + "번째 누름", Toast.LENGTH_SHORT).show();
                                    pickImageFromGallery();
                                    break; //갤러리 열어서 하는거 추가
                                case 1:
                                    /*Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if(cameraintent.resolveActivity(view.getContext().getPackageManager()) != null){
                                        File pictureFile = null;
                                        try{pictureFile = createImageFile(view.getContext());}
                                        catch (IOException ex){
                                            Toast.makeText(view.getContext(), "파일 생성 실패",Toast.LENGTH_SHORT).show();}
                                        if (pictureFile != null){
                                            try { // 현재 카메라 구동안됨
                                                Uri pictureUri = FileProvider.getUriForFile(view.getContext(),
                                                        "com.example.broaf.fileprovider", pictureFile);
                                                cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                                                startActivityForResult(cameraintent, TAKE_PICTURE_OK);
                                            }catch(Exception e){Toast.makeText(view.getContext(), "URI 구동 구동실패",Toast.LENGTH_SHORT).show();}
                                        }
                                    }*/

                                    //카메라 열어서 하는거 추가
                            }
                        }
                        else{
                            requestPermissions(new String[]{
                                    //android.Manifest.permission.READ_MEDIA_IMAGES,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA }, 10);
                        }

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    Button edit_btn = findViewById(R.id.edit_btn);
    edit_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    });




    }
    @Override
    public void onResume() {
        super.onResume();
        ActionBar ab = (this.getSupportActionBar());
        if (ab != null) {
            ab.hide();      //상단바 숨기기
            //ab.show();    //상단바 보이기
        }
        BottomAppBar bab = this.findViewById(R.id.bottomAppBar);
        bab.setVisibility(View.GONE);
        FloatingActionButton fab = this.findViewById(R.id.navi_to_home);
        fab.setVisibility(View.GONE);
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
        editedPost.setImgurl(imageUri.toString());
        del_img_btn.setVisibility(View.VISIBLE);
    }

    /*private void handleTakePicture(Bitmap bitmap){
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
    }*/

    public void onStop() {
        super.onStop();
        BottomAppBar bab = findViewById(R.id.bottomAppBar);
        bab.setVisibility(View.VISIBLE);
        FloatingActionButton fab = findViewById(R.id.navi_to_home);
        fab.setVisibility(View.VISIBLE);
    }

}