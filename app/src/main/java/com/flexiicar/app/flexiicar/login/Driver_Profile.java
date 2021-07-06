package com.flexiicar.app.flexiicar.login;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.flexiicar.app.R;

import java.util.ArrayList;

public class Driver_Profile extends AppCompatActivity
{
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    ArrayList<String> scanData = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_profile);
        scanData = getIntent().getStringArrayListExtra("scanData");

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
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
