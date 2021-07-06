package com.flexiicar.app.flexiicar.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import com.flexiicar.app.flexiicar.booking.Fragment_Payment_checkout;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static android.graphics.Color.parseColor;
import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.flexiicar.app.apicall.ApiEndPoint.GETAGREEMENTLIST;

public class Fragment_Agreements extends Fragment
{
    ImageView backarrow;
    Handler handler = new Handler();
    public static Context context;
    public String id = "";
    ImageLoader imageLoader;
    String serverpath="";
    ProgressDialog progressDialog;
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
        return inflater.inflate(R.layout.fragment_agreements, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        backarrow=view.findViewById(R.id.back_to_userdetils);
        backarrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavHostFragment.findNavController(Fragment_Agreements.this)
                        .navigate(R.id.action_Agreements_to_User_Details);
            }
        });

        initImageLoader(getActivity());
        imageLoader = ImageLoader.getInstance();

        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");
        serverpath = sp.getString("serverPath", "");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String bodyParam = "";

        try
        {
            bodyParam+="customerId="+id;
            System.out.println(bodyParam);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        AndroidNetworking.initialize(getActivity());
        Fragment_Payment_checkout.context = getActivity();

        ApiService ApiService = new ApiService(GetAgreementlist, RequestType.GET,
                GETAGREEMENTLIST, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);

    }
    OnResponseListener GetAgreementlist = new OnResponseListener()
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
                            JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                            final JSONArray getAgreementList = resultSet.getJSONArray("v0200_Agreement_Details");

                            final RelativeLayout rlAgreement = getActivity().findViewById(R.id.rl_agreement);
                            int len;
                            len = getAgreementList.length();

                            for (int j = 0; j < len; j++)
                            {
                                final JSONObject test = (JSONObject) getAgreementList.get(j);

                                final int agreement_ID = test.getInt("agreement_ID");
                                final int reservation_ID = test.getInt("reservation_ID");
                                final String agreement_No = test.getString("agreement_No");
                                final int vehicle_ID = test.getInt("vehicle_ID");
                                final int vehicle_Type_ID = test.getInt("vehicle_Type_ID");
                                final String vehicle_No = test.getString("vehicle_No");
                                final String vehicle = test.getString("vehicle");
                                final String exC_VEHICLE = test.getString("exC_VEHICLE");
                                final String veh_Full_Name = test.getString("veh_Full_Name");
                                final String check_Out = test.getString("check_Out");
                                final String check_IN = test.getString("check_IN");
                                final String default_Check_Out = test.getString("default_Check_Out");
                                final String default_Check_In = test.getString("default_Check_In");
                                final String customeR_NAME = test.getString("customeR_NAME");
                                final String created_By = test.getString("created_By");
                                final int created_ByID = test.getInt("created_ByID");
                                final String created_Date = test.getString("created_Date");
                                final String default_Created_Date = test.getString("default_Created_Date");
                                final int customer_ID = test.getInt("customer_ID");
                                final String cust_FName = test.getString("cust_FName");
                                final String cust_LName = test.getString("cust_LName");
                                final String cust_MobileNo = test.getString("cust_MobileNo");
                                final String cust_Phoneno = test.getString("cust_Phoneno");
                                final String cust_Email = test.getString("cust_Email");
                                final String vin_Num = test.getString("vin_Num");
                                final String lic_Num = test.getString("lic_Num");
                                final String check_Out_Location_Name = test.getString("check_Out_Location_Name");
                                final String check_in_Location_Name = test.getString("check_in_Location_Name");
                                final String veh_Img_Path = test.getString("veh_Img_Path");
                                final String cust_Img_Path = test.getString("cust_Img_Path");
                                final String rate_ID = test.getString("rate_ID");
                                final String totalDays=test.getString("totalDays");
                                final String veh_Bags=test.getString("veh_Bags");
                                final String veh_Seat=test.getString("veh_Seat");
                                final String transmission_Name = test.getString("transmission_Name");
                                final String vehicle_Type_Name = test.getString("vehicle_Type_Name");
                                final String status_Name = test.getString("status_Name");
                                final String status_BGColor_Name = test.getString("status_BGColor_Name");

                                final String d_FlightNo=test.getString("d_FlightNo");
                                final String d_Airport = test.getString("d_Airport");
                                final String default_D_Time = test.getString("default_D_Time");

                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                lp.setMargins(0, 18, 0, 0);

                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final LinearLayout Agreementlayout = (LinearLayout) inflater.inflate(R.layout.reservation_list, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                Agreementlayout.setId(200 + j);
                                Agreementlayout.setLayoutParams(lp);
                                LinearLayout linearLayout=(LinearLayout) Agreementlayout.findViewById(R.id.ReservationListLayout);
                                final ImageView car_image = (ImageView) Agreementlayout.findViewById(R.id.vehicle_Image);
                                String url1 = serverpath+veh_Img_Path.substring(2);
                                imageLoader.displayImage(url1, car_image);

                                final ImageView Cust_Image= (ImageView) Agreementlayout.findViewById(R.id.cust_Image);
                                String url2 = serverpath+cust_Img_Path.substring(2);
                                imageLoader.displayImage(url2, Cust_Image);

                                LinearLayout llRes_StatusBG=(LinearLayout) Agreementlayout.findViewById(R.id.ll_resStatus);

                                if (status_Name.equals("Open"))
                                {
                                    llRes_StatusBG.setBackgroundTintList(ColorStateList.valueOf(parseColor(status_BGColor_Name)));
                                }
                                else {
                                    llRes_StatusBG.setBackgroundTintList(ColorStateList.valueOf(parseColor(status_BGColor_Name)));
                                }

                                TextView txtfullName = Agreementlayout.findViewById(R.id.txtFullname);
                                TextView txtPhoneNo = Agreementlayout.findViewById(R.id.txt_PhoneNo);
                                TextView txtEmailId = Agreementlayout.findViewById(R.id.txt_CustEmail);
                                TextView txt_CheckInLoc = Agreementlayout.findViewById(R.id.checkinLocName);
                                TextView txtCheckInDate = Agreementlayout.findViewById(R.id.checkIn_DateTime);
                                TextView txtCheckOutLoc = Agreementlayout.findViewById(R.id.txt_CheckOutLocName);
                                TextView txtChekOutDate = Agreementlayout.findViewById(R.id.txt_CheckOutDateTime);
                                TextView txtVehicleName = Agreementlayout.findViewById(R.id.txt_VehicleFullname);
                                TextView txtVehicleNo = Agreementlayout.findViewById(R.id.txtVehicleNo);
                                TextView txt_VinNo = Agreementlayout.findViewById(R.id.txt_VinNo);
                                TextView txt_LicNo = Agreementlayout.findViewById(R.id.txt_LicNo);
                                TextView txt_status_Name= Agreementlayout.findViewById(R.id.txt_res_status);

                                txtfullName.setText(customeR_NAME);
                                txtPhoneNo.setText(cust_MobileNo);
                                txtEmailId.setText(cust_Email);
                                txt_CheckInLoc.setText(check_in_Location_Name);
                                //check_IN Date
                                SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm aa");
                                Date date1 = dateFormat1.parse(check_IN);
                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/ yyyy, HH:mm aa", Locale.US);
                                String check_INStr = sdf1.format(date1);
                                txtCheckInDate.setText(check_INStr);

                                txtCheckOutLoc.setText(check_Out_Location_Name);
                                //check_Out Date
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                                Date date = dateFormat.parse(check_Out);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm aa", Locale.US);
                                String check_OutStr = sdf.format(date);
                                txtChekOutDate.setText(check_OutStr);

                                txtVehicleName.setText(vehicle);
                                txtVehicleNo.setText(vehicle_No);
                                txt_VinNo.setText(vin_Num);
                                txt_LicNo.setText(lic_Num);

                                txt_status_Name.setText(status_Name);

                                rlAgreement.addView(Agreementlayout);

                                linearLayout.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        try
                                        {
                                            Bundle AgreementsBundle=new Bundle();
                                            AgreementsBundle.putInt("reservation_ID",reservation_ID);
                                            AgreementsBundle.putInt("agreement_ID",agreement_ID);
                                            AgreementsBundle.putString("agreement_No",agreement_No);
                                            AgreementsBundle.putString("check_Out",check_Out);
                                            AgreementsBundle.putString("default_Check_Out",default_Check_Out);
                                            AgreementsBundle.putString("check_Out_Location_Name",check_Out_Location_Name);
                                            AgreementsBundle.putString("check_IN",check_IN);
                                            AgreementsBundle.putString("default_Check_In",default_Check_In);
                                            AgreementsBundle.putString("check_in_Location_Name",check_in_Location_Name);
                                            AgreementsBundle.putString("vehicle_Type_Name",vehicle_Type_Name);
                                            AgreementsBundle.putInt("vehicle_Type_ID",vehicle_Type_ID);
                                            AgreementsBundle.putInt("vehicle_ID",vehicle_ID);
                                            AgreementsBundle.putString("veh_Full_Name",veh_Full_Name);
                                            AgreementsBundle.putString("vehicle_No",vehicle_No);
                                            AgreementsBundle.putString("vehicle",vehicle);
                                            AgreementsBundle.putString("vin_Num",vin_Num);
                                            AgreementsBundle.putString("lic_Num",lic_Num);
                                            AgreementsBundle.putString("cust_MobileNo",cust_MobileNo);
                                            AgreementsBundle.putInt("customer_ID",customer_ID);
                                            AgreementsBundle.putString("cust_FName",cust_FName);
                                            AgreementsBundle.putString("cust_LName",cust_LName);
                                            AgreementsBundle.putString("customeR_NAME",customeR_NAME);
                                            AgreementsBundle.putString("cust_Phoneno",cust_Phoneno);
                                            AgreementsBundle.putString("cust_Email",cust_Email);
                                            AgreementsBundle.putString("veh_Img_Path",veh_Img_Path);
                                            AgreementsBundle.putString("cust_Img_Path",cust_Img_Path);
                                            AgreementsBundle.putString("rate_ID",rate_ID);
                                            AgreementsBundle.putString("totalDays",totalDays);
                                            AgreementsBundle.putString("veh_Bags",veh_Bags);
                                            AgreementsBundle.putString("veh_Seat",veh_Seat);
                                            AgreementsBundle.putString("transmission_Name",transmission_Name);
                                            AgreementsBundle.putString("cust_Img_Path",cust_Img_Path);
                                            AgreementsBundle.putString("d_FlightNo",d_FlightNo);
                                            AgreementsBundle.putString("d_Airport",d_Airport);
                                            AgreementsBundle.putString("default_D_Time",default_D_Time);
                                            AgreementsBundle.putString("default_Created_Date",default_Created_Date);
                                            AgreementsBundle.putString("status_Name",status_Name);
                                            Bundle Agreements=new Bundle();
                                            Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                                            System.out.println(AgreementsBundle);
                                            NavHostFragment.findNavController(Fragment_Agreements.this)
                                                    .navigate(R.id.action_Agreements_to_SummaryOfChargesForAgreements,Agreements);
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            progressDialog.dismiss();

                        }
                        else
                        {
                            String errorString = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),errorString,1);
                            progressDialog.dismiss();
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
