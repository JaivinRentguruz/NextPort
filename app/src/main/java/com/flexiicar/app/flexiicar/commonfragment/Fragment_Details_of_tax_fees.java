package com.flexiicar.app.flexiicar.commonfragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.AndroidNetworking;
import com.flexiicar.app.R;
import com.flexiicar.app.adapters.CustomToast;
import com.flexiicar.app.apicall.ApiService;
import com.flexiicar.app.apicall.OnResponseListener;
import com.flexiicar.app.apicall.RequestType;
import com.flexiicar.app.flexiicar.booking.Fragment_Select_Location1;
import com.flexiicar.app.flexiicar.user.Fragment_Summary_Of_Charges_For_Reservation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_BOOKING;
import static com.flexiicar.app.apicall.ApiEndPoint.BOOKING;

public class Fragment_Details_of_tax_fees extends Fragment
{
    LinearLayout linearLayout;
    ImageView backArrow;
    public static Context context;
    Handler handler = new Handler();
    int backtoforterms;
    Boolean TaxType;
    TextView txt_Discard_Tax, txt_totaltaxfees;
    Bundle BookingBundle,VehicleBundle,returnLocationBundle,locationBundle;
    Boolean locationType,initialSelect;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {// Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_of_tax_fee_applicable, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        backtoforterms = getArguments().getInt("backtoforterms");
        TaxType = getArguments().getBoolean("TaxType");
        txt_Discard_Tax=view.findViewById(R.id.txt_Discard_Tax);
        txt_totaltaxfees=view.findViewById(R.id.txt_totaltaxfees);
        linearLayout=view.findViewById(R.id.lblback1);
        backArrow=view.findViewById(R.id.backimg_Taxandfees);


        backArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //For Booking Select Location1
                if(backtoforterms==1)
                {
                    BookingBundle=getArguments().getBundle("BookingBundle");
                    VehicleBundle=getArguments().getBundle("VehicleBundle");
                    returnLocationBundle = getArguments().getBundle("returnLocation");
                    locationBundle = getArguments().getBundle("location");
                    locationType = getArguments().getBoolean("locationType");
                    initialSelect = getArguments().getBoolean("initialSelect");

                    Bundle Booking = new Bundle();
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    Booking.putInt("BookingStep", 2);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_Select_location1, Booking);
                }
                //For Booking Finalize Rental
                if(backtoforterms==2)
                {
                    BookingBundle=getArguments().getBundle("BookingBundle");
                    VehicleBundle=getArguments().getBundle("VehicleBundle");
                    returnLocationBundle = getArguments().getBundle("returnLocation");
                    locationBundle = getArguments().getBundle("location");
                    locationType = getArguments().getBoolean("locationType");
                    initialSelect = getArguments().getBoolean("initialSelect");

                    Bundle Booking = new Bundle();
                    BookingBundle.putInt("BookingStep", 4);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_Finalize_your_rental, Booking);
                }
                //For User Reservation SummaryOfCharges
                    if(backtoforterms==3)
                    {
                        Bundle  Reservation=getArguments().getBundle("ReservationBundle");
                        Bundle ReservationBundle=new Bundle();
                        ReservationBundle.putBundle("ReservationBundle",Reservation);
                        NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                                .navigate(R.id.action_Total_tax_fee_details_to_SummaryOfCharges, ReservationBundle);
                    }
                //For User Finalize Your Rental
                    if(backtoforterms==4)
                    {
                        Bundle Reservation=getArguments().getBundle("ReservationBundle");
                        Bundle ReservationBundle=new Bundle();
                        ReservationBundle.putBundle("ReservationBundle",Reservation);
                        NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                                .navigate(R.id.action_Total_tax_fee_details_to_Finalize_your_rental, ReservationBundle);
                    }
                //For Booking Summary Of Charges
                    if(backtoforterms==5)
                    {
                        Bundle BookingBundle=getArguments().getBundle("BookingBundle");
                        Bundle VehicleBundle=getArguments().getBundle("VehicleBundle");

                        Bundle Booking = new Bundle();
                        BookingBundle.putInt("BookingStep", 6);
                        Booking.putBundle("BookingBundle", BookingBundle);
                        Booking.putBundle("VehicleBundle", VehicleBundle);
                        NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                                .navigate(R.id.action_Total_tax_fee_details_to_SummaryOfCharges, Booking);
                    }
                    //For User SummaryOfCharges For Agreements
                    if(backtoforterms==6)
                    {
                        Bundle AgreementsBundle=getArguments().getBundle("AgreementsBundle");
                        Bundle Agreements =new Bundle();
                        Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                        NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                                .navigate(R.id.action_Total_tax_fee_details_to_SummaryOfChargesForAgreements,Agreements);
                    }
                //For User Agreements SummaryOfCharge For SelfCheckI
                    if(backtoforterms==7)
                    {
                        Bundle AgreementsBundle=getArguments().getBundle("AgreementsBundle");
                        Bundle Agreements=new Bundle();
                        Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                        NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                                .navigate(R.id.action_Total_tax_fee_details_to_SummaryOfChargeForSelfCheckIn,Agreements);
                }

            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //For Booking Select Location1
                if(backtoforterms==1)
                {
                    BookingBundle=getArguments().getBundle("BookingBundle");
                    VehicleBundle=getArguments().getBundle("VehicleBundle");
                    returnLocationBundle = getArguments().getBundle("returnLocation");
                    locationBundle = getArguments().getBundle("location");
                    locationType = getArguments().getBoolean("locationType");
                    initialSelect = getArguments().getBoolean("initialSelect");

                    Bundle Booking = new Bundle();
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    BookingBundle.putInt("BookingStep", 2);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_Select_location1, Booking);
                }
                //For Booking Finalize Rental
                if(backtoforterms==2)
                {
                    BookingBundle=getArguments().getBundle("BookingBundle");
                    VehicleBundle=getArguments().getBundle("VehicleBundle");
                    returnLocationBundle = getArguments().getBundle("returnLocation");
                    locationBundle = getArguments().getBundle("location");
                    locationType = getArguments().getBoolean("locationType");
                    initialSelect = getArguments().getBoolean("initialSelect");

                    Bundle Booking = new Bundle();
                    BookingBundle.putInt("BookingStep", 4);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_Finalize_your_rental, Booking);
                }
                //For User Reservation SummaryOfCharges
                if(backtoforterms==3)
                {
                    Bundle Reservation=getArguments().getBundle("ReservationBundle");
                    Bundle ReservationBundle=new Bundle();
                    ReservationBundle.putBundle("ReservationBundle",Reservation);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_SummaryOfCharges, ReservationBundle);
                }
                //For User Finalize Your Rental
                if(backtoforterms==4)
                {
                    Bundle Reservation=getArguments().getBundle("ReservationBundle");
                    Bundle ReservationBundle=new Bundle();
                    ReservationBundle.putBundle("ReservationBundle",Reservation);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_Finalize_your_rental, ReservationBundle);
                }
                //For Booking Summary Of Charges
                if(backtoforterms==5)
                {
                    Bundle BookingBundle=getArguments().getBundle("BookingBundle");
                    Bundle VehicleBundle=getArguments().getBundle("VehicleBundle");

                    Bundle Booking = new Bundle();
                    BookingBundle.putInt("BookingStep", 6);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_SummaryOfCharges, Booking);
                }
                //For User SummaryOfCharges For Agreements
                if(backtoforterms==6)
                {
                    Bundle Agreements=getArguments().getBundle("AgreementsBundle");
                    Bundle AgreementsBundle=new Bundle();
                    AgreementsBundle.putBundle("AgreementsBundle",Agreements);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_SummaryOfChargesForAgreements,AgreementsBundle);
                }
                //For User Agreements SummaryOfCharge For SelfCheckI
                if(backtoforterms==7)
                {
                    Bundle AgreementsBundle=getArguments().getBundle("AgreementsBundle");
                    Bundle Agreements=new Bundle();
                    Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_SummaryOfChargeForSelfCheckIn,Agreements);
                }
            }
        });
        txt_Discard_Tax.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                //For Booking Select Location1
                if(backtoforterms==1)
                {
                    BookingBundle=getArguments().getBundle("BookingBundle");
                    VehicleBundle=getArguments().getBundle("VehicleBundle");
                    returnLocationBundle = getArguments().getBundle("returnLocation");
                    locationBundle = getArguments().getBundle("location");
                    locationType = getArguments().getBoolean("locationType");
                    initialSelect = getArguments().getBoolean("initialSelect");

                    Bundle Booking = new Bundle();
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    BookingBundle.putInt("BookingStep", 2);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_Select_location1, Booking);
                }
                //For Booking Finalize Rental
                if(backtoforterms==2)
                {
                    BookingBundle=getArguments().getBundle("BookingBundle");
                    VehicleBundle=getArguments().getBundle("VehicleBundle");
                    returnLocationBundle = getArguments().getBundle("returnLocation");
                    locationBundle = getArguments().getBundle("location");
                    locationType = getArguments().getBoolean("locationType");
                    initialSelect = getArguments().getBoolean("initialSelect");

                    Bundle Booking = new Bundle();
                    BookingBundle.putInt("BookingStep", 4);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_Finalize_your_rental, Booking);
                }
                //For User Reservation SummaryOfCharges
                if(backtoforterms==3)
                {
                    Bundle Reservation=getArguments().getBundle("ReservationBundle");
                    Bundle ReservationBundle=new Bundle();
                    ReservationBundle.putBundle("ReservationBundle",Reservation);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_SummaryOfCharges, ReservationBundle);
                }
                //For User Finalize Your Rental
                if(backtoforterms==4)
                {
                    Bundle Reservation=getArguments().getBundle("ReservationBundle");

                    Bundle ReservationBundle=new Bundle();
                    ReservationBundle.putBundle("ReservationBundle",Reservation);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_Finalize_your_rental, ReservationBundle);
                }
                //For Booking Summary Of Charges
                if(backtoforterms==5)
                {
                    Bundle BookingBundle=getArguments().getBundle("BookingBundle");
                    Bundle VehicleBundle=getArguments().getBundle("VehicleBundle");

                    Bundle Booking = new Bundle();
                    BookingBundle.putInt("BookingStep", 6);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_SummaryOfCharges, Booking);
                }
                //For User SummaryOfCharges For Agreements
                if(backtoforterms==6)
                {
                    Bundle Agreements=getArguments().getBundle("AgreementsBundle");
                    Bundle AgreementsBundle=new Bundle();
                    AgreementsBundle.putBundle("AgreementsBundle",Agreements);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_SummaryOfChargesForAgreements,AgreementsBundle);
                }
                //For User Agreements SummaryOfCharge For SelfCheck
                if(backtoforterms==7)
                {
                    Bundle AgreementsBundle=getArguments().getBundle("AgreementsBundle");
                    Bundle Agreements=new Bundle();
                    Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                    NavHostFragment.findNavController(Fragment_Details_of_tax_fees.this)
                            .navigate(R.id.action_Total_tax_fee_details_to_SummaryOfChargeForSelfCheckIn,Agreements);
                }
            }
        });

      /*  if(backtoforterms==5)
        {
            JSONObject bodyParam = new JSONObject();
            Bundle BookingBundle;
            BookingBundle=getArguments().getBundle("BookingBundle");
            try {
                bodyParam.accumulate("ForTransId",BookingBundle.getInt("ForTransId"));
                bodyParam.accumulate("BookingStep",BookingBundle.getInt("BookingStep"));
                System.out.println(bodyParam);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            AndroidNetworking.initialize(getActivity());
            Fragment_Summary_Of_Charges_For_Reservation.context = getActivity();


            ApiService ApiService = new ApiService(getTaxtDetails, RequestType.POST,
                    BOOKING, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);
        }*/
        if(TaxType)
        {
            BookingBundle = getArguments().getBundle("BookingBundle");
            VehicleBundle = getArguments().getBundle("VehicleBundle");

            JSONObject bodyParam = new JSONObject();
            try {
                bodyParam.accumulate("VehicleID", BookingBundle.getInt("VehicleID"));
                bodyParam.accumulate("PickupLocId", BookingBundle.getInt("PickupLocId"));
                bodyParam.accumulate("ReturnLocId", BookingBundle.getInt("ReturnLocId"));
                bodyParam.accumulate("CustomerId", BookingBundle.getInt("CustomerId"));
                bodyParam.accumulate("VehicleTypeId", BookingBundle.getInt("VehicleTypeId"));
                bodyParam.accumulate("StrFilterVehicleTypeIds", BookingBundle.getString("StrFilterVehicleTypeIds"));
                bodyParam.accumulate("StrFilterVehicleOptionIds", BookingBundle.getString("StrFilterVehicleOptionIds"));
                bodyParam.accumulate("PickupDate", BookingBundle.getString("PickupDate"));
                bodyParam.accumulate("ReturnDate", BookingBundle.getString("ReturnDate"));
                bodyParam.accumulate("FilterTransmission", BookingBundle.getInt("FilterTransmission"));
                bodyParam.accumulate("FilterPassengers", BookingBundle.getInt("FilterPassengers"));
                bodyParam.accumulate("BookingStep", BookingBundle.getInt("BookingStep"));
                bodyParam.accumulate("BookingType", BookingBundle.getInt("BookingType"));
                System.out.println(bodyParam);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            AndroidNetworking.initialize(getActivity());
            Fragment_Select_Location1.context = getActivity();

            ApiService ApiService = new ApiService(getTaxtDetails, RequestType.POST,
                    BOOKING, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);
        }
        else
            {
                if(backtoforterms==3)
                {
                    Bundle ReservationBundle = getArguments().getBundle("ReservationBundle");
                    JSONObject bodyParam = new JSONObject();
                    try {
                        bodyParam.accumulate("ForTransId", ReservationBundle.getInt("reservation_ID"));
                        bodyParam.accumulate("BookingStep", 6);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ApiService ApiService = new ApiService(getTaxtDetails, RequestType.POST,
                            BOOKING, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);
                }
                if(backtoforterms==4)
                {
                    Bundle ReservationBundle = getArguments().getBundle("ReservationBundle");
                    JSONObject bodyParam = new JSONObject();
                    try {
                        bodyParam.accumulate("ForTransId", ReservationBundle.getInt("reservation_ID"));
                        bodyParam.accumulate("BookingStep", 4);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    ApiService ApiService = new ApiService(getTaxtDetails, RequestType.POST,
                            BOOKING, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);
                }
                if(backtoforterms==6)
                {
                    Bundle AgreementsBundle = getArguments().getBundle("AgreementsBundle");
                    JSONObject bodyParam = new JSONObject();
                    try {
                        bodyParam.accumulate("ForTransId", AgreementsBundle.getInt("reservation_ID"));
                        bodyParam.accumulate("BookingStep", 6);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    ApiService ApiService = new ApiService(getTaxtDetails, RequestType.POST,
                            BOOKING, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);
                }
                if(backtoforterms==7)
                {
                    Bundle AgreementsBundle = getArguments().getBundle("AgreementsBundle");
                    JSONObject bodyParam = new JSONObject();
                    try {
                        bodyParam.accumulate("AgreementId",AgreementsBundle.getInt("agreement_ID"));
                        bodyParam.accumulate("VehicleId",AgreementsBundle.getInt("vehicle_ID"));
                        bodyParam.accumulate("originalCheckInDate",AgreementsBundle.getString("originalCheckInDate"));
                        bodyParam.accumulate("originalCheckInLocationId",AgreementsBundle.getInt("originalCheckInLocationId"));
                        bodyParam.accumulate("ActualReturnDate",AgreementsBundle.getString("ActualReturnDate"));
                        bodyParam.accumulate("actualReturnLocationId",AgreementsBundle.getInt("actualReturnLocationId"));
                    } catch (Exception e)

                    {
                        e.printStackTrace();
                    }
                    ApiService ApiService = new ApiService(getTaxtDetails, RequestType.POST,
                            BOOKING, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);
                }
        }
    }
    OnResponseListener getTaxtDetails = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {
                            try
                            {
                                JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                final JSONArray getTaxDetails = resultSet.getJSONArray("taxModel");
                                final RelativeLayout rlVehicleTaxDetails = getActivity().findViewById(R.id.rl_TotalTaxFees);

                                int len;
                                len = getTaxDetails.length();

                                Double totalTax = 0.0;

                                for (int j = 0; j < len; j++)
                                {
                                    final JSONObject test = (JSONObject) getTaxDetails.get(j);

                                    final int taxSetupId = test.getInt("taxSetupId");
                                    final String taxName = test.getString("taxName");
                                    final String taxDesc = test.getString("taxDesc");
                                    final String basicValue = test.getString("basicValue");
                                    final String quantity = test.getString("quantity");
                                    final double taxAmount = test.getDouble("taxAmount");

                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    lp.setMargins(0, 0, 0, 0);

                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.vehicle_tax_and_fees, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                    linearLayout.setId(200 + j);
                                    linearLayout.setLayoutParams(lp);

                                    TextView txt_Name, txt_DEsc,txt_Basicvalue,txt_Quantity,txt_Amount;

                                    txt_Name = linearLayout.findViewById(R.id.txt_taxName);
                                    txt_DEsc = linearLayout.findViewById(R.id.tax_Desc);
                                    txt_Basicvalue = linearLayout.findViewById(R.id.tax_basicValue);
                                    txt_Quantity = linearLayout.findViewById(R.id.tax_quantity);
                                    txt_Amount = linearLayout.findViewById(R.id.txt_taxTotalAmount);
                                    txt_Name.setText(taxName);
                                    txt_DEsc.setText(taxDesc);
                                    txt_Basicvalue.setText(basicValue);
                                    txt_Quantity.setText(quantity);
                                    txt_Amount.setText((String.format(Locale.US,"%.2f",taxAmount)));

                                    totalTax+= taxAmount;

                                    rlVehicleTaxDetails.addView(linearLayout);
                                }

                                String TotalAmountStr=(String.format(Locale.US,"%.2f",totalTax));
                                txt_totaltaxfees.setText("USD$ "+TotalAmountStr);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        else
                        {
                            String msg = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),msg,1);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };

}
