package com.example.broaf;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PostViewerFragment extends Fragment {

    String myUID;

    PostLabel postLabel;
    boolean isLikeClicked=false;
    int likeCount=0;
    String likeCount_str="0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_viewer, container, false);

        //홈에서 정보 끌어오기!!!
        if(getArguments() != null) {
            myUID = getArguments().getString("myUID");
            postLabel = (PostLabel) getArguments().getSerializable("postLabel");
        }


        /*불러온 정보 붙여넣기*/
        ImageView viewer_profimg = (ImageView)view.findViewById(R.id.viewer_profimg);
        //계정 별 프로필 사진 설정 기능 미구현. 고로, 조건문으로 임시처리
        String writerUID=postLabel.writerUID;
        if(writerUID=="B8EeoxiR4Ihi6c4MVCgTfgMfG0j1"){
        viewer_profimg.setImageResource(R.drawable.writepost);
        }else if (writerUID=="10001"){}

        TextView viewer_nickname = (TextView) view.findViewById(R.id.viewer_nickname);
        viewer_nickname.setText(postLabel.writerName);

        ImageButton viewer_btn_more = (ImageButton) view.findViewById(R.id.viewer_btn_more);
        viewer_btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클릭 시 사용자 닉네임 복사하기
                Toast.makeText(getActivity(), "개발 중인 기능입니다.",Toast.LENGTH_SHORT).show();
            }
        });

        TextView viewer_contents = (TextView) view.findViewById(R.id.viewer_contents);
        viewer_contents.setText(postLabel.content);

        ImageView viewer_postImage = (ImageView)view.findViewById(R.id.viewer_postImage);
        //이미지 저장소 미구현으로 if문으로 대체
        String postImage = postLabel.attachImageURL;
        if(postImage!=null){
            viewer_postImage.setImageResource(R.drawable.img_forpost);
        }

        TextView viewer_writtenDateTime = (TextView) view.findViewById(R.id.viewer_writtenDateTime);
        //e.g. 202311300042 -> 2023.11.30. 오전 00:42

        String year = postLabel.writtenDateTime.substring(0, 4);
        String month = postLabel.writtenDateTime.substring(4, 6);
        String date = postLabel.writtenDateTime.substring(6, 8);
        String hour = postLabel.writtenDateTime.substring(8, 10);
        String minute = postLabel.writtenDateTime.substring(10, 12);
        int hour_num=Integer.parseInt(hour);
        String formattedDateTime = year + "." + month + "." + date + ". " + hour + ":" + minute;

        if(hour_num<12){
            formattedDateTime = year + "." + month + "." + date + ". 오전 " + hour + ":" + minute;
        }
        else if (hour_num>=12){
            formattedDateTime = year + "." + month + "." + date + ". 오후 " + hour + ":" + minute;
        }
        viewer_writtenDateTime.setText(formattedDateTime);


        //좋아요: 미구현이므로 ui 형태만 구현. 버튼 누르면 count+1 그리고 image 변경
        ImageButton viewer_btn_heart = (ImageButton) view.findViewById(R.id.viewer_btn_heart);
        TextView viewer_likeCount = (TextView) view.findViewById(R.id.viewer_likeCount);

        likeCount_str=String.valueOf(postLabel.likeCount);
        viewer_likeCount.setText(likeCount_str);


        viewer_btn_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLikeClicked==false){
                    viewer_btn_heart.setImageResource(R.drawable.heart_clicked);
                    likeCount_str=String.valueOf(postLabel.likeCount+1);
                    viewer_likeCount.setText(likeCount_str);
                    isLikeClicked=true;
                } else if (isLikeClicked==true) {
                    viewer_btn_heart.setImageResource(R.drawable.heart_unclicked);
                    likeCount_str=String.valueOf(postLabel.likeCount);
                    viewer_likeCount.setText(likeCount_str);
                    isLikeClicked=false;
                }
            }
        });




        //창 닫기
        Button viewer_btn_close = (Button) view.findViewById(R.id.viewer_btn_close);
        viewer_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(PostViewerFragment.this).commit();
            }
        });




        return view;
    }
}