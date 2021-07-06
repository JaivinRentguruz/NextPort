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

import static android.content.Context.MODE_PRIVATE;
import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.flexiicar.app.apicall.ApiEndPoint.GETCUSTOMERINSURANCE;

public class Fragment_Customer_Insurance_list extends Fragment
{
    Handler handler = new Handler();
    public static Context context;
    public String id = "";
    ImageLoader imageLoader;
    String serverpath = "";
    ImageView BackArrow;

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
        return inflater.inflate(R.layout.fragment_customer_insurance_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initImageLoader(getActivity());
        imageLoader = ImageLoader.getInstance();

        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");

        BackArrow=view.findViewById(R.id.backimg_IP);

        BackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(Fragment_Customer_Insurance_list.this).
                        navigate(R.id.action_InsurancePolicyList_to_User_Details);
            }
        });

        String bodyParam = "";
        try {
            bodyParam += "customerId=" + id;

            System.out.println(bodyParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AndroidNetworking.initialize(getActivity());
        Fragment_Payment_checkout.context = getActivity();

        ApiService ApiService = new ApiService(GetCustomerInsurance, RequestType.GET,
                GETCUSTOMERINSURANCE, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);

    }

    //GetCustomerInsurance
    OnResponseListener GetCustomerInsurance = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {
                            JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                            final JSONArray getcustomerInsurance = resultSet.getJSONArray("customerInsurance");
                            final RelativeLayout rlInsurancePolicy = getActivity().findViewById(R.id.rl_InsurancePolicyList);

                            final JSONArray getInsuranceDoc = resultSet.getJSONArray("t0050_Documents");


                            int len,len1;
                            len = getcustomerInsurance.length();
                            len1 = getInsuranceDoc.length();

                            for (int j = 0; j < len; j++)
                            {
                                final JSONObject test = (JSONObject) getcustomerInsurance.get(j);

                                final int insurance_Cmp_ID = test.getInt("insurance_Cmp_ID");
                                final String insurance_Cmp_Name = test.getString("insurance_Cmp_Name");
                                final String policy_No = test.getString("policy_No");
                                final String expiryDate = test.getString("expiryDate");
                                final String insIssueDate = test.getString("insIssueDate");
                                final String imageFile = test.getString("imageList");

                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                lp.setMargins(0, 10, 0, 0);

                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.list_customer_insurance, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                linearLayout.setId(200 + j);
                                linearLayout.setLayoutParams(lp);

                                final JSONArray docArray = new JSONArray();

                                for(int i = 0; i < len1; i++)
                                {
                                    final JSONObject test1 = (JSONObject) getInsuranceDoc.get(i);

                                    final String doc_Details = test1.getString("doc_Details");
                                    final String doc_Name = test1.getString("doc_Name");

                                    JSONObject docObj = new JSONObject();
                                    docObj.put("doc_Name", doc_Name);
                                    docObj.put("doc_Details", doc_Details);
                                    docArray.put(docObj);
                                }

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                                Date date = dateFormat.parse(expiryDate);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                String strexpiryDate = sdf.format(date);

                                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                                Date date1 = dateFormat1.parse(insIssueDate);
                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                                String strinsIssueDate = sdf1.format(date1);

                                TextView txtInsPolicyNo=linearLayout.findViewById(R.id.txt_policy_NoINs);
                                TextView txtinsurance_Cmp_Name=linearLayout.findViewById(R.id.txt_insurance_Cmp_Name);
                                TextView ExpiryDate=linearLayout.findViewById(R.id.txt_ExpiryDateIns);
                                TextView IssueDate=linearLayout.findViewById(R.id.txt_IssueDateIns);

                                TextView txtName=linearLayout.findViewById(R.id.txtprimaryName);
                                TextView txtTelephoneNo=linearLayout.findViewById(R.id.txtTelephoneNo);
                                TextView txtUserEmail=linearLayout.findViewById(R.id.txtUserEmail);

                                TextView View_InsDetails=linearLayout.findViewById(R.id.View_InsDetails);

                                SharedPreferences sp1 = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
                                String cust_FName = sp1.getString("cust_FName", "");
                                String cust_LName = sp1.getString("cust_LName", "");
                                String cust_Email = sp1.getString("cust_Email", "");
                                String cust_MobileNo = sp1.getString("cust_MobileNo", "");

                                txtName.setText(cust_FName+" "+cust_LName);
                                txtTelephoneNo.setText(cust_MobileNo);
                                txtUserEmail.setText(cust_Email);

                                LinearLayout View_InsurancePolicy=linearLayout.findViewById(R.id.llInsurancePolicy);

                                View_InsurancePolicy.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Bundle InsurancePolicyBundle=new Bundle();
                                        InsurancePolicyBundle.putString("insurance_Cmp_Name",insurance_Cmp_Name);
                                        InsurancePolicyBundle.putString("policy_No",policy_No);
                                        InsurancePolicyBundle.putString("expiryDate",expiryDate);
                                        InsurancePolicyBundle.putString("insIssueDate",insIssueDate);
                                        InsurancePolicyBundle.putString("docArray",docArray.toString());
                                        Bundle InsurancePolicy=new Bundle();
                                        InsurancePolicy.putBundle("InsurancePolicyBundle", InsurancePolicyBundle);
                                        NavHostFragment.findNavController(Fragment_Customer_Insurance_list.this)
                                                .navigate(R.id.action_InsurancePolicyList_to_InsurancePolicy,InsurancePolicy);
                                    }
                                });
                                View_InsDetails.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        Bundle InsurancePolicyBundle=new Bundle();
                                        InsurancePolicyBundle.putString("insurance_Cmp_Name",insurance_Cmp_Name);
                                        InsurancePolicyBundle.putString("policy_No",policy_No);
                                        InsurancePolicyBundle.putString("expiryDate",expiryDate);
                                        InsurancePolicyBundle.putString("insIssueDate",insIssueDate);
                                        InsurancePolicyBundle.putString("docArray",docArray.toString());
                                        Bundle InsurancePolicy=new Bundle();
                                        InsurancePolicy.putBundle("InsurancePolicyBundle", InsurancePolicyBundle);
                                        NavHostFragment.findNavController(Fragment_Customer_Insurance_list.this)
                                                .navigate(R.id.action_InsurancePolicyList_to_InsurancePolicy,InsurancePolicy);
                                    }
                                });
                                txtinsurance_Cmp_Name.setText(insurance_Cmp_Name);
                                txtInsPolicyNo.setText(policy_No);
                                ExpiryDate.setText(strexpiryDate);
                                IssueDate.setText(strinsIssueDate);

                                rlInsurancePolicy.addView(linearLayout);
                            }

                        } else {
                            String errorString = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),errorString,1);
                        }
                    } catch (Exception e)
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