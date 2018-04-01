package com.example.kinjal.ohdeals;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MembershipForm extends NavigationDrawer {

    Button submit, cancel, apply;
    ImageView appLogo;
    SharedPreferences preferences;
    EditText edtpromo;
    TextInputLayout fullname, address, city, pincode, email, mobile, membershipId;
    String strname, straddress, strcity, strpincode, stremail, strmobile, strmembershipId, strbirthdate, strpromocode;
    TextView dob, adddob, promocode, addpromocode;
    static final int DATE_DIALOG_ID = 0;
    // variables to save user selected date and time
    public int year, month, day, hour, minute;
    // declare the variables to set initial values
    private int mYear, mMonth, mDay, mHour, mMinute;
    Calendar c;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    // constructor
    public MembershipForm() {
        // Assign current Date and Time Values to Variables
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_membership_form);
        LinearLayout contentFrameLayout = (LinearLayout) findViewById(R.id.linearlayout); // Remember
        getLayoutInflater().inflate(R.layout.activity_membership_form, contentFrameLayout);
        toolbar.setTitle("Membership Form");

        preferences = getSharedPreferences("RegisterPref", 0);

        appLogo = (ImageView) findViewById(R.id.ohdealslogo);

        fullname = (TextInputLayout) findViewById(R.id.txtinputFullname);
        address = (TextInputLayout) findViewById(R.id.txtinputAddress);
        city = (TextInputLayout) findViewById(R.id.txtinputCity);
        pincode = (TextInputLayout) findViewById(R.id.txtinputPin);
        email = (TextInputLayout) findViewById(R.id.txtinputEmail);
        mobile = (TextInputLayout) findViewById(R.id.txtinputMob);
        membershipId = (TextInputLayout) findViewById(R.id.txtinputMemberId);

        dob = (TextView) findViewById(R.id.tvDob);
        adddob = (TextView) findViewById(R.id.tvadddob);
        adddob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.set(1900, 1, 01, 0, 0, 0);
                long mintime = cal.getTimeInMillis();

                final DatePickerDialog datePicker = new DatePickerDialog(
                        MembershipForm.this,
                        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int yr, int mn, int dy) {
                                // TODO Auto-generated method stub
                                view.updateDate(yr, mn, dy);
                                adddob.setText(dy + "/" + (mn + 1) + "/" + yr);
                            }
                        }, mYear, mMonth, mDay);
                datePicker.getDatePicker().setMinDate(mintime);
                datePicker.show();
            }
        });

        promocode = (TextView) findViewById(R.id.tvpromo);
        promocode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(MembershipForm.this);
                dialog.setContentView(R.layout.have_promocode);
                dialog.setTitle("Add Promo Code");
                addpromocode = (TextView) dialog.findViewById(R.id.tvDob);
                edtpromo = (EditText) dialog.findViewById(R.id.edtPromo);
                cancel = (Button) dialog.findViewById(R.id.btncancel);
                apply = (Button) dialog.findViewById(R.id.btnapply);

                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strpromocode = edtpromo.getText().toString();
                        if (strpromocode.isEmpty())
                            edtpromo.setError("Please Enter Your Reference Code!!");
                        else {
                            new connectToPromoCode(strpromocode).execute();
                            if (preferences.getString("PromoStatus", "").equals("success")) {
                                promocode.setText("Promo Code Applied");
                                dialog.cancel();
                            }
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

        submit = (Button) findViewById(R.id.btnsubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strname = fullname.getEditText().getText().toString();
                straddress = address.getEditText().getText().toString();
                strcity = city.getEditText().getText().toString();
                strpincode = pincode.getEditText().getText().toString();
                stremail = email.getEditText().getText().toString();
                strmobile = mobile.getEditText().getText().toString();
                strmembershipId = membershipId.getEditText().getText().toString();
                strbirthdate = adddob.getText().toString();

                final EditText edtname = (EditText) findViewById(R.id.edtFullname);
                edtname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (v.getText().toString().equals("")) {
                            edtname.setError("Please Enter Valid Full Name");
                        }
                        return false;
                    }
                });

               /* final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher;
                */

                /*if (strname.isEmpty()) {
                    fullname.setError("Please Enter Valid Full Name");
                } else {
                    fullname.setErrorEnabled(false);
                }
                if (straddress.isEmpty()) {
                    address.setError("Please Enter Valid Address");
                }
                if (strcity.isEmpty()) {
                    city.setError("Please Enter Valid City");
                }
                if (strpincode.isEmpty()) {
                    pincode.setError("Please Enter Valid Pincode");
                }
                if (stremail.isEmpty()) {
                    email.setError("Please Enter Valid Email");
                }
                if (strmobile.isEmpty()) {
                    mobile.setError("Please Enter Valid Mobile Number");
                }
                if (strmembershipId.isEmpty()) {
                    membershipId.setError("Please Enter Valid Membership Id");
                }*/

                if (strname.isEmpty() || straddress.isEmpty() || strcity.isEmpty() || strpincode.isEmpty() || stremail.isEmpty() ||
                        strmobile.isEmpty() || strmembershipId.isEmpty() || strbirthdate.isEmpty()) {
                    fullname.setError("Please Enter Valid Full Name");
                    address.setError("Please Enter Valid Address");
                    city.setError("Please Enter Valid City");
                    pincode.setError("Please Enter Valid Pincode");
                    email.setError("Please Enter Valid Email");
                    mobile.setError("Please Enter Valid Mobile Number");
                    membershipId.setError("Please Enter Valid Membership Id");

                }
                else
                {
                    DatabaseHandler databaseHandler = new DatabaseHandler(MembershipForm.this);
                    databaseHandler.registerUser(strname, straddress, strcity, strpincode, strmobile, stremail, strmembershipId,
                            strbirthdate, strpromocode);
                    Toast.makeText(getApplicationContext(), "You are registered successfully !!", Toast.LENGTH_LONG).show();

                    ModelRegister modelRegister = new ModelRegister();
                    modelRegister.setFullname(strname);
                    modelRegister.setAddress(straddress);
                    modelRegister.setCity(strcity);
                    modelRegister.setPinCode(strpincode);
                    modelRegister.setMobile_No(strmobile);
                    modelRegister.setEmail(stremail);
                    modelRegister.setMembershipId(strmembershipId);
                    modelRegister.setBirthDate(strbirthdate);
                    modelRegister.setPromoCode(strpromocode);

                    //new connectToRegister(email, pass).execute();

                    /*address.setErrorEnabled(false);
                    city.setErrorEnabled(false);
                    pincode.setErrorEnabled(false);
                    //matcher = pattern.matcher(email.getEditText().getText().toString());
                    email.setErrorEnabled(false);
                    mobile.setErrorEnabled(false);
                    membershipId.setErrorEnabled(false);*/
                }
                Intent promointent= new Intent(MembershipForm.this,Login.class);
                startActivity(promointent);
                /* preferences = getSharedPreferences("RegisterPref", 0);
                islogin = preferences.getString("RegisterStatus", "");
                Log.e("RegisterPref", islogin);
                if (islogin.equals("success")) {
                    Toast.makeText(getApplicationContext(), "You are registered successfully !!", Toast.LENGTH_LONG).show();
                }*/
            }
        });

    }

    class connectToPromoCode extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        String response = "", id, price, msg, promocode;

        public connectToPromoCode(String promocodes) {
            promocode = promocodes;
            Log.e("PromoCode", promocode);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(MembershipForm.this);
            dialog.setTitle("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            Log.e("Promocode", promocode);

            try {
                URL url = new URL(
                        "http://ohdeals.com/mobileapp/promo_code.php?promo_code=" + promocode);
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
            Log.e("DATA", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String promostatus = jsonObject.getString("status");
                Log.e("promostatus", jsonObject.getString("status"));

                preferences = getSharedPreferences("RegisterPref", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("PromoStatus", promostatus);
                editor.commit();

                if (promostatus.equals("success")) {
                    JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
                    id = jsonObjectdata.getString("id").toString();
                    Log.e("id", jsonObjectdata.getString("id").toString());
                    price = jsonObjectdata.getString("price").toString();
                    Log.e("price", jsonObjectdata.getString("price").toString());
                    msg = jsonObjectdata.getString("message").toString();
                    Log.e("msg", jsonObjectdata.getString("message").toString());

                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), msg + "!!", Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*class connectToRegister extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        String response = "", email, pass, id, name, payment, msg;

        public connectToRegister(String emails, String passwords) {
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
            dialog = new ProgressDialog(MembershipForm.this);
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

                if (loginstatus.equals("success")) {
                    JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
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

                    Intent intent = new Intent(MembershipForm.this, MainActivity.class);
                    intent.putExtra("LoginStatus", loginstatus);
                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), msg + "!!", Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/

}