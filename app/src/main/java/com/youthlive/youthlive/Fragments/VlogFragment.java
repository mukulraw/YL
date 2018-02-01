package com.youthlive.youthlive.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youthlive.youthlive.Adapter.VideoShowAdapter;
import com.youthlive.youthlive.DBHandler.SessionManager;
import com.youthlive.youthlive.HotAdapter;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.R;
import com.youthlive.youthlive.RecyclerviewItemspace.GridSpacingItemDecoration;
import com.youthlive.youthlive.VlogFrag;
import com.youthlive.youthlive.bean;
import com.youthlive.youthlive.vlogListPOJO.Datum;
import com.youthlive.youthlive.vlogListPOJO.vlogListBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class VlogFragment extends Fragment {
    GridView gvVideoGallery;
    String userID;
    SessionManager session;
    HashMap<String, String> user;
    SwipeRefreshLayout srlvideogallery;
    Dialog dialogGalleryvideo;
    Dialog dialogShowDetail;
    String userProPost;
    String userPostId;
    ArrayList<String> video;
    VideoShowAdapter videoadapter;
    String url="http://nationproducts.in/youthlive/api/get_video.php";
    RequestQueue requestQueue;
    public VlogFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_vlog, container, false);
        gvVideoGallery = (GridView) view.findViewById(R.id.video);
        session=new SessionManager(getActivity());
        user= session.getUserDetails();
        userID = user.get(SessionManager.USER_ID);
        video= new ArrayList<String>();
        requestQueue = Volley.newRequestQueue(getContext());
        StringRequest sr=new StringRequest(Request.Method.POST,url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {System.out.println("response: "+s);
                    JSONObject json=new JSONObject(s);
                    JSONArray jsonArray= null;
                    jsonArray = json.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        video.add(js.getString("videoURL"));
                        System.out.println("Video: "+video);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                refine();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("Error response Array");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userId", "170");
             //  params.put("videoURL", "video");
                return params;
            }
        };
        requestQueue.add(sr);
        return  view;
    }
    public void refine()
    {
        videoadapter = new VideoShowAdapter(getActivity(),video);
        gvVideoGallery.setAdapter(videoadapter);
    }

    }



