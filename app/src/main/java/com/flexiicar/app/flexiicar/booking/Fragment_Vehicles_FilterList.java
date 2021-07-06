package com.flexiicar.app.flexiicar.booking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.AndroidNetworking;
import com.flexiicar.app.R;
import com.flexiicar.app.adapters.CustomToast;
import com.flexiicar.app.apicall.ApiService;
import com.flexiicar.app.apicall.OnResponseListener;
import com.flexiicar.app.apicall.RequestType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_BOOKING;
import static com.flexiicar.app.apicall.ApiEndPoint.FILTERLIST;

public class Fragment_Vehicles_FilterList extends Fragment
{
    public static Context context;
    public String id = "";
    Handler handler = new Handler();
    ImageView img_back;
    Bundle BookingBundle;
    String vehicleTypeIds="",vehicleOptionIds="";
    Spinner spinnerTransmission, spinnerPassenger;
    TextView txt_Discard,txtFilterApply;
    Bundle returnLocationBundle, locationBundle;
    Boolean locationType, initialSelect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_filterlist, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            img_back = view.findViewById(R.id.img_back_vehicleavlbl);
            txt_Discard = view.findViewById(R.id.txt_discardVehFilter);
            txtFilterApply= view.findViewById(R.id.txtFilterApply);
            BookingBundle = getArguments().getBundle("BookingBundle");

            returnLocationBundle = getArguments().getBundle("returnLocation");
            locationBundle = getArguments().getBundle("location");
            locationType = getArguments().getBoolean("locationType");
            initialSelect = getArguments().getBoolean("initialSelect");

            spinnerPassenger = view.findViewById(R.id.spinnerPassenger);
            spinnerTransmission = view.findViewById(R.id.spinnerTransmission);

            txtFilterApply.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {
                        if (vehicleOptionIds.length() > 0)
                        {
                            vehicleOptionIds = vehicleOptionIds.substring(0, vehicleOptionIds.length()-1);
                        }
                        if (vehicleTypeIds.length() > 0)
                        {
                            vehicleTypeIds = vehicleTypeIds.substring(0, vehicleTypeIds.length()-1);
                        }
                        BookingBundle.putString("StrFilterVehicleTypeIds", vehicleTypeIds);
                        BookingBundle.putString("StrFilterVehicleOptionIds", vehicleOptionIds);
                        BookingBundle.putString("vehicle_Option_Name", "");
                        BookingBundle.putInt("FilterTransmission", spinnerTransmission.getSelectedItemPosition()+1);
                        BookingBundle.putInt("FilterPassengers", spinnerPassenger.getSelectedItemPosition());

                        Bundle Booking = new Bundle();
                        BookingBundle.putInt("BookingStep", 1);
                        BookingBundle.putInt("VehicleID", 0);
                        Booking.putBundle("BookingBundle", BookingBundle);
                        Booking.putBundle("returnLocation", returnLocationBundle);
                        Booking.putBundle("location", locationBundle);
                        Booking.putBoolean("locationType", locationType);
                        Booking.putBoolean("initialSelect", initialSelect);
                        System.out.println(Booking);
                        NavHostFragment.findNavController(Fragment_Vehicles_FilterList.this)
                                .navigate(R.id.action_Vehicles_FilterList_to_Vehicles_Available, Booking);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            img_back.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (vehicleOptionIds.length() > 0)
                    {
                        vehicleOptionIds = vehicleOptionIds.substring(0, vehicleOptionIds.length()-1);
                    }
                    if (vehicleTypeIds.length() > 0)
                    {
                        vehicleTypeIds = vehicleTypeIds.substring(0, vehicleTypeIds.length()-1);
                    }
                    BookingBundle.putString("StrFilterVehicleTypeIds", vehicleTypeIds);
                    BookingBundle.putString("StrFilterVehicleOptionIds", vehicleOptionIds);
                    BookingBundle.putString("vehicle_Option_Name", "");
                    BookingBundle.putInt("FilterTransmission", spinnerTransmission.getSelectedItemPosition()+1);
                    BookingBundle.putInt("FilterPassengers", spinnerPassenger.getSelectedItemPosition());

                    Bundle Booking = new Bundle();
                    BookingBundle.putInt("BookingStep", 1);
                    BookingBundle.putInt("VehicleID", 0);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);

