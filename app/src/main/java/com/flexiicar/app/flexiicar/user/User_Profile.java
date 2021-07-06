package com.flexiicar.app.flexiicar.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.flexiicar.app.R;
import com.flexiicar.app.flexiicar.booking.Booking_Activity;

import java.util.ArrayList;

public class User_Profile extends AppCompatActivity
{

    ImageView home_icon;
    ImageView Profile_icon;
    ArrayList<String> scanData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        home_icon=(ImageView) findViewById(R.id.home_icon);
        Profile_icon=(ImageView) findViewById(R.id.profile_icon);

        home_icon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent1=(new Intent(getApplicationContext(), Booking_Activity.class));
                startActivity(intent1);
            }
        });

        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19)
        { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
        else if(Build.VERSION.SDK_INT >= 19)
        {

            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }

        try {
            scanData = getIntent().getStringArrayListExtra("scanData");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}