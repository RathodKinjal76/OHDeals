package com.example.kinjal.ohdeals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class Information extends NavigationDrawer {
    String vid;
    ListView infolist;
    ArrayList<ModelInfo> arrinfo, arrinform;
    TextView buy, tvBrand, tvDicnt, tvDiscount;
    ImageView image;
    String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_information);

        LinearLayout contentFrameLayout = (LinearLayout) findViewById(R.id.linearlayout); // Remember
        getLayoutInflater().inflate(R.layout.activity_information, contentFrameLayout);
        toolbar.setTitle("Information");
        buy = (TextView) findViewById(R.id.tvbuy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Information.this, BuyMembership.class);
                startActivity(intent);
            }
        });

        vid = getIntent().getExtras().getString("vendorid");

        new connectToInfo(vid).execute();
    }

    class connectToInfo extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        String response = "", img, vendorid, city, vendorname, shortdesc, citys, area, address, contactno;

        public connectToInfo(String vendor_id) {
            vendorid = vendor_id;
            Log.e("vendorid", vendorid);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(Information.this);
            dialog.setTitle("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try {
                URL url = new URL(
                        "http://ohdeals.com/mobileapp/viewmore.php?vid=" + vendorid);
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
            arrinfo = new ArrayList<ModelInfo>();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                Log.e("status", jsonObject.getString("status").toString());

                if (status.equals("success")) {
                    JSONObject jsonObject2 = jsonObject.getJSONObject("data");
            /*    vendorid = jsonObject2.getString("vendor_id").toString();
                Log.e("vendorid", jsonObject2.getString("vendor_id").toString());
                city = jsonObject2.getString("city").toString();
                Log.e("city", jsonObject2.getString("city").toString());
                vendorname = jsonObject2.getString("vendor_name").toString();
                Log.e("vendorname", jsonObject2.getString("vendor_name").toString());
                shortdesc = jsonObject2.getString("shortDescription").toString();
                Log.e("shortdesc", jsonObject2.getString("shortDescription").toString());
                ModelInfo modelInfo = new ModelInfo();
                modelInfo.setVendor_id(vendorid);
                modelInfo.setVendorname(vendorname);
                modelInfo.setImage(img);
                modelInfo.setShortdescription(shortdesc);
                arrinfo.add(modelInfo);*/

                    img = jsonObject2.getString("image").toString();
                    Log.e("img", jsonObject2.getString("image").toString());

                    tvBrand = (TextView) findViewById(R.id.tvBrand);
                    image = (ImageView) findViewById(R.id.imgcouponinfo);
                    tvDicnt = (TextView) findViewById(R.id.tvDiscnt);
                    tvDiscount = (TextView) findViewById(R.id.tvDiscount);
                    tvBrand.setText(jsonObject2.getString("vendor_name").toString());
                    tvDiscount.setText(jsonObject2.getString("shortDescription").toString());
                    Glide.with(Information.this).load("http://www.ohdeals.com/india/index.php/images/product/small/" + img).placeholder(R.drawable.ohdeals_fb)
                            .into(image);
                    JSONArray array = jsonObject2.getJSONArray("detail");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObjectdetail = array.getJSONObject(i);
                        citys = jsonObjectdetail.getString("city").toString();
                        Log.e("city", jsonObjectdetail.getString("city").toString());
                        area = jsonObjectdetail.getString("area").toString();
                        Log.e("area", jsonObjectdetail.getString("area").toString());
                        address = jsonObjectdetail.getString("address").toString();
                        Log.e("address", jsonObjectdetail.getString("address").toString());
                        contactno = jsonObjectdetail.getString("contact_number").toString();
                        Log.e("contactno", jsonObjectdetail.getString("contact_number").toString());
                        ModelInfo modelInfo = new ModelInfo();
                        modelInfo.setCity(citys);
                        modelInfo.setArea(area);
                        modelInfo.setAddress(address);
                        modelInfo.setContact_number(contactno);
                        arrinfo.add(modelInfo);
                    }
                    Log.e("arrinfo", arrinfo + "");
                    infolist = (ListView) findViewById(R.id.couponinfolist);
                    ada obj = new ada(Information.this);
                    infolist.setAdapter(obj);

                } else {
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(getApplicationContext(), msg + "!", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    class ada extends BaseAdapter {
        Context context;
        LayoutInflater inflater;

        class viewholder {
            TextView tv1, tvCity, tv2, tvArea, tv3, tvAddress, tv4, tvContact;
        }

        public ada(Context mycontext) {
            // TODO Auto-generated constructor stub
            context = mycontext;
            inflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrinfo.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int arg0, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            viewholder holder;
            if (arg1 == null) {
                arg1 = inflater.inflate(R.layout.activity_info_row, null);
                holder = new viewholder();
                //holder.tv1 = (TextView) arg1.findViewById(R.id.tv1);
                holder.tvCity = (TextView) arg1.findViewById(R.id.tvCity);
                //holder.tv2 = (TextView) arg1.findViewById(R.id.tv2);
                holder.tvArea = (TextView) arg1.findViewById(R.id.tvArea);
                //holder.tv3 = (TextView) arg1.findViewById(R.id.tv3);
                holder.tvAddress = (TextView) arg1.findViewById(R.id.tvAddress);
                //holder.tv4 = (TextView) arg1.findViewById(R.id.tv4);
                holder.tvContact = (TextView) arg1.findViewById(R.id.tvContNo);
                arg1.setTag(holder);
            } else {
                holder = (viewholder) arg1.getTag();
            }
            ModelInfo modelInfo1 = arrinfo.get(arg0);
            //holder.tv1.setText("City :");
            holder.tvCity.setText("City :\n" + modelInfo1.getCity());
            //holder.tv2.setText("Area :");
            holder.tvArea.setText("Area :\n" + modelInfo1.getArea());
            //holder.tv3.setText("Address :");
            holder.tvAddress.setText("Address :\n"+modelInfo1.getAddress());
            //holder.tv4.setText("Contact Number :");
            holder.tvContact.setText("Contact Number :\n" +modelInfo1.getContact_number());
            return arg1;
        }
    }

}