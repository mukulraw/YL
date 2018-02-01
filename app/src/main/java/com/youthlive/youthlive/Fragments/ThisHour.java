package com.youthlive.youthlive.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youthlive.youthlive.Adapter.Drawer_adapter;
import com.youthlive.youthlive.Adapter.thishour_Adapter;
import com.youthlive.youthlive.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThisHour extends Fragment {
    RecyclerView thishour;
    thishour_Adapter adapter;
    LinearLayoutManager linearLayoutManager;


    public ThisHour() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_ratting_fragment, container, false);
        thishour = (RecyclerView)view.findViewById(R.id.thishour);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new thishour_Adapter(getContext());
        thishour.setAdapter(adapter);
        thishour.setLayoutManager(linearLayoutManager);


        return view;
    }

}
