package com.flexiicar.app.flexiicar.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
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

public class Fragment_Select_addition_Options_For_User extends Fragment
{
    LinearLayout lblconfirm,LayoutdeliveryPickupservice;
    ImageView backarrow;
    Bundle ReservationBundle;
    ToggleButton SwitchVehicleDelivery,SwitchVehicleDeliver2;
    TextView txt_PickLocName,txt_ReturnLocName,txt_vehicletype,txt_vehName,txt_PickupDate,txt_PickupTime,txt_ReturnDate,txt_ReturnTIme,txtDays,txt_rate,txtTotalAmount,txt_Discard;
    Handler handler = new Handler();
    public static Context context;
    JSONArray EquipmentList = new JSONArray();
    JSONArray MiscList = new JSONArray();
    int cmP_DISTANCE;

    String DeliveryAndPickupModel = "";
    String previousSwitchtotal="";

    Double DeliveryChargeAmount=0.0, PickupChargeAmount=0.0;
    int DeliveryChargeLocID=0, PickupChargeLocID=0;

    ToggleButton previousSwitch=null;

    ImageLoader imageLoader;
    String serverpath="",VehImage="";

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
        return inflater.inflate(R.layout.fragment_select_additional_option, container, false);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        try {
            super.onViewCreated(view, savedInstanceState);

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            initImageLoader(getActivity());
            imageLoader = ImageLoader.getInstance();

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
            txt_Discard= view.findViewById(R.id.txt_DiscardSAO);
            SwitchVehicleDelivery =view.findViewById(R.id.switch_VehicleDelivery);
            SwitchVehicleDeliver2 = view.findViewById(R.id.switch_VehicleDelivery2);

            ReservationBundle = getArguments().getBundle("ReservationBundle");

            DeliveryAndPickupModel = getArguments().getString("DeliveryAndPickupModel");

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            serverpath = sp.getString("serverPath", "");
            cmP_DISTANCE= sp.getInt("cmP_DISTANCE", 0);

            ImageView Vehicle_Img=view.findViewById(R.id.Veh_image_bg1);

            String VehicleImgstr=ReservationBundle.getString("veh_Img_Path");
            String url1 = serverpath+VehicleImgstr.substring(2);
            imageLoader.displayImage(url1,Vehicle_Img);

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
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Select_addition_Options_For_User.this)
                            .navigate(R.id.action_Select_addtional_options_to_Reservation);
                }
            });
            lblconfirm.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ReservationBundle.putInt("BookingStep", 4);
                    ReservationBundle.putString("EquipmentList", EquipmentList.toString());
                    ReservationBundle.putString("MiscList", MiscList.toString());
                    ReservationBundle.putString("DeliveryAndPickupModel", DeliveryAndPickupModel);
                    ReservationBundle.putInt("DeliveryChargeLocID", DeliveryChargeLocID);
                    ReservationBundle.putDouble("DeliveryChargeAmount", DeliveryChargeAmount);
                    ReservationBundle.putInt("pickupChargeLocID", PickupChargeLocID);
                    ReservationBundle.putDouble("pickupChargeAmount", PickupChargeAmount);
                    Bundle Reservation = new Bundle();
                    Reservation.putBundle("ReservationBundle", ReservationBundle);
                    NavHostFragment.findNavController(Fragment_Select_addition_Options_For_User.this)
                            .navigate(R.id.action_Select_addtional_options_to_Finalize_your_rental, Reservation);
                }
            });

            backarrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle Reservation = new Bundle();
                    Reservation.putBundle("ReservationBundle", ReservationBundle);
                    NavHostFragment.findNavController(Fragment_Select_addition_Options_For_User.this)
                            .navigate(R.id.action_Select_addtional_options_to_SummaryOfCharges, Reservation);
                }
            });
            ReservationBundle = getArguments().getBundle("ReservationBundle");

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

            double Total=(ReservationBundle.getDouble("chargeAmount"));
            txtTotalAmount.setText(((String.format(Locale.US,"%.2f",Total))));

            txtDays.setText(ReservationBundle.getString("totaL_DAYS"));

            txt_rate.setText(ReservationBundle.getString("rate_ID"));

            SwitchVehicleDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                {
                    if (SwitchVehicleDelivery.isChecked())
                    {
                        ReservationBundle.putString("EquipmentList", EquipmentList.toString());
                        ReservationBundle.putString("MiscList", MiscList.toString());
                        Bundle Booking = new Bundle();
                        Booking.putBundle("ReservationBundle", ReservationBundle);
                        Booking.putBoolean("fromMap", false);
                        Booking.putString("DeliveryAndPickupModel", "");
                        NavHostFragment.findNavController(Fragment_Select_addition_Options_For_User.this)
                                .navigate(R.id.action_Select_addtional_options_to_FilterByVehicleClass, Booking);
                    } else {
                        DeliveryChargeAmount = 0.0;
                        DeliveryChargeLocID = 0;
                    }
                }
            });

            SwitchVehicleDeliver2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                {
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
                bodyParam.accumulate("ForTransId", ReservationBundle.getInt("reservation_ID"));
                bodyParam.accumulate("CustomerId", ReservationBundle.getInt("customer_ID"));
                bodyParam.accumulate("VehicleTypeId", ReservationBundle.getInt("vehicle_Type_ID"));
                bodyParam.accumulate("VehicleID", ReservationBundle.getInt("vehicle_ID"));
                bodyParam.accumulate("PickupLocId", ReservationBundle.getInt("check_IN_Location"));
                bodyParam.accumulate("ReturnLocId", ReservationBundle.getInt("check_Out_Location"));
                bodyParam.accumulate("BookingStep", ReservationBundle.getInt("BookingStep"));
                System.out.println(bodyParam);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidNetworking.initialize(getActivity());
            Fragment_Select_addition_Options_For_User.context = getActivity();

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

                                //insuranceModel
                                int len;
                                len = getInsuranceDEtails.length();

                                for (int j = 0; j < len; j++)
                                {

                                    final JSONObject test = (JSONObject) getInsuranceDEtails.get(j);

                                    final  int transId = test.getInt("transId");
                                    final  int deductableId = test.getInt("deductableId");
                                    final  String insuranceName = test.getString("insuranceName");
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
                                        ReservationBundle.putInt("DeductibleCoverId", deductableId);
                                        ReservationBundle.putDouble("DeductibleCharge", totalCharge);
                                    }

                                    s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                                    {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                                        {
                                            try {

                                                if (isChecked)
                                                {
                                                    ReservationBundle.putInt("DeductibleCoverId", deductableId);
                                                    ReservationBundle.putDouble("DeductibleCharge", totalCharge);

                                                    previousSwitch.setChecked(false);
                                                    previousSwitch = s1;

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
                                    final  String equipmentImagePath = test.getString("equipmentImagePath");
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
                                                    EquipmentList.put(equipmentObj);

                                                } else
                                                    {
                                                    equipmentObj.put("equipmentQty", 0);

                                                        int count=EquipmentList.length();

                                                        for (int i=0;i<count;i++)
                                                        {
                                                            try {
                                                                JSONObject obj=EquipmentList.getJSONObject(i);

                                                                if(obj.getInt("equipmentTypeId")==equipmentTypeId)
                                                                {
                                                                    String TotalequipmentAmount= txt_equipmentAmount.getText().toString();
                                                                    String totalAmount= txtTotalAmount.getText().toString();
                                                                    double TotalValue=Double.parseDouble(totalAmount)-Double.parseDouble(TotalequipmentAmount);
                                                                    txtTotalAmount.setText((String.format(Locale.US,"%.2f",TotalValue)));
                                                                    EquipmentList.remove(i);
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
                                    miscObj.put("isOptional",isOptional);

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

                                        String TotalequipmentAmount= txt_miscAmount.getText().toString();
                                        String totalAmount= txtTotalAmount.getText().toString();
                                        double TotalValue=Double.parseDouble(TotalequipmentAmount)+Double.parseDouble(totalAmount);

                                        txtTotalAmount.setText((String.format(Locale.US,"%.2f",TotalValue)));

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

