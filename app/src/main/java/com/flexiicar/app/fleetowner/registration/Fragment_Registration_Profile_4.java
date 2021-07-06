package com.flexiicar.app.fleetowner.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.AndroidNetworking;
import com.flexiicar.app.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Fragment_Registration_Profile_4 extends Fragment
{
    LinearLayout lblnext;
    ImageView backArrow;
    Handler handler = new Handler();
    public static Context context;
    public String id = "";
    Bundle RegistrationBundle;
    EditText edt_cardNo,edt_ExDate,edt_CvvNo,edt_cardholderName,edt_streetName,edtxtUnitNumber,edtPincodeNo,Edtext_City;
    Spinner SP_Country,SP_State;
    public String[] Country,State;
    public int[] CountryId,StateId;
    HashMap<String, Integer> countryhashmap=new HashMap<String, Integer>();
    HashMap<String,Integer>Statehashmap=new HashMap<>();

    private Geocoder geocoder;
    private final int REQUEST_PLACE_ADDRESS = 40;
    String country, state;
    JSONArray ImageList = new JSONArray();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_profile_4, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        lblnext = view.findViewById(R.id.lblnextscreen);
        edt_cardNo = view.findViewById(R.id.edt_cardNo);
        edt_ExDate = view.findViewById(R.id.edt_ExDate);
        edt_CvvNo = view.findViewById(R.id.edt_CvvNo);
        edt_cardholderName = view.findViewById(R.id.edt_cardholderName);

        edt_streetName = view.findViewById(R.id.edt_streetNameCC);
        edtxtUnitNumber = view.findViewById(R.id.edtxtUnitNumberCC);
        edtPincodeNo = view.findViewById(R.id.edtPincodeNoCC);
        Edtext_City = view.findViewById(R.id.Edtext_CityCC);

        SP_Country=view.findViewById(R.id.spi_CountryList);
        SP_State=view.findViewById(R.id.Spi_StatelistCC);

        try {
           // RegistrationBundle = getArguments().getBundle("RegistrationBundle");
          //  ImageList = new JSONArray(RegistrationBundle.getString("ImageList"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

       /* edt_streetName.setText(getArguments().getBundle("RegistrationBundle").getString("Cust_Street"));
        edtxtUnitNumber.setText(getArguments().getBundle("RegistrationBundle").getString("Cust_UnitNo"));
        edtPincodeNo.setText(getArguments().getBundle("RegistrationBundle").getString("Cust_ZipCode"));
        Edtext_City.setText(getArguments().getBundle("RegistrationBundle").getString("Cust_City"));
        edt_cardholderName.setText(getArguments().getBundle("RegistrationBundle").getString("Cust_FName"));

        country = getArguments().getBundle("RegistrationBundle").getString("Cust_Country_Name");
        state = getArguments().getBundle("RegistrationBundle").getString("Cust_State_Name");*/

        backArrow = view.findViewById(R.id.backarrow_creditcard);
        AndroidNetworking.initialize(getActivity());
        Fragment_Registration_Profile_4.context = getActivity();

        backArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle Registration = new Bundle();
                Registration.putBundle("RegistrationBundle", RegistrationBundle);
                System.out.println(Registration);
                NavHostFragment.findNavController(Fragment_Registration_Profile_4.this)
                        .navigate(R.id.action_DriverProfile4_to_DriverProfile3);
            }
        });

        ImageView imgScanDrivingLicense = view.findViewById(R.id.ScancreditCard);

        imgScanDrivingLicense.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                //Intent i = new Intent(getActivity(), ScanDrivingLicense.class);
                //i.putExtra("afterScanBackTo", 4);
                //startActivity(i);

            }
        });
        edt_streetName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    if(!Places.isInitialized())
                    {
                       // Places.initialize(getActivity(), getString(R.string.map_api_key), Locale.US);
                    }
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)).build(context);
                    startActivityForResult(intent, REQUEST_PLACE_ADDRESS);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        lblnext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    NavHostFragment.findNavController(Fragment_Registration_Profile_4.this)
                            .navigate(R.id.action_DriverProfile4_to_Complete_Register);

                   /* for (int i = 0; i < ImageList.length(); i++)
                    {
                        try {

                            System.out.println(i);

                            JSONObject imgObj = ImageList.getJSONObject(i);

                            String imgPath = imgObj.getString("fileBase64");

                            File imgFile = new File(imgPath);
                            Uri selectedImage = Uri.fromFile(imgFile);

                            Bitmap temp = getBitmapFromUri(selectedImage);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            temp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] image = stream.toByteArray();
                            String img_str_base64 = Base64.encodeToString(image, Base64.DEFAULT);

                            imgObj.put("fileBase64", img_str_base64);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        if (edt_cardNo.getText().toString().equals(""))
                            Toast.makeText(getActivity(), "Please Enter Your Credit Card No", Toast.LENGTH_LONG).show();
                        else if (edt_ExDate.getText().toString().equals(""))
                            Toast.makeText(getActivity(), "Please Enter Your Credit Card Expiry Date", Toast.LENGTH_LONG).show();
                        else if (edt_CvvNo.getText().toString().equals(""))
                            Toast.makeText(getActivity(), "Please Enter Your CVV", Toast.LENGTH_LONG).show();
                        else if (edt_cardholderName.getText().toString().equals(""))
                            Toast.makeText(getActivity(), "Please Enter Credit Card Holder Name", Toast.LENGTH_LONG).show();
                        else if (edt_streetName.getText().toString().equals(""))
                            Toast.makeText(getActivity(), "Please Enter Your Street NO & Name", Toast.LENGTH_LONG).show();
                        else if (SP_Country.getSelectedItem().toString().equals(""))
                            Toast.makeText(getActivity(), "Please Select Your Country", Toast.LENGTH_LONG).show();
                        else if (SP_State.getSelectedItem().toString().equals(""))
                            Toast.makeText(getActivity(), "Please Select Your State", Toast.LENGTH_LONG).show();
                        else if (Edtext_City.getText().toString().equals(""))
                            Toast.makeText(getActivity(), "Please Select Your City", Toast.LENGTH_LONG).show();
                        else if (edtPincodeNo.getText().toString().equals(""))
                            Toast.makeText(getActivity(), "Please Enter Zip Code", Toast.LENGTH_LONG).show();
                        else {

                            NavHostFragment.findNavController(Fragment_Driver_Profile_4.this)
                                    .navigate(R.id.action_DriverProfile4_to_Complete_Register);
                        }
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException
    {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PLACE_ADDRESS && resultCode == Activity.RESULT_OK)
        {
            Place place = Autocomplete.getPlaceFromIntent(data);
            // Log.i(TAG, "Place city and postal code: " + place.getAddress().subSequence(place.getName().length(),place.getAddress().length()));
            try {
                List<Address> addresses;
                geocoder = new Geocoder(context, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String Street=addresses.get(0).getFeatureName();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();

                    Log.e("Address: ", "" + address);
                    Log.e("City: ", "" + city);
                    Log.e("Street: ", "" + Street);
                    Log.e("State: ", "" + state);
                    Log.e("Country: ", "" + country);
                    Log.e("APostalCode: ", "" + postalCode);

                    edt_streetName.setText(Street);
                    Edtext_City.setText(city);
                    edtPincodeNo.setText(postalCode);

                    for(int i=0;i<State.length;i++)
                    {

                        if(State[i].equals(state))
                        {
                            SP_State.setSelection(i);
                            break;
                        }
                    }

                    for(int j=0;j<Country.length;j++)
                    {

                        if(Country[j].equals(country))
                        {
                            SP_Country.setSelection(j);
                            break;
                        }
                    }

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                //setMarker(latLng);
            }
        }
    }
}