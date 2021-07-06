package com.flexiicar.app.flexiicar.user;

import android.content.Context;
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

public class Fragment_Summary_Of_Charges_For_Agreements extends Fragment
{
    Handler handler = new Handler();
    public static Context context;
    Bundle AgreementsBundle;
    ImageView SummaryOfChargeArrow,SummaryOfChargeArrowDown,QRImage,arrowSelfcheckOut,driverdetails_icon,ArrowFlight;
    RelativeLayout rlSummaryOfCharge;
    TextView CheckOutLocName,CheckOutDate,CheckInLocName,CheckInDate, txtDays,txt_vehicletype,
            txt_vehName,txt_rate,txt_Seats,txt_Bags,txt_Automatic,txt_Doors,txt_conformationNo,txt_CreatedDate;
    LinearLayout lblterm_condition1,Layout_SelfCheckoutGreen,Layout_SelfCheckInGreen,flightdetailsLayout,
            ArrowselfcheckOut,ArrowselfcheckIn;
    int reservation_ID,agreement_ID;
    JSONArray summaryOfCharges = new JSONArray();
    ImageLoader imageLoader;
    String serverpath="",domainName="";
    ImageView BackArrow;
    String doc_Name,doc_Details;
    TextView txtDiscard;
    String FlightTimeStr,StatusName;
    LinearLayout AddCalender;

