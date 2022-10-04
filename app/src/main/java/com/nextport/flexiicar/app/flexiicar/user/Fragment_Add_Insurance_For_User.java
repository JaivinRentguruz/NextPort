package com.nextport.flexiicar.app.flexiicar.user;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nextport.flexiicar.app.model.base.UserData;
import com.nextport.flexiicar.app.model.DoHeader;
import com.nextport.flexiicar.app.model.InsuranceModel;
import com.nextport.flexiicar.app.R;
import com.nextport.flexiicar.app.adapters.CustomToast;
import com.nextport.flexiicar.app.apicall.ApiService;
import com.nextport.flexiicar.app.apicall.ApiService2;
import com.nextport.flexiicar.app.apicall.OnResponseListener;
import com.nextport.flexiicar.app.apicall.RequestType;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.nextport.flexiicar.app.apicall.ApiEndPoint.ADDINSURANCEDETAIL;
import static com.nextport.flexiicar.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.nextport.flexiicar.app.apicall.ApiEndPoint.INSURANCECOMPANYLIST;

public class Fragment_Add_Insurance_For_User extends Fragment {

    String TAG = "Fragment_Add_Insurance_For_User";
    Handler handler = new Handler();
    EditText InsPolicyNo;
    TextView txtInsurancePolicy,ExpiryDate,IssueDate;
    ImageView Img_UploadPolicy,BackArrow;
    ImageLoader imageLoader;
    DatePickerDialog datePickerDialog;
    Spinner SP_InsuranceCName;
    int selected = 0;
    public String[] InsuranceCompanyName, InsuranceEmail, InsuranceContactPerson;
    public int[] InsuranceId;
    JSONObject ImgObj = new JSONObject();
    String imagestr;
    private DoHeader header;
    private InsuranceModel insuranceModel;
    private static int RESULT_LOAD_IMAGE = 1;
    int key = 0;
    HashMap<String, Integer> getInsurance = new HashMap<>();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        header = new DoHeader();
        header.ut = "AGZmAhOz8Do=";
        header.exptime = "7/24/2021 11:47:18 PM";
        header.islogin = "1";

        insuranceModel = new InsuranceModel();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_insurance_for_user, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initImageLoader(getActivity());
        imageLoader = ImageLoader.getInstance();


        InsPolicyNo=view.findViewById(R.id.edt_PolicyNo);
        ExpiryDate=view.findViewById(R.id.edt_ExpiryDate);
        IssueDate=view.findViewById(R.id.IpIssue_Date);
        SP_InsuranceCName=view.findViewById(R.id.sp_InsuranceCompList);
        txtInsurancePolicy=view.findViewById(R.id.edit_InsurancePolicy);
        Img_UploadPolicy=view.findViewById(R.id.Img_UploadPolicy);
        BackArrow=view.findViewById(R.id.backimg_IP);


