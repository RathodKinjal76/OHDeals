package com.example.kinjal.ohdeals;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.QuickContactBadge;
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

import static com.example.kinjal.ohdeals.R.drawable.ohdeals_fb;

public class Coupons extends NavigationDrawer {
    ArrayList<ModelCoupons> arrcoupon;
    ListView couponlist;
    TextView buy;
    String response = "", vid, shortdesc, img, vendorname, Expiry_date, Valid, Terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_coupons);

        LinearLayout contentFrameLayout = (LinearLayout) findViewById(R.id.linearlayout); // Remember
        getLayoutInflater().inflate(R.layout.activity_coupons, contentFrameLayout);
        toolbar.setTitle("Coupons");

        new connectToCoupons().execute();

        buy = (TextView) findViewById(R.id.tvbuy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Coupons.this, BuyMembership.class);
                startActivity(intent);
            }
        });

    }

    JSONArray array;

    class connectToCoupons extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(Coupons.this);
            dialog.setTitle("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try {
                URL url = new URL("http://ohdeals.com/mobileapp/coupon.php?catid=12");
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
            arrcoupon = new ArrayList<ModelCoupons>();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                Log.e("status", jsonObject.getString("status").toString());

                if (status.equals("success")) {
                    array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject2 = array.getJSONObject(i);
                        vid = jsonObject2.getString("id").toString();
                        Log.e("V_id", jsonObject2.getString("id").toString());
                        vendorname = jsonObject2.getString("name").toString();
                        Log.e("V_name", jsonObject2.getString("name").toString());
                        img = jsonObject2.getString("image").toString();
                        Log.e("img", jsonObject2.getString("image").toString());
                        Expiry_date = jsonObject2.getString("expiry_date").toString();
                        Log.e("Expiry_date", jsonObject2.getString("expiry_date").toString());
                        shortdesc = Html.fromHtml(jsonObject2.getString("description").toString()).toString();
                        Log.e("shortdesc", jsonObject2.getString("description").toString());

                        ModelCoupons modelCoupons = new ModelCoupons();
                        modelCoupons.setVendor_id(vid);
                        modelCoupons.setVendorname(vendorname);
                        modelCoupons.setImage(img);
                        modelCoupons.setShortdescription(shortdesc);
                        modelCoupons.setVendorname(vendorname);
                        modelCoupons.setExp_date(Expiry_date);
                        arrcoupon.add(modelCoupons);
                    }
                    couponlist = (ListView) findViewById(R.id.couponslist);
                    ada obj = new ada(Coupons.this);
                    couponlist.setAdapter(obj);
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
            TextView tvbrandname, tvdiscnt, tvexpdt;
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
            return arrcoupon.size();
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
                holder.tvexpdt = (TextView) arg1.findViewById(R.id.tvexpdate);
                holder.imgarw = (ImageView) arg1.findViewById(R.id.imgarrow);
                arg1.setTag(holder);
            } else {
                holder = (viewholder) arg1.getTag();
            }
            final ModelCoupons modelCoupons = arrcoupon.get(arg0);
            Glide.with(Coupons.this).load("http://ohdeals.com/mobileapp/uploads/" + modelCoupons.getImage())
                    .placeholder(R.drawable.ohdeals_fb).into(holder.imgbrand);
            holder.tvbrandname.setText(modelCoupons.getVendorname());
            holder.tvdiscnt.setText(modelCoupons.getShortdescription());
            holder.tvexpdt.setText("Expiry Date : " + modelCoupons.getExp_date());
            holder.imgarw.setImageResource(R.drawable.maroon_arrow);
            arg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        JSONObject jsonObject3 = (JSONObject) array.get(arg0);

                        img = jsonObject3.getString("image").toString();
                        Log.e("img", jsonObject3.getString("image").toString());
                        Expiry_date = jsonObject3.getString("expiry_date").toString();
                        Log.e("Expiry_date", jsonObject3.getString("expiry_date").toString());
                        shortdesc = Html.fromHtml(jsonObject3.getString("description").toString()).toString();
                        Log.e("shortdesc", jsonObject3.getString("description").toString());
                        Valid = jsonObject3.getString("valid").toString();
                        Log.e("Valid", jsonObject3.getString("valid").toString());
                        Terms = Html.fromHtml(jsonObject3.getString("terms").toString()).toString();
                        Log.e("terms", jsonObject3.getString("terms").toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(Coupons.this, CouponDetail.class);
                    intent.putExtra("vimg", img);
                    intent.putExtra("vdesc", shortdesc);
                    intent.putExtra("vterms", Terms);
                    intent.putExtra("vexpr", Expiry_date);
                    intent.putExtra("valid", Valid);
                    startActivity(intent);
                }
            });
            return arg1;
        }
    }

}