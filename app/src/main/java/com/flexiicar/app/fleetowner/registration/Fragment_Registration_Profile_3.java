package com.flexiicar.app.fleetowner.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.flexiicar.app.R;

public class Fragment_Registration_Profile_3 extends Fragment
{
    LinearLayout lblnext;
    ImageView backArrow;
    EditText edt_CustPhoneNo,edt_CustEmailId,edt_CustPassWord,edt_CustConformPass;
    CheckBox checkBoxTC;
    Bundle RegistrationBundle;
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


       // RegistrationBundle = getArguments().getBundle("RegistrationBundle");

        lblnext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {

                    NavHostFragment.findNavController(Fragment_Registration_Profile_3.this)
                            .navigate(R.id.action_DriverProfile3_to_DriverProfile4);
                   /* if (edt_CustPhoneNo.getText().toString().equals(""))
                        Toast.makeText(getActivity(), "Please Enter Your Mobile No", Toast.LENGTH_LONG).show();
                    else if (edt_CustEmailId.getText().toString().equals(""))
                        Toast.makeText(getActivity(), "Please Enter Your Email", Toast.LENGTH_LONG).show();
                    else if (edt_CustPassWord.getText().toString().equals(""))
                        Toast.makeText(getActivity(), "Please Enter Your Password", Toast.LENGTH_LONG).show();
                    else if (edt_CustConformPass.getText().toString().equals(""))
                        Toast.makeText(getActivity(), "Please Enter Conform Password", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(getActivity(), "Your password and confirmation password do not match", Toast.LENGTH_LONG).show();
                                }
                        }
                    }*/
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
                NavHostFragment.findNavController(Fragment_Registration_Profile_3.this)
                        .navigate(R.id.action_DriverProfile3_to_DriverProfile2);
            }
        });

    }
}

