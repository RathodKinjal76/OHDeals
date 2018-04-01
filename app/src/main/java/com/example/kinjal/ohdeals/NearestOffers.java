package com.example.kinjal.ohdeals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NearestOffers extends NavigationDrawer implements LocationListener {
    ArrayList<ModelNearestOffers> arrnearestoffers;
    ListView Nearestofferslist;
    TextView tvcatgory, buy;
    String catid, NearOfferCatid;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String provider;
    protected String latitude;
    protected String longitude;
    protected boolean gps_enabled, network_enabled;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_nearest_offers);

        preferences = getSharedPreferences("NearestOfferPref", 0);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        LinearLayout contentFrameLayout = (LinearLayout) findViewById(R.id.linearlayout); // Remember
        getLayoutInflater().inflate(R.layout.activity_nearest_offers, contentFrameLayout);
        toolbar.setTitle("Nearest Offers");

        tvcatgory = (TextView) findViewById(R.id.tvcatgry);
        Nearestofferslist = (ListView) findViewById(R.id.nearofferlist);
        buy = (TextView) findViewById(R.id.tvbuy);

        tvcatgory.setText("Category");
        tvcatgory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("NearestOffersStatus", "success");
                editor.commit();
                Log.e("TestNearOfferPref", preferences.getString("NearestOffersStatus", ""));
                Intent intent = new Intent(NearestOffers.this, MainActivity.class);
                startActivity(intent);
                NearOfferCatid = getIntent().getExtras().getString("NearCatId");
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NearestOffers.this, BuyMembership.class);
                startActivity(intent);
            }
        });

    }

    JSONArray array;

    @Override
    public void onLocationChanged(Location location) {
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        Log.e("Latitude", latitude);
        Log.e("Longitude", "longitude");
        new connectToNearestOffers(latitude, longitude, NearOfferCatid).execute();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e("Latitude", "status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e("Latitude", "status");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e("Latitude", "status");
    }

    class connectToNearestOffers extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        String response = "", latitude, longitude, img, vendorid, vendorname, shortdesc, NearOffersCatid;

        public connectToNearestOffers(String latitudes, String longitudes, String NearOfferCatids) {
            latitude = latitudes;
            longitude = longitudes;
            NearOffersCatid = NearOfferCatids;
            Log.e("latitudes , longitudes", latitude + " , " + longitude);
            Log.e("NearOfferCatids", NearOffersCatid);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(NearestOffers.this);
            dialog.setTitle("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try {
                URL url = new URL("http://ohdeals.com/mobileapp/search_near.php?latitude=" + latitude + "&longitude=" + longitude +
                        "/catid=" + NearOffersCatid);
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
            arrnearestoffers = new ArrayList<ModelNearestOffers>();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                Log.e("status", jsonObject.getString("status").toString());
                array = jsonObject.getJSONArray("data");

                if (status.equals("success")) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject2 = array.getJSONObject(i);
                        vendorid = jsonObject2.getString("id").toString();
                        Log.e("V_id", jsonObject2.getString("id").toString());
                        vendorname = jsonObject2.getString("name").toString();
                        Log.e("V_name", jsonObject2.getString("name").toString());
                        img = jsonObject2.getString("image").toString();
                        Log.e("img", jsonObject2.getString("image").toString());
                        shortdesc = Html.fromHtml(jsonObject2.getString("description").toString()).toString();
                        Log.e("shortdesc", jsonObject2.getString("description").toString());

                        ModelNearestOffers modelNearestOffers = new ModelNearestOffers();
                        modelNearestOffers.setVendor_id(vendorid);
                        modelNearestOffers.setVendorname(vendorname);
                        modelNearestOffers.setImage(img);
                        modelNearestOffers.setShortdescription(shortdesc);
                        modelNearestOffers.setVendorname(vendorname);
                        arrnearestoffers.add(modelNearestOffers);
                    }
                    ada obj = new ada(NearestOffers.this);
                    Nearestofferslist.setAdapter(obj);
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
            return arrnearestoffers.size();
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

            final ModelNearestOffers modelNearestOffers = arrnearestoffers.get(arg0);
            Glide.with(NearestOffers.this).load("http://www.ohdeals.com/india/index.php/images/product/small/" +
                    modelNearestOffers.getImage()).placeholder(R.drawable.ohdeals_fb).into(holder.imgbrand);
            holder.tvbrandname.setText(modelNearestOffers.getVendorname());
            holder.tvdiscnt.setText(modelNearestOffers.getShortdescription());
            holder.tvViewMore.setText("View More");
            holder.imgarw.setImageResource(R.drawable.maroon_arrow);
            arg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    Log.e("id", id + "");
                    ModelNearestOffers modelNearestOffers1 = new ModelNearestOffers();
                    modelNearestOffers1 = arrnearestoffers.get(arg0);
                    Log.e("modelNearestOffers1", modelNearestOffers1 + "");
                    String vid = modelNearestOffers1.getVendor_id();
                    Log.e("vid", vid + "");
                    Intent intent = new Intent(NearestOffers.this, Information.class);
                    intent.putExtra("vendorid", vid);
                    startActivity(intent);
                }
            });
            return arg1;
        }
    }

}