package com.youthlive.youthlive.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youthlive.youthlive.Adapter.Drawer_adapter;
import com.youthlive.youthlive.Live;
import com.youthlive.youthlive.R;



public class FollowingFragment extends Fragment {
    RecyclerView recnotifi;
    LinearLayoutManager ltmanager;
    Drawer_adapter draadapter;


    public FollowingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_following, container, false);


        recnotifi = (RecyclerView)view.findViewById(R.id.notification_adapter);
        ltmanager = new LinearLayoutManager(getActivity());
        draadapter = new Drawer_adapter(getContext());
        recnotifi.setAdapter(draadapter);
        recnotifi.setLayoutManager(ltmanager);
        return view;
    }
}
