package com.example.kinjal.ohdeals;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.GridView;
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

public class Notifications extends NavigationDrawer {

    ListView notilist;
    ArrayList<ModelNotification> arr;
    String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_notifications);

        LinearLayout contentFrameLayout = (LinearLayout) findViewById(R.id.linearlayout); // Remember
        getLayoutInflater().inflate(R.layout.activity_notifications, contentFrameLayout);
        toolbar.setTitle("Notifications");
        new connectToNotifications().execute();

    }

    class connectToNotifications extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        String response = "",notititle,notiexpdt;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(Notifications.this);
            dialog.setTitle("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try {
                URL url = new URL(
                        "http://ohdeals.com/mobileapp/admin_msg.php");
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
            arr = new ArrayList<ModelNotification>();
            Log.e("DATA", response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray array = jsonObject.getJSONArray("data");

                for (int i = 0; i < array.length(); i++) {

                    JSONObject jsonObject2 = array.getJSONObject(i);
                    notititle = jsonObject2.getString("title").toString();
                    Log.e("Noti", jsonObject2.getString("title").toString());
                    notiexpdt = jsonObject2.getString("expiry_date").toString();
                    Log.e("expiry_date", jsonObject2.getString("expiry_date").toString());
                    ModelNotification modelNotification = new ModelNotification();
                    modelNotification.setTitle(notititle);
                    modelNotification.setExpiryDate(notiexpdt);
                    arr.add(modelNotification);
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            notilist = (ListView) findViewById(R.id.notificationlist);
            myadapter obj = new myadapter(Notifications.this);
            notilist.setAdapter(obj);

        }
    }

    class myadapter extends BaseAdapter {

        Context contxt;
        LayoutInflater inflater;

        class ViewHolder {
            TextView txttitle;
            ImageView imgCal;
            TextView txtExpDt;

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
                arg1 = inflater.inflate(R.layout.noti_row, null);
                Holder = new ViewHolder();
                Holder.txttitle = (TextView) arg1.findViewById(R.id.tvnotify);
                Holder.imgCal = (ImageView) arg1.findViewById(R.id.imgCalendar);
                Holder.txtExpDt = (TextView) arg1.findViewById(R.id.tvExpdt);
                arg1.setTag(Holder);
            } else {
                Holder = (ViewHolder) arg1.getTag();
            }
            ModelNotification modelNotification = arr.get(arg0);
            Holder.txttitle.setText(modelNotification.getTitle().toString());
            Glide.with(Notifications.this).load(R.drawable.calendar).into(Holder.imgCal);
            Holder.txtExpDt.setText(modelNotification.getExpiryDate());
            /*arg1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getApplicationContext(), "You Clicked "+arr.get(arg0), Toast.LENGTH_LONG).show();
                }
            });*/
            return arg1;
        }
    }

}