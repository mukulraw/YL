package com.youthlive.youthlive.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youthlive.youthlive.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlideRightFragment extends Fragment {


    public SlideRightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slide_right, container, false);
    }

}
