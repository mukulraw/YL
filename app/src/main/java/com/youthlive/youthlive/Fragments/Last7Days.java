package com.youthlive.youthlive.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youthlive.youthlive.Adapter.Last7DaysAdapter;
import com.youthlive.youthlive.Adapter.thishour_Adapter;
import com.youthlive.youthlive.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Last7Days extends Fragment {
    RecyclerView Last7;
    Last7DaysAdapter adapterlast7;
    LinearLayoutManager linearLayoutManager7;

    public Last7Days() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_last7_days, container, false);
        Last7 = (RecyclerView) view.findViewById(R.id.last7);
        linearLayoutManager7 = new LinearLayoutManager(getActivity());
        adapterlast7 = new Last7DaysAdapter(getContext());
        Last7.setAdapter(adapterlast7);
        Last7.setLayoutManager(linearLayoutManager7);
        return  view;
    }

}
