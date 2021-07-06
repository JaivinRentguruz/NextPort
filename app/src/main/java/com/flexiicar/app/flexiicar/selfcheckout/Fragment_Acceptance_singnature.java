package com.flexiicar.app.flexiicar.selfcheckout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.flexiicar.app.R;
import com.flexiicar.app.adapters.CustomToast;
import com.flexiicar.app.apicall.ApiService;
import com.flexiicar.app.apicall.OnResponseListener;
import com.flexiicar.app.apicall.RequestType;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.HashMap;

import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_CHECKOUT;
import static com.flexiicar.app.apicall.ApiEndPoint.UPDATESELFCHECKOUT;

public class Fragment_Acceptance_singnature extends Fragment
{
    LinearLayout lblNext;
    ImageView imgback;
    SignaturePad signaturePad;
    TextView txtclear;
    CheckBox CheckBoxTC;
    Handler handler = new Handler();
    JSONArray ImageList = new JSONArray();
    JSONArray finaleImageList = new JSONArray();

    Bundle AgreementsBundle;
    private static final String IMAGE_DIRECTORY = "/signdemo";
    TextView txt_Savesign;
    public int RESULT_LOAD_IMAGE = 1;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acceptance_signature, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            lblNext = view.findViewById(R.id.lblback_acceptance_sign);
            imgback = view.findViewById(R.id.backimg_acceptance_sign);
            signaturePad = view.findViewById(R.id.signaturePad);
            txtclear = view.findViewById(R.id.txt_clearsign);
            CheckBoxTC = view.findViewById(R.id.CheckBoxTC);
            txt_Savesign = view.findViewById(R.id.txt_Savesign);
            txtclear.setEnabled(false);
            //  signaturePad.setEnabled(false);
            AgreementsBundle = getArguments().getBundle("AgreementsBundle");
            ImageList = new JSONArray(AgreementsBundle.getString("ImageList"));

            lblNext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        for (int i = 0; i < ImageList.length()-1; i++)
                        {
                            try {
                                System.out.println(i);
                                JSONObject imgObj = ImageList.getJSONObject(i);
                                String imgPath = imgObj.getString("fileBase64");
                                try{
                                    File imgFile = new File(imgPath);
                                    if(!imgFile.exists())
                                    {
                                        Uri selectedImage = Uri.fromFile(new File(imgFile.getAbsolutePath()));
                                        System.out.println(selectedImage);

                                        Bitmap bitmap = getScaledBitmap(selectedImage, 400, 400);
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                        byte[] image = stream.toByteArray();
                                        String img_str_base64 = Base64.encodeToString(image, Base64.NO_WRAP);
                                        imgObj.put("fileBase64", img_str_base64);
                                    }
                                    //imgObj.put("Doc_For", 9);
                                    //ImageList.put(imgObj);
                                } catch (FileNotFoundException e)
                                {
                                    e.printStackTrace();
                                } catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        String AudioFile = AgreementsBundle.getString("AudioFileBase64");
                        if (AudioFile != null)
                        {
                            try {
                                    /*File file = new File(Environment.getExternalStorageDirectory() + AudioFile);
                                    byte[] bytes = new byte[(int) file.length()];
                                    //bytes = FileUtils.readFileToByteArray(file);*/
                                File file = new File(AudioFile);
                                FileInputStream fileInputStreamReader = new FileInputStream(file);
                                byte[] bytes = new byte[(int) file.length()];
                                fileInputStreamReader.read(bytes);
                                String audio_str_base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);

                                JSONObject AudioFileObj = new JSONObject();
                                AudioFileObj.put("Doc_For", 21);
                                AudioFileObj.put("FileExtention", ".mp3");
                                AudioFileObj.put("fileBase64", audio_str_base64);
                                System.out.println(AudioFile);
                                System.out.println(AudioFileObj);
                                ImageList.put(AudioFileObj);

                                JSONObject bodyParam = new JSONObject();
                                try {
                                    bodyParam.accumulate("AgreementId", AgreementsBundle.getInt("agreement_ID"));
                                    bodyParam.accumulate("VehicleId", AgreementsBundle.getInt("vehicle_ID"));
                                    bodyParam.accumulate("checkListOptionIDS", AgreementsBundle.getString("checkListOptionIDS"));
                                    bodyParam.accumulate("Notes", AgreementsBundle.getString("Notes"));
                                    bodyParam.accumulate("ImageList", ImageList);
                                    System.out.println(bodyParam);
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                ApiService ApiService = new ApiService(Updatecheckout, RequestType.POST,
                                        UPDATESELFCHECKOUT, BASE_URL_CHECKOUT, new HashMap<String, String>(), bodyParam);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            imgback.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {
                        ImageList = new JSONArray(AgreementsBundle.getString("ImageList"));
                        AgreementsBundle.putString("ImageList", ImageList.toString());
                        Bundle Agreements = new Bundle();
                        Agreements.putBundle("AgreementsBundle", AgreementsBundle);
                        System.out.println(Agreements);
                        NavHostFragment.findNavController(Fragment_Acceptance_singnature.this)
                                .navigate(R.id.action_Acceptance_signature_to_Vehicle_Additional_image, Agreements);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener()
            {
                @Override
                public void onStartSigning()
                {
                }
                @Override
                public void onSigned()
                {
                    txtclear.setEnabled(true);
                    signaturePad.setEnabled(true);
                }
                @Override
                public void onClear()
                {
                    txtclear.setEnabled(false);
                    //signaturePad.setEnabled(false);
                }
            });
            txtclear.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    signaturePad.clear();
                }
            });
            txt_Savesign.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    signatureBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String img_strbase64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    try {
                        JSONObject signObj = new JSONObject();
                        signObj.put("Doc_For", 22);
                        signObj.put("fileBase64",img_strbase64);
                        System.out.println(signObj);
                        ImageList.put(signObj);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(),"Signature Saved",Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //Updatecheckout
    OnResponseListener Updatecheckout = new OnResponseListener()
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
                            if(CheckBoxTC.isChecked())
                            {
                                String msg = responseJSON.getString("message");
                                CustomToast.showToast(getActivity(), msg, 0);

                                AgreementsBundle.putString("ImageList", ImageList.toString());
                                Bundle Agreements = new Bundle();
                                Agreements.putBundle("AgreementsBundle", AgreementsBundle);
                                System.out.println(Agreements);
                                NavHostFragment.findNavController(Fragment_Acceptance_singnature.this)
                                        .navigate(R.id.action_Acceptance_signature_to_Waiver_Signature, Agreements);
                            }
                            else
                            {
                                String msg = "Please accept term & condition";
                                CustomToast.showToast(getActivity(),msg,1);
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
        public void onError(String error)
        {
            System.out.println("Error-" + error);
        }
    };
    private Bitmap getScaledBitmap(Uri selectedImage, int width, int height) throws FileNotFoundException
    {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(selectedImage), null, sizeOptions);
        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(selectedImage), null, sizeOptions);
    }
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {
            // Calculate ratios of height and width to requested one
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // Choose the smallest ratio as inSampleSize value
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
