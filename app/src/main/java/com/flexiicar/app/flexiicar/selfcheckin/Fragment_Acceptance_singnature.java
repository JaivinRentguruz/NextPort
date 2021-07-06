package com.flexiicar.app.flexiicar.selfcheckin;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Fragment_Acceptance_singnature extends Fragment
{
    LinearLayout lblNext;
    ImageView imgback;
    SignaturePad signaturePad;
    TextView txtclear;
    CheckBox checkBoxTC;
    Bundle AgreementsBundle;
    JSONArray ImageList = new JSONArray();
    TextView txt_Savesign;
    Bitmap bitmap;
    String path;
    private static final String IMAGE_DIRECTORY ="/signdemo";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acceptance_signature, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        lblNext = view.findViewById(R.id.lblback_acceptance_sign);
        imgback = view.findViewById(R.id.backimg_acceptance_sign);
        signaturePad = view.findViewById(R.id.signaturePad);
        txtclear = view.findViewById(R.id.txt_clearsign);
        checkBoxTC = view.findViewById(R.id.CheckBoxTC);
        txt_Savesign = view.findViewById(R.id.txt_Savesign);

        txtclear.setEnabled(false);

        try {
            ImageList = new JSONArray(getArguments().getString("ImageList"));
            AgreementsBundle = getArguments().getBundle("AgreementsBundle");

            txt_Savesign.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    bitmap = signaturePad.getSignatureBitmap();
                    path = saveImage(bitmap);
                    try {
                        JSONObject signObj = new JSONObject();
                        signObj.put("Doc_For", 22);
                        signObj.put("fileBase64", path);
                        System.out.println(signObj);
                        ImageList.put(signObj);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    System.out.println(path);
                }
            });
            lblNext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try{
                        if(checkBoxTC.isChecked())
                        {
                            Bundle SelfCheckInBundle = new Bundle();
                            SelfCheckInBundle.putString("ImageList", ImageList.toString());
                            SelfCheckInBundle.putBundle("AgreementsBundle", AgreementsBundle);
                            System.out.println(SelfCheckInBundle);
                            NavHostFragment.findNavController(Fragment_Acceptance_singnature.this)
                                    .navigate(R.id.action_Signature_to_Self_check_In, SelfCheckInBundle);
                        }
                        else {
                            String msg = "Please accept term & condition";
                            CustomToast.showToast(getActivity(),msg,1);
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
                    Bundle SelfCheckInBundle=new Bundle();
                    SelfCheckInBundle.putString("ImageList",ImageList.toString());
                    SelfCheckInBundle.putBundle("AgreementsBundle",AgreementsBundle);
                    System.out.println(SelfCheckInBundle);
                    NavHostFragment.findNavController(Fragment_Acceptance_singnature.this)
                            .navigate(R.id.action_Signature_to_Vehicle_All_image_SelfCheckIn,SelfCheckInBundle);
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
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String saveImage(Bitmap myBitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists())
        {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();

            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            CustomToast.showToast(getActivity(),"File Saved::--->"+f.getAbsolutePath(),1);

            Uri SignImage = Uri.fromFile(f);

            return f.getAbsolutePath();
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return "";
    }
}
