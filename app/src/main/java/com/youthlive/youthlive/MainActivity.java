package com.youthlive.youthlive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.youthlive.youthlive.DBHandler.SessionManager;
import com.youthlive.youthlive.Handler.CrudHandler;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.Response.AddPostImageResponse;
import com.youthlive.youthlive.addVideoPOJO.addVideoBean;
import com.youthlive.youthlive.feedBackPOJO.feedBackBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final String BASEVIDEO_URL = "http://nationproducts.in/youthlive/api/add_video.php";
    private final String TAG = "ERROR";
    String str, userId;
    public final int GALLEY_REQUEST_CODE_CUSTOMER = 10;
    public final int REQUEST_IMAGE_CAPTURE = 1;
    public final int MEDIA_TYPE_VIDEO = 2;
    private final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private MediaRecorder mediaRecorder;
    String formattedDate;
    Toolbar toolbar;

    TextView name , logout;

    /*private ProgressDialog pDialog;*/
    DrawerLayout drawer;
    ImageButton online, vlog, live, timeline, profile;
    String mediaPath, mediaPath1;
    ImageView imgView;
    private Uri fileUri;
    private Uri realUri;
    Bitmap bitmap;
    SessionManager session;
    String[] mediaColumns = {MediaStore.Video.Media._ID};
    ProgressDialog progressDialog;
    SmoothProgressBar videoProgress;
    final int REQUEST_VIDEO_CAPTURE = 1;

    //AHBottomNavigation bottom;

    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;

    TextView feedBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //pDialog = new ProgressDialog(this);
        //pDialog.setMessage("Please wait...");
        //pDialog.setCancelable(false);


        pref = getSharedPreferences("pref" , Context.MODE_PRIVATE);
        edit = pref.edit();


        feedBack = (TextView)findViewById(R.id.feedback);

        feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("asdasd" , "asdasd");

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_feed_back);
                dialog.setCancelable(true);
                dialog.show();

                final EditText comment = (EditText)dialog.findViewById(R.id.comment);
                Button submit = (Button)dialog.findViewById(R.id.submit);
                final ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String com = comment.getText().toString();

                        if (com.length() > 0)
                        {


                            progressBar.setVisibility(View.VISIBLE);

                            final bean b = (bean) getApplicationContext();

                            final Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            final AllAPIs cr = retrofit.create(AllAPIs.class);


                            Call<feedBackBean> call = cr.feedback(b.userId , com);

                            call.enqueue(new Callback<feedBackBean>() {
                                @Override
                                public void onResponse(Call<feedBackBean> call, retrofit2.Response<feedBackBean> response) {

                                    Toast.makeText(MainActivity.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();

                                    progressBar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(Call<feedBackBean> call, Throwable t) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this , "Please Enter a Comment" , Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                if (drawer.isDrawerOpen(GravityCompat.START))
                {
                    drawer.closeDrawer(GravityCompat.START);
                }



            }
        });

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        online = (ImageButton) findViewById(R.id.live);
        vlog = (ImageButton) findViewById(R.id.vlog);
       // live = (ImageButton) findViewById(R.id.golive);
        timeline = (ImageButton) findViewById(R.id.timeline);
        profile = (ImageButton) findViewById(R.id.profile);
        SharedPreferences settings = getSharedPreferences("mypref", MODE_PRIVATE);
        userId = settings.getString("userid", "");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        name = (TextView)findViewById(R.id.name);
        logout = (TextView)findViewById(R.id.logout);

        //bottom = (AHBottomNavigation)findViewById(R.id.bottom_navigation);

        videoProgress = (SmoothProgressBar)findViewById(R.id.video_progress);

        drawer = (DrawerLayout) findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Live Room" , R.drawable.live , R.color.black);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("VLOG" , R.drawable.icon , R.color.black);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Go Live" , R.drawable.eye , R.color.colorAccent);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Timeline" , R.drawable.timeline , R.color.black);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("Profile" , R.drawable.user2 , R.color.black);





        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, GoLive.class);
                startActivity(intent);
            }
        });


        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("asd", "online clicked");

                toolbar.setTitle("Live Room");

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }

                Live frag1 = new Live();
                ft.replace(R.id.replace, frag1);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.commit();

            }
        });


        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("asd", "online clicked");

                toolbar.setTitle("Timeline");

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }

                Timeline frag1 = new Timeline();
                ft.replace(R.id.replace, frag1);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.commit();

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("asd", "online clicked");

                toolbar.setTitle("Profile");

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }

                Profile frag1 = new Profile();
                ft.replace(R.id.replace, frag1);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.commit();

            }
        });
        vlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("asd", "vlog clicked");

                toolbar.setTitle("Vlog");

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }

                Vlog frag1 = new Vlog();
                ft.replace(R.id.replace, frag1);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.commit();

            }
        });

        /*bottom.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                if (position == 0)
                {
                    Log.d("asd", "online clicked");

                    toolbar.setTitle("Live Room");

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }

                    Live frag1 = new Live();
                    ft.replace(R.id.replace, frag1);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();
                }
                else if (position == 1)
                {
                    Log.d("asd", "vlog clicked");

                    toolbar.setTitle("Vlog");

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }

                    Vlog frag1 = new Vlog();
                    ft.replace(R.id.replace, frag1);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();
                }
                else if (position == 2)
                {



                    Intent intent = new Intent(MainActivity.this, GoLive.class);
                    startActivity(intent);
                }
                else if (position == 3)
                {
                    Log.d("asd", "online clicked");

                    toolbar.setTitle("Timeline");

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }

                    Timeline frag1 = new Timeline();
                    ft.replace(R.id.replace, frag1);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();
                }
                else if (position == 4)
                {
                    Log.d("asd", "online clicked");

                    toolbar.setTitle("Profile");

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }

                    Profile frag1 = new Profile();
                    ft.replace(R.id.replace, frag1);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();
                }


                return true;
            }
        });*/

        buildGoogleApiClient();

        bean b = (bean)getApplicationContext();

        name.setText("Hi, " + b.userName);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mGoogleApiClient.isConnected())
                {
                    signOut();
                }

                LoginManager.getInstance().logOut();

                edit.remove("type");
                edit.remove("user");
                edit.remove("pass");
                edit.apply();

                Intent i = new Intent(getApplicationContext() , Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

            }
        });

        toolbar.setTitle("Live Room");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Live frag1 = new Live();
        ft.replace(R.id.replace, frag1);
        //ft.addToBackStack(null);
        ft.commit();
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onResult(@NonNull Status status) {


                        if (status.isSuccess())
                        {

                            edit.remove("type");
                            edit.remove("user");
                            edit.remove("pass");
                            edit.apply();
                            Intent i = new Intent(getApplicationContext() , Login.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            overridePendingTransition(0,0);
                            finish();

                        }


                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.notifications:

                break;
            case R.id.video:
                /*Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);*/

                Log.d("jg" , "video");

                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void dialogCamera() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(MainActivity.this);
        myAlertDialog.setTitle("Uploade Your Pictures");
        myAlertDialog.setMessage("How do you want to set your picture?");
        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(openGallery, "Open Gallery"), GALLEY_REQUEST_CODE_CUSTOMER);
            }
        });
        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        myAlertDialog.show();
    }

    //multipart image..............................
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //dialog =ProgressDialog.show(Activity.this, "", "loading...",false,true);
        try {
            if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
                Uri videoUri = data.getData();
                uploadVideo(videoUri);
            }
            else if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
                if (resultCode == MainActivity.RESULT_OK) {
                    realUri = Uri.parse(fileUri.getPath());
                    //imgPostServices();

                    uploadVideo(realUri);

                } else if (resultCode == MainActivity.RESULT_CANCELED) {
                    Toast.makeText(MainActivity.this, "User cancelled the video capture.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(MainActivity.this, "Video capture failed.", Toast.LENGTH_LONG).show();

                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == MainActivity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                //    ivProfileEditProfile.setImageBitmap(photo);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(MainActivity.this.getContentResolver(), photo, "Title", null);
                realUri = Uri.parse(path);

                try {
                    realUri = Uri.parse(getPath(MainActivity.this , realUri));
                    //  ivProfileEditProfile.setImageURI(data.getData());
                    //imgPostServices();
                    uploadVideo(realUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //   Toast.makeText(RegisterRider.this, ""+realUri, Toast.LENGTH_SHORT).show();
            } else if (resultCode == MainActivity.RESULT_OK && requestCode == GALLEY_REQUEST_CODE_CUSTOMER) {
                if (data.getData() != null) {
                    try {
                        Log.d("TAG", "not cust");
                        realUri = data.getData();
                        // Get real path to make File
                        realUri = Uri.parse(getPath(MainActivity.this , data.getData()));
                        bitmap = BitmapFactory.decodeFile(realUri.getPath());
                        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                        //imgPostServices();

                        uploadVideo(realUri);

                        //Log.d(TAG, "Image path :- " + realUri);
                        //   ivProfileEditProfile.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        Log.e("TAG", e.getMessage());
                    }
                } else {
                    Toast.makeText(MainActivity.this, "plaese select another image", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private String getPath(Uri uri) throws Exception {
        // this method will be used to get real path of Image chosen from gallery.
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = MainActivity.this.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }*/

    private TypedFile makeFile(String uri) {
        // this will make file which is required by Retrofit.
        File file = new File(uri);
//        File photo = new File(file,  "Pic.image");
        TypedFile typedFile = new TypedFile("", file);
        Log.d("show", String.valueOf(file));
        Log.d("show", String.valueOf(uri));
        return typedFile;
    }
    //video uploading here.,.............................................
    /** Create a file Uri for saving an image or video */
    /**
     * Create a File for saving an image or video
     */
    private Uri getOutputMediaFileUri(int type) {
     /*    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
       mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
       mediaRecorder.setVideoEncodingBitRate(690000);
       mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
       mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoSize(640, 480);*/
        return Uri.fromFile(getOutputMediaFile(type));

    }

    private File getOutputMediaFile(int type) {
        // Check that the SDCard is mounted
        //   File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "JUMP Video");
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/youyhlive/" + "/Send Video/");
        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // Toast.makeText(ActivityContext, "Failed to create directory MyCameraVideo.", Toast.LENGTH_LONG).show();
                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }
        // Create a media file name
        // For unique file name appending current timeStamp with file name
        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());
        File mediaFile;
        if (type == MEDIA_TYPE_VIDEO) {
            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");

        } else {
            return null;
        }
        return mediaFile;
    }

    private void imgPostServices() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        formattedDate = df.format(c.getTime());
        final ProgressDialog pd = ProgressDialog.show(MainActivity.this, "Please wait", "Loading...", false, false);
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constant.ROOT_URL).build();
        CrudHandler.AddPostImage addPostImage = adapter.create(CrudHandler.AddPostImage.class);
        addPostImage.addPost(attechPostImg(), new retrofit.Callback<AddPostImageResponse>() {
            @Override
            public void success(AddPostImageResponse addPostImageResponse, retrofit.client.Response response) {
                if (addPostImageResponse.getStatus() == 1) {
                    Toast.makeText(MainActivity.this, "" + addPostImageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    pd.dismiss();
                    Log.d("====> Error Image", error.getMessage());
                   /* AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Network Problem.....");
                    builder.setMessage(error.getMessage());
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.show();
                    TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);
                    pd.dismiss();*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private MultipartTypedOutput attechPostImg() {

        Log.d("video test", "" + realUri.toString());
        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
        try {
            multipartTypedOutput.addPart("userId", new TypedString("94"));
            // multipartTypedOutput.addPart("date",new TypedString(formattedDate));
            // multipartTypedOutput.addPart("photo",makeFile(realUri.toString()));
            //  multipartTypedOutput.addPart("post",makeFile(realUri.toString()));
            multipartTypedOutput.addPart("video", makeFile(realUri.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartTypedOutput;
    }
    public void uploadVideo(Uri uri)
    {
        videoProgress.setVisibility(View.VISIBLE);
        MultipartBody.Part body = null;
        String mCurrentPhotoPath = getPath(MainActivity.this , uri);

        File file = new File(mCurrentPhotoPath);


        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        body = MultipartBody.Part.createFormData("video", file.getName(), reqFile);

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        Call<addVideoBean> call = cr.addVideo(b.userId , "" , "" , body);

        call.enqueue(new Callback<addVideoBean>() {
            @Override
            public void onResponse(Call<addVideoBean> call,retrofit2.Response<addVideoBean> response) {
                Toast.makeText(MainActivity.this , "Video Uploaded Successfully" , Toast.LENGTH_SHORT).show();
                videoProgress.setVisibility(View.GONE);
                Log.d("status" , response.body().getStatus());
                Log.d("message" , response.body().getMessage());
            }
            @Override
            public void onFailure(Call<addVideoBean> call, Throwable t) {
                videoProgress.setVisibility(View.GONE);
            }
        });
    }
    private String getPath(final Context context, final Uri uri)
    {
        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private synchronized void buildGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).enableAutoManage(this,this)
                .addApi(LocationServices.API).build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
