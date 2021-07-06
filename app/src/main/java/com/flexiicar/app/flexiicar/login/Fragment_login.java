package com.flexiicar.app.flexiicar.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.flexiicar.app.R;
import com.flexiicar.app.adapters.CustomToast;
import com.flexiicar.app.flexiicar.booking.Booking_Activity;
import com.flexiicar.app.apicall.ApiService;
import com.flexiicar.app.apicall.OnResponseListener;
import com.flexiicar.app.apicall.RequestType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.flexiicar.app.apicall.ApiEndPoint.LOGIN_VERIFICATION;

public class Fragment_login extends Fragment
{
    public static Context context;
    ProgressDialog Loading;
    public String id = "", role = "";
    TextView txtRegister,txtforgetpassword,txt_GuestUser;
    LinearLayout lbllogin;
    EditText editText_Username, editText_Password;
    Handler handler=new Handler();
    CheckBox chksavepass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        txtRegister=view.findViewById(R.id.txtRegister);
        editText_Username = view.findViewById(R.id.edt_username);
        editText_Password = view.findViewById(R.id.edt_password);
        txtforgetpassword=view.findViewById(R.id.txt_forgetPassword);
        lbllogin = view.findViewById(R.id.lbllogin);
        chksavepass=view.findViewById(R.id.checkboxSavePass);
        txt_GuestUser=view.findViewById(R.id.txt_GuestUser);