    public static void initImageLoader(Context context)
    {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MAX_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); //50 Mib
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_summary_of_charges_for_agreements, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        try {
            initImageLoader(getActivity());
            imageLoader = ImageLoader.getInstance();

            AddCalender=view.findViewById(R.id.AddCalender);
            BackArrow=view.findViewById(R.id.backtoAgreements);
            txtDiscard=view.findViewById(R.id.discard_summaryofcharge);
            lblterm_condition1 = view.findViewById(R.id.termAndCondition1);
            Layout_SelfCheckoutGreen = view.findViewById(R.id.Layout_SelfCheckoutGreen);
            Layout_SelfCheckInGreen= view.findViewById(R.id.Layout_SelfCheckInGreen);
            arrowSelfcheckOut = view.findViewById(R.id.ArrowSelfcheckout);

            ArrowselfcheckOut= view.findViewById(R.id.ArrowselfcheckOut);
            ArrowselfcheckIn= view.findViewById(R.id.ArrowselfcheckIn);

            QRImage = view.findViewById(R.id.QRImage);
            ArrowFlight=view.findViewById(R.id.ArrowFlight);

            SummaryOfChargeArrow = view.findViewById(R.id.ArrowForSummaryOfCharges);
            SummaryOfChargeArrowDown= view.findViewById(R.id.DownArrowForSummaryOfCharges);

            rlSummaryOfCharge = view.findViewById(R.id.rlSummaryofcharge);
            flightdetailsLayout= view.findViewById(R.id.flightdetails);

            txt_conformationNo=view.findViewById(R.id.txtConformationNo);
            CheckOutLocName = view.findViewById(R.id.CheckOutLocName);
            CheckOutDate = view.findViewById(R.id.CheckOutDate);
            CheckInLocName = view.findViewById(R.id.CheckInLocName);
            CheckInDate = view.findViewById(R.id.CheckInDate);
            txt_CreatedDate=view.findViewById(R.id.txtDateAgreements);

            txtDays = view.findViewById(R.id.txt_TotalDay2);
            txt_rate = view.findViewById(R.id.txt_Vehiclerate2);

            txt_Seats = view.findViewById(R.id.textViewSeats);
            txt_Bags = view.findViewById(R.id.textViewBags);
            txt_Automatic = view.findViewById(R.id.txtAutoMatic);
            txt_Doors = view.findViewById(R.id.txtDoor);

            txt_vehName = view.findViewById(R.id.VehicleName);
            txt_vehicletype = view.findViewById(R.id.VehicleType_name);

            AgreementsBundle = getArguments().getBundle("AgreementsBundle");

            CheckOutLocName.setText(AgreementsBundle.getString("check_Out_Location_Name"));
            CheckInLocName.setText(AgreementsBundle.getString("check_in_Location_Name"));
            final String check_IN=(AgreementsBundle.getString("check_IN"));
            // Date
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm aa");
            Date date1 = dateFormat1.parse(check_IN);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/ yyyy, HH:mm aa", Locale.US);
            String checkinStr = sdf1.format(date1);
            CheckOutDate.setText(checkinStr);

            final String CheckOut=(AgreementsBundle.getString("check_Out"));

            //check_Out Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            Date date = dateFormat.parse(CheckOut);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm aa", Locale.US);
            String check_OutStr = sdf.format(date);
            CheckInDate.setText(check_OutStr);

            txt_vehName.setText(AgreementsBundle.getString("vehicle_Type_Name"));
            txt_vehicletype.setText(AgreementsBundle.getString("vehicle"));
            
            String DayStr=AgreementsBundle.getString("totalDays");
            DayStr=DayStr.substring(0,DayStr.length()-2);
            txtDays.setText(DayStr);

            txt_Bags.setText(AgreementsBundle.getString("veh_Bags"));
            txt_rate.setText(AgreementsBundle.getString("rate_ID"));
            txt_Automatic.setText(AgreementsBundle.getString("transmission_Name"));

            reservation_ID=AgreementsBundle.getInt("reservation_ID");
            StatusName=AgreementsBundle.getString("status_Name");
            agreement_ID=AgreementsBundle.getInt("agreement_ID");

            txt_conformationNo.setText(String.valueOf(reservation_ID));
            txt_Seats.setText(AgreementsBundle.getString("veh_Seat"));

            String Date=(AgreementsBundle.getString("default_Created_Date"));
           /* SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date2 = dateFormat2.parse(Date);
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy,HH:mm aa",Locale.US);
            String Issue_DateStr = sdf2.format(date2);*/
            txt_CreatedDate.setText("SEE YOU ON "+(CheckInDate.getText().toString()));

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            serverpath = sp.getString("serverPath", "");
            domainName= sp.getString("domainName", "");

            String id = sp.getString(getString(R.string.id), "");

            ImageView Vehicle_Img=view.findViewById(R.id.Vehicle_Img);

            String VehicleImgstr=AgreementsBundle.getString("veh_Img_Path");
            String url1 = serverpath+VehicleImgstr.substring(2);
            imageLoader.displayImage(url1,Vehicle_Img);

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

            txtDiscard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_Agreements);
                }
            });

            BackArrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_Agreements);
                }
            });

            arrowSelfcheckOut.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    arrowSelfcheckOut.setVisibility(View.INVISIBLE);
                    if(StatusName.equals("Open"))
                    {
                        if (Layout_SelfCheckoutGreen.getVisibility() == View.GONE)
                        {
                            Layout_SelfCheckoutGreen.setVisibility(View.VISIBLE);

                        } else
                            Layout_SelfCheckoutGreen.setVisibility(View.GONE);
                    }
                    else
                    {
                        if (Layout_SelfCheckInGreen.getVisibility() == View.GONE)
                        {
                            Layout_SelfCheckInGreen.setVisibility(View.VISIBLE);
                        } else
                            Layout_SelfCheckInGreen.setVisibility(View.GONE);
                    }
                }
            });

            lblterm_condition1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle Agreements = new Bundle();
                    Agreements.putInt("termcondition", 3);
                    Agreements.putBundle("AgreementsBundle", AgreementsBundle);
                    System.out.println(Agreements);
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_TermAndCondition,Agreements);
                }
            });

            ArrowselfcheckOut.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle Agreements=new Bundle();
                    Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                    System.out.println(Agreements);
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_Self_check_Out,Agreements);
                }
            });

            ArrowselfcheckIn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Bundle Agreements=new Bundle();
                    AgreementsBundle.putString("doc_Name",doc_Name);
                    AgreementsBundle.putString("doc_Details",doc_Details);
                    Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                    System.out.println(Agreements);
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                            .navigate(R.id.action_SummaryOfChargesForAgreements_to_LocationKey_ForSelfCheckOut,Agreements);
                }
            });

            final LinearLayout driverdetails1 = view.findViewById(R.id.Driverdetails);

            TextView driverName = view.findViewById(R.id.textDriverName);
            TextView driverPhone = view.findViewById(R.id.TextDriverPhoneno);
            TextView driverEmail = view.findViewById(R.id.TextDriverEmail);

            String cust_FName=AgreementsBundle.getString("cust_FName");
            String cust_LName=AgreementsBundle.getString("cust_LName");
            String cust_MobileNo=AgreementsBundle.getString("cust_MobileNo");
            String cust_Email=AgreementsBundle.getString("cust_Email");

            driverName.setText(cust_FName + " " + cust_LName);
            driverPhone.setText(cust_MobileNo);
            driverEmail.setText(cust_Email);

            driverdetails_icon = view.findViewById(R.id.driversIcon1);
            driverdetails_icon.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (driverdetails1.getVisibility() == View.GONE)
                    {
                        driverdetails1.setVisibility(View.VISIBLE);
                    }
                    else {
                        driverdetails1.setVisibility(View.GONE);
                    }
                }
            });

            final TextView flightName=view.findViewById(R.id.lblFlightName);
            final TextView flightNo=view.findViewById(R.id.lblflightNumber);
            final TextView flightTime=view.findViewById(R.id.lblflightTime);

            ArrowFlight.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try {
                        flightName.setText(getArguments().getBundle("AgreementsBundle").getString("d_Airport"));
                        flightNo.setText(getArguments().getBundle("AgreementsBundle").getString("d_FlightNo"));

                        FlightTimeStr = (getArguments().getBundle("AgreementsBundle").getString("default_D_Time"));

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                        Date date = dateFormat.parse(FlightTimeStr);
                        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy,HH:mm aa", Locale.US);
                        String FlightTimeDateStr = sdf.format(date);
                        flightTime.setText(FlightTimeDateStr);

                        if(flightdetailsLayout.getVisibility()== View.GONE)
                        {
                            flightdetailsLayout.setVisibility(View.VISIBLE);
                        }
                        else
                            flightdetailsLayout.setVisibility(View.GONE);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
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
                bodyParam.accumulate("ForTransId", AgreementsBundle.getInt("reservation_ID"));
                bodyParam.accumulate("BookingStep", 6);
                System.out.println(bodyParam);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            AndroidNetworking.initialize(getActivity());
            Fragment_Summary_Of_Charges_For_Agreements.context = getActivity();

            ApiService ApiService = new ApiService(getTaxtDetails, RequestType.POST,
                    BOOKING, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
                            try {
                                JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                final JSONArray getsummaryOfCharges = resultSet.getJSONArray("summaryOfCharges");
                                final RelativeLayout rlSummaryOfCharge = getActivity().findViewById(R.id.rlSummaryofcharge);

                                final JSONArray getIncludeDetails = resultSet.getJSONArray("includesItem");
                                RelativeLayout rlIncludeDetails = getActivity().findViewById(R.id.rl_includeDetailsItem);

                                if(resultSet.equals("t0050_Documents"))
                                {
                                    //QrImage
                                    JSONObject Documents=resultSet.getJSONObject("t0050_Documents");
                                    final int doc_ID = Documents.getInt("doc_ID");
                                    doc_Name = Documents.getString("doc_Name");
                                    doc_Details = Documents.getString("doc_Details");
                                    final String doc_Status = Documents.getString("doc_Status");
                                    final String created_Date = Documents.getString("created_Date");

                                    String url2 = domainName+doc_Details.substring(2);
                                    System.out.println(url2);
                                    //url2=url2.substring(0,url2.lastIndexOf("/")+1)+doc_Name;
                                    imageLoader.displayImage(url2, QRImage);
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

                                    if (sortId == 10) {
                                        arrowIcon.setVisibility(View.VISIBLE);
                                    }
                                    arrowIcon.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View view) {
                                            Bundle Agreements = new Bundle();
                                            Agreements.putBundle("AgreementsBundle", AgreementsBundle);
                                            Agreements.putInt("backtoforterms", 6);
                                            Agreements.putBoolean("TaxType", false);
                                            NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_Agreements.this)
                                                    .navigate(R.id.action_SummaryOfChargesForAgreements_to_Total_tax_fee_details, Agreements);
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
                                    }
                                    txt_chargeName.setText(chargeName);

                                    rlSummaryOfCharge.addView(linearLayout);
                                }

                                int len1;
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
                                }

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
        public void onError(String error)
        {
            System.out.println("Error-" + error);
        }
    };
}


