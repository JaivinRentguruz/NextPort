package com.flexiicar.app.flexiicar.booking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.AndroidNetworking;
import com.flexiicar.app.R;
import com.flexiicar.app.ScanDrivingLicense;
import com.flexiicar.app.adapters.CustomToast;
import com.flexiicar.app.apicall.ApiService;
import com.flexiicar.app.apicall.OnResponseListener;
import com.flexiicar.app.apicall.RequestType;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.flexiicar.app.apicall.ApiEndPoint.ADDCREDITCARD;
import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_SETTINGS;
import static com.flexiicar.app.apicall.ApiEndPoint.GETCOUNTRYLIST;
import static com.flexiicar.app.apicall.ApiEndPoint.STATELIST;

public class Fragment_Add_credit_card extends Fragment
{
    ImageView imgback,ScanCreditcard;
    EditText edt_Card_No, edt_ExpiryDate, edt_CVV, edt_NameofCard,edtStreetNo,edtUnitNo,edtCityName,edt_Zipcode;
    Spinner Sp_CountryList,SP_StateList;
    TextView txt_CardNumber, txt_CardholderName, txt_cardExpiryDate;
    LinearLayout AddCreditCardLayout;
    Handler handler = new Handler();
    public static Context context;
    public String id = "",transactionId;
    public String[] Country,State;
    public int[] CountyId,StateId;
    HashMap<String, Integer> countryhashmap=new HashMap<String, Integer>();
    HashMap<String,Integer>Statehashmap=new HashMap<>();
    TextView Discard;
    Bundle BookingBundle,VehicleBundle;
    Bundle returnLocationBundle, locationBundle;
    Boolean locationType, initialSelect;
    private Geocoder geocoder;
    private final int REQUEST_PLACE_ADDRESS = 40;
    CheckBox chk_DeleteCard, chk_DefaultCard;

