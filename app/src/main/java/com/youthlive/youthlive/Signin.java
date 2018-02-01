package com.youthlive.youthlive;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.youthlive.youthlive.Activitys.UserInformation;
import com.youthlive.youthlive.DBHandler.SessionManager;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.login2POJO.login2Bean;
import com.youthlive.youthlive.loginResponsePOJO.loginResponseBean;
import com.youthlive.youthlive.socialPOJO.socialBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Signin extends AppCompatActivity {
    EditText phone_number, password_login, phoneno;
    Button Login;
    SessionManager session;
    private ProgressDialog pDialog;
    CallbackManager mCallbackManager;
    public static final String MyPREFERENCES = "userSession";
    private static final String BASEELOGIN_URL = "http://nationproducts.in/youthlive/api/mobile_signin.php";
    private static final String KEY_NUM = "number";
    private static final String BASEOTP_URL = "http://nationproducts.in/youthlive/api/sign_up.php";
    private static final String KEY_PASS = "passs";
    String User_number, User_passwords, Counter_code, Phonenumber = "", verificationCode, str, userId, pass1 = "";
    AlertDialog.Builder builder;
    Button login;
    LinearLayout cancel_layout, cancel_ok;
    ProgressBar progress;

    ImageView facebook_login, googleLogin , twitter_login;

    private int RC_SIGN_IN = 22;
    GoogleSignInClient mGoogleSignInClient;


    SharedPreferences pref;
    SharedPreferences.Editor edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();
        // FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());
                                        Log.d("LoginJSONActivity", object.toString());
                                        try {
                                            JSONObject json = response.getJSONObject();
                                            final String name = json.getString("name");
                                            final String email = json.getString("email");
                                            final String id = json.getString("id");
                                            Log.d("FACEBOOKNAME", name + " Email > " + email);
                                            Log.d("FACEBOOKNAME", name + " Email > " + id);
                                            //   String profilePicUrl = json.getJSONObject("picture").getJSONObject("data").getString("url");
                                            String image = "https://graph.facebook.com/" + id + "/picture?type=large";



                                            progress.setVisibility(View.VISIBLE);

                                            final bean b = (bean) getApplicationContext();

                                            final Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl(b.BASE_URL)
                                                    .addConverterFactory(ScalarsConverterFactory.create())
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();

                                            final AllAPIs cr = retrofit.create(AllAPIs.class);


                                            Call<socialBean> call = cr.socialSignIn(id , email);

                                            call.enqueue(new Callback<socialBean>() {
                                                @Override
                                                public void onResponse(Call<socialBean> call, retrofit2.Response<socialBean> response) {

                                                    if (response.body().getData().getUserName().length() > 0)
                                                    {

                                                        b.userId = response.body().getData().getUserId();
                                                        b.userName = response.body().getData().getUserName();

                                                        try {
                                                            b.userImage = response.body().getData().getUserImage();
                                                        }catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }


                                                        edit.putString("type" , "social");
                                                        edit.putString("user" , email);
                                                        edit.putString("pass" , id);
                                                        edit.commit();

                                                        Toast.makeText(Signin.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Signin.this , HomeActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);

                                                    }
                                                    else
                                                    {

                                                        b.userId = response.body().getData().getUserId();
                                                        b.userName = response.body().getData().getUserName();

                                                        try {
                                                            b.userImage = response.body().getData().getUserImage();
                                                        }catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }


                                                        edit.putString("type" , "social");
                                                        edit.putString("user" , email);
                                                        edit.putString("pass" , id);
                                                        edit.commit();

                                                        Toast.makeText(Signin.this , "Please update your info" , Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Signin.this, UserInformation.class);
                                                        intent.putExtra("userId" , response.body().getData().getUserId());
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);


                                                    }


                                                    progress.setVisibility(View.GONE);


                                                }

                                                @Override
                                                public void onFailure(Call<socialBean> call, Throwable t) {
                                                    progress.setVisibility(View.GONE);
                                                }
                                            });



                                            //new FacebookloginAsyncTask().execute(email);
                                        } catch (Exception e) {

                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(Signin.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(Signin.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        pref = getSharedPreferences("pref" , Context.MODE_PRIVATE);
        edit = pref.edit();

        builder = new AlertDialog.Builder(Signin.this);
        phone_number = findViewById(R.id.phone_number);
        password_login = findViewById(R.id.password_login);
        login = (Button) findViewById(R.id.login);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        session = new SessionManager(getApplicationContext());
        SharedPreferences settings = getSharedPreferences("mypref", MODE_PRIVATE);
        userId = settings.getString("userid", "");
        pass1 = settings.getString("password", "");

        progress = (ProgressBar) findViewById(R.id.progress);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        googleLogin = findViewById(R.id.ggleLogin);
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });
        facebook_login = (ImageView) findViewById(R.id.facebook_login);
        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(Signin.this, Arrays.asList("public_profile", "email", "user_birthday"));
            }
        });

        twitter_login = (ImageView)findViewById(R.id.twitter);
        twitter_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login();

            }
        });
    }

    public void showCustomDialog(View view) {
        forgotdialog();
    }

    private void forgotdialog() {
        final Dialog dialog = new Dialog(Signin.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.forgote_customedialog);
        cancel_layout = dialog.findViewById(R.id.cancel_layout);
        phoneno = (EditText) dialog.findViewById(R.id.txtphoneno);
        cancel_ok = dialog.findViewById(R.id.txtok);
        cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneno.getText().toString().equals("")) {
                    Toast.makeText(Signin.this, "enter phone number", Toast.LENGTH_SHORT).show();
                }
                forgotPassword();
            }
        });
    }


    public void login() {

        final String phone = phone_number.getText().toString();
        final String password = password_login.getText().toString();

        if (phone.length() > 0) {

            if (password.length() > 0) {


                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<login2Bean> call = cr.signIn(phone , password);

                call.enqueue(new Callback<login2Bean>() {
                    @Override
                    public void onResponse(Call<login2Bean> call, retrofit2.Response<login2Bean> response) {



                        if (Objects.equals(response.body().getStatus(), "1"))
                        {
                            Toast.makeText(Signin.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                            b.userId = response.body().getData().getUserId();
                            b.userName = response.body().getData().getUserName();

                            try {
                                b.userImage = response.body().getData().getUserImage();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            edit.putString("type" , "phone");
                            edit.putString("user" , phone);
                            edit.putString("pass" , password);
                            edit.commit();

                            Intent Inbt = new Intent(Signin.this, HomeActivity.class);
                            Inbt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(Inbt);

                        }
                        else
                        {
                            Toast.makeText(Signin.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                        }






                        progress.setVisibility(View.GONE);
                    }
                    @Override
                    public void onFailure(Call<login2Bean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });

            } else {
                password_login.setError("Invalid Password");
            }

        } else {
            phone_number.setError("Invalid Phone");
        }
    }


    private void userloginn() {

        User_number = phone_number.getText().toString().trim();
        User_passwords = password_login.getText().toString().trim();
        if (User_number.equals("") || User_passwords.equals("")) {
            builder.setTitle("Somethimg went wrong..");
            builder.setMessage("Please fill all the fields...");
            Displayalart("input_error");
        } else {
            showpDialog();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BASEELOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        String status = jObj.getString("status");
                        if (!status.equals(0)) {
                            JSONObject obj2 = jObj.getJSONObject("data");
                            userId = obj2.getString("userId");
                            // str = jObj.getString("message");
                            Phonenumber = obj2.getString("phone");
                            User_passwords = obj2.getString("password");
                            Intent Inbt = new Intent(Signin.this, HomeActivity.class);
                            startActivity(Inbt);
                            SharedPreferences sharedpreferences = Signin.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("userid", userId);
                            editor.commit();
                            session.createLoginSession(Signin.this, userId);

                        } else {
                            str = jObj.getString("message");
                            Toast.makeText(Signin.this, str, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    hidepDialog();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Signin.this, error.toString(), Toast.LENGTH_SHORT).show();
                }

            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("phone", User_number);
                    params.put("password", pass1);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void forgotPassword() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://nationproducts.in/youthlive/api/sign_up.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Log.d(TAG, response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    if (!status.equals("0")) {
                        JSONObject user = jObj.getJSONObject("data");
                        Counter_code = user.getString("countryCode");
                        Phonenumber = user.getString("phone");
                        verificationCode = user.getString("verificationCode");
                        str = jObj.getString("message");
                        userId = user.getString("userId");
                        SharedPreferences settings = getSharedPreferences("mypref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("userid", userId);
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), OTP.class);
                        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(Phonenumber, null, verificationCode, pi, null);
                    } else {
                        str = jObj.getString("message");
                        Toast.makeText(Signin.this, str, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Signin.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("countryCode", Counter_code);
                params.put("phone", Phonenumber);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void OTP() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASEOTP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.d(TAG, response.toString());
                Intent intent = new Intent(Signin.this, Signin.class);
                intent.putExtra("userId", userId);
                intent.putExtra("phone", Phonenumber);
                intent.putExtra("countryCode", Counter_code);
                startActivity(intent);
                StringRequest sr = new StringRequest(Request.Method.POST, "http://nationproducts.in/youthlive/api/sign_up.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("countryCode", Counter_code);
                        params.put("phone", Phonenumber);
                        return params;
                    }
                };
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Signin.this, error.toString(), Toast.LENGTH_SHORT).show();
                //hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", Phonenumber);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void Displayalart(final String code) {
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (code.equals("input_error")) {
                    phone_number.setText("");
                    password_login.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            progress.setVisibility(View.VISIBLE);

            final bean b = (bean) getApplicationContext();
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final AllAPIs cr = retrofit.create(AllAPIs.class);


            Call<socialBean> call = cr.socialSignIn(account.getId() , account.getEmail());

            call.enqueue(new Callback<socialBean>() {
                @Override
                public void onResponse(Call<socialBean> call, retrofit2.Response<socialBean> response) {

                    if (response.body().getData().getUserName().length() > 0)
                    {

                        b.userId = response.body().getData().getUserId();
                        b.userName = response.body().getData().getUserName();

                        try {
                            b.userImage = response.body().getData().getUserImage();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                        edit.putString("type" , "social");
                        edit.putString("user" , account.getEmail());
                        edit.putString("pass" , account.getId());
                        edit.commit();

                        Toast.makeText(Signin.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Signin.this , HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);



                    }
                    else
                    {

                        b.userId = response.body().getData().getUserId();
                        b.userName = response.body().getData().getUserName();

                        try {
                            b.userImage = response.body().getData().getUserImage();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                        edit.putString("type" , "social");
                        edit.putString("user" , account.getEmail());
                        edit.putString("pass" , account.getId());
                        edit.commit();

                        Toast.makeText(Signin.this , "Please update your info" , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Signin.this, UserInformation.class);
                        intent.putExtra("userId" , response.body().getData().getUserId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                    progress.setVisibility(View.GONE);
                }
                @Override
                public void onFailure(Call<socialBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("asdas", "signInResult:failed code=" + e.getStatusCode());

        }
    }

}