        Img_UploadPolicy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });
        BackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(Fragment_Add_Insurance_For_User.this)
                        .navigate(R.id.action_AddInsurancePolicy_to_InsurancePolicyList);
            }
        });

        String bodyParam = "";
        JSONObject object = new JSONObject();
        List<Integer> ints = new ArrayList<>();
        JSONObject filter = new JSONObject();

        try
        {
            bodyParam+="customerId=";
            object.accumulate("limit", 10);
            object.accumulate("orderDir", "desc");
            object.accumulate("pageSize", 10);
            ints.add(10);
            ints.add(20);
            ints.add(30);
            ints.add(40);
            ints.add(50);
            object.accumulate("pageLimits", ints);

            filter.accumulate("CompanyId", 1);
            filter.accumulate("IsActive", true);

            object.accumulate("filterObj", filter);
            System.out.println(bodyParam);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        ApiService ApiService = new ApiService(InsuranceList, RequestType.POST,
                INSURANCECOMPANYLIST, BASE_URL_LOGIN, header, object);

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
        IssueDate.setOnClickListener(new View.OnClickListener()
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
                                    IssueDate.setText(year + "-" + month + "-" + day);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        txtInsurancePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insuranceModel.InsuranceType = 3;
               // insuranceModel.InsuranceFor = UserData.customerProfile.UserModel.UserFor;
               // insuranceModel.Id = UserData.customerProfile.UserModel.UserFor;
                insuranceModel.VerifiedBy = UserData.loginResponse.User.Id;
                insuranceModel.PolicyNo = InsPolicyNo.getText().toString();
                insuranceModel.ExpiryDate = ExpiryDate.getText().toString();
                insuranceModel.IssueDate = IssueDate.getText().toString();
                insuranceModel.InsuranceFor = UserData.loginResponse.User.UserFor;
                insuranceModel.GetCompanyDetail = true;
                insuranceModel.InsuranceCompanyId = getInsurance.get(SP_InsuranceCName.getSelectedItem());

                Log.d(TAG, "onClick: " + insuranceModel);

               /* insuranceModel.InsuranceCompanyDetailsModel.Name = SP_InsuranceCName.getSelectedItem().toString();
                insuranceModel.InsuranceCompanyDetailsModel.CompanyId = 1;

                key = Integer.valueOf(String.valueOf(SP_InsuranceCName.getSelectedItemId()));
                insuranceModel.InsuranceCompanyId = InsuranceId[key];
                insuranceModel.InsuranceCompanyDetailsModel.ContactName = InsuranceContactPerson[key];
                insuranceModel.InsuranceCompanyDetailsModel.Email = InsuranceEmail[key];*/

                ApiService2<InsuranceModel> apiService2 = new ApiService2<InsuranceModel>(AddInsuranceDetail,RequestType.POST, ADDINSURANCEDETAIL,BASE_URL_LOGIN, header, insuranceModel);
            }
        });

    }

    OnResponseListener InsuranceList = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");
                        if (status)
                        {
                           // JSONObject resultSet = responseJSON.getJSONObject("Data");
                            final JSONArray resultSet = responseJSON.getJSONArray("Data");
                            int len;
                            len = resultSet.length();

                            InsuranceId = new int[len];
                            InsuranceCompanyName = new String[len];
                            InsuranceContactPerson = new String[len];
                            InsuranceEmail = new String[len];
                            for (int i = 0; i <len ; i++) {
                                final JSONObject test = (JSONObject) resultSet.get(i);
                                InsuranceCompanyName[i] = test.getString("Name");
                                InsuranceId[i] = test.getInt("Id");
                                InsuranceContactPerson[i] = test.getString("ContactName");
                                InsuranceEmail[i] = test.getString("Email");
                                getInsurance.put(InsuranceCompanyName[i], InsuranceId[i]);

                            }
                            ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getContext().getApplicationContext(), R.layout.spinner_layout, R.id.text1, InsuranceCompanyName);
                            SP_InsuranceCName.setAdapter(adapterCategories);
                            SP_InsuranceCName.setSelection(selected);
                        }

                    } catch (Exception e)
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

    OnResponseListener AddInsuranceDetail = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");
                        if (status)
                        {
                            NavHostFragment.findNavController(Fragment_Add_Insurance_For_User.this)
                                    .navigate(R.id.action_AddInsurancePolicy_to_InsurancePolicyList);

                        } else {
                            CustomToast.showToast(getActivity(), responseJSON.getString("Message"),1);
                        }


                    } catch (Exception e)
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
            try
            {
                bitmap = getScaledBitmap(selectedImage,400,250);
                Bitmap temp = bitmap;

                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                temp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] image=stream.toByteArray();
                String img_str_base64 = Base64.encodeToString(image, Base64.NO_WRAP);

                ImgObj.put("fileBase64", img_str_base64);
                ImgObj.put("Doc_For", 8);
                Img_UploadPolicy.setImageBitmap(bitmap);

            } catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
}