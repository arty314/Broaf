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

        Intent receiveEditPost = getIntent();
        editedPost = (NormalPost) receiveEditPost.getSerializableExtra("editpost");

        EditText contents = findViewById(R.id.post_content);
        contents.setText(editedPost.getContents());
        SeekBar seekBar = findViewById(R.id.open_time_ratio);
        seekBar.setEnabled(false);

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

        open_range[editedPost.intGetOpenRange()].setChecked(true); // 받은 포스트의 공개범위 선택되어있게
        // 나중에 해당 이모티콘이 표시회어있고 만약에 사진 있으면 사진을 표시하는(구현못할듯)기능도 추가하기

        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = String.valueOf(contents.getText());
                if (text.trim().isEmpty()) {
                    //메인액티비티 또는 내 게시글 모아보기로 이동
                } else {
                    isclosedialog();
                }
            }
        });// --------여기까지 상단 뒤로가기 버튼 지정

        // 하단 취소버튼 지정
        Button cancel_btn = findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = String.valueOf(contents.getText());
                if (text.trim().isEmpty()) {
                    // 메인액티비티 또는 내 게시글 모아보기로 이동
                } else {
                    isclosedialog();
                }
            }
        });// -------여기까지 하단 취소버튼 지정

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
                                    Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                                    }

                                    //카메라 열어서 하는거 추가
                            }
                        }
                        else{
                            requestPermissions(new String[]{
                                    android.Manifest.permission.READ_MEDIA_IMAGES,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA }, 10);
                        }

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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

    public void onStop() {
        super.onStop();
        BottomAppBar bab = findViewById(R.id.bottomAppBar);
        bab.setVisibility(View.VISIBLE);
        FloatingActionButton fab = findViewById(R.id.navi_to_home);
        fab.setVisibility(View.VISIBLE);
    }
    public void isclosedialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("수정취소");
        builder.setMessage("수정을 취소하고 뒤로 돌아가시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                // 메인액티비티 또는 내 게시글 모아보기로 이동
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