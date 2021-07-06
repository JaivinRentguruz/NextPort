package com.flexiicar.app.flexiicar.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.flexiicar.app.R;
import com.flexiicar.app.adapters.CustomToast;

public class Fragment_Driver_Profile_3 extends Fragment
{
    LinearLayout lblnext;
    ImageView backArrow;
    EditText edt_CustPhoneNo,edt_CustEmailId,edt_CustPassWord,edt_CustConformPass;
    CheckBox checkBoxTC;
    Bundle RegistrationBundle;
    TextView txtDiscard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_profile_3, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        lblnext = view.findViewById(R.id.lblnext1);
        backArrow =view.findViewById(R.id.backarrow_profile2);
        edt_CustPhoneNo=view.findViewById(R.id.edt_CustPhoneNo);
        edt_CustEmailId=view.findViewById(R.id.edt_CustEmailId);
        edt_CustPassWord=view.findViewById(R.id.edt_CustPassWord);
        edt_CustConformPass=view.findViewById(R.id.edt_CustConformPass);
        checkBoxTC=view.findViewById(R.id.CheckboxtTC);
        txtDiscard=view.findViewById(R.id.txtDiscardReg3);

        RegistrationBundle = getArguments().getBundle("RegistrationBundle");

        lblnext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {

                    if (edt_CustPhoneNo.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Your Mobile No.",1);
                    else if (edt_CustEmailId.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Your Email",1);
                    else if (edt_CustPassWord.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Your Password",1);
                    else if (edt_CustConformPass.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Conform Password",1);
                    else {
                        final String password = edt_CustPassWord.getText().toString();
                        final String confirmPassword = edt_CustConformPass.getText().toString();

                            if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword))
                            {
                                if (password.equals(confirmPassword))
                                {
                                    if(checkBoxTC.isChecked())
                                    {
                                    RegistrationBundle.putString("Cust_MobileNo", edt_CustPhoneNo.getText().toString());
                                    RegistrationBundle.putString("Cust_Email", edt_CustEmailId.getText().toString());
                                    RegistrationBundle.putString("PasswordHash", edt_CustConformPass.getText().toString());
                                    Bundle Registration = new Bundle();
                                    Registration.putBundle("RegistrationBundle", RegistrationBundle);
                                    System.out.println(Registration);

                                        NavHostFragment.findNavController(Fragment_Driver_Profile_3.this)
                                                .navigate(R.id.action_DriverProfile3_to_DriverProfile4, Registration);
                                    }
                                    else
                                    {
                                        String msg = "Please accept term & condition";
                                        CustomToast.showToast(getActivity(),msg,1);
                                    }

                                } else
                                    {
                                        CustomToast.showToast(getActivity(),"Your password and confirmation password do not match",1);
                                    }
                        }
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle Registration = new Bundle();
                Registration.putBundle("RegistrationBundle", RegistrationBundle);
                System.out.println(Registration);
                NavHostFragment.findNavController(Fragment_Driver_Profile_3.this)
                        .navigate(R.id.action_DriverProfile3_to_DriverProfile2,Registration);
            }
        });
        txtDiscard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(Fragment_Driver_Profile_3.this)
                        .navigate(R.id.action_DriverProfile3_to_CreateProfile);
            }
        });
    }
}

