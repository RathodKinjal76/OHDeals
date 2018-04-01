package com.example.kinjal.ohdeals;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Search extends NavigationDrawer {

    Spinner spnrcity, spnrarea, spnrcatgry;
    EditText edtbrand;
    Button srch;
    TextView buy;
    ArrayList<String> cityarray = new ArrayList<>();
    ArrayList<String> areaarray = new ArrayList<>();
    ArrayList<String> catarray = new ArrayList<>();
    ArrayAdapter<String> citadapter, areaadapter, catadapter;
    String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_search);

        LinearLayout contentFrameLayout = (LinearLayout) findViewById(R.id.linearlayout); // Remember
        getLayoutInflater().inflate(R.layout.activity_search, contentFrameLayout);
        toolbar.setTitle("Search");
        toolbar.setTitleTextColor(Color.WHITE);

        new connectToCity().execute();
        new connectToCategory().execute();

        spnrcity = (Spinner) findViewById(R.id.cityspinner);
        spnrarea = (Spinner) findViewById(R.id.areaspinner);
        spnrcatgry = (Spinner) findViewById(R.id.catgoryspinner);

    }

    class connectToCity extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        String response = "", citynames, cityid, categorynames;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(Search.this);
            dialog.setTitle("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try {
                URL url = new URL(
                        "http://ohdeals.com/mobileapp/city.php");
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
            cityarray.add("Select City");
            try {
                JSONObject jsonObject = new JSONObject(response);
                final JSONArray array = jsonObject.getJSONArray("data");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject2 = array.getJSONObject(i);
                    citynames = jsonObject2.getString("name").toString();
                    Log.e("Cityname", jsonObject2.getString("name").toString());
                    cityid = jsonObject2.getString("id").toString();
                    Log.e("cityid", jsonObject2.getString("id").toString());
                    cityarray.add(citynames);
                    /*ModelCity modelCity = new ModelCity();
                    modelCity.setCityid(cityid);
                    modelCity.setCityname(citynames);*/
                }

                citadapter = new ArrayAdapter<String>
                        (Search.this, android.R.layout.simple_spinner_dropdown_item, cityarray);

                spnrcity.setAdapter(citadapter);
                spnrcity.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                       int arg2, long arg3) {
                                if (arg2 != 0) {
                                    try {
                                        JSONObject jsonObject2 = array.getJSONObject(arg2 - 1);
                                        Log.e("jsonObject2", arg2 + "");
                                        cityid = jsonObject2.getString("id").toString();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                    Toast.makeText(getApplicationContext(), "You have selected " + cityarray.get(arg2) + " " + cityid, Toast.LENGTH_LONG).show();
                                    new connectToArea(cityid).execute();
                                } else {
                                    spnrarea.setAdapter(areaadapter);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub
                            }
                        });

                srch = (Button) findViewById(R.id.btsrch);
                srch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Search.this, SearchResult.class);
                        startActivity(intent);
                    }
                });

                buy = (TextView) findViewById(R.id.tvbuy);
                buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Search.this, BuyMembership.class);
                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    class connectToArea extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        String response = "", areaid, areaname, cityid, msg;

        public connectToArea(String cityids) {
            cityid = cityids;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(Search.this);
            dialog.setTitle("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try {
                URL url = new URL(
                        "http://ohdeals.com/mobileapp/area.php?id=" + cityid);
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
            areaarray = new ArrayList<String>();
            Log.e("DATA", response);
            areaarray.add("Select Area");
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                Log.e("status", jsonObject.getString("status").toString());
                if (status.equals("success")) {

                    JSONArray array = jsonObject.getJSONArray("data");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject2 = array.getJSONObject(i);
                        areaid = jsonObject2.getString("cityid").toString();
                        Log.e("cityid", jsonObject2.getString("cityid").toString());
                        areaname = jsonObject2.getString("name").toString();
                        Log.e("Areaname", jsonObject2.getString("name").toString());
                        ModelArea modelArea = new ModelArea();
                        modelArea.setAreaid(areaid);
                        modelArea.setAreaname(areaname);
                        areaarray.add(areaname);
                    }

                    areaadapter = new ArrayAdapter<String>(
                            Search.this, android.R.layout.simple_spinner_dropdown_item, areaarray);

                    spnrarea.setAdapter(areaadapter);
                    spnrarea.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                           int arg2, long arg3) {
                                    Toast.makeText(getApplicationContext(), "You have selected " + areaarray.get(arg2), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub
                                }
                            });
                    edtbrand = (EditText) findViewById(R.id.edtBrand);
                } else {
                    JSONObject jsonObject3 = jsonObject.getJSONObject("data");
                    msg = jsonObject3.getString("msg");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    class connectToCategory extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        String response = "", catgrynames, categoryimage;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(Search.this);
            dialog.setTitle("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try {
                URL url = new URL(
                        "http://ohdeals.com/mobileapp/category_list.php");
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
            catarray = new ArrayList<String>();
            Log.e("DATA", response);
            catarray.add("Select Category");

            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONArray array = jsonObject.getJSONArray("data");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject2 = array.getJSONObject(i);
                    catgrynames = jsonObject2.getString("category_name").toString();
                    Log.e("category_name", jsonObject2.getString("category_name").toString());
                    catarray.add(catgrynames);
                }

                catadapter = new ArrayAdapter<String>(
                        Search.this, android.R.layout.simple_spinner_dropdown_item, catarray);
                spnrcatgry.setAdapter(catadapter);
                spnrcatgry.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                       int arg2, long arg3) {
                                //int position = spnrcatgry.getSelectedItemPosition();
                                Toast.makeText(getApplicationContext(), "You have selected " + catarray.get(arg2), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub
                            }
                        });
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}