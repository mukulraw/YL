package com.youthlive.youthlive;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.youthlive.youthlive.Activitys.CommentS;
import com.youthlive.youthlive.Activitys.Videocomment_Activity;
import com.youthlive.youthlive.DBHandler.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by TBX on 9/21/2017.
 */

public class VlogFrag extends Fragment {
   GridView grid;
    ShineButton likevideo;
    StaggeredGridLayoutManager manager;
    GridVideoadapter videoadapter;
    String userID;
    SessionManager session;
    HashMap<String, String> user;
    SwipeRefreshLayout srlvideogallery;
    Dialog dialogGalleryvideo;
    String str, userid1, videoidd,timedate;
    ArrayList<String> video;
    private static final String TAG = "ERROR";
    String url = "http://nationproducts.in/youthlive/api/get_video.php";
    String URL = "http://nationproducts.in/youthlive/api/video_likes.php";
    RequestQueue requestQueue;
    SharedPreferences settings;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vlog_frag_layout, container, false);
        grid = (GridView) view.findViewById(R.id.grid);
        SharedPreferences settings = this.getActivity().getSharedPreferences("mypref", MODE_PRIVATE);
        userid1=settings.getString("userid","");
        session = new SessionManager(getContext());
        user = session.getUserDetails();
        userID = user.get(SessionManager.USER_ID);
        video = new ArrayList<String>();
        requestQueue = Volley.newRequestQueue(getContext());
        StringRequest sr = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    System.out.println("response: " + s);
                    JSONObject json = new JSONObject(s);
                    JSONArray jsonArray = null;
                    jsonArray = json.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        userID=js.getString("userId");
                        videoidd=js.getString("videoId");
                        timedate=js.getString("uploadTime");
                        video.add(js.getString("videoURL"));
                        System.out.println("Video: " + video);
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
                params.put("userId", userID);
                params.put("videoURL", String.valueOf(video));
                return params;
            }
        };
        requestQueue.add(sr);
        return view;
    }
    public void refine() {
        videoadapter = new GridVideoadapter(getContext(),video);
        grid.setAdapter(videoadapter);
    }
    public void Likevideo() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    if (!status.equals("0")) {
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        userID = jObj.getString("userId");
                        videoidd = jObj.getString("videoId");
                    } else {
                        str = jObj.getString("message");
                        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                    }

//                            Toast.makeText(getApplicationContext(), "Message Sent successfully!",
//                                    Toast.LENGTH_LONG).show();
//                            Intent i=new Intent(OTP.this,MainActivity.class);
//                            startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("videoId", videoidd);
                params.put("userId",userid1);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    public class GridVideoadapter extends BaseAdapter {
        Context context;
        ArrayList<String> Video;
        public GridVideoadapter(
                Context context,
                ArrayList<String> video
        ) {
            this.context = context;
            this.Video = video;
        }

        public int getCount() {
            return Video.size();
        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(final int position, View child, ViewGroup parent) {
            GridVideoadapter.Holder holder;
            LayoutInflater layoutInflater;
            View v;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.vlog_list_model, null);
            holder = new GridVideoadapter.Holder();
            holder.video1 = (VideoView) v.findViewById(R.id.videouri);
            v.setTag(holder);
            holder = (GridVideoadapter.Holder) v.getTag();
            System.out.println("videos: " + Video);
            holder.video1.setVideoURI(Uri.parse(Video.get(position)));
            return v;
        }
        public class Holder {
            VideoView video1;
        }
    }
}


