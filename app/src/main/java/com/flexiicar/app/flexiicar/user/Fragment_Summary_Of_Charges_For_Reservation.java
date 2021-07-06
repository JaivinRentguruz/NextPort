package com.flexiicar.app.flexiicar.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.flexiicar.app.flexiicar.booking.Fragment_Summary_Of_Charges;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_BOOKING;
import static com.flexiicar.app.apicall.ApiEndPoint.BOOKING;
import static com.flexiicar.app.apicall.ApiEndPoint.CANCELBOOKING;

public class Fragment_Summary_Of_Charges_For_Reservation extends Fragment
{
    Handler handler = new Handler();
    public static Context context;
    Bundle ReservationBundle;
    ImageView SummaryOfChargeArrow,SummaryOfChargeArrowDown,QR_Image,BackArrow;
    RelativeLayout rlSummaryOfCharge;
    TextView txt_PickLocName,txt_ReturnLocName,txt_PickupDate,txt_ReturnDate,txt_PickupTime,txt_ReturnTIme,txtDays,txt_vehicletype,
            txttransmission,txt_vehName,txt_rate,txt_Seats,txt_Bags,txt_Automatic,txt_Doors,txt_Home,txt_conformationNo,txtDiscard,txt_createdDate;
    ImageView arrowSelfcheckOut,driverdetails_icon;
    LinearLayout lblterm_condition1,LL_selfcheckout;
    LinearLayout llCancelBooking,llModify;
    int reservation_ID;
    LinearLayout llToolbar;
    JSONArray summaryOfCharges = new JSONArray();

