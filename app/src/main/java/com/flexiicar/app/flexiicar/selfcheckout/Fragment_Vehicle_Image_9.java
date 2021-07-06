package com.flexiicar.app.flexiicar.selfcheckout;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.flexiicar.app.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Fragment_Vehicle_Image_9 extends Fragment
{
    LinearLayout lblNext;
    ImageView imgback,UploadImg;
    private static final int RESULT_LOAD_IMAGE = 1;
    public String id="";
    JSONArray ImageList = new JSONArray();
    Bundle AgreementsBundle;
    TextView DateTime,txtDiscard;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {// Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_images_9, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        DateTime=view.findViewById(R.id.txt_DateTimeVehImg9);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
        String datetime = dateformat.format(c.getTime());
        DateTime.setText(datetime);

        lblNext=view.findViewById(R.id.lblnext9);
        UploadImg=view.findViewById(R.id.Image_Upload9);
        imgback=view.findViewById(R.id.backimg9);
        UploadImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });
        AgreementsBundle = getArguments().getBundle("AgreementsBundle");
        try {
            ImageList = new JSONArray(AgreementsBundle.getString("ImageList"));

            lblNext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AgreementsBundle.putString("ImageList",ImageList.toString());
                    Bundle Agreements=new Bundle();
                    Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                    System.out.println(Agreements);
                    NavHostFragment.findNavController(Fragment_Vehicle_Image_9.this)
                            .navigate(R.id.action_Vehicle_Image_9_to_Vehicle_Image_10,Agreements);
                }
            });
            imgback.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AgreementsBundle.putString("ImageList",ImageList.toString());
                    Bundle Agreements=new Bundle();
                    Agreements.putBundle("AgreementsBundle",AgreementsBundle);
                    System.out.println(Agreements);
                    NavHostFragment.findNavController(Fragment_Vehicle_Image_9.this)
                            .navigate(R.id.action_VVehicle_Image_9_to_Vehicle_Image_8,Agreements);
                }
            });
            txtDiscard=view.findViewById(R.id.DiscardVeh9);

            txtDiscard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Vehicle_Image_9.this)
                            .navigate(R.id.action_Vehicle_Image_9_to_Agreements);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data)
        {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try
            {
                bitmap = getScaledBitmap(selectedImage,400,400);
                JSONObject docObj = new JSONObject();
                docObj.put("Doc_For",9);
                docObj.put("VhlPictureSide", 9);
                docObj.put("fileBase64",getImagePathFromUri(selectedImage) );
                ImageList.put(docObj);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            UploadImg.setImageBitmap(bitmap);
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
