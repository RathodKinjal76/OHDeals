package com.example.kinjal.ohdeals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.graphics.Color.WHITE;

public abstract class NavigationDrawer extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private ListView mDrawerList;
    Toolbar toolbar;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    LinearLayout ll2;
    private ActionBarDrawerToggle mDrawerToggle;
    String[] CatArray = {"Home", "Search", "Coupons", "Nearest Offers", "Buy Membership", "Notification"
            , "Who We Are", "Login"};
    String islogin;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        //preferences = getSharedPreferences("mypreference", Context.MODE_PRIVATE);

        preferences = getSharedPreferences("LoginPref", 0);
        islogin = preferences.getString("LoginStatus", "");
        Log.e("LoginPref", islogin);

        // Manually checking internet connection
        checkConnection();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Categories");
        toolbar.setTitleTextColor(WHITE);

        //setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerList = (ListView) findViewById(R.id.ListView1);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(NavigationDrawer.this, mDrawerLayout, toolbar,
                R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor((R.color.drawertoggle)));

        //ll2 = (LinearLayout) findViewById(R.id.linearlayout2);
/*
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                if(mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                else
                    mDrawerLayout.openDrawer(Gravity.RIGHT);

            }
        });
*/
        if (islogin.equals("success")) {
            List<String> list = new ArrayList<String>();
            list = Arrays.asList(CatArray);
            ArrayList<String> arrayList = new ArrayList<String>(list);
            arrayList.remove("Login");
            arrayList.add("Change Password");
            arrayList.add("Logout");
            CatArray = arrayList.toArray(new String[list.size()]);
            Log.e("Items added", CatArray[7] + CatArray[8]);
        }
        myadapter obj2 = new myadapter(NavigationDrawer.this);
        obj2.notifyDataSetChanged();
        mDrawerList.setAdapter(obj2);
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
            Intent intent = new Intent(NavigationDrawer.this, Search.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
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
                .make(findViewById(R.id.linearl), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);

    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
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
            return CatArray.length;
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
            final ViewHolder Holder;
            if (arg1 == null) {
                arg1 = inflater.inflate(R.layout.nav_drawer, null);
                Holder = new ViewHolder();
                Holder.txt = (TextView) arg1.findViewById(R.id.tvDrwCat);
                Holder.img = (ImageView) arg1.findViewById(R.id.tvDrwArw);
                arg1.setTag(Holder);
            } else {
                Holder = (ViewHolder) arg1.getTag();
            }
            Holder.txt.setText(CatArray[arg0]);
            Holder.img.setImageResource(R.drawable.maroon_arrow);
            arg1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Holder.txt.setTextColor(Color.BLUE);
                    openActivity(CatArray[arg0]);
                }
            });
            return arg1;
        }
    }

    void openActivity(String position) {
        switch (position) {
            case "Home":
                startActivity(new Intent(NavigationDrawer.this, MainActivity.class));
                break;
            case "Search":
                startActivity(new Intent(NavigationDrawer.this, Search.class));
                break;
            case "Coupons":
                startActivity(new Intent(NavigationDrawer.this, Coupons.class));
                break;
            case "Nearest Offers":
                startActivity(new Intent(NavigationDrawer.this, NearestOffers.class));
                break;
            case "Buy Membership":
                startActivity(new Intent(NavigationDrawer.this, BuyMembership.class));
                break;
            case "Notification":
                startActivity(new Intent(NavigationDrawer.this, Notifications.class));
                break;
            case "Who We Are":
                startActivity(new Intent(NavigationDrawer.this, WhoWeAre.class));
                break;
            case "Login":
                startActivity(new Intent(NavigationDrawer.this, Login.class));
                break;
            case "Logout":
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("LoginStatus", "");
                editor.commit();
                Log.e("TestLogoutPref", preferences.getString("LoginStatus", ""));
                startActivity(new Intent(NavigationDrawer.this, Login.class));
                break;
            case "Change Password":
                startActivity(new Intent(NavigationDrawer.this, ChangePassword.class));
                break;
            default:
                break;
        }
        Toast.makeText(this, "You Selected :" + position,
                Toast.LENGTH_LONG).show();
    }
}