package com.flexiicar.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.flexiicar.app.fleetowner.ownerlogin.FleetOwner_Login;
import com.flexiicar.app.flexiicar.login.Login;

public class RentOption extends AppCompatActivity
{

    LinearLayout lblRentCar,lblRentMyCar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_option);

        lblRentCar=findViewById(R.id.lblRentCar);
        lblRentMyCar=findViewById(R.id.lblRentMyCar);

        lblRentCar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(RentOption.this, Login.class);
                startActivity(intent);
            }
        });
        lblRentMyCar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(RentOption.this, FleetOwner_Login.class);
                startActivity(intent);
            }
        });

    }
}