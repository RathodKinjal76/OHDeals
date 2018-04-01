package com.example.kinjal.ohdeals;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ChangePassword extends NavigationDrawer {
    EditText edtCurPasswrd, edtNewPasswrd, edtConfirmPass;
    Button changepass;
    String curPass, newPass, matchPass, loginId;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_change_password);
        preferences = getSharedPreferences("LoginPref", 0);
        loginId = preferences.getString("LoginId", "");
        Log.e("LoginId", loginId);

        LinearLayout contentFrameLayout = (LinearLayout) findViewById(R.id.linearlayout); // Remember
        getLayoutInflater().inflate(R.layout.activity_change_password, contentFrameLayout);
        toolbar.setTitle("Change Password");

        edtCurPasswrd = (EditText) findViewById(R.id.edtCurPass);
        edtNewPasswrd = (EditText) findViewById(R.id.edtNewPass);
        edtConfirmPass = (EditText) findViewById(R.id.edtMatchPass);
        changepass = (Button) findViewById(R.id.changepassword);
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curPass = edtCurPasswrd.getText().toString();
                newPass = edtNewPasswrd.getText().toString();
                matchPass = edtConfirmPass.getText().toString();
                if (curPass.isEmpty() || newPass.isEmpty() || matchPass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Details !!", Toast.LENGTH_LONG).show();
                } else {
                    new connectToChangePassword(curPass, newPass, matchPass, loginId).execute();
                }
            }
        });
    }

    class connectToChangePassword extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        String response = "", status, msg, oldpass, newpass, confrmpass, id;

        public connectToChangePassword(String CurPass, String NewPass, String MatchPass, String ids) {
            oldpass = CurPass;
            newpass = NewPass;
            confrmpass = MatchPass;
            id = ids;
            Log.e("oldpass", oldpass);
            Log.e("newpass", newpass);
            Log.e("confrmpass", confrmpass);
            Log.e("id", id);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(ChangePassword.this);
            dialog.setTitle("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            try {
                URL url = new URL("http://ohdeals.com/mobileapp/change_password.php?oldpass=" + oldpass + "&newpass=" + newpass +
                        "&conpass=" + confrmpass + "&id=" + id);
                Log.e("url", url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();
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
            Log.e("DATA", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String ChangePassstatus = jsonObject.getString("status");
                JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
                msg = jsonObjectdata.getString("message").toString();
                Log.e("msg", msg);
                Toast.makeText(getApplicationContext(), msg + " !!", Toast.LENGTH_SHORT).show();

                /*if (ChangePassstatus.equals("success")) {
                    Log.e("msg", jsonObjectdata.getString("message").toString());
                    Toast.makeText(getApplicationContext(), msg + " !!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), msg + " !!", Toast.LENGTH_SHORT).show();*/

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}