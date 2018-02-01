package com.youthlive.youthlive;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;
import com.youthlive.youthlive.Activitys.MessaageActivity;
import com.youthlive.youthlive.Activitys.PersonalInfo;
import com.youthlive.youthlive.ExchangeDiamondPOJO.ExchangeBean;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.addWalletPOJO.addWalletBean;
import com.youthlive.youthlive.allMessagePOJO.allMessageBean;
import com.youthlive.youthlive.followPOJO.followBean;
import com.youthlive.youthlive.walletPOJO.walletBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WalletActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText amount;
    Button add;
    TextView diamond;
    TextView diamonds , beans , coins;
    ProgressBar progress;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        amount = (EditText)findViewById(R.id.amount);
        add = (Button)findViewById(R.id.add);
        diamonds = (TextView)findViewById(R.id.diamonds);
        beans = (TextView)findViewById(R.id.beans);
        coins = (TextView)findViewById(R.id.coins);
        diamond = (TextView)findViewById(R.id.diamond);
        progress = (ProgressBar)findViewById(R.id.progress);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setTitle("Wallet");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        amount.setText("100");

        diamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog = new Dialog(WalletActivity.this);
                dialog.setContentView(R.layout.diamond);
                dialog.setCancelable(true);
                dialog.show();


                final EditText bean = dialog.findViewById(R.id.beans);
                Button submit = dialog.findViewById(R.id.submit);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progress.setVisibility(View.VISIBLE);

                        final bean b = (bean) getApplicationContext();

                        final Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.BASE_URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        final AllAPIs cr = retrofit.create(AllAPIs.class);


                        Call<ExchangeBean> call = cr.exchange(b.userId ,"",bean.getText().toString());

                        call.enqueue(new Callback<ExchangeBean>() {
                            @Override
                            public void onResponse(Call<ExchangeBean> call, Response<ExchangeBean> response) {


                                Toast.makeText(WalletActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                loadData();
                                dialog.dismiss();

                                progress.setVisibility(View.GONE);




                            }

                            @Override
                            public void onFailure(Call<ExchangeBean> call, Throwable t) {

                                progress.setVisibility(View.GONE);

                            }
                        });




                    }
                });







            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String am = amount.getText().toString();

                if (am.length() > 0)
                {


                    bean b = (bean)getApplicationContext();

                    Random random = new Random();

                    String key = "sL0p7U95";
                    String txnid = String.valueOf(random.nextInt(8));
                    String productinfo = "beans";
                    String firstname = b.userName;
                    String email = "test@gmail.com";
                    String udf1 = "";
                    String udf2 = "";
                    String udf3 = "";
                    String udf4 = "";
                    String udf5 = "";
                    String salt = "jPU0s1mj8V";





                    String hashSequence = key + "|" + txnid + "|" + am + "|" + productinfo + "|" + firstname + "|" + email + "|" + udf1 + "|" + udf2 + "|" + udf3 + "|" + udf4 + "|" + udf5 + "||||||" + salt;
                    String serverCalculatedHash= hashCal("SHA-512", hashSequence);



                    PayUmoneySdkInitializer.PaymentParam.Builder builder = new
                            PayUmoneySdkInitializer.PaymentParam.Builder();
                    builder.setAmount(Double.parseDouble(am))                          // Payment amount
                            .setTxnId(txnid)                                             // Transaction ID
                            .setPhone("9991262626")                                           // User Phone number
                            .setProductName("Beans")                   // Product Name or description
                            .setFirstName(firstname)                              // User First name
                            .setEmail(email)                                            // User Email ID
                            .setsUrl("https://test.payumoney.com/mobileapp/payumoney/success.php")                    // Success URL (surl)
                            .setfUrl("https://test.payumoney.com/mobileapp/payumoney/failure.php")                     //Failure URL (furl)
                            .setUdf1(udf1)
                            .setUdf2(udf2)
                            .setUdf3(udf3)
                            .setUdf4(udf4)
                            .setUdf5(udf5)
                            .setUdf6("")
                            .setUdf7("")
                            .setUdf8("")
                            .setUdf9("")
                            .setUdf10("")
                            .setIsDebug(true)                              // Integration environment - true (Debug)/ false(Production)
                            .setKey(key)                        // Merchant key
                            .setMerchantId("5979090");

                    PayUmoneySdkInitializer.PaymentParam paymentParam = builder.build();
//set the hash
                    paymentParam.setMerchantHash(serverCalculatedHash);

                    PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, WalletActivity.this, R.style.AppTheme, false);





                }


            }
        });



    }


    public static String hashCal(String type, String hashString) {
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }


    @Override
    protected void onResume() {
        super.onResume();

        loadData();

    }

    public void loadData() {
        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<walletBean> call = cr.getWalletData(b.userId);

        call.enqueue(new Callback<walletBean>() {
            @Override
            public void onResponse(Call<walletBean> call, Response<walletBean> response) {



                beans.setText(response.body().getData().getBeans());
                diamonds.setText(response.body().getData().getDiamond());
                coins.setText(response.body().getData().getCoin());


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<walletBean> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction


                    progress.setVisibility(View.VISIBLE);


                    bean b = (bean)getApplicationContext();


                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    Call<addWalletBean> call = cr.addBeans(b.userId , amount.getText().toString());

                    call.enqueue(new Callback<addWalletBean>() {
                        @Override
                        public void onResponse(Call<addWalletBean> call, Response<addWalletBean> response) {

                            Toast.makeText(WalletActivity.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                            loadData();

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<addWalletBean> call, Throwable t) {

                            progress.setVisibility(View.GONE);

                        }
                    });



                } else {
                    //Failure Transaction
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

                Log.d("response", payuResponse);


                /*try {
                    JSONObject obj = new JSONObject(payuResponse);

                    JSONObject res = obj.getJSONObject("result");

                    String tid = res.getString("txnid");
                    final String pid = res.getString("paymentId");

                    Log.d("tid", tid);

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://nationproducts.in/")
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllAPIs cr = retrofit.create(AllAPIs.class);
                    final bean b = (bean) getApplicationContext();

                    Call<successBean> call = cr.orderSuccess(b.userId, tid, "success");

                    call.enqueue(new Callback<successBean>() {
                        @Override
                        public void onResponse(Call<successBean> call, retrofit2.Response<successBean> response) {

                            String paymentId = pid;
                            //showDialogMessage("Payment Success Id : " + paymentId);

                            new AlertDialog.Builder(CheckOut.this)
                                    .setCancelable(false)
                                    .setMessage("Payment Success Id : " + paymentId)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                        }

                        @Override
                        public void onFailure(Call<successBean> call, Throwable throwable) {

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }*/


            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("Asdasd", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("asdasdasd", "Both objects are null!");
            }
        }

    }
}
