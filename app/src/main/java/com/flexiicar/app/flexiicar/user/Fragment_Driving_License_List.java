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
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.flexiicar.app.apicall.ApiEndPoint.DRIVINGLICENSE;

public class Fragment_Driving_License_List extends Fragment
{
    Handler handler = new Handler();
    public static Context context;
    public String id = "",cust_FName="";
    ImageLoader imageLoader;
    TextView txt_Add_LicenseDetails;
    ImageView BackArrow;
    private Date oneWayTripDate;
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
        return inflater.inflate(R.layout.fragment_driving_license_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initImageLoader(getActivity());

        imageLoader = ImageLoader.getInstance();
        txt_Add_LicenseDetails=view.findViewById(R.id.txt_Add_LicenseDetails);
        BackArrow=view.findViewById(R.id.backarrowDLlist);
        BackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                NavHostFragment.findNavController(Fragment_Driving_License_List.this)
                        .navigate(R.id.action_DrivingLicense_Details_to_User_Details);
            }
        });
        txt_Add_LicenseDetails.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                NavHostFragment.findNavController(Fragment_Driving_License_List.this)
                        .navigate(R.id.action_DrivingLicense_Details_to_DrivingLicense_Add);
            }
        });
        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");

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
        Fragment_Driving_License_List.context = getActivity();

        ApiService ApiService = new ApiService(GetDrivingLicense, RequestType.GET,
                DRIVINGLICENSE, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);

    }
    OnResponseListener GetDrivingLicense = new OnResponseListener()
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

                            final JSONArray getdrivingLicense = resultSet.getJSONArray("drivingLicense");

                            final JSONArray getdrivingLicenseDoc = resultSet.getJSONArray("t0050_Documents");

                            final RelativeLayout rlDrivingLicence = getActivity().findViewById(R.id.rl_DrivingLicenceList);
                            int len,len1;
                            len = getdrivingLicense.length();
                            len1 = getdrivingLicenseDoc.length();

                            for (int j = 0; j < len; j++)
                            {
                                final JSONObject test = (JSONObject) getdrivingLicense.get(j);

                                final String licence_No = test.getString("licence_No");
                                final String lIssue_Date = test.getString("lIssue_Date");
                                final String lExpiry_Date = test.getString("lExpiry_Date");
                                final String lIssue_By=test.getString("lIssue_By");
                                final String lCategory = test.getString("lCategory");
                                final String driverFullName = test.getString("driverFullName");
                                final String driverRelationship = test.getString("driverRelationship");
                                final String driverEmailId=test.getString("driverEmailId");
                                final String driverTelephone = test.getString("driverTelephone");


                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                lp.setMargins(0, 10, 0, 0);

                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final LinearLayout Llayout = (LinearLayout) inflater.inflate(R.layout.list_driving_license, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                Llayout.setId(200 + j);
                                Llayout.setLayoutParams(lp);

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                                Date date = dateFormat.parse(lExpiry_Date);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                String strexpiryDate = sdf.format(date);

                                final JSONArray docArray = new JSONArray();

                                for(int i = 0; i < len1; i++)
                                {
                                    final JSONObject test1 = (JSONObject) getdrivingLicenseDoc.get(i);

                                    final String doc_Details = test1.getString("doc_Details");
                                    final String doc_Name = test1.getString("doc_Name");
                                    final int vhlPictureSide = test1.getInt("vhlPictureSide");

                                    JSONObject docObj = new JSONObject();
                                    docObj.put("doc_Name", doc_Name);
                                    docObj.put("doc_Details", doc_Details);
                                    docObj.put("vhlPictureSide", vhlPictureSide);
                                    docArray.put(docObj);
                                }

                                LinearLayout View_DL_Details=Llayout.findViewById(R.id.View_DL_Details);
                                TextView txt_primaryDriverName=Llayout.findViewById(R.id.txt_primaryDriverName);
                                TextView txt_DRelationship=Llayout.findViewById(R.id.txt_DRelationship);
                                TextView txt_DTelephone=Llayout.findViewById(R.id.txt_DTelephone);
                                TextView txt_DEmail=Llayout.findViewById(R.id.txt_DEmail);
                                TextView txt_DLNumber=Llayout.findViewById(R.id.txt_DLNumber);
                                TextView txt_DLExDate=Llayout.findViewById(R.id.txt_DLExDate);
                                TextView txt_DissueBy=Llayout.findViewById(R.id.txt_DissueBy);

                                SharedPreferences sp1 = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
                                String cust_FName = sp1.getString("cust_FName", "");
                                String cust_LName = sp1.getString("cust_LName", "");
                                String cust_Email = sp1.getString("cust_Email", "");
                                String cust_MobileNo = sp1.getString("cust_MobileNo", "");

                               // txt_primaryDriverName.setText(cust_FName+" "+cust_LName);
                                //txt_DTelephone.setText(cust_MobileNo);
                                //txt_DEmail.setText(cust_Email);

                                View_DL_Details.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Bundle DrivingLicenseBundle=new Bundle();
                                        DrivingLicenseBundle.putString("licence_No",licence_No);
                                        DrivingLicenseBundle.putString("lIssue_Date",lIssue_Date);
                                        DrivingLicenseBundle.putString("lExpiry_Date",lExpiry_Date);
                                        DrivingLicenseBundle.putString("lIssue_By",lIssue_By);
                                        DrivingLicenseBundle.putString("lCategory",lCategory);
                                        DrivingLicenseBundle.putString("driverFullName",driverFullName);
                                        DrivingLicenseBundle.putString("driverRelationship",driverRelationship);
                                        DrivingLicenseBundle.putString("driverEmailId",driverEmailId);
                                        DrivingLicenseBundle.putString("driverTelephone",driverTelephone);
                                        DrivingLicenseBundle.putString("docArray",docArray.toString());

                                        Bundle LicenseBundle=new Bundle();
                                        LicenseBundle.putBundle("LicenseBundle", DrivingLicenseBundle);

                                        System.out.println(LicenseBundle);
                                        NavHostFragment.findNavController(Fragment_Driving_License_List.this)
                                                .navigate(R.id.action_DrivingLicense_Details_to_DrivingLicense_Update,LicenseBundle);
                                    }
                                });

                                txt_primaryDriverName.setText(driverFullName);
                                txt_DRelationship.setText(driverRelationship);
                                txt_DTelephone.setText(driverTelephone);
                                txt_DEmail.setText(driverEmailId);
                                txt_DLNumber.setText(licence_No);
                                txt_DLExDate.setText(strexpiryDate);
                                txt_DissueBy.setText(lIssue_By);
                                rlDrivingLicence.addView(Llayout);
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
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };

}
