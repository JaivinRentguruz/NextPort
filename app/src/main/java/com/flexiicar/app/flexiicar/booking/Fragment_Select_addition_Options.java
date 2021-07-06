package com.flexiicar.app.flexiicar.booking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

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

public class Fragment_Select_addition_Options extends Fragment
{
    LinearLayout lblconfirm,LayoutdeliveryPickupservice;
    ImageView backarrow, CarImage;
    Bundle BookingBundle,VehicleBundle;
    ToggleButton SwitchVehicleDelivery,SwitchVehicleDeliver2;
    TextView txt_PickLocName,txt_ReturnLocName,txt_vehicletype,txt_vehName,txt_PickupDate,
            txt_PickupTime,txt_ReturnDate,txt_ReturnTIme,txtDays,txt_rate,txtTotalAmount,txt_Discard,txtMileage;
    Handler handler = new Handler();
    public static Context context;

    JSONArray SelectedEquipmentList = new JSONArray();

    JSONArray MiscList = new JSONArray();
    JSONArray SummaryOfCharges = new JSONArray();
    int cmP_DISTANCE;
    Bundle returnLocationBundle, locationBundle;
    Boolean locationType, initialSelect;

    String DeliveryAndPickupModel = "";

    Double DeliveryChargeAmount=0.0, PickupChargeAmount=0.0;
    int DeliveryChargeLocID=0, PickupChargeLocID=0;

    String previousSwitchtotal="";

    ImageLoader imageLoader;
    String serverpath="";