    int cust_State_ID,cust_Country_ID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_creditcard, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        String bodyParam = "";
        try {
            super.onViewCreated(view, savedInstanceState);

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            edt_Card_No = view.findViewById(R.id.edt_Card_NoAdd);
            edt_ExpiryDate = view.findViewById(R.id.edt_ExpiryDateAdd);
            edt_CVV = view.findViewById(R.id.edt_CVV_Add);
            edt_NameofCard = view.findViewById(R.id.edt_NameofCardAdd);
            edtStreetNo = view.findViewById(R.id.edt_streetNoName);
            edtUnitNo = view.findViewById(R.id.edtxtUnitNumber);
            edtCityName = view.findViewById(R.id.Edtext_City);
            edt_Zipcode = view.findViewById(R.id.edtPincodeNo);
            Discard=view.findViewById(R.id.txtDiscardAddCC);
            ScanCreditcard=view.findViewById(R.id.scanCreditcard);

            Sp_CountryList = view.findViewById(R.id.spinner_CountryList);
            SP_StateList = view.findViewById(R.id.Spinner_Statelist);

            chk_DeleteCard = view.findViewById(R.id.DeleteCard);
            chk_DefaultCard = view.findViewById(R.id.DefaultCard);

            txt_CardNumber = view.findViewById(R.id.txt_CardNumberAdd);
            txt_CardholderName = view.findViewById(R.id.txt_CardholderNameAdd);
            txt_cardExpiryDate = view.findViewById(R.id.txt_cardExpiryDateAdd);
            imgback = view.findViewById(R.id.back_addcreditcard);

            AndroidNetworking.initialize(getActivity());
            Fragment_Add_credit_card.context = getActivity();

            BookingBundle = getArguments().getBundle("BookingBundle");
            VehicleBundle = getArguments().getBundle("VehicleBundle");
            returnLocationBundle = getArguments().getBundle("returnLocation");
            locationBundle = getArguments().getBundle("location");
            locationType = getArguments().getBoolean("locationType");
            initialSelect = getArguments().getBoolean("initialSelect");
            transactionId = getArguments().getString("transactionId");

            ScanCreditcard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent i = new Intent(getActivity(), ScanDrivingLicense.class);
                    i.putExtra("afterScanBackTo", 3);
                    startActivity(i);
                }
            });
            Discard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("transactionId",transactionId);
                   // bundle.putDouble("total",total);
                    bundle.putBundle("VehicleBundle",VehicleBundle);
                    bundle.putBundle("BookingBundle",BookingBundle);
                    bundle.putBundle("location", locationBundle);
                    bundle.putBoolean("locationType", locationType);
                    bundle.putBoolean("initialSelect", initialSelect);
                    bundle.putBundle("returnLocation", returnLocationBundle);
                    bundle.putString("transactionId", transactionId);
                    NavHostFragment.findNavController(Fragment_Add_credit_card.this)
                            .navigate(R.id.action_AddCreditCardDetails_to_CardsOnAccount,bundle);
                }
            });

            imgback.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("transactionId",transactionId);
                  //  bundle.putDouble("total",total);
                    bundle.putBundle("VehicleBundle",VehicleBundle);
                    bundle.putBundle("BookingBundle",BookingBundle);
                    bundle.putBundle("location", locationBundle);
                    bundle.putBoolean("locationType", locationType);
                    bundle.putBoolean("initialSelect", initialSelect);
                    bundle.putBundle("returnLocation", returnLocationBundle);
                    bundle.putString("transactionId", transactionId);
                    NavHostFragment.findNavController(Fragment_Add_credit_card.this)
                            .navigate(R.id.action_AddCreditCardDetails_to_CardsOnAccount,bundle);
                }
            });
            edtStreetNo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try
                    {
                        if(!Places.isInitialized())
                        {
                            Places.initialize(getActivity(), getString(R.string.map_api_key), Locale.US);
                        }
                        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, Arrays.asList( Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)).build(context);
                        startActivityForResult(intent, REQUEST_PLACE_ADDRESS);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            edt_Card_No.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {
                    txt_CardNumber.setText(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            edt_NameofCard.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {
                    txt_CardholderName.setText(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable)
                {

                }
            });

            edt_ExpiryDate.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {
                    txt_cardExpiryDate.setText(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable)
                {

                }
            });

            AddCreditCardLayout = view.findViewById(R.id.lblSaveCardAdd);

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            id = sp.getString(getString(R.string.id), "");
            cust_State_ID = sp.getInt("cmp_State",0);
            cust_Country_ID = sp.getInt("cmp_Country",0);
            System.out.println(cust_State_ID);
            String cust_FName = sp.getString("cust_FName", "");
            String cust_LName = sp.getString("cust_LName", "");
            String cust_Street = sp.getString("cust_Street", "");
            String cust_UnitNo = sp.getString("cust_UnitNo", "");
            String cust_ZipCode = sp.getString("cust_ZipCode", "");
            String cmp_City = sp.getString("cmp_City", "");

            String CardOnName=cust_FName+" "+cust_LName;

            edt_NameofCard.setText(CardOnName);
            edtStreetNo.setText(cust_Street);
            edtUnitNo.setText(cust_UnitNo);
            edt_Zipcode.setText(cust_ZipCode);

            System.out.println(id);

            AddCreditCardLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try {
                        if (edt_Card_No.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter CardNumber",1);
                        else if (edt_ExpiryDate.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter ExpiryDate",1);
                        else if (edt_CVV.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter CVV",1);
                        else if (edt_NameofCard.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Card Holder Name",1);
                        else if (edtStreetNo.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Your Street NO & Name",1);
                        else if (Sp_CountryList.getSelectedItem().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Select Your Country",1);
                        else if (SP_StateList.getSelectedItem().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Select Your State",1);
                        else if (edtCityName.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Select Your City",1);
                        else if (edt_Zipcode.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(),"Please Enter Zip Code",1);
                        else {
                            JSONObject bodyParam = new JSONObject();
                            try {
                                    bodyParam.accumulate("Customer_ID", Integer.parseInt(id));
                                    bodyParam.accumulate("Card_ID", 0);
                                    bodyParam.accumulate("Card_PStreet", edtStreetNo.getText().toString());
                                    bodyParam.accumulate("Card_PCity", edtCityName.getText().toString());
                                    bodyParam.accumulate("Card_PUnitNo", edtUnitNo.getText().toString());

                                    int s = countryhashmap.get(Sp_CountryList.getSelectedItem());
                                    int s1 = Statehashmap.get(SP_StateList.getSelectedItem());
                                    bodyParam.accumulate("Card_PCountry", s);
                                    bodyParam.accumulate("Card_PState", s1);

                                    bodyParam.accumulate("Card_No", edt_Card_No.getText().toString());
                                    bodyParam.accumulate("Expiry_Date", edt_ExpiryDate.getText().toString());
                                    bodyParam.accumulate("CVS_Code", edt_CVV.getText().toString());
                                    bodyParam.accumulate("Card_Name", edt_NameofCard.getText().toString());
                                    bodyParam.accumulate("ZipCode", edt_Zipcode.getText().toString());

                                    if (chk_DefaultCard.isChecked())
                                        bodyParam.accumulate("IsDefault", 1);
                                    else
                                        bodyParam.accumulate("IsDefault", 0);
                                    System.out.println(bodyParam);

                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                            ApiService ApiService1 = new ApiService(AddCreditCard, RequestType.POST,
                                    ADDCREDITCARD, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            Sp_CountryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                {
                    try {
                        int s = countryhashmap.get(Sp_CountryList.getSelectedItem());
                        System.out.println(Sp_CountryList.getSelectedItem());
                        try {
                            String bodyParam1 = "countryId=" + s;
                            ApiService ApiService = new ApiService(StateList, RequestType.GET,
                                    STATELIST, BASE_URL_SETTINGS, new HashMap<String, String>(), bodyParam1);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView)
                {

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            ApiService ApiService = new ApiService(CountryList, RequestType.GET,
                    GETCOUNTRYLIST, BASE_URL_SETTINGS, new HashMap<String, String>(), bodyParam);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    OnResponseListener AddCreditCard = new OnResponseListener()
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
                            Bundle bundle = new Bundle();
                            bundle.putString("transactionId",transactionId);
                            bundle.putBundle("VehicleBundle",VehicleBundle);
                            bundle.putBundle("BookingBundle",BookingBundle);
                            bundle.putBundle("location", locationBundle);
                            bundle.putBundle("returnLocation", returnLocationBundle);
                            bundle.putBoolean("locationType", locationType);
                            bundle.putBoolean("initialSelect", initialSelect);
                            bundle.putString("transactionId", transactionId);
                            NavHostFragment.findNavController(Fragment_Add_credit_card.this)
                                    .navigate(R.id.action_AddCreditCardDetails_to_CardsOnAccount,bundle);
                        }
                        else
                        {
                            String msg = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),msg,1);
                        }

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        @Override
        public void onError(String error)
        {
            System.out.println("Error" + error);
        }
    };

//Countrylist
    OnResponseListener CountryList = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {
                            JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                            final JSONArray Countrylist = resultSet.getJSONArray("t0040_Country_Master");

                            int len;
                            len = Countrylist.length();

                            Country = new String[len];
                            CountyId = new int[len];
                            int position=0;

                            for (int j = 0; j < len; j++)
                            {
                                final JSONObject test = (JSONObject) Countrylist.get(j);
                                final int country_ID = test.getInt("country_ID");
                                final String country_Name = test.getString("country_Name");

                                Country[j] = country_Name;
                                CountyId[j] = country_ID;

                                countryhashmap.put(country_Name,country_ID);

                                if(cust_Country_ID==(country_ID))
                                {
                                    position = j;
                                }
                            }

                            ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getContext().getApplicationContext(), R.layout.spinner_layout, R.id.text1, Country);
                            Sp_CountryList.setAdapter(adapterCategories);
                            Sp_CountryList.setSelection(position);

                        }
                        else
                        {
                            String errorString = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),errorString,1);
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
    //StateList
    OnResponseListener StateList = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {

                            JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                            final JSONArray StateList = resultSet.getJSONArray("t0040_State_Master");

                            int len;
                            len = StateList.length();
                            StateId = new int[len];
                            State = new String[len];
                            int position=0;

                            for (int j = 0; j < len; j++)
                            {
                                final JSONObject test = (JSONObject) StateList.get(j);
                                final int state_ID = test.getInt("state_ID");
                                final String state_Name = test.getString("state_Name");

                                State[j] = state_Name;
                                StateId[j] = state_ID;

                                Statehashmap.put(state_Name,state_ID);

                                if(cust_State_ID==(state_ID))
                                {
                                    position = j;
                                }
                            }

                            ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.text1, State);
                            SP_StateList.setAdapter(adapterCategories);
                            SP_StateList.setSelection(position);
                        }
                        else
                        {
                            String errorString = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),errorString,1);
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
                    String UnitNo=addresses.get(0).getFeatureName();
                    String Street=addresses.get(0).getThoroughfare();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();

                    Log.e("Address: ", "" + address);
                    Log.e("City: ", "" + city);
                    Log.e("Street: ", "" + Street);
                    Log.e("State: ", "" + state);
                    Log.e("Country: ", "" + country);
                    Log.e("APostalCode: ", "" + postalCode);
                    Log.e("UnitNO: ", "" + UnitNo);

                    edtStreetNo.setText(Street);
                    edtCityName.setText(city);
                    edt_Zipcode.setText(postalCode);
                    edtUnitNo.setText(UnitNo);

                    for(int i=0;i<State.length;i++)
                    {

                        if(State[i].equals(state))
                        {
                            SP_StateList.setSelection(i);
                            break;
                        }
                    }
                    for(int j=0;j<Country.length;j++)
                    {

                        if(Country[j].equals(country))
                        {
                            Sp_CountryList.setSelection(j);
                            break;
                        }
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}




