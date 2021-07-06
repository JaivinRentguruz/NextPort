package com.flexiicar.app.flexiicar.booking;

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

public class Fragment_Finalize_Your_Rental extends Fragment
{
    ImageView backarrow,SummaryOfChargeArrowdown,SummaryOfChargeArrow,CarImage;
    Handler handler = new Handler();
    public static Context context;
    Bundle BookingBundle,VehicleBundle;
    LinearLayout TermCondition_layout,lblpay,SummaryOfChargeLayout,greenlayout,blacklayout;
    TextView txt_PickLocName,txt_ReturnLocName,txt_PickupDate,txt_ReturnDate,txt_PickupTime,txt_ReturnTIme,txtDays,txt_vehicletype,
            txt_vehName,txtMileage,txt_rate,txtTotalAmount,txt_Seats,txt_Bags,txt_Automatic,txt_Doors,txtpayNow,txtPayLater,txt_Discard,txtvehDesc;

    ImageView driverdetails_icon1,arrowflight_details;

    Bundle returnLocationBundle, locationBundle;
    Boolean locationType, initialSelect;

    ImageLoader imageLoader;
    String serverpath="",VehImage="";
    String id="";
    TextView driverName,driverPhone,driverEmail;
    TextView txt_driverdetails,txt_AsGuestdriver;
    double totalMilesAllowed;
    int cmP_DISTANCE;