    ToggleButton previousSwitch = null;

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
        return inflater.inflate(R.layout.fragment_select_additional_option, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            initImageLoader(getActivity());
            imageLoader = ImageLoader.getInstance();

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            serverpath = sp.getString("serverPath", "");
            cmP_DISTANCE = sp.getInt("cmP_DISTANCE", 0);

            lblconfirm = view.findViewById(R.id.lbl_confirm_2);
            backarrow = view.findViewById(R.id.backbtn1);
            LayoutdeliveryPickupservice = view.findViewById(R.id.layout_deliverypickupservice);
            txt_vehicletype = view.findViewById(R.id.txt_vehicleTypeName);
            txt_vehName = view.findViewById(R.id.txt_VehicleMOdelName);
            txtTotalAmount = view.findViewById(R.id.txt_TotalAmount);
            txt_PickLocName = view.findViewById(R.id.txt_PickupLocationName);
            txt_ReturnLocName = view.findViewById(R.id.txt_returnLocationName);
            txt_PickupDate = view.findViewById(R.id.txt_Pickupdate1);
            txt_PickupTime = view.findViewById(R.id.txt_PICKUPTime);
            txt_ReturnDate = view.findViewById(R.id.txt_REturnDate);
            txt_ReturnTIme = view.findViewById(R.id.txt_REturnTime);
            txtDays = view.findViewById(R.id.Txt_Days);
            txt_rate = view.findViewById(R.id.txt_rate1);
            txt_Discard = view.findViewById(R.id.txt_DiscardSAO);
            CarImage = view.findViewById(R.id.Veh_image_bg1);
            txtMileage = view.findViewById(R.id.txtMileage);

            SwitchVehicleDelivery =view.findViewById(R.id.switch_VehicleDelivery);
            SwitchVehicleDeliver2 =view.findViewById(R.id.switch_VehicleDelivery2);

            BookingBundle = getArguments().getBundle("BookingBundle");
            VehicleBundle = getArguments().getBundle("VehicleBundle");

            returnLocationBundle = getArguments().getBundle("returnLocation");
            locationBundle = getArguments().getBundle("location");
            locationType = getArguments().getBoolean("locationType");
            initialSelect = getArguments().getBoolean("initialSelect");
            DeliveryAndPickupModel = getArguments().getString("DeliveryAndPickupModel");

           if (!DeliveryAndPickupModel.equals(""))
            {
                try {
                    JSONArray DPArray = new JSONArray(DeliveryAndPickupModel);

                    if (DPArray.length() > 0)
                    {
                        TextView deliveryCharge = view.findViewById(R.id.deliveryCharge);

                        deliveryCharge.setText(DPArray.getJSONObject(0).getDouble("chargeAmount") + "");

                        DeliveryChargeLocID = DPArray.getJSONObject(0).getInt("businessLocID");
                        DeliveryChargeAmount = DPArray.getJSONObject(0).getDouble("chargeAmount");

                        SwitchVehicleDelivery.setChecked(true);

                    }

                    if (DPArray.length() > 1)
                    {
                        TextView pickupCharge = view.findViewById(R.id.pickupCharge);

                        pickupCharge.setText(DPArray.getJSONObject(1).getDouble("chargeAmount") + "");

                        PickupChargeLocID = DPArray.getJSONObject(1).getInt("businessLocID");
                        PickupChargeAmount = DPArray.getJSONObject(1).getDouble("chargeAmount");

                        SwitchVehicleDeliver2.setChecked(true);

                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            txt_Discard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(Fragment_Select_addition_Options.this)
                            .navigate(R.id.action_Select_addtional_options_to_Search_activity);
                }
            });

            lblconfirm.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    BookingBundle.putInt("BookingStep", 4);
                    BookingBundle.putString("EquipmentList", SelectedEquipmentList.toString());
                    BookingBundle.putString("MiscList", MiscList.toString());
                    BookingBundle.putInt("DeliveryChargeLocID", DeliveryChargeLocID);
                    BookingBundle.putDouble("DeliveryChargeAmount", DeliveryChargeAmount);
                    BookingBundle.putInt("PickupChargeLocID", PickupChargeLocID);
                    BookingBundle.putDouble("PickupChargeAmount", PickupChargeAmount);
                    BookingBundle.putString("SummaryOfCharges", SummaryOfCharges.toString());

                    Bundle Booking = new Bundle();
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    System.out.println(Booking);
                    NavHostFragment.findNavController(Fragment_Select_addition_Options.this)
                            .navigate(R.id.action_Select_addtional_options_to_Finalize_your_rental, Booking);
                }
            });
          /*  backarrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    Bundle Booking = new Bundle();
                    BookingBundle.putInt("BookingStep", 2);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    NavHostFragment.findNavController(Fragment_Select_addition_Options.this)
                            .navigate(R.id.action_Select_addtional_options_to_Select_location1, Booking);
                }
            });*/

          backarrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle Booking = new Bundle();
                    BookingBundle.putInt("BookingStep", 1);
                    BookingBundle.putInt("VehicleID", 0);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    System.out.println(Booking);
                    NavHostFragment.findNavController(Fragment_Select_addition_Options.this)
                            .navigate(R.id.action_Select_addtional_options_to_Vehicles_Available, Booking);
                }
            });

            txt_PickLocName.setText(BookingBundle.getString("PickupLocName"));
            txt_ReturnLocName.setText(BookingBundle.getString("ReturnLocName"));

            String StrPickupDate = (BookingBundle.getString("PickupDate"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(StrPickupDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String PickUpDateStr = sdf.format(date);
            txt_PickupDate.setText(PickUpDateStr);

            String StrReturnDate = (BookingBundle.getString("ReturnDate"));
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = dateFormat1.parse(StrReturnDate);
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

            txt_Discard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(Fragment_Select_addition_Options.this)
                            .navigate(R.id.action_Select_location1_to_Search_activity);

                }
            });

            SwitchVehicleDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                {
                    if (SwitchVehicleDelivery.isChecked())
                    {
                        BookingBundle.putString("EquipmentList", SelectedEquipmentList.toString());
                        BookingBundle.putString("MiscList", MiscList.toString());
                        Bundle Booking = new Bundle();
                        Booking.putBundle("BookingBundle", BookingBundle);
                        Booking.putBundle("VehicleBundle", VehicleBundle);
                        Booking.putBundle("returnLocation", returnLocationBundle);
                        Booking.putBundle("location", locationBundle);
                        Booking.putBoolean("locationType", locationType);
                        Booking.putBoolean("initialSelect", initialSelect);
                        Booking.putBoolean("fromMap", false);
                        Booking.putString("DeliveryAndPickupModel", DeliveryAndPickupModel);
                        NavHostFragment.findNavController(Fragment_Select_addition_Options.this)
                                .navigate(R.id.action_Select_addtional_options_to_FilterByVehicleClass, Booking);
                    } else {
                        DeliveryChargeAmount = 0.0;
                        DeliveryChargeLocID = 0;
                    }
                }
            });

            SwitchVehicleDeliver2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (SwitchVehicleDeliver2.isChecked()) {
                        //SwitchVehicleDeliver2.setEnabled(false);
                    } else {
                        PickupChargeLocID = 0;
                        PickupChargeAmount = 0.0;
                    }
                }
            });

            JSONObject bodyParam = new JSONObject();
            try {
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
                bodyParam.accumulate("ReturnDate", ReturnDateTime);
                bodyParam.accumulate("FilterTransmission", BookingBundle.getInt("FilterTransmission"));
                bodyParam.accumulate("FilterPassengers", BookingBundle.getInt("FilterPassengers"));
                bodyParam.accumulate("BookingStep", BookingBundle.getInt("BookingStep"));
                bodyParam.accumulate("BookingType", BookingBundle.getInt("BookingType"));
                System.out.println(bodyParam);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            AndroidNetworking.initialize(getActivity());
            Fragment_Select_addition_Options.context = getActivity();

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

                                final JSONArray getInsuranceDEtails = resultSet.getJSONArray("insuranceModel");
                                final RelativeLayout rlInsuranceDetails = getActivity().findViewById(R.id.rlInsuranceCover);

                                final JSONArray getEquipmentdetails = resultSet.getJSONArray("equipmentModel");
                                final RelativeLayout rlEquipmentDetails = getActivity().findViewById(R.id.rl_EquipmentExtra);

                                final JSONArray getmiscellaneousModel = resultSet.getJSONArray("miscellaneousModel");
                                final RelativeLayout rlmiscellaneousModel = getActivity().findViewById(R.id.rl_miscellaneousModel);

                                //Vehicle Model
                                JSONObject vehicleModel =resultSet.getJSONObject("vehicleModel");
                                final int vehiclE_ID = vehicleModel.getInt("vehiclE_ID");
                                final String vehiclE_NAME = vehicleModel.getString("vehiclE_NAME");
                                final int vehiclE_TYPE_ID = vehicleModel.getInt("vehiclE_TYPE_ID");
                                final String vehiclE_TYPE_NAME = vehicleModel.getString("vehiclE_TYPE_NAME");
                                final String vehiclE_SEAT_NO = vehicleModel.getString("vehiclE_SEAT_NO");
                                final String transmission = vehicleModel.getString("transmission");
                                final String v_CURR_ODOM = vehicleModel.getString("v_CURR_ODOM");
                                final String img_Path = vehicleModel.getString("img_Path");
                                final String vehiclE_OPTIONS_IDS = vehicleModel.getString("vehiclE_OPTIONS_IDS");
                                final String rate_ID = vehicleModel.getString("rate_ID");
                                final String rate_Name = vehicleModel.getString("rate_Name");
                                final int package_ID = vehicleModel.getInt("package_ID");
                                final double totaL_DAYS = vehicleModel.getDouble("totaL_DAYS");
                                final String veh_bags = vehicleModel.getString("veh_bags");
                                final String doors = vehicleModel.getString("doors");
                                final String fuel_Name = vehicleModel.getString("fuel_Name");
                                final String transmission_Name = vehicleModel.getString("transmission_Name");
                                final String veh_Name = vehicleModel.getString("veh_Name");
                                final String year_Name = vehicleModel.getString("year_Name");
                                final String lateR_PRICE = vehicleModel.getString("lateR_PRICE");
                                final int lateR_RATE_ID = vehicleModel.getInt("lateR_RATE_ID");
                                final String lateR_RATE_NAME = vehicleModel.getString("lateR_RATE_NAME");
                                final double daily_Price = vehicleModel.getDouble("daily_Price");
                                final String available_QTY = vehicleModel.getString("available_QTY");
                                final String vehDescription = vehicleModel.getString("vehDescription");
                                final String vehicle_Make_Model_Name = vehicleModel.getString("vehicle_Make_Model_Name");
                                final int isDepositMandatory = vehicleModel.getInt("isDepositMandatory");
                                final double securityDeposit = vehicleModel.getDouble("securityDeposit");
                                final double hourlyMilesAllowed = vehicleModel.getDouble("hourlyMilesAllowed");
                                final double halfDayMilesAllowed = vehicleModel.getDouble("halfDayMilesAllowed");
                                final double dailyMilesAllowed = vehicleModel.getDouble("dailyMilesAllowed");
                                final double weeklyMilesAllowed = vehicleModel.getDouble("weeklyMilesAllowed");
                                final double monthlyMilesAllowed = vehicleModel.getDouble("monthlyMilesAllowed");
                                final double totalMilesAllowed = vehicleModel.getDouble("totalMilesAllowed");
                                final int lockKey = vehicleModel.getInt("lockKey");

                                String url1 = serverpath + img_Path;
                                imageLoader.displayImage(url1, CarImage);

                                txtDays.setText(String.valueOf(totaL_DAYS));
                                txt_vehicletype.setText(vehiclE_TYPE_NAME);
                                txt_vehName.setText(vehiclE_NAME);
                                txt_rate.setText(rate_ID);

                                if(cmP_DISTANCE==1)
                                {
                                    String Miles=(String.valueOf(totalMilesAllowed));
                                    txtMileage.setText(Miles+"kms");
                                }
                                else {
                                    String Miles=(String.valueOf(totalMilesAllowed));
                                    txtMileage.setText(Miles+"kms");
                                }

                                //insuranceModel
                                int len;
                                len = getInsuranceDEtails.length();


                                for (int j = 0; j < len; j++)
                                {

                                    final JSONObject test = (JSONObject) getInsuranceDEtails.get(j);

                                    final  int transId = test.getInt("transId");
                                    final  int deductableId = test.getInt("deductableId");
                                    final String insuranceName = test.getString("insuranceName");
                                    final String insuranceDesc=test.getString("insuranceDesc");
                                    final int isSelected=test.getInt("isSelected");
                                    final double excessCharge=test.getDouble("excessCharge");
                                    final double perDayCharge=test.getDouble("perDayCharge");
                                    final double totalCharge=test.getDouble("totalCharge");

                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    lp.setMargins(0, 10, 0, 0);

                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final LinearLayout l1 = (LinearLayout) inflater.inflate(R.layout.tax_details_list, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                    l1.setId(200 + j);
                                    l1.setLayoutParams(lp);

                                    final TextView txt_insuranceName,txt_insuranceDesc,txt_totalCharge;
                                    txt_insuranceName= (TextView)l1.findViewById(R.id.txt_insuranceName);
                                    txt_insuranceDesc=(TextView)l1.findViewById(R.id.txt_insuranceDesc);
                                    txt_totalCharge=(TextView)l1.findViewById(R.id.txt_totalCharge);

                                    txt_insuranceName.setText(insuranceName);
                                    txt_insuranceDesc.setText(insuranceDesc);
                                    txt_totalCharge.setText(((String.format(Locale.US,"%.2f",totalCharge))));

                                    final ToggleButton s1=l1.findViewById(R.id.switch2);

                                    if(isSelected == 1)
                                    {
                                        previousSwitch = s1;
                                        s1.setChecked(true);

                                        BookingBundle.putInt("DeductibleCoverId", deductableId);
                                        BookingBundle.putDouble("DeductibleCharge", totalCharge);
                                    }

                                    s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                                    {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                                        {
                                            try {
                                                if (isChecked)
                                                {
                                                    previousSwitch.setChecked(false);
                                                    previousSwitch = s1;

                                                    BookingBundle.putInt("DeductibleCoverId", deductableId);
                                                    BookingBundle.putDouble("DeductibleCharge", totalCharge);

                                                    previousSwitchtotal=txt_totalCharge.getText().toString();

                                                    String totalAmount = txtTotalAmount.getText().toString();
                                                    double TotalValue = Double.parseDouble(totalAmount)+Double.parseDouble(previousSwitchtotal) ;

                                                    txtTotalAmount.setText((String.format(Locale.US, "%.2f", TotalValue)));
                                                }
                                                else
                                                {
                                                        previousSwitchtotal=txt_totalCharge.getText().toString();
                                                        String totalAmount = txtTotalAmount.getText().toString();
                                                        double TotalValue = Double.parseDouble(totalAmount)-Double.parseDouble(previousSwitchtotal);
                                                        txtTotalAmount.setText((String.format(Locale.US, "%.2f", TotalValue)));
                                                }
                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    rlInsuranceDetails.addView(l1);
                                }

                                //equipmentModel
                                int len1;
                                len1 = getEquipmentdetails.length();

                                for (int j = 0; j < len1; j++)
                                {

                                    final JSONObject test = (JSONObject) getEquipmentdetails.get(j);
                                    final  int equipmentTypeId = test.getInt("equipmentTypeId");
                                    final String equipmentImagePath = test.getString("equipmentImagePath");
                                    final String equipmentName=test.getString("equipmentName");
                                    final String equipmentDesc=test.getString("equipmentDesc");
                                    final int equipmentQty=test.getInt("equipmentQty");
                                    final int taxValue=test.getInt("taxValue");
                                    final int taxAmount=test.getInt("taxAmount");
                                    final double equipmentAmount=test.getDouble("equipmentAmount");

                                    final JSONObject equipmentObj = new JSONObject();
                                    equipmentObj.put("equipmentTypeId", equipmentTypeId);
                                    equipmentObj.put("equipmentImagePath", equipmentImagePath);
                                    equipmentObj.put("equipmentName", equipmentName);
                                    equipmentObj.put("equipmentDesc", equipmentDesc);
                                    equipmentObj.put("equipmentQty", equipmentQty);
                                    equipmentObj.put("taxValue", taxValue);
                                    equipmentObj.put("taxAmount", taxAmount);
                                    equipmentObj.put("equipmentAmount", equipmentAmount+"");

                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    lp.setMargins(0, 0, 0, 0);

                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    LinearLayout linearLayout2 = (LinearLayout) inflater.inflate(R.layout.tax_details_list, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                    linearLayout2.setId(200 + j);
                                    linearLayout2.setLayoutParams(lp);

                                    final TextView txt_equipmentName,txt_equipmentDescc,txt_equipmentAmount;

                                    txt_equipmentName= (TextView) linearLayout2.findViewById(R.id.txt_insuranceName);
                                    txt_equipmentDescc=(TextView)linearLayout2.findViewById(R.id.txt_insuranceDesc);
                                    txt_equipmentAmount=(TextView)linearLayout2.findViewById(R.id.txt_totalCharge);
                                    txt_equipmentName.setText(equipmentName);
                                    txt_equipmentDescc.setText(equipmentDesc);
                                    txt_equipmentAmount.setText(((String.format(Locale.US,"%.2f",equipmentAmount))));

                                    final ToggleButton s1=linearLayout2.findViewById(R.id.switch2);
                                    s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                                    {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                                        {
                                            try {

                                                if (isChecked)
                                                {
                                                    equipmentObj.put("equipmentQty", 1);

                                                    String TotalequipmentAmount= txt_equipmentAmount.getText().toString();
                                                    String totalAmount= txtTotalAmount.getText().toString();
                                                    double TotalValue=Double.parseDouble(TotalequipmentAmount)+Double.parseDouble(totalAmount);

                                                    txtTotalAmount.setText((String.format(Locale.US,"%.2f",TotalValue)));
                                                    SelectedEquipmentList.put(equipmentObj);

                                                } else
                                                    {
                                                    equipmentObj.put("equipmentQty", 0);

                                                        int count=SelectedEquipmentList.length();

                                                        for (int i=0;i<count;i++)
                                                        {
                                                            try {
                                                                JSONObject obj=SelectedEquipmentList.getJSONObject(i);

                                                                if(obj.getInt("equipmentTypeId")==equipmentTypeId)
                                                                {
                                                                    String TotalequipmentAmount= txt_equipmentAmount.getText().toString();
                                                                    String totalAmount= txtTotalAmount.getText().toString();
                                                                    double TotalValue=Double.parseDouble(totalAmount)-Double.parseDouble(TotalequipmentAmount);
                                                                    txtTotalAmount.setText((String.format(Locale.US,"%.2f",TotalValue)));
                                                                    SelectedEquipmentList.remove(i);
                                                                    i--;
                                                                    count--;
                                                                }

                                                            }catch (Exception e)
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                }
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    rlEquipmentDetails.addView(linearLayout2);
                                }

                                //miscellaneousModel
                                int len2;
                                len2 = getmiscellaneousModel.length();

                                for (int j = 0; j < len2; j++)
                                {
                                    final JSONObject test = (JSONObject) getmiscellaneousModel.get(j);
                                    final  int miscId = test.getInt("miscId");
                                    final String miscName = test.getString("miscName");
                                    final String miscDesc=test.getString("miscDesc");
                                    final double basicValue=test.getDouble("basicValue");
                                    final int quantity=test.getInt("quantity");
                                    final double miscAmount=test.getDouble("miscAmount");
                                    final int taxableAmount=test.getInt("taxableAmount");
                                    final int isOptional=test.getInt("isOptional");

                                    final JSONObject miscObj = new JSONObject();
                                    miscObj.put("miscId",miscId);
                                    miscObj.put("miscName",miscName);
                                    miscObj.put("miscDesc",miscDesc);
                                    miscObj.put("basicValue",basicValue);
                                    miscObj.put("quantity",quantity);
                                    miscObj.put("miscAmount",miscAmount);
                                    miscObj.put("taxableAmount",taxableAmount);

                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    lp.setMargins(0, 0, 0, 0);

                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.miscellaneous_tax_details, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                    linearLayout.setId(200 + j);
                                    linearLayout.setLayoutParams(lp);

                                    final TextView txt_miscName,txt_miscDesc,txt_miscAmount;
                                    final ToggleButton s1=linearLayout.findViewById(R.id.switch1);


                                    txt_miscName= (TextView)linearLayout.findViewById(R.id.txt_miscName);
                                    txt_miscDesc=(TextView)linearLayout.findViewById(R.id.txt_miscDesc);
                                    txt_miscAmount=(TextView)linearLayout.findViewById(R.id.txt_miscAmount);
                                    txt_miscName.setText(miscName);
                                    txt_miscDesc.setText(miscDesc);

                                    txt_miscAmount.setText(((String.format(Locale.US,"%.2f",miscAmount))));

                                    if (isOptional==1)
                                    {
                                        s1.setChecked(true);
                                        s1.setEnabled(false);

                                        miscObj.put("miscId",miscId);
                                        miscObj.put("miscAmount",miscAmount);
                                        MiscList.put(miscObj);
                                    }
                                    else
                                    {
                                        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                                        {
                                            @Override
                                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                                            {
                                                try {
                                                    if (isChecked)
                                                    {
                                                        miscObj.put("isOptional",1);
                                                    } else {
                                                        miscObj.put("isOptional",0);
                                                    }
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                    rlmiscellaneousModel.addView(linearLayout);
                                }

                                //getsummaryOfCharges
                                final JSONArray getsummaryOfCharges = resultSet.getJSONArray("summaryOfCharges");
                                len = getsummaryOfCharges.length();

                                for (int j = 0; j < len; j++)
                                {
                                    final JSONObject test = (JSONObject) getsummaryOfCharges.get(j);

                                    final  int sortId = test.getInt("sortId");
                                    final  double chargeAmount=test.getDouble("chargeAmount");
                                    String chargeCode = "";
                                    
                                    if(test.has("chargeCode"))
                                    {
                                        chargeCode = test.getString("chargeCode");
                                    }
                                    
                                    final  String chargeName = test.getString("chargeName");

                                    JSONObject summaryOfChargesObj = new JSONObject();
                                    summaryOfChargesObj.put("sortId",sortId);
                                    summaryOfChargesObj.put("chargeCode",chargeCode);
                                    summaryOfChargesObj.put("chargeName",chargeName);
                                    summaryOfChargesObj.put("chargeAmount",chargeAmount);

                                    SummaryOfCharges.put(summaryOfChargesObj);

                                    if(chargeName.equals("Estimated Total"))
                                    {
                                        txtTotalAmount.setText((String.format(Locale.US,"%.2f",chargeAmount)));
                                    }
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