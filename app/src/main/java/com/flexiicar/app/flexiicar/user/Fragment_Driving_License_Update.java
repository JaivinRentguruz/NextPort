package com.flexiicar.app.flexiicar.user;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import com.flexiicar.app.R;
import com.flexiicar.app.ScanDrivingLicense;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.flexiicar.app.apicall.ApiEndPoint.UPDATEDRIVINGLICENSE;

public class Fragment_Driving_License_Update extends Fragment
{
    EditText edt_DriverName,edt_LicenseNo,edt_IssueBy,edt_RelationShip,edt_Email,edt_Telephone;
    TextView txt_EditDrivingL,ExpiryDate,txtIssueDate;
    Handler handler = new Handler();
    public static Context context;
    public String id="";
    Bundle LicenseBundle;
    ImageView Image_Back;
    DatePickerDialog datePickerDialog;
    ImageView img_DLFronside,img_DLBackside;
    private static int RESULT_LOAD_IMAGE = 1;
    String imagestr;
    String imgId = "";
    ImageLoader imageLoader;
    String serverpath="";
    JSONArray ImageList = new JSONArray();
    JSONObject frontImgObj = new JSONObject();
    JSONObject backImgObj = new JSONObject();

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driving_license_add_update, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        try {
            initImageLoader(getActivity());
            imageLoader = ImageLoader.getInstance();

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            serverpath = sp.getString("serverPath", "");
            id = sp.getString(getString(R.string.id), "");

            edt_DriverName = view.findViewById(R.id.edt_driverName);
            edt_LicenseNo = view.findViewById(R.id.edt_LicenseNo);
            ExpiryDate = view.findViewById(R.id.edt_exDate);
            edt_IssueBy = view.findViewById(R.id.edt_issueBy);
            txtIssueDate=view.findViewById(R.id.edt_issuedate);
            edt_RelationShip = view.findViewById(R.id.edt_Relationship);
            edt_Email = view.findViewById(R.id.edt_DriverEmail);
            edt_Telephone = view.findViewById(R.id.edt_driverTelephone);
            txt_EditDrivingL = view.findViewById(R.id.txt_EditDL);

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

            Image_Back = view.findViewById(R.id.Image_BackDL);
            Image_Back.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    NavHostFragment.findNavController(Fragment_Driving_License_Update.this)
                            .navigate(R.id.action_DrivingLicense_Update_to_DrivingLicense_Details);
                }
            });

            ImageView imgScanDrivingLicense = view.findViewById(R.id.imgScanDrivingLicense);

            imgScanDrivingLicense.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    Intent i = new Intent(getActivity(), ScanDrivingLicense.class);
                    i.putExtra("afterScanBackTo", 2);
                    startActivity(i);

                }
            });

            ExpiryDate.setOnClickListener(new View.OnClickListener()
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

                                        ExpiryDate.setText(year + "-" + month + "-" + day);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            txtIssueDate.setOnClickListener(new View.OnClickListener()
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

                                        txtIssueDate.setText(year + "-" + month + "-" + day);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            LicenseBundle = getArguments().getBundle("LicenseBundle");

            String lExpiry_Date = LicenseBundle.getString("lExpiry_Date");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date = dateFormat.parse(lExpiry_Date);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String lExpiry_DateStr = sdf.format(date);

            String lIssue_Date=LicenseBundle.getString("lIssue_Date");
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date1 = dateFormat1.parse(lIssue_Date);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            String lIssue_DateStr = sdf1.format(date1);

            edt_DriverName.setText(LicenseBundle.getString("driverFullName"));
            edt_LicenseNo.setText(LicenseBundle.getString("licence_No"));
            ExpiryDate.setText(lExpiry_DateStr);
            txtIssueDate.setText(lIssue_DateStr);

            edt_IssueBy.setText(LicenseBundle.getString("lIssue_By"));
            edt_RelationShip.setText(LicenseBundle.getString("driverRelationship"));
            edt_Email.setText(LicenseBundle.getString("driverEmailId"));
            edt_Telephone.setText(LicenseBundle.getString("driverTelephone"));

            JSONArray docArray = new JSONArray(LicenseBundle.getString("docArray"));

            for(int imagecount=0;imagecount<docArray.length();imagecount++)
            {
                final String doc_Details = ((JSONObject) (docArray.get(imagecount))).getString("doc_Details");
                final String doc_Name = ((JSONObject) (docArray.get(imagecount))).getString("doc_Name");

                final int vhlPictureSide = ((JSONObject) (docArray.get(imagecount))).getInt("vhlPictureSide");

                if (vhlPictureSide==2)
                {
                    String url1 = serverpath + doc_Details.substring(2);
                    url1 = url1.substring(0, url1.lastIndexOf("/") + 1) + doc_Name;
                    imageLoader.displayImage(url1, img_DLFronside);

                }
                if (vhlPictureSide==3)
                {
                    String url2 = serverpath + doc_Details.substring(2);
                    url2 = url2.substring(0, url2.lastIndexOf("/") + 1) + doc_Name;
                    imageLoader.displayImage(url2, img_DLBackside);

                }
            }

            txt_EditDrivingL.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    if (edt_DriverName.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Your Name",1);
                    else if (edt_LicenseNo.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter License No",1);
                    else if (ExpiryDate.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Expiry Date",1);
                    else if (txtIssueDate.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Issue Date",1);
                    else if (edt_Email.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Email",1);
                    else if (edt_Telephone.getText().toString().equals(""))
                        CustomToast.showToast(getActivity(),"Please Enter Your Telephone No.",1);
                    else {
                        JSONObject bodyParam = new JSONObject();
                        try
                        {
                            bodyParam.accumulate("CustomerId", Integer.parseInt(id));
                            bodyParam.accumulate("LCategory", LicenseBundle.getString("lCategory"));
                            bodyParam.accumulate("Licence_No", edt_LicenseNo.getText().toString());
                            bodyParam.accumulate("LIssue_Date", txtIssueDate.getText().toString());
                            bodyParam.accumulate("LExpiry_Date", ExpiryDate.getText().toString());
                            bodyParam.accumulate("LIssue_By", edt_IssueBy.getText().toString());
                            bodyParam.accumulate("DriverFullName", edt_DriverName.getText().toString());
                            bodyParam.accumulate("DriverRelationship", edt_RelationShip.getText().toString());
                            bodyParam.accumulate("DriverEmailId", edt_Email.getText().toString());
                            bodyParam.accumulate("DriverTelephone", edt_Telephone.getText().toString());

                            ImageList=new JSONArray();
                            if(frontImgObj.getString("fileBase64") != null)
                                ImageList.put(frontImgObj);
                            if(backImgObj.getString("fileBase64") != null)
                                ImageList.put(backImgObj);
                            bodyParam.accumulate("ImageList", ImageList);

                            System.out.println(bodyParam);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        ApiService ApiService = new ApiService(UpdateDrivingLicese, RequestType.POST,
                                UPDATEDRIVINGLICENSE, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);

                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data)
        {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = null;
            Uri targetUri = data.getData();
            try
            {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                imagestr = ConvertBitmapToString(resizedBitmap);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

            try {
                bitmap = getScaledBitmap(selectedImage,400,400);
                Bitmap temp = bitmap;

                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                temp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] image=stream.toByteArray();
                String img_str_base64 = Base64.encodeToString(image, Base64.NO_WRAP);

                if(imgId.equals("2"))
                {
                    frontImgObj.put("VhlPictureSide", 2);
                    frontImgObj.put("Doc_For", 6);
                    frontImgObj.put("fileBase64",img_str_base64);
                }
                else if (imgId.equals("3"))
                {
                    backImgObj.put("VhlPictureSide", 3);
                    backImgObj.put("Doc_For", 6);
                    backImgObj.put("fileBase64", img_str_base64);
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(imgId.equals("2"))
            {
                img_DLFronside.setImageBitmap(bitmap);
            }
            else if (imgId.equals("3"))
            {
                img_DLBackside.setImageBitmap(bitmap);
            }
            }
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

  //method to convert the selected image to base64 encoded string
    public static String ConvertBitmapToString(Bitmap bitmap)
    {
        String encodedImage = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            encodedImage= URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return encodedImage;
    }
    OnResponseListener UpdateDrivingLicese = new OnResponseListener()
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
                            String msg = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),msg,0);
                            NavHostFragment.findNavController(Fragment_Driving_License_Update.this)
                                    .navigate(R.id.action_DrivingLicense_Update_to_DrivingLicense_Details);
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
        public void onError(String error)
        {
            System.out.println("Error-" + error);
        }
    };

}