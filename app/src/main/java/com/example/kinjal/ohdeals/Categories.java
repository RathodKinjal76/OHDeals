package com.example.kinjal.ohdeals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class Categories extends NavigationDrawer {
    TextView buy, tvBrand;
    LinearLayout contentFrameLayout;
    String CatId, CatName;
    ListView searchcatlist;
    ArrayList<ModelSearch> arrsearchcat;
    String answer, NearestOffer;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_nearest_offers);
        contentFrameLayout = (LinearLayout) findViewById(R.id.linearlayout);
        getLayoutInflater().inflate(R.layout.activity_nearest_offers, contentFrameLayout);
        toolbar.setTitle("Categories");

        preferences = getSharedPreferences("NearestOfferPref", 0);
        NearestOffer = preferences.getString("NearestOffersStatus", "");

        CatId = getIntent().getExtras().getString("catid");
        CatName = getIntent().getExtras().getString("catname");

        if (NearestOffer.equals("success")) {
            Intent intent = new Intent(Categories.this, NearestOffers.class);
            intent.putExtra("NearCatId", CatId);
            startActivity(intent);
        }

        buy = (TextView) findViewById(R.id.tvbuy);
        searchcatlist = (ListView) findViewById(R.id.nearofferlist);
        tvBrand = (TextView) findViewById(R.id.tvnearestofr);
        tvBrand.setText(CatName);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Categories.this, BuyMembership.class);
                startActivity(intent);
            }
        });
        new connectToSearchCategories(CatId).execute();
    }

    class connectToSearchCategories extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        String response = "", id, img, vendorid, vendorname, shortdesc, msg;

        public connectToSearchCategories(String ids) {
            id = ids;
            Log.e("id", id);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(Categories.this);
            dialog.setTitle("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try {
                URL url = new URL(
                        "http://ohdeals.com/mobileapp/product.php?id=" + id);
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
            arrsearchcat = new ArrayList<ModelSearch>();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                Log.e("status", jsonObject.getString("status").toString());

                if (status.equals("success")) {
                    JSONArray array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject2 = array.getJSONObject(i);
                        vendorid = jsonObject2.getString("id").toString();
                        Log.e("vendorid", jsonObject2.getString("id").toString());
                        img = jsonObject2.getString("image").toString();
                        Log.e("img", jsonObject2.getString("image").toString());
                        vendorname = jsonObject2.getString("vendor_name").toString();
                        Log.e("vendorname", jsonObject2.getString("vendor_name").toString());
                        shortdesc = jsonObject2.getString("shortDescription").toString();
                        Log.e("shortdesc", jsonObject2.getString("shortDescription").toString());
                        ModelSearch modelSearch = new ModelSearch();
                        modelSearch.setVendor_id(vendorid);
                        modelSearch.setImage(img);
                        modelSearch.setVendorname(vendorname);
                        modelSearch.setShortdescription(shortdesc);
                        arrsearchcat.add(modelSearch);
                    }
                    ada obj = new ada(Categories.this);
                    searchcatlist.setAdapter(obj);

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

    class ada extends BaseAdapter {
        Context context;
        LayoutInflater inflater;

        class viewholder {
            ImageView imgbrand, imgarw;
            TextView tvbrandname, tvdiscnt, tvViewMore;
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
            return arrsearchcat.size();
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
                arg1 = inflater.inflate(R.layout.layout_coupon_row, null);
                holder = new viewholder();
                holder.imgbrand = (ImageView) arg1.findViewById(R.id.imgbrandlogo);
                holder.tvbrandname = (TextView) arg1.findViewById(R.id.tvbrand);
                holder.tvdiscnt = (TextView) arg1.findViewById(R.id.tvoffer);
                holder.tvViewMore = (TextView) arg1.findViewById(R.id.tvexpdate);
                holder.imgarw = (ImageView) arg1.findViewById(R.id.imgarrow);

                arg1.setTag(holder);
            } else {
                holder = (viewholder) arg1.getTag();
            }
            final ModelSearch modelSearch = arrsearchcat.get(arg0);
            Glide.with(Categories.this).load("http://www.ohdeals.com/india/index.php/images/product/small/" + modelSearch.getImage()).placeholder(R.drawable.ohdeals_fb)
                    .into(holder.imgbrand);
            holder.tvbrandname.setText(modelSearch.getVendorname());
            holder.tvdiscnt.setText(modelSearch.getShortdescription());
            holder.tvViewMore.setText("View More");
            holder.imgarw.setImageResource(R.drawable.maroon_arrow);
            arg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    Log.e("id", id + "");
                    ModelSearch modelSearch1 = new ModelSearch();
                    modelSearch1 = arrsearchcat.get(arg0);
                    Log.e("modelSearch1", modelSearch1 + "");
                    String vid = modelSearch1.getVendor_id();
                    Log.e("vid", vid + "");
                    Intent intent = new Intent(Categories.this, Information.class);
                    intent.putExtra("vendorid", vid);
                    startActivity(intent);
                }
            });
            return arg1;
        }
    }

}