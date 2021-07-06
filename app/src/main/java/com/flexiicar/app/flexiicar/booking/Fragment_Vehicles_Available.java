package com.flexiicar.app.flexiicar.booking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_BOOKING;
import static com.flexiicar.app.apicall.ApiEndPoint.BOOKING;

public class Fragment_Vehicles_Available extends Fragment
{
    LinearLayout lblselectedlocation, filter_icon;
    ImageView backarrowimage;
    public static Context context;
    public String id = "";
    Bundle BookingBundle;
    Handler handler = new Handler();
    ImageLoader imageLoader;
    String serverpath="";
    Bundle returnLocationBundle, locationBundle;
    Boolean locationType, initialSelect;
    EditText edt_searchVehicleName;
    TextView txt_Discard;
    Double Totalprice;
    JSONArray VehicleList = new JSONArray();

    public static void initImageLoader(Context context)
    {
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
        return inflater.inflate(R.layout.fragment_vehicles_available, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initImageLoader(getActivity());
        imageLoader = ImageLoader.getInstance();

        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        serverpath = sp.getString("serverPath", "");

        txt_Discard=view.findViewById(R.id.txt_discardVehAvlbl);

        returnLocationBundle = getArguments().getBundle("returnLocation");
        locationBundle = getArguments().getBundle("location");
        locationType = getArguments().getBoolean("locationType");
        initialSelect = getArguments().getBoolean("initialSelect");

        lblselectedlocation = view.findViewById(R.id.lblvehicles_avilable);
        backarrowimage = view.findViewById(R.id.backarrowimg);
        edt_searchVehicleName= view.findViewById(R.id.edt_searchVehicleName);
        BookingBundle = getArguments().getBundle("BookingBundle");

        txt_Discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                        .navigate(R.id.action_Vehicles_Available_to_Search_activity);
            }
        });
        filter_icon = view.findViewById(R.id.filter_icon);
        filter_icon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle Booking = new Bundle();
                Booking.putBundle("BookingBundle", BookingBundle);
                Booking.putBundle("returnLocation", returnLocationBundle);
                Booking.putBundle("location", locationBundle);
                Booking.putBoolean("locationType", locationType);
                Booking.putBoolean("initialSelect", initialSelect);
                NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                        .navigate(R.id.action_Vehicles_Available_to_Select_addtional_options,Booking);
            }
        });
        backarrowimage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle Booking = new Bundle();
                Booking.putBundle("returnLocation", returnLocationBundle);
                Booking.putBundle("location", locationBundle);
                Booking.putBoolean("locationType", locationType);
                Booking.putBoolean("initialSelect", initialSelect);
                NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                        .navigate(R.id.action_Vehicles_Available_to_Selected_location, Booking);
            }
        });
        Fragment_Vehicles_Available.context = getActivity();

        JSONObject bodyParam = new JSONObject();
        try
        {
            bodyParam.accumulate("ForTransId", BookingBundle.getInt("ForTransId"));
            bodyParam.accumulate("PickupLocId", BookingBundle.getInt("PickupLocId"));
            bodyParam.accumulate("ReturnLocId", BookingBundle.getInt("ReturnLocId"));
            bodyParam.accumulate("CustomerId", BookingBundle.getInt("CustomerId"));
            bodyParam.accumulate("VehicleTypeId", BookingBundle.getInt("VehicleTypeId"));
            bodyParam.accumulate("VehicleID", BookingBundle.getInt("VehicleID"));
            bodyParam.accumulate("StrFilterVehicleTypeIds", BookingBundle.getString("StrFilterVehicleTypeIds"));
            bodyParam.accumulate("StrFilterVehicleOptionIds", BookingBundle.getString("StrFilterVehicleOptionIds"));
            bodyParam.accumulate("PickupDate", BookingBundle.getString("PickupDate"));
            bodyParam.accumulate("ReturnDate", BookingBundle.getString("ReturnDate"));
            bodyParam.accumulate("PickupTime", BookingBundle.getString("PickupTime"));
            bodyParam.accumulate("ReturnTime", BookingBundle.getString("ReturnTime"));
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

        ApiService ApiService = new ApiService(getVehicleList, RequestType.POST,
                BOOKING, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);

        edt_searchVehicleName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                try {

                    final RelativeLayout rlVehicleAvilable = getActivity().findViewById(R.id.relative_vehicle_available);
                    int len;
                    len = VehicleList.length();
                    rlVehicleAvilable.removeAllViews();

                    for (int j = 0; j < len; j++)
                    {

                        final JSONObject test = (JSONObject) VehicleList.get(j);
                        final String vehiclE_NAME = test.getString("vehiclE_NAME");

                        if(vehiclE_NAME.contains(charSequence))
                        {
                            final String img_Path = test.getString("img_Path");
                            final int vehiclE_ID = test.getInt("vehiclE_ID");
                            final int vehiclE_TYPE_ID = test.getInt("vehiclE_TYPE_ID");
                            final String vehiclE_TYPE_NAME = test.getString("vehiclE_TYPE_NAME");
                            final String vehiclE_SEAT_NO = test.getString("vehiclE_SEAT_NO");
                            final String transmission = test.getString("transmission");
                            final String v_CURR_ODOM = test.getString("v_CURR_ODOM");
                            final String vehiclE_OPTIONS_IDS = test.getString("vehiclE_OPTIONS_IDS");
                            final String rate_ID = test.getString("rate_ID");
                            final String rate_Name = test.getString("rate_Name");
                            final int package_ID = test.getInt("package_ID");
                            final double totaL_DAYS = test.getDouble("totaL_DAYS");
                            final String veh_bags = test.getString("veh_bags");
                            final String doors = test.getString("doors");
                            final String fuel_Name = test.getString("fuel_Name");
                            final String transmission_Name = test.getString("transmission_Name");
                            final String veh_Name = test.getString("veh_Name");
                            final String year_Name = test.getString("year_Name");
                            final String lateR_PRICE = test.getString("lateR_PRICE");
                            final int lateR_RATE_ID = test.getInt("lateR_RATE_ID");
                            final String lateR_RATE_NAME = test.getString("lateR_RATE_NAME");
                            final double daily_Price = test.getDouble("daily_Price");
                            final String available_QTY = test.getString("available_QTY");
                            final String vehDescription = test.getString("vehDescription");
                            final String vehicle_Make_Model_Name = test.getString("vehicle_Make_Model_Name");
                            final int isDepositMandatory = test.getInt("isDepositMandatory");
                            final double securityDeposit = test.getDouble("securityDeposit");
                            final double hourlyMilesAllowed = test.getDouble("hourlyMilesAllowed");
                            final double halfDayMilesAllowed = test.getDouble("halfDayMilesAllowed");
                            final double dailyMilesAllowed = test.getDouble("dailyMilesAllowed");
                            final double weeklyMilesAllowed = test.getDouble("weeklyMilesAllowed");
                            final double monthlyMilesAllowed = test.getDouble("monthlyMilesAllowed");
                            final double totalMilesAllowed = test.getDouble("totalMilesAllowed");
                            final int lockKey = test.getInt("lockKey");

                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            lp.setMargins(0, 10, 0, 0);

                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final LinearLayout vehiclelayoutlist = (LinearLayout) inflater.inflate(R.layout.vehicle_available_list, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                            vehiclelayoutlist.setId(200 + j);
                            vehiclelayoutlist.setLayoutParams(lp);

                            final ImageView imageview = (ImageView) vehiclelayoutlist.findViewById(R.id.car_imgview);

                            String url1 = serverpath + img_Path.substring(2);
                            imageLoader.displayImage(url1, imageview);

                            vehiclelayoutlist.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View arg0)
                                {
                                    int len = rlVehicleAvilable.getChildCount();

                                    for (int m = 0; m < len; m++)
                                    {
                                        LinearLayout linearLayout = (LinearLayout) rlVehicleAvilable.getChildAt(m);
                                        linearLayout.setBackground(getResources().getDrawable(R.drawable.top_curve_light_gray));

                                    }
                                    vehiclelayoutlist.setBackground(getResources().getDrawable(R.drawable.curve_box_dark_gray));

                                    Bundle VehicleBundle = new Bundle();
                                    VehicleBundle.putString("img_Path ", img_Path);
                                    VehicleBundle.putInt("vehiclE_ID", vehiclE_ID);
                                    VehicleBundle.putString("vehiclE_TYPE_NAME", vehiclE_TYPE_NAME);
                                    VehicleBundle.putInt("vehiclE_TYPE_ID", vehiclE_TYPE_ID);
                                    VehicleBundle.putDouble("price", Totalprice);
                                    VehicleBundle.putString("vehiclE_NAME", vehiclE_NAME);
                                    VehicleBundle.putString("vehiclE_SEAT_NO", vehiclE_SEAT_NO);
                                    VehicleBundle.putString("transmission", transmission);
                                    VehicleBundle.putString("v_CURR_ODOM", v_CURR_ODOM);
                                    VehicleBundle.putString("vehiclE_OPTIONS_IDS", vehiclE_OPTIONS_IDS);
                                    VehicleBundle.putString("rate_ID", rate_ID);
                                    VehicleBundle.putString("rate_Name", rate_Name);
                                    VehicleBundle.putDouble("totaL_DAYS", totaL_DAYS);
                                    VehicleBundle.putString("veh_bags", veh_bags);
                                    VehicleBundle.putString("doors", doors);
                                    VehicleBundle.putString("fuel_Name", fuel_Name);
                                    VehicleBundle.putString("transmission_Name", transmission_Name);
                                    VehicleBundle.putString("veh_Name", veh_Name);
                                    VehicleBundle.putString("year_Name", year_Name);
                                    VehicleBundle.putString("lateR_PRICE", lateR_PRICE);
                                    VehicleBundle.putInt("lateR_RATE_ID", lateR_RATE_ID);
                                    VehicleBundle.putString("lateR_RATE_NAME", lateR_RATE_NAME);
                                    VehicleBundle.putDouble("daily_Price", daily_Price);
                                    VehicleBundle.putString("available_QTY", available_QTY);
                                    VehicleBundle.putString("vehDescription", vehDescription);
                                    VehicleBundle.putString("vehicle_Make_Model_Name", vehicle_Make_Model_Name);
                                    VehicleBundle.putDouble("totalMilesAllowed", totalMilesAllowed);

                                    BookingBundle.putInt("BookingStep", 3);
                                    BookingBundle.putInt("VehicleID", vehiclE_ID);
                                    BookingBundle.putInt("vehiclE_TYPE_ID", vehiclE_TYPE_ID);

                                    Bundle Booking = new Bundle();
                                    Booking.putBundle("BookingBundle", BookingBundle);
                                    Booking.putBundle("VehicleBundle", VehicleBundle);
                                    Booking.putBundle("returnLocation", returnLocationBundle);
                                    Booking.putBundle("location", locationBundle);
                                    Booking.putBoolean("locationType", locationType);
                                    Booking.putBoolean("initialSelect", initialSelect);
                                    Booking.putString("DeliveryAndPickupModel", "");
                                    System.out.println(VehicleBundle);
                                    NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                                            .navigate(R.id.action_Vehicles_Available_to_Select_addtional_options, Booking);
                                }
                            });

                            TextView txtvehiclename, txtAvailableQty, txt_VehicleTypeName,txtfuel_Name,
                                    txt_transmission_Name, txtseats, txtDoors, txtbags, txtDayPrice, txtDeposite,txt_price;

                            txtvehiclename = vehiclelayoutlist.findViewById(R.id.vehicle_model_name);
                            txtAvailableQty = vehiclelayoutlist.findViewById(R.id.txt_availableqty);
                            txt_VehicleTypeName = vehiclelayoutlist.findViewById(R.id.txt_VehicleTypeName);
                            txt_transmission_Name = vehiclelayoutlist.findViewById(R.id.txt_transmission_Name);

                            txtseats = vehiclelayoutlist.findViewById(R.id.txt_seats);
                            txtDoors = vehiclelayoutlist.findViewById(R.id.txt_doors);
                            txtbags = vehiclelayoutlist.findViewById(R.id.txtbags);
                            txtDayPrice = vehiclelayoutlist.findViewById(R.id.txtDayPrice);
                            txtDeposite = vehiclelayoutlist.findViewById(R.id.txtDeposite);
                            txt_price = vehiclelayoutlist.findViewById(R.id.txt_price);
                            // txtfuel_Name= vehiclelayoutlist.findViewById(R.id.txtfuel_Name);

                            txtvehiclename.setText(veh_Name);
                            txtAvailableQty.setText(available_QTY);
                            txt_VehicleTypeName.setText(vehiclE_TYPE_NAME);

                            txt_transmission_Name.setText(transmission_Name);
                            // txtfuel_Name.setText(fuel_Name);

                            txtseats.setText(vehiclE_SEAT_NO+ " Seats");
                            txtDoors.setText(doors + " Doors");
                            txtbags.setText(veh_bags + " Bags");

                            Totalprice=(daily_Price*totaL_DAYS + securityDeposit);
                            txt_price.setText((String.format(Locale.US,"%.2f",Totalprice)));

                            String strDeposite=((String.format(Locale.US,"%.2f",securityDeposit)));
                            txtDeposite.setText("US$ "+strDeposite+"/ Deposite");

                            String strDayPrice=((String.format(Locale.US,"%.2f",daily_Price)));
                            txtDayPrice.setText("US$ "+strDayPrice+"/ Day");

                            rlVehicleAvilable.addView(vehiclelayoutlist);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });

    }

    OnResponseListener getVehicleList = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {
                            JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                            VehicleList = resultSet.getJSONArray("vehicleModel");

                            final RelativeLayout rlVehicleAvilable = getActivity().findViewById(R.id.relative_vehicle_available);
                            int len;
                            len = VehicleList.length();
                            rlVehicleAvilable.removeAllViews();

                            for (int j = 0; j < len; j++)
                            {
                                final JSONObject test = (JSONObject)VehicleList.get(j);
                                final String vehiclE_NAME = test.getString("vehiclE_NAME");
                                final String img_Path = test.getString("img_Path");
                                final int vehiclE_ID = test.getInt("vehiclE_ID");
                                final int vehiclE_TYPE_ID = test.getInt("vehiclE_TYPE_ID");
                                // final Double price = test.getDouble("price");
                                final String vehiclE_TYPE_NAME = test.getString("vehiclE_TYPE_NAME");
                                final String vehiclE_SEAT_NO = test.getString("vehiclE_SEAT_NO");
                                final String transmission = test.getString("transmission");
                                final String v_CURR_ODOM = test.getString("v_CURR_ODOM");
                                final String vehiclE_OPTIONS_IDS = test.getString("vehiclE_OPTIONS_IDS");
                                final String rate_ID = test.getString("rate_ID");
                                final String rate_Name = test.getString("rate_Name");
                                final int package_ID = test.getInt("package_ID");
                                final double totaL_DAYS = test.getDouble("totaL_DAYS");
                                final String veh_bags = test.getString("veh_bags");
                                final String doors = test.getString("doors");
                                final String fuel_Name = test.getString("fuel_Name");
                                final String transmission_Name = test.getString("transmission_Name");
                                final String veh_Name = test.getString("veh_Name");
                                final String year_Name = test.getString("year_Name");
                                final String lateR_PRICE = test.getString("lateR_PRICE");
                                final int lateR_RATE_ID = test.getInt("lateR_RATE_ID");
                                final String lateR_RATE_NAME = test.getString("lateR_RATE_NAME");
                                final double daily_Price = test.getDouble("daily_Price");
                                final String available_QTY = test.getString("available_QTY");
                                final String vehDescription = test.getString("vehDescription");
                                final String vehicle_Make_Model_Name = test.getString("vehicle_Make_Model_Name");
                                final int isDepositMandatory = test.getInt("isDepositMandatory");
                                final double securityDeposit = test.getDouble("securityDeposit");
                                final double hourlyMilesAllowed = test.getDouble("hourlyMilesAllowed");
                                final double halfDayMilesAllowed = test.getDouble("halfDayMilesAllowed");
                                final double dailyMilesAllowed = test.getDouble("dailyMilesAllowed");
                                final double weeklyMilesAllowed = test.getDouble("weeklyMilesAllowed");
                                final double monthlyMilesAllowed = test.getDouble("monthlyMilesAllowed");
                                final double totalMilesAllowed = test.getDouble("totalMilesAllowed");
                                final int lockKey = test.getInt("lockKey");

                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                lp.setMargins(0, 10, 0, 0);

                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                                final LinearLayout vehiclelayoutlist = (LinearLayout) inflater.inflate(R.layout.vehicle_available_list, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                vehiclelayoutlist.setId(200 + j);
                                vehiclelayoutlist.setLayoutParams(lp);

                                final ImageView imageview = (ImageView) vehiclelayoutlist.findViewById(R.id.car_imgview);

                                String url1 = serverpath+img_Path.substring(2);
                                imageLoader.displayImage(url1, imageview);

                                vehiclelayoutlist.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View arg0)
                                    {
                                        int len = rlVehicleAvilable.getChildCount();

                                        for (int m = 0; m < len; m++)
                                        {
                                            LinearLayout linearLayout = (LinearLayout) rlVehicleAvilable.getChildAt(m);
                                            linearLayout.setBackground(getResources().getDrawable(R.drawable.top_curve_light_gray));

                                        }
                                        vehiclelayoutlist.setBackground(getResources().getDrawable(R.drawable.curve_box_dark_gray));

                                        Bundle VehicleBundle = new Bundle();

                                        VehicleBundle.putString("img_Path", img_Path.substring(2) );

                                        VehicleBundle.putInt("vehiclE_ID", vehiclE_ID);
                                        VehicleBundle.putString("vehiclE_TYPE_NAME", vehiclE_TYPE_NAME);
                                        VehicleBundle.putInt("vehiclE_TYPE_ID", vehiclE_TYPE_ID);
                                        VehicleBundle.putDouble("price", Totalprice);
                                        VehicleBundle.putString("vehiclE_NAME", vehiclE_NAME);
                                        VehicleBundle.putString("vehiclE_SEAT_NO", vehiclE_SEAT_NO);
                                        VehicleBundle.putString("transmission", transmission);
                                        VehicleBundle.putString("v_CURR_ODOM", v_CURR_ODOM);
                                        VehicleBundle.putString("vehiclE_OPTIONS_IDS", vehiclE_OPTIONS_IDS);
                                        VehicleBundle.putString("rate_ID", rate_ID);
                                        VehicleBundle.putString("rate_Name", rate_Name);
                                        VehicleBundle.putDouble("totaL_DAYS", totaL_DAYS);
                                        VehicleBundle.putString("veh_bags", veh_bags);
                                        VehicleBundle.putString("doors", doors);
                                        VehicleBundle.putString("fuel_Name", fuel_Name);
                                        VehicleBundle.putString("transmission_Name", transmission_Name);
                                        VehicleBundle.putString("veh_Name", veh_Name);
                                        VehicleBundle.putString("year_Name", year_Name);
                                        VehicleBundle.putString("lateR_PRICE", lateR_PRICE);
                                        VehicleBundle.putInt("lateR_RATE_ID", lateR_RATE_ID);
                                        VehicleBundle.putString("lateR_RATE_NAME", lateR_RATE_NAME);
                                        VehicleBundle.putDouble("daily_Price", daily_Price);
                                        VehicleBundle.putString("available_QTY", available_QTY);
                                        VehicleBundle.putString("vehDescription", vehDescription);
                                        VehicleBundle.putString("vehicle_Make_Model_Name", vehicle_Make_Model_Name);
                                        VehicleBundle.putDouble("totalMilesAllowed", totalMilesAllowed);

                                        BookingBundle.putInt("BookingStep", 3);
                                        BookingBundle.putInt("VehicleID", vehiclE_ID);
                                        BookingBundle.putInt("vehiclE_TYPE_ID",vehiclE_TYPE_ID);

                                        Bundle Booking = new Bundle();
                                        Booking.putBundle("BookingBundle", BookingBundle);
                                        Booking.putBundle("VehicleBundle", VehicleBundle);
                                        Booking.putBundle("returnLocation", returnLocationBundle);
                                        Booking.putBundle("location", locationBundle);
                                        Booking.putBoolean("locationType", locationType);
                                        Booking.putBoolean("initialSelect", initialSelect);
                                        Booking.putString("DeliveryAndPickupModel", "");
                                        System.out.println(VehicleBundle);
                                        System.out.println(BookingBundle);
                                        NavHostFragment.findNavController(Fragment_Vehicles_Available.this)
                                                .navigate(R.id.action_Vehicles_Available_to_Select_addtional_options, Booking);
                                    }
                                });
                                TextView txtvehiclename, txtAvailableQty, txt_VehicleTypeName,
                                        txt_transmission_Name, txtseats, txtDoors, txtbags, txtDayPrice, txtDeposite,txtfuel_Name,txt_price;

                                txtvehiclename = vehiclelayoutlist.findViewById(R.id.vehicle_model_name);
                                txtAvailableQty = vehiclelayoutlist.findViewById(R.id.txt_availableqty);
                                txt_VehicleTypeName = vehiclelayoutlist.findViewById(R.id.txt_VehicleTypeName);
                                txt_transmission_Name = vehiclelayoutlist.findViewById(R.id.txt_transmission_Name);
                                txtseats = vehiclelayoutlist.findViewById(R.id.txt_seats);
                                txtDoors = vehiclelayoutlist.findViewById(R.id.txt_doors);
                                txtbags = vehiclelayoutlist.findViewById(R.id.txtbags);
                                txtDayPrice = vehiclelayoutlist.findViewById(R.id.txtDayPrice);
                                txtDeposite = vehiclelayoutlist.findViewById(R.id.txtDeposite);
                                txt_price = vehiclelayoutlist.findViewById(R.id.txt_price);
                                ///  txtfuel_Name= vehiclelayoutlist.findViewById(R.id.txtfuel_Name);

                                txtvehiclename.setText(veh_Name);
                                txtAvailableQty.setText(available_QTY);
                                txt_VehicleTypeName.setText(vehiclE_TYPE_NAME);
                                txtseats.setText(vehiclE_SEAT_NO+ " Seats");
                                txtDoors.setText(doors + " Doors");
                                txtbags.setText(veh_bags + " Bags");
                                txt_transmission_Name.setText(transmission_Name);

                                Totalprice=(daily_Price*totaL_DAYS + securityDeposit);
                                txt_price.setText(String.valueOf(Totalprice));

                                // txtfuel_Name.setText(fuel_Name);

                                Totalprice=(daily_Price*totaL_DAYS + securityDeposit);
                                txt_price.setText((String.format(Locale.US,"%.2f",Totalprice)));

                                String strDeposite=((String.format(Locale.US,"%.2f",securityDeposit)));
                                txtDeposite.setText("US$ "+strDeposite+"/ Deposite");

                                String strDayPrice=((String.format(Locale.US,"%.2f",daily_Price)));
                                txtDayPrice.setText("US$ "+strDayPrice+"/ Day");


                                rlVehicleAvilable.addView(vehiclelayoutlist);
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
//,"doc_Name":"14332_QRCode_20502021034350.png","doc_Details":"../Images/Company/204/Booking/QRCode/6721/14332_QRCode_20502021034350.png"