package com.example.broaf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
//그냥 빈 frag 만드니까 이렇게 떴음. 알아서 수정해주세요

public class PostViewerFragment extends Fragment {
//    Button btn_close_viewer;    //뷰어 닫기 버튼

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_viewer, container, false);


/*근데 뭔가 안 돼서 일단 주석 걸어둠
        //post viewer frag 닫기
        Button btn_close_viewer=(Button) getView().findViewById(R.id.btn_close_viewer);
        btn_close_viewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //현재 Frag 종료
                getFragmentManager().popBackStack();
            }
        });
        //여기까지 post viewer frag 닫기
*/

        return view;
    }
}