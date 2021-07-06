package com.flexiicar.app.flexiicar.selfcheckin;

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
import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_CHECKIN;
import static com.flexiicar.app.apicall.ApiEndPoint.UPDATESELFCHECKIN;

public class Fragment_Summary_Of_Charges_For_SelfCheckIn extends Fragment
{
    Handler handler = new Handler();
    public static Context context;
    Bundle AgreementsBundle;
    ImageView SummaryOfChargeArrow,SummaryOfChargeArrowDown,QrImages,BackArrow,driverdetailsArrow,ArrowSelfcheckoutComplete,term_conditionArrow;
    RelativeLayout rlSummaryOfCharge;
    LinearLayout SelfCheckoutComplete,lblpay;
    TextView txtVehicleType,txtVehicleName,txtcheckOutLocName,txtcheckOutDate,txtCheckInLocName,txtCheckInDate,txtconforNo,
            TotalAmount,txtTotalDay,txtHome,txt_Vehiclerate;

    ImageLoader imageLoader;
    String serverpath="";
    JSONArray ImageList = new JSONArray();
    int reservation_ID;
    JSONArray summaryOfCharges = new JSONArray();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary_of_charges_for_selfcheckin, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        try {

            initImageLoader(getActivity());
            imageLoader = ImageLoader.getInstance();

            AgreementsBundle = getArguments().getBundle("AgreementsBundle");
            TotalAmount=view.findViewById(R.id.TotalAmountForSC);

            SummaryOfChargeArrow = view.findViewById(R.id.ArrowForSC);
            SummaryOfChargeArrowDown= view.findViewById(R.id.DownArrowForSC);

            QrImages = view.findViewById(R.id.QrImages);
            BackArrow = view.findViewById(R.id.backimg_selfcheckIN);
            driverdetailsArrow = view.findViewById(R.id.driverdetailsArrow);
            ArrowSelfcheckoutComplete = view.findViewById(R.id.ArrowSelfcheckoutComplete);
            term_conditionArrow = view.findViewById(R.id.term_conditionSC);
            SelfCheckoutComplete= view.findViewById(R.id.SelfCheckoutComplete);
            rlSummaryOfCharge = view.findViewById(R.id.rl_SummaryofchargeForSC);

            txtTotalDay=view.findViewById(R.id.txtTotalDay11);
            txtVehicleType=view.findViewById(R.id.txtVehiclename);
            txtVehicleName=view.findViewById(R.id.txtVehiclename2);
            txtcheckOutLocName=view.findViewById(R.id.txtcheckOutLocName);
            txtcheckOutDate=view.findViewById(R.id.txtcheckOutDate);

            txtCheckInLocName=view.findViewById(R.id.txtCheckInLocName);
            txtCheckInDate=view.findViewById(R.id.txtCheckInDate);
            txtconforNo=view.findViewById(R.id.txtconforNo);
            lblpay=view.findViewById(R.id.lblpayNowforSC);
            txt_Vehiclerate=view.findViewById(R.id.txt_Vehiclerate11);

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            serverpath = sp.getString("serverPath", "");

            ImageView Vehicle_Img=view.findViewById(R.id.VehicleImage11);

            String VehicleImgstr=AgreementsBundle.getString("veh_Img_Path");
            String url1 = serverpath+VehicleImgstr.substring(2);
            imageLoader.displayImage(url1,Vehicle_Img);

            String DocName=AgreementsBundle.getString("doc_Name");
            String doc_Details=AgreementsBundle.getString("doc_Details");

            if(DocName !=null && doc_Details !=null)
            {
                String url2 = serverpath + doc_Details.substring(2);
                url2 = url2.substring(0, url2.lastIndexOf("/") + 1) + DocName;
                imageLoader.displayImage(url2, QrImages);
            }

            txtCheckInLocName.setText(AgreementsBundle.getString("check_Out_Location_Name"));
            txtcheckOutLocName.setText(AgreementsBundle.getString("check_in_Location_Name"));

            String check_IN=(AgreementsBundle.getString("check_IN"));
            // Date
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm aa");
            Date date1 = dateFormat1.parse(check_IN);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/ yyyy, HH:mm aa", Locale.US);
            String checkinStr = sdf1.format(date1);
            txtCheckInDate.setText(checkinStr);

            String CheckOut=(AgreementsBundle.getString("check_Out"));
            //check_Out Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            Date date = dateFormat.parse(CheckOut);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm aa", Locale.US);
            String check_OutStr = sdf.format(date);
            txtcheckOutDate.setText(check_OutStr);

            String DayStr=AgreementsBundle.getString("totalDays");
            DayStr=DayStr.substring(0,DayStr.length()-2);
            txtTotalDay.setText(DayStr);
            txt_Vehiclerate.setText(AgreementsBundle.getString("rate_ID"));

            txtVehicleType.setText(AgreementsBundle.getString("vehicle_Type_Name"));
            txtVehicleName.setText(AgreementsBundle.getString("vehicle"));

            reservation_ID=AgreementsBundle.getInt("reservation_ID");
            txtconforNo.setText(String.valueOf(reservation_ID));

            txtHome=view.findViewById(R.id.txtHome);

            txtHome.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_SelfCheckIn.this)
                            .navigate(R.id.action_SummaryOfChargeForSelfCheckIn_to_Agreements);
                }
            });

            BackArrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try {
                        ImageList = new JSONArray(getArguments().getString("ImageList"));
                        Bundle SelfCheckInBundle = new Bundle();
                        SelfCheckInBundle.putBundle("AgreementsBundle", AgreementsBundle);
                        SelfCheckInBundle.putString("ImageList",ImageList.toString());
                        System.out.println(SelfCheckInBundle);
                        NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_SelfCheckIn.this)
                                .navigate(R.id.action_SummaryOfChargeForSelfCheckIn_to_Self_check_In, SelfCheckInBundle);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            ArrowSelfcheckoutComplete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (SelfCheckoutComplete.getVisibility() == View.GONE)
                    {
                        SelfCheckoutComplete.setVisibility(View.VISIBLE);

                    } else
                        SelfCheckoutComplete.setVisibility(View.GONE);
                }
            });
            term_conditionArrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle Agreements = new Bundle();
                    Agreements.putInt("termcondition", 6);
                    Agreements.putBundle("AgreementsBundle", AgreementsBundle);
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_SelfCheckIn.this)
                            .navigate(R.id.action_SummaryOfChargeForSelfCheckIn_to_TermAndCondition,Agreements);
                }
            });
            lblpay.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle SelfCheckInBundle = new Bundle();
                    AgreementsBundle.putInt("BookingStep",5);
                    AgreementsBundle.putString("TotalAmount",TotalAmount.getText().toString());
                    AgreementsBundle.putString("SummaryOfCharges", summaryOfCharges.toString());
                    SelfCheckInBundle.putBundle("AgreementsBundle", AgreementsBundle);
                    System.out.println(SelfCheckInBundle);
                    NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_SelfCheckIn.this)
                            .navigate(R.id.action_SummaryOfChargeForSelfCheckIn_to_PaymentCheckOutSelfCheckIn,SelfCheckInBundle);
                }
            });

            final LinearLayout driverdetails1 = view.findViewById(R.id.driver_detailsForSC);

            TextView driverName = view.findViewById(R.id.DriverName);
            TextView driverPhone = view.findViewById(R.id.DriverPhoneno);
            TextView driverEmail = view.findViewById(R.id.DriverEmail);

            String cust_FName = AgreementsBundle.getString("cust_FName");
            String cust_LName = AgreementsBundle.getString("cust_LName");
            String cust_MobileNo = AgreementsBundle.getString("cust_MobileNo");
            String cust_Email = AgreementsBundle.getString("cust_Email");

            driverName.setText(cust_FName + " " + cust_LName);
            driverPhone.setText(cust_MobileNo);
            driverEmail.setText(cust_Email);

            driverdetailsArrow = view.findViewById(R.id.driverdetailsArrow);
            driverdetailsArrow.setOnClickListener(new View.OnClickListener()
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
                bodyParam.accumulate("AgreementId",AgreementsBundle.getInt("agreement_ID"));
                bodyParam.accumulate("VehicleId",AgreementsBundle.getInt("vehicle_ID"));
                bodyParam.accumulate("originalCheckInDate",AgreementsBundle.getString("originalCheckInDate"));
                bodyParam.accumulate("originalCheckInLocationId",AgreementsBundle.getInt("originalCheckInLocationId"));
                bodyParam.accumulate("ActualReturnDate",AgreementsBundle.getString("ActualReturnDate"));
                bodyParam.accumulate("actualReturnLocationId",AgreementsBundle.getInt("actualReturnLocationId"));
                System.out.println(bodyParam);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            ApiService ApiService = new ApiService(getSummaryOfCharges, RequestType.POST,
                    UPDATESELFCHECKIN, BASE_URL_CHECKIN, new HashMap<String, String>(), bodyParam);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    OnResponseListener getSummaryOfCharges = new OnResponseListener()
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

                                //getTaxDetails
                                final JSONArray getTaxDetails = resultSet.getJSONArray("taxModel");

                                //getsummaryOfCharges

                                final JSONArray getsummaryOfCharges = resultSet.getJSONArray("summaryOfCharges");
                                final RelativeLayout rlSummaryofcharge = getActivity().findViewById(R.id.rl_SummaryofchargeForSC);

                                int len;
                                len = getsummaryOfCharges.length();

                                for (int j = 0; j < len; j++)
                                {
                                    final JSONObject test = (JSONObject) getsummaryOfCharges.get(j);

                                    final  int sortId = test.getInt("sortId");
                                    final  String chargeCode = test.getString("chargeCode");
                                    final  String chargeName = test.getString("chargeName");
                                    final  double chargeAmount=test.getDouble("chargeAmount");

                                    JSONObject summaryOfChargesObj = new JSONObject();

                                    summaryOfChargesObj.put("sortId",sortId);
                                    summaryOfChargesObj.put("chargeCode",chargeCode);
                                    summaryOfChargesObj.put("chargeName",chargeName);
                                    summaryOfChargesObj.put("chargeAmount",chargeAmount);

                                    summaryOfCharges.put(summaryOfChargesObj);
                                    if(chargeName.equals("Balance"))
                                    {
                                        TotalAmount.setText((String.format(Locale.US,"%.2f",chargeAmount)));
                                        AgreementsBundle.putDouble("total",chargeAmount);
                                    }

                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    lp.setMargins(0, 0, 0, 0);

                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.vehicle_tax_details, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                    linearLayout.setId(200 + j);
                                    linearLayout.setLayoutParams(lp);
                                    LinearLayout arrowIcon=(LinearLayout)linearLayout.findViewById(R.id.arrow_icon);
                                    LinearLayout SummaryOfChargeLayout=(LinearLayout)linearLayout.findViewById(R.id.SummaryOfChargesLayout);

                                    TextView txt_charge= (TextView)linearLayout.findViewById(R.id.txt_charge);
                                    TextView txt_chargeName=(TextView)linearLayout.findViewById(R.id.txt_chargeName);
                                    if(chargeName.equals("Taxes And Fees"))
                                    {
                                        arrowIcon.setVisibility(View.VISIBLE);

                                        arrowIcon.setOnClickListener(new View.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(View view)
                                            {
                                                Bundle Agreements = new Bundle();
                                                Agreements.putBundle("AgreementsBundle", AgreementsBundle);
                                                Agreements.putInt("backtoforterms",7);
                                                Agreements.putBoolean("TaxType",false);
                                                NavHostFragment.findNavController(Fragment_Summary_Of_Charges_For_SelfCheckIn.this)
                                                       .navigate(R.id.action_SummaryOfChargeForSelfCheckIn_to_Total_tax_fee_details,Agreements);
                                            }
                                        });

                                    }
                                    if(chargeName.equals("Paid Amount"))
                                    {
                                        SummaryOfChargeLayout.setBackgroundColor(getResources().getColor(R.color.footerButtonBC));
                                        txt_chargeName.setTextColor(getResources().getColor(R.color.screen_bg_color));
                                        txt_charge.setTextColor(getResources().getColor(R.color.screen_bg_color));
                                    }
                                    if(chargeName.equals("Balance"))
                                    {
                                        SummaryOfChargeLayout.setBackgroundColor(getResources().getColor(R.color.selected_dot));
                                        txt_chargeName.setTextColor(getResources().getColor(R.color.screen_bg_color));
                                        txt_charge.setTextColor(getResources().getColor(R.color.screen_bg_color));
                                    }
                                    String str=String.format(Locale.US,"%.2f",chargeAmount);
                                    txt_charge.setText("USD$ "+str);
                                    txt_chargeName.setText(chargeName);

                                    if(chargeName.equals("Discount Applied"))
                                    {
                                        txt_charge.setTextColor(getResources().getColor(R.color.btn_bg_color_2));
                                    }

                                    rlSummaryofcharge.addView(linearLayout);
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
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };

}
