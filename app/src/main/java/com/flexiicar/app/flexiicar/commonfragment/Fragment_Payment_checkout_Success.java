package com.flexiicar.app.flexiicar.commonfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.flexiicar.app.R;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Payment_checkout_Success extends Fragment
{
    LinearLayout lblPaymentSuceess;
    public static Context context;
    TextView txtMessage, txtTotal;
    Bundle BookingBundle, VehicleBundle;
    int sendTo;
    Bundle returnLocationBundle, locationBundle;
    Boolean locationType, initialSelect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {// Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_checkout_success, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        try {
            super.onViewCreated(view, savedInstanceState);

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            lblPaymentSuceess = view.findViewById(R.id.lblPaymentSuceess);

            txtTotal = view.findViewById(R.id.txtTotal);
            txtMessage = view.findViewById(R.id.txtMessage);

            final String transactionId = getArguments().getString("transactionId");
            Double total = getArguments().getDouble("total");
            sendTo = getArguments().getInt("sendTo");

            txtTotal.setText("US$ " +((String.format(Locale.US, "%.2f", total))));

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            String id = sp.getString(getString(R.string.id), "");
            String cust_FName = sp.getString("cust_FName", "");
            String cust_LName = sp.getString("cust_LName", "");
            String cust_Email = sp.getString("cust_Email", "");
            String cust_Phoneno = sp.getString("cust_Phoneno", "");

            txtMessage.setText(" Your payment is processed successfully. Your payment reference number is " + transactionId + ". Confirmation email is been sent to " + cust_Email + ". Please call customer service for any changes or cancellations.  ");

            lblPaymentSuceess.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (sendTo==1)
                    {
                        try {
                            //For Booking
                            BookingBundle = getArguments().getBundle("BookingBundle");
                            VehicleBundle = getArguments().getBundle("VehicleBundle");
                            returnLocationBundle = getArguments().getBundle("returnLocation");
                            locationBundle = getArguments().getBundle("location");
                            locationType = getArguments().getBoolean("locationType");
                            initialSelect = getArguments().getBoolean("initialSelect");

                            Bundle Booking = new Bundle();
                            BookingBundle.putInt("BookingStep", 6);
                            BookingBundle.putString("transactionId", transactionId);
                            Booking.putBundle("BookingBundle", BookingBundle);
                            Booking.putBundle("VehicleBundle", VehicleBundle);
                            Booking.putBundle("returnLocation", returnLocationBundle);
                            Booking.putBundle("location", locationBundle);
                            Booking.putBoolean("locationType", locationType);
                            Booking.putBoolean("initialSelect", initialSelect);
                            NavHostFragment.findNavController(Fragment_Payment_checkout_Success.this)
                                    .navigate(R.id.action_Payment_Success_to_Summary_Of_Charges, Booking);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else if (sendTo==2)
                    {
                        //AccountStatement
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("StatmentType", true);
                        NavHostFragment.findNavController(Fragment_Payment_checkout_Success.this)
                                .navigate(R.id.action_Payment_Success_to_Bills_and_Payment, bundle);
                    }
                    //For Reservation
                    else if (sendTo==3)
                    {
                        Bundle ReservationBundle = getArguments().getBundle("ReservationBundle");
                        final String total = getArguments().getString("total");
                        NavHostFragment.findNavController(Fragment_Payment_checkout_Success.this)
                          .navigate(R.id.action_Payment_Success_to_User_Details);
                    }
                    else if (sendTo==4)
                    {
                        Bundle AgreementsBundle = getArguments().getBundle("AgreementsBundle");
                        String msg=getArguments().getString("message");
                        String transactionId=getArguments().getString("transactionId");
                        Double total=getArguments().getDouble("total");
                        Bundle Agreements = new Bundle();
                        Agreements.putBundle("AgreementsBundle", AgreementsBundle);
                        Agreements.putString("message",msg);
                        Agreements.putString("transactionId",transactionId);
                        Agreements.putDouble("total",total);
                        NavHostFragment.findNavController(Fragment_Payment_checkout_Success.this)
                                .navigate(R.id.action_Payment_Success_to_WaiverSign_ForSelfCheckIn,Agreements);
                    }
                }
            });

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}