        txt_GuestUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), Booking_Activity.class);
                startActivity(i);
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), Driver_Profile.class);
                startActivity(i);
            }
        });
        txtforgetpassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavHostFragment.findNavController(Fragment_login.this)
                        .navigate(R.id.action_LoginFragment_to_Forgot_Password);
            }
        });


        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");
        String default_Email=sp.getString("default_Email","");
        String default_Password=sp.getString("default_Password","");

        editText_Username.setText(default_Email);
        editText_Password.setText(default_Password);

        System.out.println("ID : " + id);
        System.out.println("Role : " + role);


        if (!id.equals("0") && id != null && !id.equals(null) && !id.equals("null") && role.equals("1"))
        {
            Intent i = new Intent(getActivity(), Booking_Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        Fragment_login.context = getActivity();

        lbllogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (editText_Username.getText().toString().equals(""))
                    CustomToast.showToast(getActivity(),"Please Enter UserName",1);
                else if (editText_Password.getText().toString().equals(""))
                CustomToast.showToast(getActivity(),"Please Enter Password",1);

                else
                {
                    JSONObject bodyParam = new JSONObject();
                    try
                    {
                        bodyParam.accumulate("Cust_Email", editText_Username.getText().toString());
                        bodyParam.accumulate("PasswordHash", editText_Password.getText().toString());
                        System.out.println(bodyParam);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    ApiService ApiService = new ApiService(doLogin, RequestType.POST,
                            LOGIN_VERIFICATION, BASE_URL_LOGIN,  new HashMap<String, String>(), bodyParam);
                }
            }
        });
    }
    OnResponseListener doLogin = new OnResponseListener()
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
                            JSONObject obj = resultSet.getJSONObject("t0040_Customer_Master");
                            System.out.println(obj);

                            id = obj.getString("customer_ID");
                            String cust_FName=obj.getString("cust_FName");
                            String cust_LName=obj.getString("cust_LName");
                            String cust_Street=obj.getString("cust_Street");
                            String cust_UnitNo=obj.getString("cust_UnitNo");
                            String cust_City=obj.getString("cust_City");
                            int cust_State_ID=obj.getInt("cust_State_ID");
                            int cust_Country_ID=obj.getInt("cust_Country_ID");
                            String cust_ZipCode=obj.getString("cust_ZipCode");
                            String cust_DOB=obj.getString("cust_DOB");
                            String cust_Email=obj.getString("cust_Email");
                            String cust_Phoneno=obj.getString("cust_Phoneno");
                            String cust_MobileNo=obj.getString("cust_MobileNo");
                            int customer_Type_ID=obj.getInt("customer_Type_ID");
                            int location_ID=obj.getInt("location_ID");
                            int rate_ID=obj.getInt("rate_ID");
                            int insurance_Cmp_ID=obj.getInt("insurance_Cmp_ID");
                            String cmp_Name=obj.getString("cmp_Name");
                            String cmp_Street=obj.getString("cmp_Street");
                            String cmp_UnitNo=obj.getString("cmp_UnitNo");
                            String cmp_City=obj.getString("cmp_City");
                            int cmp_State_ID=obj.getInt("cmp_State_ID");
                            int cmp_Country_ID=obj.getInt("cmp_Country_ID");
                            String cmp_ZipCode=obj.getString("cmp_ZipCode");
                            String cmp_Phoneno=obj.getString("cmp_Phoneno");
                            String licence_No=obj.getString("licence_No");
                            String lIssue_Date=obj.getString("lIssue_Date");
                            String lExpiry_Date=obj.getString("lExpiry_Date");
                            String lIssue_By=obj.getString("lIssue_By");
                            String passport_No=obj.getString("passport_No");
                            String pIssue_Date=obj.getString("pIssue_Date");
                            String pExpiry_Date=obj.getString("pExpiry_Date");
                            String pIssue_By=obj.getString("pIssue_By");
                            int cmp_ID=obj.getInt("cmp_ID");
                            int created_By=obj.getInt("created_By");
                            String created_Date=obj.getString("created_Date");
                            String cust_Gender=obj.getString("cust_Gender");
                            int popupNoteID=obj.getInt("popupNoteID");
                            int currentAgrID=obj.getInt("currentAgrID");
                            int isMobile=obj.getInt("isMobile");
                            int qbcustomerId=obj.getInt("qbcustomerId");
                            String cust_Country_Name=obj.getString("cust_Country_Name");
                            String cust_State_Name=obj.getString("cust_State_Name");
                            String cmp_Country_Name=obj.getString("cmp_Country_Name");
                            String cmp_State_Name=obj.getString("cmp_State_Name");

                            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar",MODE_PRIVATE);
                            SharedPreferences.Editor editor= sp.edit();

                            editor.putString(getString(R.string.id), id);
                            editor.putString("cust_FName" ,cust_FName);
                            editor.putString("cust_LName" ,cust_LName);
                            editor.putString("cust_Street" ,cust_Street);
                            editor.putString("cust_UnitNo" ,cust_UnitNo);
                            editor.putString("cust_City" ,cust_City);
                            editor.putInt("cust_State_ID" ,cust_State_ID);
                            editor.putInt("cust_Country_ID" ,cust_Country_ID);
                            editor.putString("cust_ZipCode" ,cust_ZipCode);
                            editor.putString("cust_DOB" ,cust_DOB);
                            editor.putString("cust_Email" ,cust_Email);
                            editor.putString("cust_Phoneno" ,cust_Phoneno);
                            editor.putString("cust_MobileNo" ,cust_MobileNo);
                            editor.putInt("customer_Type_ID" ,customer_Type_ID);
                            editor.putInt("location_ID" ,location_ID);
                            editor.putInt("rate_ID" ,rate_ID);
                            editor.putInt("insurance_Cmp_ID" ,insurance_Cmp_ID);
                            editor.putString("cmp_Name" ,cmp_Name);
                            editor.putString("cmp_Street" ,cmp_Street);
                            editor.putString("cmp_UnitNo" ,cmp_UnitNo);
                            editor.putString("cmp_City" ,cmp_City);
                            editor.putInt("cmp_State_ID" ,cmp_State_ID);
                            editor.putInt("cmp_Country_ID" ,cmp_Country_ID);
                            editor.putString("cmp_ZipCode" ,cmp_ZipCode);
                            editor.putString("cmp_Phoneno" ,cmp_Phoneno);
                            editor.putString("licence_No" ,licence_No);
                            editor.putString("lIssue_Date" ,lIssue_Date);
                            editor.putString("lIssue_By" ,lIssue_By);
                            editor.putString("passport_No" ,passport_No);
                            editor.putString("pIssue_Date" ,pIssue_Date);
                            editor.putString("pExpiry_Date" ,pExpiry_Date);
                            editor.putString("pIssue_By" ,pIssue_By);
                            editor.putInt("cmp_ID" ,cmp_ID);
                            editor.putString("lExpiry_Date" ,lExpiry_Date);
                            editor.putInt("created_By" ,created_By);
                            editor.putString("created_Date" ,created_Date);
                            editor.putString("cust_Gender" ,cust_Gender);
                            editor.putInt("popupNoteID" ,popupNoteID);
                            editor.putInt("currentAgrID" ,currentAgrID);
                            editor.putInt("isMobile" ,isMobile);
                            editor.putInt("qbcustomerId" ,qbcustomerId);
                            editor.putString("cust_Country_Name" ,cust_Country_Name);
                            editor.putString("cust_State_Name" ,cust_State_Name);
                            editor.putString("cmp_Country_Name" ,cmp_Country_Name);
                            editor.putString("cmp_State_Name" ,cmp_State_Name);

                            String msg = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),msg,0);

                            Intent i = new Intent(getActivity(), Booking_Activity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);

                            if (chksavepass.isChecked())
                            {
                                editor.putString("default_Email",editText_Username.getText().toString());
                                editor.putString("default_Password",editText_Password.getText().toString());
                            }
                            else
                            {
                                editor.putString("default_Email","");
                                editor.putString("default_Password","");
                            }
                            editor.commit();
                        }
                        else
                        {
                            String msg = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),msg,1);
                        }
                        // Loading.hide();
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
            System.out.println("Error"+error);
        }
    };
}