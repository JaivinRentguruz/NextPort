package com.flexiicar.app.flexiicar.login;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.flexiicar.app.R;
import com.flexiicar.app.ScanDrivingLicense;
import com.flexiicar.app.adapters.CustomToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Fragment_Driver_Profile_2 extends Fragment
{
    ImageView backArrow;
    LinearLayout lblnext;
    TextView edt_ExpiryDateDL,Cus_DateofBirth,IssueDate;
    EditText edt_DriverLicenseNO,DL_IssueBy,StateandProvience;
    DatePickerDialog datePickerDialog;
    Bundle RegistrationBundle;
    public int RESULT_LOAD_IMAGE = 1;
    JSONArray ImageList = new JSONArray();
    ImageView DLBacksideImg,DLFronsideImg;
    String imgId = "";
    TextView txtDiscard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_profile_2, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            lblnext = view.findViewById(R.id.lblnextscreen);
            backArrow = view.findViewById(R.id.backArrow_profile);
            edt_DriverLicenseNO = view.findViewById(R.id.edt_DriverLicenseNO);
            DL_IssueBy = view.findViewById(R.id.DL_IssueDate);
            StateandProvience = view.findViewById(R.id.StatandProvience);
            DLBacksideImg = view.findViewById(R.id.DLBacksideImg);
            DLFronsideImg = view.findViewById(R.id.DLFronsideImg);
            txtDiscard = view.findViewById(R.id.txtDiscardReg2);

            RegistrationBundle = getArguments().getBundle("RegistrationBundle");


            ImageView imgScanDrivingLicense = view.findViewById(R.id.LicenceScanimg);

            imgScanDrivingLicense.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    Intent i = new Intent(getActivity(), ScanDrivingLicense.class);
                    i.putExtra("afterScanBackTo", 4);
                    startActivity(i);

                }
            });
            txtDiscard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Driver_Profile_2.this)
                            .navigate(R.id.action_DriverProfile2_to_CreateProfile);
                }
            });
            edt_ExpiryDateDL = view.findViewById(R.id.edt_ExpiryDateDL);
            edt_ExpiryDateDL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // calender class's instance and get current date , month and year from calender
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year
                        int mMonth = c.get(Calendar.MONTH); // current month
                        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                        // date picker dialog
                        datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        String month, day;
                                        if (monthOfYear < 9)
                                            month = "0" + (monthOfYear + 1);
                                        else
                                            month = (monthOfYear + 1) + "";

                                        if (dayOfMonth < 10)
                                            day = "0" + dayOfMonth;
                                        else
                                            day = dayOfMonth + "";

                                        edt_ExpiryDateDL.setText(month + "/" + day + "/" + year);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Cus_DateofBirth = view.findViewById(R.id.Cus_DateofBirth);
            Cus_DateofBirth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // calender class's instance and get current date , month and year from calender
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year
                        int mMonth = c.get(Calendar.MONTH); // current month
                        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                        // date picker dialog
                        datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        String month, day;
                                        if (monthOfYear < 9)
                                            month = "0" + (monthOfYear + 1);
                                        else
                                            month = (monthOfYear + 1) + "";

                                        if (dayOfMonth < 10)
                                            day = "0" + dayOfMonth;
                                        else
                                            day = dayOfMonth + "";

                                        Cus_DateofBirth.setText(month + "/" + day + "/" + year);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            IssueDate = view.findViewById(R.id.ISSueDate);
            IssueDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // calender class's instance and get current date , month and year from calender
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year
                        int mMonth = c.get(Calendar.MONTH); // current month
                        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                        // date picker dialog
                        datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        String month, day;
                                        if (monthOfYear < 9)
                                            month = "0" + (monthOfYear + 1);
                                        else
                                            month = (monthOfYear + 1) + "";

                                        if (dayOfMonth < 10)
                                            day = "0" + dayOfMonth;
                                        else
                                            day = dayOfMonth + "";

                                        IssueDate.setText(month + "/" + day + "/" + year);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            DLFronsideImg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    imgId = "2";
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                }
            });
            DLBacksideImg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    imgId = "3";
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                }
            });
            lblnext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {

                        if (edt_DriverLicenseNO.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Your Driving License NO.",1);
                        else if (edt_ExpiryDateDL.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Your Driving License Expiry Date",1);
                        else if (Cus_DateofBirth.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Your Date Of Birth",1);
                        else if (IssueDate.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter License Issue Date",1);
                        else if (StateandProvience.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Your State Name",1);
                        else {

                            RegistrationBundle.putString("Licence_No", edt_DriverLicenseNO.getText().toString());

                            //ExpiryDate
                            String Exdatestr = edt_ExpiryDateDL.getText().toString();
                            Date ExDate = new SimpleDateFormat("mm/dd/yyyy").parse(Exdatestr);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                            String parsedDate = formatter.format(ExDate);
                            RegistrationBundle.putString("LExpiry_Date", parsedDate);
                            //Date Of Birth
                            String Cus_DateofBirthStr = Cus_DateofBirth.getText().toString();
                            Date DateofBirth = new SimpleDateFormat("mm/dd/yyyy").parse(Cus_DateofBirthStr);
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd");
                            String parsedDateofBirth = format1.format(DateofBirth);
                            RegistrationBundle.putString("Cust_DOB", parsedDateofBirth);
                            RegistrationBundle.putString("LIssue_By", DL_IssueBy.getText().toString());
                            //Issue Date
                            String IssueDateStr = IssueDate.getText().toString();
                            Date DateofIssue = new SimpleDateFormat("mm/dd/yyyy").parse(IssueDateStr);
                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-mm-dd");
                            String parsedIssueDate = format2.format(DateofIssue);
                            RegistrationBundle.putString("LIssue_Date", parsedIssueDate);
                            RegistrationBundle.putString("Cust_StateProvience", StateandProvience.getText().toString());
                            RegistrationBundle.putString("ImageList", ImageList.toString());
                            Bundle Registration = new Bundle();
                            Registration.putBundle("RegistrationBundle", RegistrationBundle);
                            System.out.println(Registration);

                            NavHostFragment.findNavController(Fragment_Driver_Profile_2.this)
                                    .navigate(R.id.action_DriverProfile2_to_DriverProfile3, Registration);
                        }
                    } catch (Exception e) {
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
                    NavHostFragment.findNavController(Fragment_Driver_Profile_2.this)
                            .navigate(R.id.action_DriverProfile2_to_DriverProfile,Registration);
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        try {


            if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                Bitmap bitmap = null;

                try {
                    bitmap = getScaledBitmap(selectedImage,250,250);
                    if (imgId.equals("2"))
                    {
                        JSONObject docObj = new JSONObject();
                        docObj.put("Doc_For", 6);
                        docObj.put("VhlPictureSide", 2);
                        docObj.put("fileBase64", getImagePathFromUri(selectedImage));
                        ImageList.put(docObj);
                        DLFronsideImg.setImageBitmap(bitmap);
                    }
                    if (imgId.equals("3"))
                    {
                        JSONObject docObj = new JSONObject();
                        docObj.put("Doc_For", 6);
                        docObj.put("VhlPictureSide", 3);
                        docObj.put("fileBase64", getImagePathFromUri(selectedImage));
                        ImageList.put(docObj);
                        DLBacksideImg.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getImagePathFromUri(Uri contentUri)
    {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private Bitmap getScaledBitmap(Uri selectedImage, int width, int height) throws FileNotFoundException
    {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested one
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
