package com.example.broaf;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PostViewerFragment extends Fragment {

    String myUID;

    PostLabel postLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_viewer, container, false);

        //홈에서 정보 끌어오기!!!
        if(getArguments() != null) {
            myUID = getArguments().getString("myUID");
            postLabel = (PostLabel) getArguments().getSerializable("postLabel");
        }




        return view;
    }
}