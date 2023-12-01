package com.example.broaf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPostFragment extends Fragment {

    ImageView view_post_profile_image,view_post_Button,view_post_heart;
    TextView view_post_Nickname,view_post_likeCount;
    EditText view_post_text, writtenDateTime;




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

        TextView nickname = (TextView) view.findViewById(R.id.viewer_nickname);
        nickname.setText(receiveNormalPost.getWriterName());
        TextView context = (TextView) view.findViewById(R.id.viewer_contents);
        context.setText(receiveNormalPost.getContents());
        TextView writtenDateTime = (TextView) view.findViewById(R.id.viewer_writtenDateTime);
        writtenDateTime.setText(receiveNormalPost.getWriteTime());
        TextView view_post_likeCount = (TextView) view.findViewById(R.id.viewer_likeCount);
        view_post_likeCount.setText(receiveNormalPost.getLikeCount());
        // ImageView = (ImageView)view.findViewById(R.id.view_post_profile_image)
        return view;
    }

}