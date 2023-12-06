package com.example.broaf;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPostFragment extends Fragment {

    ImageView view_post_profile_image,view_post_Button,view_post_heart;
    TextView view_post_Nickname,view_post_likeCount;
    EditText view_post_text, writtenDateTime;
    Button viewer_btn_close;


    //이거 추가하면
    public ViewPostFragment(ReceiveNormalPost receiveNormalPost) {
        this.receiveNormalPost = receiveNormalPost;
    }

    ReceiveNormalPost receiveNormalPost;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPostFragment newInstance(String param1, String param2) {
        ViewPostFragment fragment = new ViewPostFragment();
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
    //여기가 fragment 부분
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_viewer, container, false);
        //fragment_view_post에 연결되어있다.
        // Inflate the layout for this fragment
        FloatingActionButton fab = getActivity().findViewById(R.id.navi_to_home);
        fab.setImageResource(R.drawable.btn_center_back);
        //

//        아이콘 변경
//        ImageView viewer_icon =(ImageView) view.findViewById(R.id.viewer_icon);
//        viewer_icon.setImageDrawable(R.id.post);

        TextView nickname = (TextView) view.findViewById(R.id.viewer_nickname);
        nickname.setText(receiveNormalPost.getWriterName());
        TextView context = (TextView) view.findViewById(R.id.viewer_contents);
        context.setText(receiveNormalPost.getContents());
        TextView writtenDateTime = (TextView) view.findViewById(R.id.viewer_writtenDateTime);

        //작성 시간
        String year = receiveNormalPost.writeTime.substring(0, 4);
        String month = receiveNormalPost.writeTime.substring(4, 6);
        String date = receiveNormalPost.writeTime.substring(6, 8);
        String hour = receiveNormalPost.writeTime.substring(8, 10);
        String minute = receiveNormalPost.writeTime.substring(10, 12);
        int hour_num=Integer.parseInt(hour);
        String formattedDateTime = "time";

        if(hour_num<12){
            formattedDateTime = year + "." + month + "." + date + ". 오전 " + hour + ":" + minute;
        }
        else if (hour_num>=12){
            hour=String.valueOf(hour_num-12);
            formattedDateTime = year + "." + month + "." + date + ". 오후 " + hour + ":" + minute;
        }
        writtenDateTime.setText(formattedDateTime);

        //여기까지 게시글 뷰어 작성 시간 표시법

        ImageButton viewer_btn_copyNickname = (ImageButton) view.findViewById(R.id.viewer_btn_copyNickname);
        viewer_btn_copyNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", receiveNormalPost.getWriterName());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "작성자의 닉네임이 복사되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        //viewer_btn_copyNickname


        //버튼 구현
        Button viewer_btn_close = (Button) view.findViewById(R.id.viewer_btn_close);
        viewer_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                } else {
                    //onBackPressed();
                }
            }
        });
        //여기까지 계정정보 frag

        // ImageView = (ImageView)view.findViewById(R.id.view_post_profile_image)
        return view;
    }

}