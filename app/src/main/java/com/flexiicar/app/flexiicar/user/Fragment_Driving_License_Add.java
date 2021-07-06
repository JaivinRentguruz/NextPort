package com.flexiicar.app.flexiicar.user;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.flexiicar.app.R;
import com.flexiicar.app.ScanDrivingLicense;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class Fragment_Driving_License_Add extends Fragment
{
    EditText edt_DriverName,edt_LicenseNo,edt_IssueBy,edt_RelationShip,edt_Email,edt_Telephone;
    TextView edt_ExpiryDate,edt_IssueDate;
    TextView txt_EditDrivingL;
    Handler handler = new Handler();
    public static Context context;
    public String id="";
    ImageView Image_Back;
    DatePickerDialog datePickerDialog;
    ImageView img_DLFronside,img_DLBackside;
    private static int RESULT_LOAD_IMAGE = 1;
    String imagestr;
    String imgId = "";
    JSONArray ImageList = new JSONArray();
    JSONObject frontImgObj = new JSONObject();
    JSONObject backImgObj = new JSONObject();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driving_license_add_update, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        edt_DriverName = view.findViewById(R.id.edt_driverName);
        edt_LicenseNo = view.findViewById(R.id.edt_LicenseNo);
        edt_ExpiryDate = view.findViewById(R.id.edt_exDate);
        edt_IssueDate = view.findViewById(R.id.edt_issuedate);
        edt_IssueBy = view.findViewById(R.id.edt_issueBy);
        edt_RelationShip = view.findViewById(R.id.edt_Relationship);
        edt_Email = view.findViewById(R.id.edt_DriverEmail);
        edt_Telephone = view.findViewById(R.id.edt_driverTelephone);
        txt_EditDrivingL = view.findViewById(R.id.txt_EditDL);
        Image_Back=view.findViewById(R.id.Image_BackDL);

        img_DLBackside= view.findViewById(R.id.img_DLBackside);
        img_DLFronside= view.findViewById(R.id.img_DLFronside);

        img_DLFronside.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                imgId = "2";
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        img_DLBackside.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                imgId = "3";
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        Image_Back.setOnClickListener(
                new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavHostFragment.findNavController(Fragment_Driving_License_Add.this)
                        .navigate(R.id.action_DrivingLicense_Update_to_DrivingLicense_Details);
            }
        });

        ImageView imgScanDrivingLicense = view.findViewById(R.id.imgScanDrivingLicense);
        imgScanDrivingLicense.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), ScanDrivingLicense.class);
                i.putExtra("afterScanBackTo", 2);
                startActivity(i);

            }
        });

        edt_ExpiryDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    // calender class's instance and get current date , month and year from calender
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR); // current year
                    int mMonth = c.get(Calendar.MONTH); // current month
                    int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                    // date picker dialog
                    datePickerDialog = new DatePickerDialog(getActivity(),R.style.DialogTheme,
                            new DatePickerDialog.OnDateSetListener()
                            {

                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                {
                                    String month, day;
                                    if(monthOfYear<9)
                                        month="0"+(monthOfYear+1);
                                    else
                                        month = (monthOfYear+1)+"";

                                    if(dayOfMonth<10)
                                        day="0"+dayOfMonth;
                                    else
                                        day=dayOfMonth+"";

                                    edt_ExpiryDate.setText(year + "-" + month + "-" + day);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        edt_IssueDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    // calender class's instance and get current date , month and year from calender
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR); // current year
                    int mMonth = c.get(Calendar.MONTH); // current month
                    int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                    // date picker dialog
                    datePickerDialog = new DatePickerDialog(getActivity(),R.style.DialogTheme,
                            new DatePickerDialog.OnDateSetListener()
                            {

                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                {
                                    String month, day;
                                    if(monthOfYear<9)
                                        month="0"+(monthOfYear+1);
                                    else
                                        month = (monthOfYear+1)+"";

                                    if(dayOfMonth<10)
                                        day="0"+dayOfMonth;
                                    else
                                        day=dayOfMonth+"";

                                    edt_IssueDate.setText(year + "-" + month + "-" + day);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

}
