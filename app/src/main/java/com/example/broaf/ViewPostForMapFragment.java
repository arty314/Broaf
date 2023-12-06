package com.example.broaf;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class ViewPostForMapFragment extends Fragment {

    String myNickname;

    ReceiveNormalPost normalPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_viewer, container, false);

        //홈에서 정보 끌어오기!!!
        if(getArguments() != null) {
            myNickname = getArguments().getString("myNickname");
            normalPost = (ReceiveNormalPost) getArguments().getSerializable("normalPost");
        }


        TextView viewer_nickname = (TextView) view.findViewById(R.id.viewer_nickname);
        viewer_nickname.setText(normalPost.getWriterName());

        TextView viewer_openrange = (TextView) view.findViewById(R.id.viewer_openrange);
        if(normalPost.getOpenRange().equals("1"))
            viewer_openrange.setText("전체 공개");
        else if(normalPost.getOpenRange().equals("2"))
            viewer_openrange.setText("친구 공개");
        else if(normalPost.getOpenRange().equals("3"))
            viewer_openrange.setText("비공개");


        TextView viewer_contents = (TextView) view.findViewById(R.id.viewer_contents);
        viewer_contents.setText(normalPost.getContents());

        TextView viewer_writtenDateTime = (TextView) view.findViewById(R.id.viewer_writtenDateTime);
        //e.g. 202311300042 -> 2023.11.30. 오전 00:42

        String year = normalPost.writeTime.substring(0, 4);
        String month = normalPost.writeTime.substring(4, 6);
        String date = normalPost.writeTime.substring(6, 8);
        String hour = normalPost.writeTime.substring(8, 10);
        String minute = normalPost.writeTime.substring(10, 12);
        int hour_num=Integer.parseInt(hour);
        String formattedDateTime = "time";

        if(hour_num<12){
            formattedDateTime = year + "." + month + "." + date + ". 오전 " + hour + ":" + minute;
        }
        else if (hour_num>=12){
            hour=String.valueOf(hour_num-12);
            formattedDateTime = year + "." + month + "." + date + ". 오후 " + hour + ":" + minute;
        }
        viewer_writtenDateTime.setText(formattedDateTime);


        ImageButton viewer_btn_copyNickname = (ImageButton) view.findViewById(R.id.viewer_btn_copyNickname);
        viewer_btn_copyNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", normalPost.getWriterName());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "작성자의 닉네임이 복사되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        //창 닫기
        Button viewer_btn_close = (Button) view.findViewById(R.id.viewer_btn_close);
        viewer_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(ViewPostForMapFragment.this).commit();
            }
        });




        return view;
    }
}