    public static void initImageLoader(Context context)
    {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.MAX_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_finalize_your_rental, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            initImageLoader(getActivity());
            imageLoader = ImageLoader.getInstance();

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            id = sp.getString(getString(R.string.id), "");
            serverpath = sp.getString("serverPath", "");
            cmP_DISTANCE=sp.getInt("cmP_DISTANCE",0);

            TermCondition_layout = view.findViewById(R.id.lblterm_condition);
            lblpay = view.findViewById(R.id.layout_payment);
            backarrow = view.findViewById(R.id.backbtn2);
            SummaryOfChargeArrowdown = view.findViewById(R.id.SummaryofChargesArrowDown);
            SummaryOfChargeArrow= view.findViewById(R.id.img_bottomArrow);
            SummaryOfChargeLayout = view.findViewById(R.id.LinearL_SummaryOfCharges);
            greenlayout = view.findViewById(R.id.green_layout1);
            blacklayout = view.findViewById(R.id.black_layout1);
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
            txt_Discard = view.findViewById(R.id.DiscardFinalizeRen);
            txtvehDesc=view.findViewById(R.id.txtvehDesciption);
            txt_vehName = view.findViewById(R.id.textV_VehicleModelName);
            txt_vehicletype = view.findViewById(R.id.txtV_TypeName);

            txtpayNow = view.findViewById(R.id.txt_paymentN);
            txtPayLater = view.findViewById(R.id.txt_PaymentL);

            txtMileage= view.findViewById(R.id.txtMileage);

            BookingBundle = getArguments().getBundle("BookingBundle");
            VehicleBundle = getArguments().getBundle("VehicleBundle");

            returnLocationBundle = getArguments().getBundle("returnLocation");
            locationBundle = getArguments().getBundle("location");
            locationType = getArguments().getBoolean("locationType");
            initialSelect = getArguments().getBoolean("initialSelect");

            txt_PickLocName.setText(BookingBundle.getString("PickupLocName"));
            txt_ReturnLocName.setText(BookingBundle.getString("ReturnLocName"));

            totalMilesAllowed=(VehicleBundle.getDouble("totalMilesAllowed"));

            if(cmP_DISTANCE==1)
            {
                String Miles=(String.valueOf(totalMilesAllowed));
                txtMileage.setText(Miles+"kms");
            }
            else {
                String Miles=(String.valueOf(totalMilesAllowed));
                txtMileage.setText(Miles+"kms");
            }

            String StrPickupDate = (BookingBundle.getString("PickupDate"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(StrPickupDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String PickUpDateStr = sdf.format(date);
            txt_PickupDate.setText(PickUpDateStr);


            String StrReturnDate = (BookingBundle.getString("ReturnDate"));
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = dateFormat1.parse(StrReturnDate);
            System.out.println(date1);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            String ReturnDateStr = sdf1.format(date1);
            txt_ReturnDate.setText(ReturnDateStr);


            String strPickUpTime = (BookingBundle.getString("PickupTime"));
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
            Date date2 = dateFormat2.parse(strPickUpTime);
            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm aa", Locale.US);
            String PickUpTimeStr = sdf2.format(date2);
            txt_PickupTime.setText(PickUpTimeStr);

            String strReturntime = (BookingBundle.getString("ReturnTime"));
            SimpleDateFormat dateFormat3 = new SimpleDateFormat("HH:mm");
            Date date3 = dateFormat3.parse(strReturntime);
            SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa", Locale.US);
            String ReturntimeStr = sdf3.format(date3);
            txt_ReturnTIme.setText(ReturntimeStr);

            Double totaL_DAYS=VehicleBundle.getDouble("totaL_DAYS");
            // DayStr=DayStr.substring(0,DayStr.length()-2);
            txtDays.setText(String.valueOf(totaL_DAYS));

            txt_vehName.setText(VehicleBundle.getString("vehiclE_NAME"));
            txt_vehicletype.setText(VehicleBundle.getString("vehiclE_TYPE_NAME"));

            txt_Seats.setText(VehicleBundle.getString("vehiclE_SEAT_NO"));
            txt_Bags.setText(VehicleBundle.getString("veh_bags"));
            txt_Automatic.setText(VehicleBundle.getString("transmission_Name"));
            txt_Doors.setText(VehicleBundle.getString("doors"));
            txt_rate.setText(VehicleBundle.getString("rate_ID"));
            txtvehDesc.setText(VehicleBundle.getString("vehDescription"));

            driverName = view.findViewById(R.id.driverName);
            driverPhone = view.findViewById(R.id.driverPhone);
            driverEmail = view.findViewById(R.id.driverEmail);

            CarImage = view.findViewById(R.id.Veh_image_bg3);

            VehImage = VehicleBundle.getString("img_Path");

            String url1 = serverpath + VehImage;
            imageLoader.displayImage(url1, CarImage);

            txt_Discard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                            .navigate(R.id.action_Finalize_your_rental_to_Search_activity);
                }
            });
            backarrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle Booking = new Bundle();
                    BookingBundle.putInt("BookingStep", 3);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    Booking.putString("DeliveryAndPickupModel", "");
                    NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
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
                    if (isSecondPlace[0]) {
                        blacklayout.setBackground(getResources().getDrawable(R.drawable.ic_rectangle_blackbox));
                        isSecondPlace[0] = false;
                    }
                }
            });
            blacklayout.setOnClickListener(new View.OnClickListener() {

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

            arrowflight_details = view.findViewById(R.id.arrowflight_details);

            arrowflight_details.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {
                        String temp = BookingBundle.getString("airPortModel");

                        if (temp != null)
                        {
                            JSONObject airPortModel = new JSONObject(temp);
                            if (flight_details_layout.getVisibility() == View.GONE)
                            {
                                flight_details_layout.setVisibility(View.VISIBLE);

                            } else
                                flight_details_layout.setVisibility(View.GONE);

                            flightName.setText(airPortModel.getString("airLine"));
                            flightNumber.setText(airPortModel.getString("flightNumber"));
                            String flightTimeStr = airPortModel.getString("arrivalDateTime");
                            flightTime.setText(airPortModel.getString("arrivalDateTime"));

                        } else
                            {
                            Bundle Booking = new Bundle();
                            Booking.putBundle("BookingBundle", BookingBundle);
                            Booking.putBundle("VehicleBundle", VehicleBundle);
                            Booking.putBundle("returnLocation", returnLocationBundle);
                            Booking.putBundle("location", locationBundle);
                            Booking.putBoolean("locationType", locationType);
                            Booking.putBoolean("initialSelect", initialSelect);
                            Booking.putInt("forflight", 1);
                            NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                                    .navigate(R.id.action_Finalize_your_rental_to_flight_details, Booking);

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
                    Bundle Booking = new Bundle();
                    Booking.putInt("termcondition", 1);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                            .navigate(R.id.action_Finalize_your_rental_to_Term_and_Condtion, Booking);
                }
            });

            lblpay.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(!id.equals(""))
                    {
                        Bundle Booking = new Bundle();
                        BookingBundle.putInt("BookingStep", 5);
                        Booking.putBundle("BookingBundle", BookingBundle);
                        Booking.putBundle("VehicleBundle", VehicleBundle);
                        Booking.putBundle("returnLocation", returnLocationBundle);
                        Booking.putBundle("location", locationBundle);
                        Booking.putBoolean("locationType", locationType);
                        Booking.putBoolean("initialSelect", initialSelect);
                        Booking.putBoolean("isDefaultCard", true);
                        System.out.println("id not null"+Booking);
                        NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                                .navigate(R.id.action_Finalize_your_rental_to_Payment_checkout, Booking);
                    }
                    else
                    {
                        String temp = BookingBundle.getString("GuestDriverDetails");
                        System.out.println(temp);
                        if (temp != null)
                        {
                            try {
                                Bundle Booking = new Bundle();
                                BookingBundle.putInt("BookingStep", 5);
                                Booking.putBundle("BookingBundle", BookingBundle);
                                Booking.putBundle("VehicleBundle", VehicleBundle);
                                Booking.putBundle("returnLocation", returnLocationBundle);
                                Booking.putBundle("location", locationBundle);
                                Booking.putBoolean("locationType", locationType);
                                Booking.putBoolean("initialSelect", initialSelect);
                                System.out.println("id null"+Booking);
                                NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                                        .navigate(R.id.action_Finalize_your_rental_to_Payment_checkout, Booking);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            String msgString = "Please add guest driver details";
                            CustomToast.showToast(getActivity(), msgString, 1);

                            Bundle Booking = new Bundle();
                            Booking.putBundle("BookingBundle", BookingBundle);
                            Booking.putBundle("VehicleBundle", VehicleBundle);
                            Booking.putBundle("returnLocation", returnLocationBundle);
                            Booking.putBundle("location", locationBundle);
                            Booking.putBoolean("locationType", locationType);
                            Booking.putBoolean("initialSelect", initialSelect);
                            System.out.println(Booking);
                            NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                                    .navigate(R.id.action_Finalize_your_rental_to_Guest_driver_details, Booking);
                        }
                    }
                }
            });

            final LinearLayout driverdetails1 = view.findViewById(R.id.driverdetails1);

            driverdetails_icon1 = view.findViewById(R.id.driverdetails_icon1);

            txt_driverdetails= view.findViewById(R.id.txt_driverdetails);
            txt_AsGuestdriver= view.findViewById(R.id.txt_AsGuestdriver);

            if(id.equals(""))
            {
                txt_AsGuestdriver.setVisibility(View.VISIBLE);
                txt_driverdetails.setVisibility(View.GONE);
                driverdetails1.setVisibility(View.VISIBLE);

                String temp = BookingBundle.getString("GuestDriverDetails");
                System.out.println(temp);

                if (temp != null)
                {
                    JSONObject GuestDriverModel = new JSONObject(temp);

                    String FirstName = GuestDriverModel.getString("firstName");
                    String LastName = GuestDriverModel.getString("lastName");
                    String Fullname = FirstName + LastName;
                    driverName.setText(Fullname);
                    driverPhone.setText(GuestDriverModel.getString("driverEmail"));
                    driverEmail.setText(GuestDriverModel.getString("driverPhoneNo"));
                    System.out.println(temp);
                }
                else
                    {
                    driverdetails_icon1.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            try {
                                Bundle Booking = new Bundle();
                                Booking.putBundle("BookingBundle", BookingBundle);
                                Booking.putBundle("VehicleBundle", VehicleBundle);
                                Booking.putBundle("returnLocation", returnLocationBundle);
                                Booking.putBundle("location", locationBundle);
                                Booking.putBoolean("locationType", locationType);
                                Booking.putBoolean("initialSelect", initialSelect);
                                System.out.println(Booking);
                                NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                                        .navigate(R.id.action_Finalize_your_rental_to_Guest_driver_details, Booking);
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
            else
                {
                txt_driverdetails.setVisibility(View.VISIBLE);
                txt_AsGuestdriver.setVisibility(View.GONE);

                    driverdetails_icon1.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
                            String cust_FName = sp.getString("cust_FName", "");
                            String cust_LName = sp.getString("cust_LName", "");
                            String cust_Email = sp.getString("cust_Email", "");
                            String cust_Phoneno = sp.getString("cust_MobileNo", "");

                            driverName.setText(cust_FName + " " + cust_LName);
                            driverPhone.setText(cust_Phoneno);
                            driverEmail.setText(cust_Email);

                            if (driverdetails1.getVisibility() == View.GONE)
                            {
                                driverdetails1.setVisibility(View.VISIBLE);
                            } else
                                driverdetails1.setVisibility(View.GONE);
                        }
                    });
            }

            JSONObject bodyParam = new JSONObject();
            try
            {
                bodyParam.accumulate("ForTransId", BookingBundle.getInt("ForTransId"));
                bodyParam.accumulate("PickupLocId", BookingBundle.getInt("PickupLocId"));
                bodyParam.accumulate("ReturnLocId", BookingBundle.getInt("ReturnLocId"));
                bodyParam.accumulate("CustomerId", BookingBundle.getInt("CustomerId"));
                bodyParam.accumulate("VehicleTypeId", BookingBundle.getInt("vehiclE_TYPE_ID"));
                bodyParam.accumulate("VehicleID", BookingBundle.getInt("VehicleID"));
                bodyParam.accumulate("StrFilterVehicleTypeIds", BookingBundle.getString("StrFilterVehicleTypeIds"));
                bodyParam.accumulate("StrFilterVehicleOptionIds", BookingBundle.getString("StrFilterVehicleOptionIds"));

                String dateFormatPickupDate = (BookingBundle.getString("PickupDate"));
                String strPickUpTime1 = (BookingBundle.getString("PickupTime"));
                String PickupDateTime=dateFormatPickupDate+"T"+strPickUpTime1;
                bodyParam.accumulate("PickupDate",PickupDateTime);
                String dateFormatReturnDate = (BookingBundle.getString("ReturnDate"));
                String strReturnTime1 = (BookingBundle.getString("ReturnTime"));
                String ReturnDateTime=dateFormatReturnDate+"T"+strReturnTime1;

                bodyParam.accumulate("returnDate", ReturnDateTime);
                bodyParam.accumulate("FilterTransmission", BookingBundle.getInt("FilterTransmission"));
                bodyParam.accumulate("FilterPassengers", BookingBundle.getInt("FilterPassengers"));
                bodyParam.accumulate("BookingStep", BookingBundle.getInt("BookingStep"));
                bodyParam.accumulate("BookingType", BookingBundle.getInt("BookingType"));
                bodyParam.accumulate("DeliveryChargeLocID", BookingBundle.getInt("DeliveryChargeLocID"));
                bodyParam.accumulate("DeliveryChargeAmount", BookingBundle.getDouble("DeliveryChargeAmount"));
                bodyParam.accumulate("PickupChargeLocID", BookingBundle.getInt("PickupChargeLocID"));
                bodyParam.accumulate("PickupChargeAmount", BookingBundle.getDouble("PickupChargeAmount"));
                bodyParam.accumulate("DeductibleCoverId", BookingBundle.getInt("DeductibleCoverId"));
                bodyParam.accumulate("DeductibleCharge", BookingBundle.getDouble("DeductibleCharge"));
                bodyParam.accumulate("EquipmentList", new JSONArray(BookingBundle.getString("EquipmentList")));
                bodyParam.accumulate("MiscList", new JSONArray(BookingBundle.getString("MiscList")));
                bodyParam.accumulate("SummaryOfCharges", new JSONArray(BookingBundle.getString("SummaryOfCharges")));
              // bodyParam.accumulate("DeliveryAndPickupModel", new JSONArray(BookingBundle.getString("DeliveryAndPickupModel")));
                System.out.println(bodyParam);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            AndroidNetworking.initialize(getActivity());
            Fragment_Finalize_Your_Rental.context = getActivity();

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

                                    if(test.has("chargeCode"))
                                    {
                                        final String chargeCode = test.getString("chargeCode");
                                    }

                                    final String chargeName = test.getString("chargeName");
                                    final Double chargeAmount = test.getDouble("chargeAmount");

                                    if (chargeName.equals("Estimated Total"))
                                    {
                                        txtTotalAmount.setText(((String.format(Locale.US,"%.2f",chargeAmount))));
                                        BookingBundle.putDouble("total",chargeAmount);
                                    }
                                    if (sortId==1)
                                    {
                                        txtpayNow.setText((String.format(Locale.US,"%.2f",chargeAmount)));
                                        txtPayLater.setText((String.format(Locale.US,"%.2f",chargeAmount)));
                                    }

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
                                            Bundle Booking = new Bundle();
                                            Booking.putBundle("VehicleBundle", VehicleBundle);
                                            Booking.putBundle("BookingBundle", BookingBundle);
                                            Booking.putBundle("returnLocation", returnLocationBundle);
                                            Booking.putBundle("location", locationBundle);
                                            Booking.putBoolean("locationType", locationType);
                                            Booking.putBoolean("initialSelect", initialSelect);
                                            BookingBundle.putInt("BookingStep", 4);
                                            Booking.putInt("backtoforterms",2);
                                            Booking.putBoolean("TaxType",true);
                                            NavHostFragment.findNavController(Fragment_Finalize_Your_Rental.this)
                                                    .navigate(R.id.action_Finalize_your_rental_to_Total_tax_fee_details, Booking);
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

