package com.example.broaf;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class CreatePostFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        ToggleButton[] open_range = new ToggleButton[3];
        open_range[0] = view.findViewById(R.id.toggle_open_range_public);

        return view;
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
    public void onStop(){
        super.onStop();
        BottomAppBar bab = requireActivity().findViewById(R.id.bottomAppBar);
        bab.setVisibility(View.VISIBLE);
        FloatingActionButton fab = requireActivity().findViewById(R.id.navi_to_home);
        fab.setVisibility(View.VISIBLE);
    }


}