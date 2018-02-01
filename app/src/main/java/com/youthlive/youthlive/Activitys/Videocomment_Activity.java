package com.youthlive.youthlive.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youthlive.youthlive.MainActivity;
import com.youthlive.youthlive.Model.Commentmodel;
import com.youthlive.youthlive.R;
import com.youthlive.youthlive.ServiceHandler;
import com.youthlive.youthlive.Signin;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Videocomment_Activity extends AppCompatActivity {
    String userid, commenthere, videoidhere, str;
    EditText comment_here;
    ImageView comment_send;
    ListView commentrecycle;
    LinearLayoutManager manager;
    ArrayList<Commentmodel>arrlistComment;
    ProgressDialog pDialog;
    public static final String MyPREFERENCES = "userSession" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videocomment_);
        SharedPreferences sharedpreferance=getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
        userid=sharedpreferance.getString("userId","");
        comment_here = findViewById(R.id.comment_here);
        comment_send = findViewById(R.id.comment_send);
        commentrecycle=findViewById(R.id.comentlist);
        arrlistComment=new ArrayList<Commentmodel>();
        comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment_send.setClickable(false);
                String edtcmnt=comment_here.getText().toString();
                if(edtcmnt.length()>0){
                    new CommentAsyncTask().execute(userid,edtcmnt,videoidhere);
                }

            }
        });
    }
    private class CommentAsyncTask extends AsyncTask<String, String, Void> {    //////today list asyntask
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Videocomment_Activity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
        }
        @Override
        protected Void doInBackground(String... params) {
            String userid=params[0];
            String comment=params[1];
            String postid=params[2];
            String url = "http://nationproducts.in/youthlive/api/video_comment.php";
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userId", userid));
            nameValuePairs.add(new BasicNameValuePair("comment", comment));
            nameValuePairs.add(new BasicNameValuePair("videoId", "21"));
            ServiceHandler sh = new ServiceHandler();
            String userdata = sh.makeServiceCall(url,
                    ServiceHandler.POST, nameValuePairs);
            if (userdata != null)
                try {
                    JSONObject jsonObj = new JSONObject(userdata);
                    JSONArray jsonarray=jsonObj.getJSONArray("data");
                    for (int i=0;i<jsonarray.length();i++) {
                        JSONObject jsonobj1 = jsonarray.getJSONObject(i);
                        String posttext = jsonobj1.getString("comment");
                      /*  String postTime = jsonobj1.getString("comment_time");
                        String postName = jsonobj1.getString("name");
                        String usericon = jsonobj1.getString("user_img");
                        String cmntUserid = jsonobj1.getString("comment_by_user_id");*/
                        Commentmodel data = new Commentmodel();
                        data.setComment(posttext);
                      //  data.setCreateddate(postTime);
                       /* data.setUserName(postName);
                        data.setUserpics(usericon);*/
                       // data.setCmntUserid(cmntUserid);
                        arrlistComment.add(data);
                    }


                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                    e.printStackTrace();
                }
            else {
                Toast.makeText(getApplicationContext(), "No Data To Display", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Commentadapter adapter=new Commentadapter(Videocomment_Activity.this,arrlistComment);
            commentrecycle.setAdapter(adapter);
            comment_here.setText("");
            comment_send.setClickable(true);

           // new ViewallAsyncTask().execute(postId);
        }
    }

    public class Commentadapter extends BaseAdapter {

        Context mContext;
        ArrayList<Commentmodel> categoryarralist;
        private LayoutInflater layoutInflater;

        public Commentadapter(Context c, ArrayList<Commentmodel> categoryarralist) {
            mContext = c;
            this.categoryarralist = categoryarralist;
            layoutInflater = LayoutInflater.from(c);

        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return categoryarralist.size();
        }
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return categoryarralist.get(position);
        }
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Videocomment_Activity.Commentadapter.ViewHolder holder;

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.layout_coment_design, null);
                holder = new Videocomment_Activity.Commentadapter.ViewHolder();

                holder.comment = (TextView) convertView.findViewById(R.id.comment_now);
               /* holder.tvdate= (TextView) convertView.findViewById(R.id.tv_createdate);
                holder.tvName= (TextView) convertView.findViewById(R.id.tv_name);*/
                convertView.setTag(holder);

            } else {
                holder = (Videocomment_Activity.Commentadapter.ViewHolder) convertView.getTag();
            }

            holder.comment.setText(categoryarralist.get(position).getComment());
            holder.tvdate.setText(categoryarralist.get(position).getCreateddate());


            return convertView;
        }
        public class ViewHolder {
            TextView comment,tvName,tvdate;
          /*  ImageView userpics;
            HeartImageView usericon;*/
        }
    }
}





