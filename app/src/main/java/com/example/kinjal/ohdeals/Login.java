package com.example.kinjal.ohdeals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.POST;

public class Login extends NavigationDrawer {

    Button login;
    TextView forgetpass, register, havememberid;
    EditText etemail, etpass;
    String email, pass, answer;
    ArrayList<ModelLogin> loginarr;
    ImageView loadAd;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);

        LinearLayout contentFrameLayout = (LinearLayout) findViewById(R.id.linearlayout); // Remember
        getLayoutInflater().inflate(R.layout.activity_login, contentFrameLayout);
        toolbar.setTitle("Log in");

        preferences = getSharedPreferences("LoginPref", 0);

        etemail = (EditText) findViewById(R.id.edtemail);
        etpass = (EditText) findViewById(R.id.edtpassword);
        login = (Button) findViewById(R.id.btnlogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etemail.getText().toString();
                Log.e("mail", email);
                pass = etpass.getText().toString();
                Log.e("pass", pass);
                //check_login(email,pass);
                new connectToLogin(email, pass).execute();

            }
        });

        forgetpass = (TextView) findViewById(R.id.tvforgotpass);
        register = (TextView) findViewById(R.id.tvreg);
        havememberid = (TextView) findViewById(R.id.tvmember);

        loadAd = (ImageView) findViewById(R.id.imgAd);
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgetPassword.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, BuyMembership.class);
                startActivity(intent);
            }
        });

        havememberid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MembershipIdLogin.class);
                startActivity(intent);
            }
        });
        Glide.with(Login.this).load("http://ohdeals.com/mobileapp/uploads/banner.png").placeholder(R.drawable.ohdeals_fb)
                .into(loadAd);
    }

    /*void check_login(String email,String pass) {
        HttpApi Api = HttpApi.getInstance();
        Call<ModelLogin> call = Api.getService().postlogindata(email, pass);

        call.enqueue(new Callback<ModelLogin>() {

            @Override
            public void onResponse(final Response<ModelLogin> resp, Retrofit arg1) {
                Log.e("response", resp + "");

                if (!resp.isSuccess()) {
                    try {
                        System.out.println(resp.errorBody().toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if (resp.body().getCod().equals("200")) {
                    Log.e("code",resp.body().getCod());

                    for(int i=0;i<resp.body().getList().size();i++){
                        Main main=new Main();
                        main.setTemp(resp.body().getList().get(i).getMain().getTemp());
                        main.setTemp_min(resp.body().getList().get(i).getMain().getTemp_min());
                        main.setTemp_max(resp.body().getList().get(i).getMain().getTemp_max());
                        arrmain.add(main);
                    }
                    Log.e("Arraymain",arrmain+"");
                }

            }
            @Override
            public void onFailure(Call<ModelLogin> call, Throwable t) {

            }

        });

    }*/

    class connectToLogin extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        String response = "", email, pass, id, name, payment, msg;

        public connectToLogin(String emails, String passwords) {
            email = emails;
            pass = passwords;
            Log.e("emails", email);
            pass = etpass.getText().toString();
            Log.e("passwords", pass);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(Login.this);
            dialog.setTitle("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            Log.e("vdimail", email);
            pass = etpass.getText().toString();
            Log.e("vdipass", pass);
            try {
                URL url = new URL(
                        "http://ohdeals.com/mobileapp/login.php?email=" + email + "&password=" + pass);
                Log.e("url", url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();
                //urlConnection.setRequestMethod("POST");
                try {
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(
                                    urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    response = stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();
            loginarr = new ArrayList<ModelLogin>();
            Log.e("DATA", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String loginstatus = jsonObject.getString("status");
                Log.e("loginstatus", jsonObject.getString("status"));

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("LoginStatus", loginstatus);
                editor.commit();
                Log.e("TestLoginPref", preferences.getString("LoginStatus", ""));
                JSONObject jsonObjectdata = jsonObject.getJSONObject("data");

                if (loginstatus.equals("success")) {
                    id = jsonObjectdata.getString("id").toString();
                    Log.e("id", jsonObjectdata.getString("id").toString());
                    editor.putString("LoginId", id);
                    editor.commit();
                    name = jsonObjectdata.getString("name").toString();
                    Log.e("name", jsonObjectdata.getString("name").toString());
                    payment = jsonObjectdata.getString("payment").toString();
                    Log.e("payment", jsonObjectdata.getString("payment").toString());
                    msg = jsonObjectdata.getString("msg").toString();
                    Log.e("msg", jsonObjectdata.getString("msg").toString());
                    ModelLogin modelLogin = new ModelLogin();
                    modelLogin.setId(id);
                    modelLogin.setName(name);
                    modelLogin.setPayment(payment);
                    modelLogin.setMsg(msg);
                    loginarr.add(modelLogin);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("LoginStatus", loginstatus);
                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), jsonObjectdata.getString("msg").toString() + "!!", Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}