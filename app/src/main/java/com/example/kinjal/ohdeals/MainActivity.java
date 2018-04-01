package com.example.kinjal.ohdeals;

import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.graphics.Color.WHITE;

public class MainActivity extends NavigationDrawer {
    private AdView mAdMobAdView;
    LinearLayout contentFrameLayout;
    GridView grid;
    TextView buy;
    ArrayList<ModelCategory> arr;
    String answer;
    String islogin;
    //SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        contentFrameLayout = (LinearLayout) findViewById(R.id.linearlayout);
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);
        toolbar.setTitle("Categories");

        buy = (TextView) findViewById(R.id.tvbuy);

        /*// Manually checking internet connection
        checkConnection();*/
        new connectToNetwork().execute();
/*
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                answer = "You are connected to a WiFi Network";
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                answer = "You are connected to a Mobile Network";

        } else {
            answer = "No Internet Connection !!";
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this);
            builder.setTitle("Alert");
            builder.setMessage(answer);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            //Toast.makeText(getApplicationContext(), answer, Toast.LENGTH_LONG).show();
        }*/

        mAdMobAdView = (AdView) findViewById(R.id.admob_adview);
        AdRequest adRequest = new AdRequest.Builder()

                // To get emulator ID write given below and replace Device id got from the logcat verbose
                // .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)

                //To test the device with test ads
                //.addTestDevice("49BFD63418A26BC9B39977ED0ABFE214")

                // To get real phone ID write("Device ID")
                //.addTestDevice("5DD0B5CE7277C29635835B3D9C4CD72B")
                .build();
        mAdMobAdView.loadAd(adRequest);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuyMembership.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Toast.makeText(getApplicationContext(), id + "", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Search.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*// Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        new connectToNetwork().execute();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.tvbuy), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
        *//*@Override
        protected void onResume(){
            super.onResume();
        }
        *//*
    */

    /**
     * Callback will be triggered when there is change in
     * network connection
     *//*
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
*/
    class connectToNetwork extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        String response = "", catgrynames, categoryimage, id;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
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
            arr = new ArrayList<ModelCategory>();
            Log.e("DATA", response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray array = jsonObject.getJSONArray("data");
                String status = jsonObject.getString("status");
                Log.e("status", jsonObject.getString("status").toString());

                if (status.equals("success")) {
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonObject2 = array.getJSONObject(i);
                        id = jsonObject2.getString("id").toString();
                        Log.e("id", jsonObject2.getString("id").toString());
                        catgrynames = jsonObject2.getString("category_name").toString();
                        Log.e("category_name", jsonObject2.getString("category_name").toString());
                        categoryimage = jsonObject2.getString("category_image").toString();
                        Log.e("category_image", jsonObject2.getString("category_image").toString());
                        ModelCategory modelCategory = new ModelCategory();
                        modelCategory.setId(id);
                        modelCategory.setCategoryImage(categoryimage);
                        modelCategory.setCategoryName(catgrynames);
                        arr.add(modelCategory);

                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            grid = (GridView) findViewById(R.id.gridView1);
            myadapter obj = new myadapter(MainActivity.this);
            grid.setAdapter(obj);

        }
    }

    class myadapter extends BaseAdapter {

        Context contxt;
        LayoutInflater inflater;

        class ViewHolder {
            ImageView img;
            TextView txt;
        }

        public myadapter(Context mycontext) {
            contxt = mycontext;
            inflater = (LayoutInflater) contxt
                    .getSystemService(contxt.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arr.size();
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
            ViewHolder Holder;
            if (arg1 == null) {
                arg1 = inflater.inflate(R.layout.card_row, null);
                Holder = new ViewHolder();
                Holder.img = (ImageView) arg1.findViewById(R.id.ivcard);
                Holder.txt = (TextView) arg1.findViewById(R.id.tvcard);
                arg1.setTag(Holder);
            } else {
                Holder = (ViewHolder) arg1.getTag();
            }
            final ModelCategory modelCategory = arr.get(arg0);
            Glide.with(MainActivity.this).load("http://ohdeals.com/mobileapp/images/" + modelCategory.getCategoryImage()).placeholder(R.drawable.ohdeals_fb)
                    .into(Holder.img);
            Holder.txt.setText(modelCategory.getCategoryName());
            arg1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String catid = modelCategory.getId();
                    String catname = modelCategory.getCategoryName();
                    Intent intent = new Intent(MainActivity.this, Categories.class);
                    intent.putExtra("catid", catid);
                    intent.putExtra("catname", catname);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "You Clicked " + arr.get(arg0).getCategoryName(), Toast.LENGTH_LONG).show();
                }
            });
            return arg1;

        }
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        // register connection status listener
        if (mAdMobAdView != null) {
            mAdMobAdView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        MyApplication.getInstance().setConnectivityListener(this);
        super.onResume();
        if (mAdMobAdView != null) {
            mAdMobAdView.resume();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdMobAdView != null) {
            mAdMobAdView.destroy();
        }
        super.onDestroy();
    }
}