                    NavHostFragment.findNavController(Fragment_Vehicles_FilterList.this)
                            .navigate(R.id.action_Vehicles_FilterList_to_Vehicles_Available, Booking);
                }
            });

            txt_Discard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (vehicleOptionIds.length() > 0)
                    {
                        vehicleOptionIds = vehicleOptionIds.substring(0, vehicleOptionIds.length()-1);
                    }
                    if (vehicleTypeIds.length() > 0)
                    {
                        vehicleTypeIds = vehicleTypeIds.substring(0, vehicleTypeIds.length()-1);
                    }
                    BookingBundle.putString("StrFilterVehicleTypeIds", vehicleTypeIds);
                    BookingBundle.putString("StrFilterVehicleOptionIds", vehicleOptionIds);
                    BookingBundle.putString("vehicle_Option_Name", "");
                    BookingBundle.putInt("FilterTransmission", spinnerTransmission.getSelectedItemPosition()+1);
                    BookingBundle.putInt("FilterPassengers", spinnerPassenger.getSelectedItemPosition());

                    Bundle Booking = new Bundle();
                    BookingBundle.putInt("BookingStep", 1);
                    BookingBundle.putInt("VehicleID", 0);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);

                    NavHostFragment.findNavController(Fragment_Vehicles_FilterList.this)
                            .navigate(R.id.action_Vehicles_FilterList_to_Vehicles_Available,Booking);
                }
            });
            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            id = sp.getString(getString(R.string.id), "");

            System.out.println("ID :- " + id);

            Fragment_Vehicles_FilterList.context = getActivity();

            JSONObject bodyParam = new JSONObject();
            try {
                bodyParam.accumulate("VehicleTypeId", BookingBundle.getInt("VehicleTypeId"));
                System.out.println(bodyParam);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidNetworking.initialize(getActivity());

            ApiService ApiService = new ApiService(getfilterList, RequestType.GET,
                    FILTERLIST, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    OnResponseListener getfilterList = new OnResponseListener()
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

                            JSONObject resultSet = responseJSON.getJSONObject("resultSet");

                            final JSONArray getfilterList = resultSet.getJSONArray("t0040_Vehicle_Type_Master");

                            final JSONArray getvehiclefeatureList = resultSet.getJSONArray("t0040_Vehicle_Option_Master");

                            RelativeLayout rlVehicleAvilable = getActivity().findViewById(R.id.rl_vehicle_type);

                            RelativeLayout rlvehicleFeturelist = getActivity().findViewById(R.id.rl_vehicle_features);

                            int len;
                            len = getfilterList.length();

                            for (int j = 0; j < len;)
                            {
                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                lp.addRule(RelativeLayout.BELOW, (200 + (j/2) - 1));
                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                lp.setMargins(0, 0, 0, 0);

                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                LinearLayout vehicleTypelayoutlist = (LinearLayout) inflater.inflate(R.layout.vehicle_type_list, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                vehicleTypelayoutlist.setId(200 + (j/2));
                                vehicleTypelayoutlist.setLayoutParams(lp);

                                CheckBox checkBox1, checkBox2;

                                checkBox1 = vehicleTypelayoutlist.findViewById(R.id.chk_title1);
                                checkBox2 = vehicleTypelayoutlist.findViewById(R.id.chk_title2);

                                if(j<len && (j%2) == 0)
                                {
                                    final JSONObject test = (JSONObject) getfilterList.get(j);
                                    final String vehiclE_ID = test.getString("vehicle_Type_ID");
                                    String vehicle_Type_Name = test.getString("vehicle_Type_Name");
                                    String doors = test.getString("doors");
                                    String sample = test.getString("sample");
                                    String baggages = test.getString("baggages");
                                    String seat = test.getString("seat");
                                    String isOnline = test.getString("isOnline");
                                    String cmp_ID = test.getString("cmp_ID");
                                    String created_By = test.getString("created_By");
                                    String created_Date = test.getString("created_Date");
                                    String type_Of_Image = test.getString("type_Of_Image");
                                    String applySIPP = test.getString("applySIPP");
                                    String transmission = test.getString("transmission");

                                    checkBox1.setText(vehicle_Type_Name);

                                    checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                                    {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                                        {
                                            vehicleTypeIds += vehiclE_ID+",";
                                        }
                                    });
                                    j++;
                                }

                                if(j<len && (j%2) == 1)
                                {
                                    final JSONObject test = (JSONObject) getfilterList.get(j);
                                    final String vehiclE_ID = test.getString("vehicle_Type_ID");
                                    String vehicle_Type_Name = test.getString("vehicle_Type_Name");
                                    String doors = test.getString("doors");
                                    String sample = test.getString("sample");
                                    String baggages = test.getString("baggages");
                                    String seat = test.getString("seat");
                                    String isOnline = test.getString("isOnline");
                                    String cmp_ID = test.getString("cmp_ID");
                                    String created_By = test.getString("created_By");
                                    String created_Date = test.getString("created_Date");
                                    String type_Of_Image = test.getString("type_Of_Image");
                                    String applySIPP = test.getString("applySIPP");
                                    String transmission = test.getString("transmission");

                                    checkBox2.setVisibility(View.VISIBLE);
                                    checkBox2.setText(vehicle_Type_Name);
                                    checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                                    {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                                        {
                                            vehicleTypeIds += vehiclE_ID+",";
                                        }
                                    });
                                    j++;
                                }

                                rlVehicleAvilable.addView(vehicleTypelayoutlist);
                            }

                            int len1;
                            len1 = getvehiclefeatureList.length();

                            for (int i = 0; i < len1;)
                            {
                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                lp.addRule(RelativeLayout.BELOW, (300 + (i/2) - 1));
                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                lp.setMargins(0, 0, 0, 0);

                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                LinearLayout vehicleFetureLayout = (LinearLayout) inflater.inflate(R.layout.vehicle_type_list, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                vehicleFetureLayout.setId(300 + (i/2));
                                vehicleFetureLayout.setLayoutParams(lp);

                                CheckBox checkBox1, checkBox2, checkBox3;

                                checkBox1 = vehicleFetureLayout.findViewById(R.id.chk_title1);
                                checkBox2 = vehicleFetureLayout.findViewById(R.id.chk_title2);

                                if(i<len1 && (i%2) == 0)
                                {
                                    final JSONObject test = (JSONObject) getvehiclefeatureList.get(i);
                                    final String vehicle_Option_ID = test.getString("vehicle_Option_ID");
                                    String vehicle_Option_Name = test.getString("vehicle_Option_Name");
                                    String description = test.getString("description");
                                    String cmp_ID = test.getString("cmp_ID");
                                    String created_By = test.getString("created_By");
                                    String created_Date = test.getString("created_Date");

                                    checkBox1.setText(vehicle_Option_Name);
                                    checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                                    {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                                        {
                                            vehicleOptionIds += vehicle_Option_ID+",";
                                        }
                                    });
                                    i++;
                                }

                                if(i<len1 && (i%2) == 1)
                                {
                                    final JSONObject test = (JSONObject) getvehiclefeatureList.get(i);
                                    final String vehicle_Option_ID = test.getString("vehicle_Option_ID");
                                    String vehicle_Option_Name = test.getString("vehicle_Option_Name");
                                    String description = test.getString("description");
                                    String cmp_ID = test.getString("cmp_ID");
                                    String created_By = test.getString("created_By");
                                    String created_Date = test.getString("created_Date");

                                    checkBox2.setVisibility(View.VISIBLE);
                                    checkBox2.setText(vehicle_Option_Name);
                                    checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                                    {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                                        {
                                            vehicleOptionIds += vehicle_Option_ID+",";
                                        }
                                    });
                                    i++;
                                }

                                rlvehicleFeturelist.addView(vehicleFetureLayout);
                            }
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
}