    ImageLoader imageLoader;
    String serverpath="",domainName="";
    double TotalAmount;
    LinearLayout AddCalender;
    public static void initImageLoader(Context context)
    {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MAX_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary_of_charges, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        try {

            initImageLoader(getActivity());
            imageLoader = ImageLoader.getInstance();

            AddCalender=view.findViewById(R.id.lblAddCalender);
            llToolbar=view.findViewById(R.id.llTitle);
            llToolbar.setVisibility(View.VISIBLE);
            BackArrow=view.findViewById(R.id.backToHome);
            lblterm_condition1 = view.findViewById(R.id.term_condition);

            LL_selfcheckout = view.findViewById(R.id.Layout_SelfCheckout);
            arrowSelfcheckOut = view.findViewById(R.id.arrow_Selfcheckout);

            QR_Image = view.findViewById(R.id.QR_Image);
            txtDiscard= view.findViewById(R.id.Discardlbl);
            llCancelBooking = view.findViewById(R.id.llCancelBooking);
            llModify=view.findViewById(R.id.llModify);

            SummaryOfChargeArrowDown = view.findViewById(R.id.ArrowDownForSummary);
            SummaryOfChargeArrow= view.findViewById(R.id.ArrowForSummary);

            rlSummaryOfCharge = view.findViewById(R.id.rl_Summaryofcharge);
            txt_Home=view.findViewById(R.id.txt_Home);
            txt_Home.setVisibility(View.GONE);
            txt_conformationNo=view.findViewById(R.id.txt_conformationNo);

            txt_PickLocName = view.findViewById(R.id.txtPic_LocName);
            txt_PickupDate = view.findViewById(R.id.txtPic_LocDate);
            txt_PickupTime = view.findViewById(R.id.txtPic_LocTime);
            txt_ReturnLocName = view.findViewById(R.id.txtReturn_LocName);

            txt_ReturnDate = view.findViewById(R.id.txtReturn_Date);
            txt_ReturnTIme = view.findViewById(R.id.txtReturn_Time);
            txt_createdDate= view.findViewById(R.id.txt_createdDate);

            txtDays = view.findViewById(R.id.txt_TotalDay1);
            txt_rate = view.findViewById(R.id.txt_Vehiclerate1);
            txt_Seats = view.findViewById(R.id.textView_Seats1);
            txt_Bags = view.findViewById(R.id.textView_Bags1);
            txt_Automatic = view.findViewById(R.id.txtAuto);
            txt_Doors = view.findViewById(R.id.txt_door1);

            txt_vehName = view.findViewById(R.id.vehiclE_NAME);
            txt_vehicletype = view.findViewById(R.id.txtvehiclE_TYPE_NAME);

            ReservationBundle = getArguments().getBundle("ReservationBundle");

            txt_PickLocName.setText(ReservationBundle.getString("chk_Out_Location_Name"));
            txt_ReturnLocName.setText(ReservationBundle.getString("chk_In_Loc_Name"));

            txt_vehName.setText(ReservationBundle.getString("vehicle_Name"));
            txt_vehicletype.setText(ReservationBundle.getString("vehicle_Type_Name"));

            String CheckOut=(ReservationBundle.getString("default_Check_Out"));
            // Date
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            Date date1 = dateFormat1.parse(CheckOut);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/ yyyy, HH:mm aa", Locale.US);
            String CheckOutStr = sdf1.format(date1);
            txt_PickupDate.setText(CheckOutStr);

            String CheckIn=(ReservationBundle.getString("default_Check_In"));
            //check_Out Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            Date date = dateFormat.parse(CheckIn);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm aa", Locale.US);
            String CheckInStr = sdf.format(date);
            txt_ReturnDate.setText(CheckInStr);

            String Date=(ReservationBundle.getString("default_Created_Date"));

            /*SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            java.util.Date date2 = dateFormat2.parse(Date);
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy,HH:mm aa",Locale.US);
            String Issue_DateStr = sdf2.format(date2);*/

            txt_createdDate.setText("SEE YOU ON "+(txt_PickupDate.getText().toString()));

            txt_rate.setText(ReservationBundle.getString("rate_ID"));
            reservation_ID=ReservationBundle.getInt("reservation_ID");

            txt_conformationNo.setText(String.valueOf(reservation_ID));

           // txt_Seats.setText(getArguments().getBundle("AgreementsBundle").getString("veh_Seat"));

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            serverpath = sp.getString("serverPath", "");
            domainName= sp.getString("domainName", "");
            String id = sp.getString(getString(R.string.id), "");

            ImageView Vehicle_Img=view.findViewById(R.id.Veh_image_bg4);

            String VehicleImgstr=ReservationBundle.getString("veh_Img_Path");
            String url1 = serverpath+VehicleImgstr.substring(2);
            imageLoader.displayImage(url1,Vehicle_Img);

            arrowSelfcheckOut.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    arrowSelfcheckOut.setVisibility(View.INVISIBLE);
                    if(LL_selfcheckout.getVisibility()== View.GONE)
                    {
                        LL_selfcheckout.setVisibility(View.VISIBLE);

                    }
                    else
                        LL_selfcheckout.setVisibility(View.GONE);
                }
            });
            lblterm_condition1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle Reservation = new Bundle();
                    Reservation.putInt("termcondition", 2);
                    Reservation.putBundle("ReservationBundle", ReservationBundle);
                    System.out.println(Reservation);
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Reservation.this)
                            .navigate(R.id.action_SummaryOfCharges_to_TermAndCondition,Reservation);
                }
            });
            AddCalender.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Calendar calendarEvent = Calendar.getInstance();
                    Intent i = new Intent(Intent.ACTION_EDIT);
                    i.setType("vnd.android.cursor.item/event");
                    i.putExtra("Date",calendarEvent.getTimeInMillis());
                    i.putExtra("allDay", true);
                    i.putExtra("rule", "FREQ=YEARLY");
                    i.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
                    i.putExtra("title", "Calendar Event");
                    startActivity(i);
                }
            });

          /*  llModify.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Bundle Reservation=new Bundle();
                    ReservationBundle.putInt("BookingStep",3);
                    ReservationBundle.putString("summaryOfCharges", summaryOfCharges.toString());
                    ReservationBundle.putString("vehiclE_SEAT_NO",txt_Seats.getText().toString());
                    ReservationBundle.putString("veh_bags",txt_Bags.getText().toString());
                    ReservationBundle.putString("transmission_Name",txt_Automatic.getText().toString());
                    ReservationBundle.putString("doors",txt_Doors.getText().toString());
                    ReservationBundle.putString("totaL_DAYS",txtDays.getText().toString());
                    ReservationBundle.putDouble("chargeAmount",TotalAmount);
                    Reservation.putString("DeliveryAndPickupModel", "");
                    Reservation.putBundle("ReservationBundle",ReservationBundle);
                    System.out.println(Reservation);
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Reservation.this)
                            .navigate(R.id.action_SummaryOfCharges_to_Select_addtional_options,Reservation);
                }
            });*/
          llModify.setOnClickListener(new View.OnClickListener()
          {
              @Override
              public void onClick(View v)
              {
                  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                  builder.setMessage("Please call our office for modifiction.");
                 /* builder.setPositiveButton("Yes",
                          new DialogInterface.OnClickListener()
                          {
                              @Override
                              public void onClick(DialogInterface dialog, int which)
                              {

                              }
                          });*/
                  builder.setNegativeButton("Cancel",
                          new DialogInterface.OnClickListener()
                          {
                              @Override
                              public   void onClick(DialogInterface dialog, int which)
                              {
                                  dialog.dismiss();
                              }
                          });

                  final AlertDialog dialog = builder.create();
                  dialog.show();
              }
          });
            txtDiscard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Reservation.this)
                            .navigate(R.id.action_SummaryOfCharges_to_Reservation);
                }
            });
            BackArrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Reservation.this)
                            .navigate(R.id.action_SummaryOfCharges_to_Reservation);
                }
            });

            final LinearLayout driverdetails1 = view.findViewById(R.id.driver_details);

            TextView driverName = view.findViewById(R.id.textV_driverName);
            TextView driverPhone = view.findViewById(R.id.TextV_DriverPhoneno);
            TextView driverEmail = view.findViewById(R.id.TextV_DriverEmail);


            String cust_FName=ReservationBundle.getString("cust_FName");
            String cust_LName=ReservationBundle.getString("cust_LName");
            String cust_MobileNo=ReservationBundle.getString("cust_MobileNo");
            String cust_Phoneno=ReservationBundle.getString("cust_Phoneno");
            String cust_Email=ReservationBundle.getString("cust_Email");

            driverName.setText(cust_FName + " " + cust_LName);
            driverPhone.setText(cust_MobileNo);
            driverEmail.setText(cust_Email);

            driverdetails_icon = view.findViewById(R.id.driverdetails_icon);
            driverdetails_icon.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {


                    if (driverdetails1.getVisibility() == View.GONE)
                    {
                        driverdetails1.setVisibility(View.VISIBLE);

                    } else
                        driverdetails1.setVisibility(View.GONE);

                }
            });

            SummaryOfChargeArrow.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    if (rlSummaryOfCharge.getVisibility() == View.GONE)
                    {
                        rlSummaryOfCharge.setVisibility(View.VISIBLE);
                        SummaryOfChargeArrowDown.setVisibility(View.VISIBLE);
                        SummaryOfChargeArrow.setVisibility(View.GONE);
                    }
                }
            });
            SummaryOfChargeArrowDown.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(rlSummaryOfCharge.getVisibility() == View.VISIBLE)
                    {
                        rlSummaryOfCharge.setVisibility(View.GONE);
                        SummaryOfChargeArrow.setVisibility(View.VISIBLE);
                        SummaryOfChargeArrowDown.setVisibility(View.GONE);
                    }
                }
            });

            JSONObject bodyParam = new JSONObject();
            try
            {
                bodyParam.accumulate("ForTransId", ReservationBundle.getInt("reservation_ID"));
                bodyParam.accumulate("BookingStep", 6);
                System.out.println(bodyParam);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            AndroidNetworking.initialize(getActivity());
            Fragment_Summary_Of_Charges_For_Reservation.context = getActivity();

            ApiService ApiService = new ApiService(getTaxtDetails, RequestType.POST,
                    BOOKING, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        llCancelBooking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                JSONObject bodyParam = new JSONObject();
                try
                {
                    bodyParam.accumulate("bookingId", reservation_ID);
                    System.out.println(bodyParam);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                AndroidNetworking.initialize(getActivity());
                Fragment_Summary_Of_Charges_For_Reservation.context = getActivity();

                ApiService ApiService = new ApiService(CancelBooking, RequestType.GET,
                        CANCELBOOKING, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);
            }
        });


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

                                    final JSONArray getsummaryOfCharges = resultSet.getJSONArray("summaryOfCharges");
                                    final RelativeLayout rlSummaryOfCharge = getActivity().findViewById(R.id.rl_Summaryofcharge);


                                    final JSONArray getIncludeDetails = resultSet.getJSONArray("includesItem");
                                    RelativeLayout rlIncludeDetails = getActivity().findViewById(R.id.rl_includeDetails1);

                                    if(!resultSet.isNull("t0050_Documents"))
                                    {
                                        //QrImage
                                        JSONObject Documents=resultSet.getJSONObject("t0050_Documents");
                                        final int doc_ID = Documents.getInt("doc_ID");
                                        final String doc_Name = Documents.getString("doc_Name");
                                        final String doc_Details = Documents.getString("doc_Details");
                                        final String doc_Status = Documents.getString("doc_Status");
                                        final String created_Date = Documents.getString("created_Date");

                                        String url2 = domainName+doc_Details.substring(2);
                                        System.out.println(url2);
                                        // url2=url2.substring(0,url2.lastIndexOf("/")+1)+doc_Name;
                                        imageLoader.displayImage(url2, QR_Image);
                                    }
                                    if(!resultSet.isNull("vehicleModel"))
                                    {
                                        //VehicleDetails
                                        JSONObject vehicleModel = resultSet.getJSONObject("vehicleModel");
                                        final String vehiclE_NAME = vehicleModel.getString("vehiclE_NAME");
                                        final String vehiclE_TYPE_NAME = vehicleModel.getString("vehiclE_TYPE_NAME");
                                        final String vehiclE_SEAT_NO = vehicleModel.getString("vehiclE_SEAT_NO");
                                        final String veh_bags = vehicleModel.getString("veh_bags");
                                        final String transmission_Name = vehicleModel.getString("transmission_Name");
                                        final String doors = vehicleModel.getString("doors");
                                        final double totaL_DAYS = vehicleModel.getDouble("totaL_DAYS");
                                        String totaLDAYSStr = (String.valueOf(totaL_DAYS));
                                        totaLDAYSStr = totaLDAYSStr.substring(0, totaLDAYSStr.length() - 2);
                                        txtDays.setText(totaLDAYSStr);

                                        txt_Seats.setText(vehiclE_SEAT_NO);
                                        txt_Bags.setText(veh_bags);
                                        txt_Automatic.setText(transmission_Name);
                                        txt_Doors.setText(doors);
                                    }

                                        //Summary Of Charges
                                        int len;
                                        len = getsummaryOfCharges.length();

                                        for (int j = 0; j < len; j++)
                                        {
                                            final JSONObject test = (JSONObject) getsummaryOfCharges.get(j);

                                            final int sortId = test.getInt("sortId");
                                            final String chargeCode = test.getString("chargeCode");
                                            final String chargeName = test.getString("chargeName");
                                            final Double chargeAmount = test.getDouble("chargeAmount");

                                            JSONObject summaryOfChargesObj = new JSONObject();

                                            summaryOfChargesObj.put("sortId", sortId);
                                            summaryOfChargesObj.put("chargeCode", chargeCode);
                                            summaryOfChargesObj.put("chargeName", chargeName);
                                            summaryOfChargesObj.put("chargeAmount", chargeAmount);

                                            summaryOfCharges.put(summaryOfChargesObj);

                                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                            lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                            lp.setMargins(0, 0, 0, 0);

                                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.vehicle_tax_details, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                            linearLayout.setId(200 + j);
                                            linearLayout.setLayoutParams(lp);

                                            TextView txt_charge, txt_chargeName;
                                            LinearLayout arrowIcon = (LinearLayout) linearLayout.findViewById(R.id.arrow_icon);

                                            if (sortId == 10)
                                            {
                                                arrowIcon.setVisibility(View.VISIBLE);
                                            }
                                            if (sortId == 12)
                                            {
                                                TotalAmount = chargeAmount;
                                                System.out.println(TotalAmount);
                                            }
                                            arrowIcon.setOnClickListener(new View.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(View view)
                                                {
                                                    Bundle Agreements = new Bundle();
                                                    Agreements.putBundle("ReservationBundle", ReservationBundle);
                                                    Agreements.putInt("backtoforterms", 3);
                                                    Agreements.putBoolean("TaxType", false);
                                                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Reservation.this)
                                                            .navigate(R.id.action_SummaryOfCharges_to_Total_tax_fee_details, Agreements);
                                                }
                                            });

                                            txt_charge = (TextView) linearLayout.findViewById(R.id.txt_charge);
                                            txt_chargeName = (TextView) linearLayout.findViewById(R.id.txt_chargeName);
                                            String str=String.format(Locale.US,"%.2f",chargeAmount);
                                            txt_charge.setText("USD$ "+str);
                                            txt_chargeName.setText(chargeName);

                                            if(chargeName.equals("Discount Applied"))
                                            {
                                                txt_charge.setTextColor(getResources().getColor(R.color.btn_bg_color_2));
                                            }                                            txt_chargeName.setText(chargeName);

                                            rlSummaryOfCharge.addView(linearLayout);
                                        }

                                       /* int len1;
                                        len1 = getIncludeDetails.length();
                                        for (int j = 0; j < len1; )
                                        {

                                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                            lp.addRule(RelativeLayout.BELOW, (200 + (j / 3) - 1));
                                            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                            lp.setMargins(0, 0, 0, 0);

                                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            LinearLayout linearLayout1 = (LinearLayout) inflater.inflate(R.layout.include_details, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                            linearLayout1.setId(200 + (j / 3));
                                            linearLayout1.setLayoutParams(lp);

                                            TextView txt_InsurancePro, txt_carMaintanance, txt_roadsideAssistance;
                                            txt_InsurancePro = (TextView) linearLayout1.findViewById(R.id.txt_InsurancePro);
                                            txt_carMaintanance = (TextView) linearLayout1.findViewById(R.id.txt_carMaintanance);
                                            txt_roadsideAssistance = (TextView) linearLayout1.findViewById(R.id.txt_roadsideAssistance);


                                            if (j < len1 && (j % 3) == 0) {
                                                final JSONObject test = (JSONObject) getIncludeDetails.get(j);
                                                final int includesId = test.getInt("includesId");
                                                final String includesName = test.getString("includesName");

                                                txt_InsurancePro.setText(includesName);
                                                j++;
                                            }
                                            if (j < len1 && (j % 3) == 1) {
                                                final JSONObject test = (JSONObject) getIncludeDetails.get(j);
                                                final int includesId = test.getInt("includesId");
                                                final String includesName = test.getString("includesName");

                                                txt_carMaintanance.setText(includesName);
                                                j++;
                                            }
                                            if (j < len1 && (j % 3) == 2) {
                                                final JSONObject test = (JSONObject) getIncludeDetails.get(j);
                                                final int includesId = test.getInt("includesId");
                                                final String includesName = test.getString("includesName");

                                                txt_roadsideAssistance.setText(includesName);
                                                j++;
                                            }
                                            rlIncludeDetails.addView(linearLayout1);
                                        }*/

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

    OnResponseListener CancelBooking = new OnResponseListener()
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
                            String msg = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),msg,0);
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
        public void onError(String error)
        {
            System.out.println("Error-" + error);
        }
    };

    }


