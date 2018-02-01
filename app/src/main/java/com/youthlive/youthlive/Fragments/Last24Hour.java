package com.youthlive.youthlive.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youthlive.youthlive.Adapter.Last24hourAdapter;
import com.youthlive.youthlive.Adapter.thishour_Adapter;
import com.youthlive.youthlive.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Last24Hour extends Fragment {
    RecyclerView Last24;
    Last24hourAdapter adapterlast24;
    LinearLayoutManager linearLayoutManager24;


    public Last24Hour() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_last24_hour, container, false);

        Last24 = (RecyclerView) view.findViewById(R.id.last24h);
        linearLayoutManager24 = new LinearLayoutManager(getActivity());
        adapterlast24 = new Last24hourAdapter(getContext());
        Last24.setAdapter(adapterlast24);
        Last24.setLayoutManager(linearLayoutManager24);
        return  view;
    }

}
