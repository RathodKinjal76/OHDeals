package com.example.kinjal.ohdeals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CouponDetail extends NavigationDrawer {

    ImageView imgcoupon;
    TextView tvDesc,tvtnc,tvexpr,tvvalid,tvbuy;
    String desc,terms,expdt,valid,img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_coupon_detail);

        img = getIntent().getExtras().getString("vimg");
        desc = getIntent().getExtras().getString("vdesc");
        terms = getIntent().getExtras().getString("vterms");
        expdt = getIntent().getExtras().getString("vexpr");
        valid = getIntent().getExtras().getString("valid");

        LinearLayout contentFrameLayout = (LinearLayout) findViewById(R.id.linearlayout); // Remember
        getLayoutInflater().inflate(R.layout.activity_coupon_detail, contentFrameLayout);
        toolbar.setTitle("Coupon Detail");

        imgcoupon = (ImageView) findViewById(R.id.imgcoupon);
        tvDesc = (TextView) findViewById(R.id.tvDescription);
        tvtnc = (TextView) findViewById(R.id.tvTnC);
        tvexpr = (TextView) findViewById(R.id.tvExp);
        tvvalid = (TextView) findViewById(R.id.tvValid);
        tvbuy = (TextView) findViewById(R.id.tvbuy);

        Glide.with(CouponDetail.this).load("http://ohdeals.com/mobileapp/uploads/" + img).placeholder(R.drawable.ohdeals_fb)
                .into(imgcoupon);
        tvDesc.setText("Description : \n\n"+ desc);
        tvtnc.setText("Terms : \n\n"+ terms);
        tvexpr.setText("Expiry : \n\n"+ expdt);
        tvvalid.setText("Valid : \n\n"+ valid);

        tvbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CouponDetail.this,BuyMembership.class);
                startActivity(intent);
            }
        });
    }
}
