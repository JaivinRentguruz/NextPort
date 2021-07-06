package com.flexiicar.app.flexiicar.user;

import android.content.Context;
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
import android.widget.Toast;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_BOOKING;
import static com.flexiicar.app.apicall.ApiEndPoint.BOOKING;

public class Fragment_Finalize_Your_Rental_For_User extends Fragment
{
    ImageView backarrow,SummaryOfChargeArrowdown,SummaryOfChargeArrow;
    Handler handler = new Handler();
    public static Context context;
    Bundle ReservationBundle;
    LinearLayout linearLayout3,TermCondition_layout,lblpay,SummaryOfChargeLayout,greenlayout,blacklayout;;
    TextView txt_PickLocName,txt_ReturnLocName,txt_PickupDate,txt_ReturnDate,txt_PickupTime,txt_ReturnTIme,txtDays,txt_vehicletype,
            txt_vehName,txt_rate,txtTotalAmount,txt_Seats,txt_Bags,txt_Automatic,txt_Doors,txtpayNow,txtPayLater,txtDiscard,txtvehDesc;
    ImageView driverdetails_icon1,arrowflight_details;
    ImageLoader imageLoader;
    String serverpath="";

    public static void initImageLoader(Context context)
    {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MAX_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();
        ImageLoader.getInstance().init(config.build());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_finalize_your_rental, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
    {
        try {
            super.onViewCreated(view, savedInstanceState);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            initImageLoader(getActivity());
            imageLoader = ImageLoader.getInstance();
            ReservationBundle = getArguments().getBundle("ReservationBundle");

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            String id = sp.getString(getString(R.string.id), "");
            serverpath = sp.getString("serverPath", "");

            ImageView CarImage=view.findViewById(R.id.Veh_image_bg3);

            String VehicleImgstr=ReservationBundle.getString("veh_Img_Path");
            String url1 = serverpath+VehicleImgstr.substring(2);
            imageLoader.displayImage(url1,CarImage);

            linearLayout3 = view.findViewById(R.id.flight_details_1);
            TermCondition_layout = view.findViewById(R.id.lblterm_condition);
            lblpay = view.findViewById(R.id.layout_payment);
            backarrow = view.findViewById(R.id.backbtn2);
            greenlayout = view.findViewById(R.id.green_layout1);
            blacklayout = view.findViewById(R.id.black_layout1);
            SummaryOfChargeArrowdown = view.findViewById(R.id.SummaryofChargesArrowDown);
            SummaryOfChargeArrow= view.findViewById(R.id.img_bottomArrow);
            SummaryOfChargeLayout = view.findViewById(R.id.LinearL_SummaryOfCharges);
            txt_PickLocName = view.findViewById(R.id.txt_PickupLocation);
            txt_PickupDate = view.findViewById(R.id.txt_PickupLoationDate);
            txt_PickupTime = view.findViewById(R.id.txt_PickupLocationTime);
            txt_ReturnLocName = view.findViewById(R.id.txt_ReturnLocation);
            txt_ReturnDate = view.findViewById(R.id.txt_ReturnLocationDate);
            txt_ReturnTIme = view.findViewById(R.id.txt_ReturnLocationTime);
            txtDays = view.findViewById(R.id.textView_TotalDays);
            txt_rate = view.findViewById(R.id.txt_Vehiclerate);
            txt_Seats = view.findViewById(R.id.textView_Seats);
            txt_Bags = view.findViewById(R.id.textView_Bags);
            txt_Automatic = view.findViewById(R.id.textView_Automatic);
            txt_Doors = view.findViewById(R.id.textView_Doors);
            txtTotalAmount = view.findViewById(R.id.textview_TotalAmount);
            txtDiscard=view.findViewById(R.id.DiscardFinalizeRen);
            txt_vehName=view.findViewById(R.id.textV_VehicleModelName);
            txt_vehicletype=view.findViewById(R.id.txtV_TypeName);
            txtvehDesc=view.findViewById(R.id.txtvehDesciption);
            txtpayNow = view.findViewById(R.id.txt_paymentN);
            txtPayLater = view.findViewById(R.id.txt_PaymentL);
            txt_PickLocName.setText(ReservationBundle.getString("chk_Out_Location_Name"));
            txt_ReturnLocName.setText(ReservationBundle.getString("chk_In_Loc_Name"));

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

            txt_vehName.setText(ReservationBundle.getString("vehicle_Name"));
            txt_vehicletype.setText(ReservationBundle.getString("vehicle_Type_Name"));

            txt_Automatic.setText(ReservationBundle.getString("transmission_Name"));
            txtDays.setText(ReservationBundle.getString("totaL_DAYS"));
            txt_Bags.setText(ReservationBundle.getString("veh_bags"));
            txt_Seats.setText(ReservationBundle.getString("vehiclE_SEAT_NO"));
            txt_Doors.setText(ReservationBundle.getString("doors"));

            txt_rate.setText(ReservationBundle.getString("rate_ID"));
            //txtTotalAmount.setText(ReservationBundle.getString("chargeAmount"));
          //  txtvehDesc.setText(ReservationBundle.getString("vehDescription"));

            txtDiscard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Finalize_Your_Rental_For_User.this)
                            .navigate(R.id.action_Finalize_your_rental_to_Reservation);
                }
            });

            backarrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle Booking = new Bundle();
                    ReservationBundle.putInt("BookingStep", 3);
                    Booking.putString("DeliveryAndPickupModel", "");
                    Booking.putBundle("ReservationBundle",ReservationBundle);
                    NavHostFragment.findNavController(Fragment_Finalize_Your_Rental_For_User.this)
                            .navigate(R.id.action_Finalize_your_rental_to_Select_addtional_options, Booking);
                }
            });

            SummaryOfChargeArrow.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    if (SummaryOfChargeLayout.getVisibility() == View.GONE)
                    {
                        SummaryOfChargeLayout.setVisibility(View.VISIBLE);
                        SummaryOfChargeArrowdown.setVisibility(View.VISIBLE);
                        SummaryOfChargeArrow.setVisibility(View.GONE);
                    }
                }
            });

            SummaryOfChargeArrowdown.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(SummaryOfChargeLayout.getVisibility() == View.VISIBLE)
                    {
                        SummaryOfChargeLayout.setVisibility(View.GONE);
                        SummaryOfChargeArrow.setVisibility(View.VISIBLE);
                        SummaryOfChargeArrowdown.setVisibility(View.GONE);
                    }
                }
            });

            final Boolean[] isOnePressed = {true};
            final Boolean[] isSecondPlace = {true};
            greenlayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    isOnePressed[0] = true;
                    greenlayout.setBackground(getResources().getDrawable(R.drawable.ic_rectangle_yellow_green));
                    if (isSecondPlace[0])
                    {
                        blacklayout.setBackground(getResources().getDrawable(R.drawable.ic_rectangle_blackbox));
                        isSecondPlace[0] = false;
                    }
                }
            });

            blacklayout.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View arg0) {
                    blacklayout.setBackground(getResources().getDrawable(R.drawable.ic_rectangle_yellow_green));
                    isSecondPlace[0] = true;
                    if (isOnePressed[0]) {
                        greenlayout.setBackground(getResources().getDrawable(R.drawable.ic_rectangle_blackbox));
                        isOnePressed[0] = false;
                    }
                }
            });

            final LinearLayout flight_details_layout = view.findViewById(R.id.flight_details_layout);

            final TextView flightName = view.findViewById(R.id.flightName);
            final TextView flightNumber = view.findViewById(R.id.flightNumber);
            final TextView flightTime = view.findViewById(R.id.flightTime);

            linearLayout3.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {
                        String temp = ReservationBundle.getString("airPortModel");
                        if (temp != null)
                        {
                            JSONObject airPortModel = new JSONObject(temp);
                            if(flight_details_layout.getVisibility()== View.GONE)
                            {
                                flight_details_layout.setVisibility(View.VISIBLE);
                            }
                            else
                            flight_details_layout.setVisibility(View.GONE);

                            flightName.setText(airPortModel.getString("airLine"));
                            flightNumber.setText(airPortModel.getString("flightNumber"));
                            flightTime.setText(airPortModel.getString("arrivalDateTime"));
                        }
                        else
                            {
                            Bundle Reservation = new Bundle();
                            Reservation.putBundle("ReservationBundle", ReservationBundle);
                            Reservation.putInt("forflight", 2);
                            NavHostFragment.findNavController(Fragment_Finalize_Your_Rental_For_User.this)
                                    .navigate(R.id.action_Finalize_your_rental_to_flight_details, Reservation);
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            TermCondition_layout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle Reservation = new Bundle();
                    Reservation.putInt("termcondition", 5);
                    Reservation.putBundle("ReservationBundle", ReservationBundle);
                    NavHostFragment.findNavController(Fragment_Finalize_Your_Rental_For_User.this)
                            .navigate(R.id.action_Finalize_your_rental_to_TermAndCondition, Reservation);
                }
            });

            lblpay.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle Booking = new Bundle();
                    ReservationBundle.putInt("BookingStep", 5);
                    Booking.putBundle("ReservationBundle", ReservationBundle);
                    NavHostFragment.findNavController(Fragment_Finalize_Your_Rental_For_User.this)
                            .navigate(R.id.action_Finalize_your_rental_to_Payment_checkout, Booking);
                }
            });

            final LinearLayout driverdetails1 = view.findViewById(R.id.driverdetails1);
            String cust_FName=ReservationBundle.getString("cust_FName");
            String cust_LName=ReservationBundle.getString("cust_LName");
            String cust_MobileNo=ReservationBundle.getString("cust_MobileNo");
            String cust_Email=ReservationBundle.getString("cust_Email");

            TextView driverName = view.findViewById(R.id.driverName);
            TextView driverPhone = view.findViewById(R.id.driverPhone);
            TextView driverEmail = view.findViewById(R.id.driverEmail);

            driverName.setText(cust_FName + " " + cust_LName);
            driverPhone.setText(cust_MobileNo);
            driverEmail.setText(cust_Email);

            driverdetails_icon1 = view.findViewById(R.id.driverdetails_icon1);
            driverdetails_icon1.setOnClickListener(new View.OnClickListener()
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

            arrowflight_details = view.findViewById(R.id.arrowflight_details);
            arrowflight_details.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                }
            });

            JSONObject bodyParam = new JSONObject();
            try {
                bodyParam.accumulate("ForTransId", ReservationBundle.getInt("reservation_ID"));
                bodyParam.accumulate("CustomerId", ReservationBundle.getInt("customer_ID"));
                bodyParam.accumulate("VehicleTypeId", ReservationBundle.getInt("vehicle_Type_ID"));
                bodyParam.accumulate("VehicleID", ReservationBundle.getInt("vehicle_ID"));
                bodyParam.accumulate("BookingStep", ReservationBundle.getInt("BookingStep"));
                bodyParam.accumulate("DeliveryChargeLocID", ReservationBundle.getInt("DeliveryChargeLocID"));
                bodyParam.accumulate("DeliveryChargeAmount", ReservationBundle.getDouble("DeliveryChargeAmount"));
                bodyParam.accumulate("PickupChargeLocID", ReservationBundle.getInt("PickupChargeLocID"));
                bodyParam.accumulate("PickupChargeAmount", ReservationBundle.getDouble("PickupChargeAmount"));
                bodyParam.accumulate("DeductibleCoverId", ReservationBundle.getInt("DeductibleCoverId"));
                bodyParam.accumulate("DeductibleCharge", ReservationBundle.getDouble("DeductibleCharge"));

                bodyParam.accumulate("EquipmentList", new JSONArray(ReservationBundle.getString("EquipmentList")));
                bodyParam.accumulate("MiscList", new JSONArray(ReservationBundle.getString("MiscList")));
                bodyParam.accumulate("SummaryOfCharges", new JSONArray(ReservationBundle.getString("summaryOfCharges")));
                bodyParam.accumulate("DeliveryAndPickupModel", new JSONArray(ReservationBundle.getString("DeliveryAndPickupModel")));
                System.out.println(bodyParam);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            AndroidNetworking.initialize(getActivity());
            Fragment_Finalize_Your_Rental_For_User.context = getActivity();

            ApiService ApiService = new ApiService(getTaxtDetails, RequestType.POST,
                    BOOKING, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);
        }catch (Exception e)
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
                            try
                            {
                                JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                final JSONArray getsummaryOfCharges = resultSet.getJSONArray("summaryOfCharges");

                                final RelativeLayout rlSummaryOfCharge = getActivity().findViewById(R.id.rl_SummaryOfCharges);

                                int len;
                                len = getsummaryOfCharges.length();

                                for (int j = 0; j < len; j++)
                                {
                                    final JSONObject test = (JSONObject) getsummaryOfCharges.get(j);
                                    final int sortId = test.getInt("sortId");
                                    final String chargeCode = test.getString("chargeCode");
                                    final String chargeName = test.getString("chargeName");
                                    final Double chargeAmount = test.getDouble("chargeAmount");

                                    if (chargeName.equals("Estimated Total"))
                                    {
                                        txtTotalAmount.setText(((String.format(Locale.US,"%.2f",chargeAmount))));
                                        ReservationBundle.putDouble("total",chargeAmount);
                                    }
                                  /*  if (sortId==1)
                                    {
                                        txtpayNow.setText((String.format(Locale.US,"%.2f",chargeAmount)));
                                        txtPayLater.setText((String.format(Locale.US,"%.2f",chargeAmount)));
                                    }*/
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
                                    arrowIcon.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View view)
                                        {
                                            Bundle Reservation = new Bundle();
                                            Reservation.putBundle("ReservationBundle", ReservationBundle);
                                            Reservation.putInt("backtoforterms",4);
                                            Reservation.putInt("BookingStep",4);
                                            Reservation.putBoolean("TaxType",false);
                                            NavHostFragment.findNavController(Fragment_Finalize_Your_Rental_For_User.this)
                                                    .navigate(R.id.action_Finalize_your_rental_to_Total_tax_fee_details, Reservation);
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
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            String errorString = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),errorString,1);
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

