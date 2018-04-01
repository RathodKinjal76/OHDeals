package com.example.kinjal.ohdeals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WhoWeAre extends NavigationDrawer {

    TextView txt1, txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_who_we_are);

        LinearLayout contentFrameLayout = (LinearLayout) findViewById(R.id.linearlayout); // Remember
        getLayoutInflater().inflate(R.layout.activity_who_we_are, contentFrameLayout);
        toolbar.setTitle("Who We Are ?");

        txt1 = (TextView) findViewById(R.id.tv1);
        txt2 = (TextView) findViewById(R.id.tv2);

        txt1.setText("The Oh! Deals! Membership Club is a membership Club with the sole intent of offering discounts and benifits to its Club members. On becoming a member, a member will receive a membership card (discount card) which can be used to get UNLIMITED number of discounts at Restaurants, Sweet Shops, Beauty Salons, Medical Stores, Bakeries, Pharmacy, Laboratories, Gym and many more  at more than 200 different locations in the metro-Ahmedabad area. Oh! Deals! Membership Card can be used by whole family and enjoy discount. Show your OH DEALS CARD, UNLIMITED no of times to get UNLIMITED DISCOUNT to get UNLIMITED SAVINGS! ");
        txt1.setText("We have started  Oh Deals Membership Club in April, 2008 with the sole intent of getting discount to our members. We have started only 40 business and now we reached to 200 businesses where our member get UNLIMITED discount through the year. Join the club that allows you to show your card and save through the year!");
    }
}