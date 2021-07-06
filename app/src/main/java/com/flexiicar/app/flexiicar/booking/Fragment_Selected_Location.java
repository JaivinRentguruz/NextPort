package com.flexiicar.app.flexiicar.booking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.archit.calendardaterangepicker.customviews.CalendarListener;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.flexiicar.app.R;
import com.flexiicar.app.flexiicar.login.Login;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Selected_Location   extends Fragment
{
    private DateRangeCalendarView calendar;
    TextView txtstartdate, txtenddate, txtdone, txtpickuptime, txtreturntime, txtpickup_cancel, txt_droptime_cancel, txt_location_name, txtcity, txtzipcode, txtstreet,
            txtreturn_location, txtreturn_city, txtreturn_street, txtreturn_zipcode,txt_cancelCal;
    ImageView backarrowimageView ;
    RelativeLayout img_plus;
    LinearLayout linearlayout_different_location, lbl_change_returnLoc, lbl_change_location,llLogin,LoginLayout,RegisterLayout;
    RelativeLayout calanderlayout, time_relative_layout, relative_layout_droptime;
    Bundle returnLocationBundle, locationBundle;
    Boolean locationType, initialSelect;
    int cmP_DATE_FORMAT;
    TextView txt_Discard;
    String StarDate, EndDate;
    public String id = "";
    Bundle Booking;
    //boolean isPickTimeSelectedcolor = true;
    private static final String TAG = Fragment_Selected_Location.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selected_location, container, false);
    }

    @SuppressLint("ResourceType")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");
       // cmP_DATE_FORMAT = sp.getString("cmP_DATE_FORMAT", " ");

        returnLocationBundle = getArguments().getBundle("returnLocation");
        locationBundle = getArguments().getBundle("location");
        locationType = getArguments().getBoolean("locationType");
        initialSelect = getArguments().getBoolean("initialSelect");

        LoginLayout=view.findViewById(R.id.Login_Layout);
        RegisterLayout=view.findViewById(R.id.Register_Layout);

        llLogin=view.findViewById(R.id.llLogin);

       /* if (id.equals(""))
        {
            llLogin.setVisibility(View.VISIBLE);
        }
        else
        {
            llLogin.setVisibility(View.GONE);
        }*/
        LoginLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle Booking=new Bundle();
                Booking.putBundle("returnLocation",returnLocationBundle);
                Booking.putBundle("location",locationBundle);
                Booking.putBoolean("locationType",locationType);
                Booking.putBoolean("initialSelect",initialSelect);
                Booking.putBoolean("LoginForUser",true);
                System.out.println(Booking);
                NavHostFragment.findNavController(Fragment_Selected_Location.this)
                        .navigate(R.id.action_Selected_location_to_LoginFragment,Booking);
            }
        });
        RegisterLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        try
        {
            LinearLayout lblselectedlocation = view.findViewById(R.id.lblcontinue1);
            txt_Discard=view.findViewById(R.id.txt_DiscardSelectedLoc);
            calendar = view.findViewById(R.id.cdrvCalendar);
            txtstartdate = view.findViewById(R.id.lblstartdate);
            txtenddate = view.findViewById(R.id.lblenddate);
            backarrowimageView = view.findViewById(R.id.backarrow1);
            calanderlayout = view.findViewById(R.id.calender_layout);
            txtdone = view.findViewById(R.id.txt_done);
            img_plus = view.findViewById(R.id.img_plus);
            linearlayout_different_location = view.findViewById(R.id.different_location_layout);
            txt_location_name = view.findViewById(R.id.txt_location_name);
            txtstreet = view.findViewById(R.id.txt_Street_loc);
            txtcity = view.findViewById(R.id.txt_city_loc);
            txtzipcode = view.findViewById(R.id.txt_zip_loc);
            txtpickuptime = view.findViewById(R.id.pickup_time);
            time_relative_layout = view.findViewById(R.id.time_relative_layout);
            relative_layout_droptime = view.findViewById(R.id.time_relative_layout2);
            txtreturntime = view.findViewById(R.id.return_time);
            lbl_change_returnLoc = view.findViewById(R.id.lbl_change);
            lbl_change_location = view.findViewById(R.id.lblchange_location);
            txtreturn_location = view.findViewById(R.id.return_locationName);
            txtreturn_city = view.findViewById(R.id.txt_return_Street);
            txtreturn_street = view.findViewById(R.id.txt_return_city);
            txtreturn_zipcode = view.findViewById(R.id.txt_return_zip);
            txtpickup_cancel=view.findViewById(R.id.pickup_cancel);
            txt_droptime_cancel=view.findViewById(R.id.droptime_cancel);
            txt_cancelCal=view.findViewById(R.id.txt_cancelCal);

            if (!initialSelect)
            {
                linearlayout_different_location.setVisibility(View.VISIBLE);
            }

            txt_location_name.setText(locationBundle.getString("location_Name"));
            txtstreet.setText(locationBundle.getString("street"));
            txtcity.setText(locationBundle.getString("city"));
            txtzipcode.setText(locationBundle.getString("zipcode"));

            txtreturn_location.setText(returnLocationBundle.getString("location_Name"));
            txtreturn_city.setText(returnLocationBundle.getString("street"));
            txtreturn_street.setText(returnLocationBundle.getString("city"));
            txtreturn_zipcode.setText(returnLocationBundle.getString("zipcode"));

            img_plus.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    linearlayout_different_location.setVisibility(View.VISIBLE);
                }
            });
            txt_Discard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    NavHostFragment.findNavController(Fragment_Selected_Location.this)
                            .navigate(R.id.action_Selected_location_to_Search_activity);
                }
            });
            backarrowimageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are You Sure You Want To Cancel?");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    NavHostFragment.findNavController(Fragment_Selected_Location.this)
                                            .navigate(R.id.action_Selected_location_to_Search_activity);
                                }
                            });
                    builder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            });

                    final AlertDialog dialog = builder.create();
                    dialog.show();
                       /*Bundle locations = new Bundle();
                    locations.putBundle("location", locationBundle);
                    locations.putBundle("returnLocation", returnLocationBundle);
                    locations.putBoolean("locationType", true);*/

                }
            });

            lbl_change_location.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Bundle locations = new Bundle();
                    locations.putBundle("location", locationBundle);
                    locations.putBundle("returnLocation", returnLocationBundle);
                    locations.putBoolean("locationType", true);
                    NavHostFragment.findNavController(Fragment_Selected_Location.this)
                            .navigate(R.id.action_Selected_location_to_Available_location, locations);
                }
            });

            lbl_change_returnLoc.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Bundle locations = new Bundle();
                    locations.putBundle("location", locationBundle);
                    locations.putBundle("returnLocation", returnLocationBundle);
                    locations.putBoolean("locationType", false);
                    NavHostFragment.findNavController(Fragment_Selected_Location.this)
                            .navigate(R.id.action_Selected_location_to_Available_location, locations);
                }
            });

            txtstartdate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    calanderlayout.setVisibility(View.VISIBLE);
                }
            });

          /*  txtenddate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    calanderlayout.setVisibility(View.VISIBLE);
                }
            });*/

            txtdone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    calanderlayout.setVisibility(View.GONE);
                }
            });
            txt_cancelCal.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    calanderlayout.setVisibility(View.GONE);
                }
            });
            txtpickuptime.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    time_relative_layout.setVisibility(View.VISIBLE);
                }
            });

            txtreturntime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relative_layout_droptime.setVisibility(View.VISIBLE);
                }
            });
            txtpickup_cancel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    time_relative_layout.setVisibility(View.GONE);
                }
            });
            txt_droptime_cancel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    relative_layout_droptime.setVisibility(View.GONE);
                }
            });

            //Pick Up Time Selection
            final RelativeLayout timelayout = (RelativeLayout) view.findViewById(R.id.relative_layout_time);

            for (int i = 0; i <= 47; i++)
            {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.BELOW, (200 + i - 1));
                lp.setMargins(0, 0, 0, 0);

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout Timelist = (LinearLayout) inflater.inflate(R.layout.timepickup_list, (ViewGroup) view.findViewById(android.R.id.content), false);
                Timelist.setId(200 + i);
                Timelist.setLayoutParams(lp);

                final TextView txttime = (TextView) Timelist.findViewById(R.id.txt_time);
                final Button btnselect = (Button) Timelist.findViewById(R.id.btn_selecttime);
                final LinearLayout linear = (LinearLayout) Timelist.findViewById(R.id.linearlayout2);

                final int tempPosition = i;

                linear.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        txtpickuptime.setText(txttime.getText());
                        txtreturntime.setText(txttime.getText());
                        linear.setBackgroundColor(Color.BLACK);
                        txttime.setTextColor(Color.WHITE);

                        int len = timelayout.getChildCount();

                        for (int m = 0; m < len; m++)
                        {
                            if (m != tempPosition)
                            {
                                LinearLayout llTimeLayout = timelayout.getChildAt(m).findViewById(R.id.linearlayout2);
                                TextView txtTime = timelayout.getChildAt(m).findViewById(R.id.txt_time);
                                llTimeLayout.setBackgroundColor(Color.WHITE);
                                txtTime.setTextColor(Color.BLACK);
                                time_relative_layout.setVisibility(View.GONE);
                            }
                        }
                    }
                });

                String hour = (i / 2) + "";
                String mins = ((i % 2) * 30) + "";

                if (hour.length() == 1)
                    hour = "0" + hour;

                if (mins.length() == 1)
                    mins += "0";

                txttime.setText(hour + ":" + mins);
                timelayout.addView(Timelist);
            }

            //Drop Time Selection
            final RelativeLayout timelayout2 = (RelativeLayout) view.findViewById(R.id.droptime_layout);

            for (int i = 0; i <= 47; i++)
            {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.BELOW, (200 + i - 1));
                lp.setMargins(0, 0, 0, 0);

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout Timelist = (LinearLayout) inflater.inflate(R.layout.timepickup_list, (ViewGroup) view.findViewById(android.R.id.content), false);
                Timelist.setId(200 + i);
                Timelist.setLayoutParams(lp);

                final TextView txttime = (TextView) Timelist.findViewById(R.id.txt_time);
                Button btnselect = (Button) Timelist.findViewById(R.id.btn_selecttime);
                final LinearLayout linear = (LinearLayout) Timelist.findViewById(R.id.linearlayout2);

                final int tempPosition = i;

                linear.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        txtreturntime.setText(txttime.getText());
                        linear.setBackgroundColor(Color.BLACK);
                        txttime.setTextColor(Color.WHITE);
                        int len = timelayout2.getChildCount();

                        for (int m = 0; m < len; m++)
                        {
                            if (m != tempPosition)
                            {
                                LinearLayout llTimeLayout = timelayout2.getChildAt(m).findViewById(R.id.linearlayout2);
                                TextView txtTime = timelayout2.getChildAt(m).findViewById(R.id.txt_time);
                                llTimeLayout.setBackgroundColor(Color.WHITE);
                                txtTime.setTextColor(Color.BLACK);
                                relative_layout_droptime.setVisibility(View.GONE);
                            }
                        }
                    }
                });

                String hour = (i / 2) + "";
                String mins = ((i % 2) * 30) + "";

                if (hour.length() == 1)
                    hour = "0" + hour;

                if (mins.length() == 1)
                    mins += "0";

                txttime.setText(hour + ":" + mins);
                timelayout2.addView(Timelist);
            }
            lblselectedlocation.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                  //  if(!id.equals(""))
                        // {
                            Bundle BookingBundle = new Bundle();
                            BookingBundle.putInt("ForTransId", 0);
                            BookingBundle.putInt("PickupLocId", locationBundle.getInt("location_ID"));
                            BookingBundle.putInt("ReturnLocId", returnLocationBundle.getInt("location_ID"));
                            BookingBundle.putInt("CustomerId",Integer.parseInt(id));
                            BookingBundle.putInt("VehicleTypeId", 0);
                            BookingBundle.putInt("VehicleID", 0);
                            BookingBundle.putString("StrFilterVehicleTypeIds", "");
                            BookingBundle.putString("StrFilterVehicleOptionIds", "");
                            BookingBundle.putString("PickupDate", StarDate);
                            BookingBundle.putString("ReturnDate", EndDate);
                          //  BookingBundle.putInt("FilterTransmission", 0);
                           // BookingBundle.putInt("FilterPassengers", 0);
                            BookingBundle.putInt("BookingStep", 1);
                            BookingBundle.putInt("BookingType", 0);
                            BookingBundle.putString("PickupTime",txtpickuptime.getText().toString());
                            BookingBundle.putString("ReturnTime", txtreturntime.getText().toString());
                            BookingBundle.putString("PickupLocName", locationBundle.getString("location_Name"));
                            BookingBundle.putString("ReturnLocName", returnLocationBundle.getString("location_Name"));

                            Booking = new Bundle();
                            Booking.putBundle("BookingBundle", BookingBundle);
                            Booking.putBundle("returnLocation", returnLocationBundle);
                            Booking.putBundle("location", locationBundle);
                            Booking.putBoolean("locationType", locationType);
                            Booking.putBoolean("initialSelect", initialSelect);
                            System.out.println(Booking);

                           NavHostFragment.findNavController(Fragment_Selected_Location.this)
                                 .navigate(R.id.action_Selected_location_to_Vehicles_Available, Booking);

                           /* if(txt_location_name.getText().toString().equals(txtreturn_location.getText().toString()))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Are you sure you want to continue with pickup & return same loaction?");
                                builder.setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                NavHostFragment.findNavController(Fragment_Selected_Location.this)
                                                        .navigate(R.id.action_Selected_location_to_Vehicles_Available, Booking);
                                            }
                                        });
                                builder.setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public   void onClick(DialogInterface dialog, int which)
                                            {
                                                dialog.dismiss();
                                            }
                                        });

                                final AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                            else
                            {
                                NavHostFragment.findNavController(Fragment_Selected_Location.this)
                                        .navigate(R.id.action_Selected_location_to_Vehicles_Available, Booking);
                            }*/
                    }
                   /* else
                    {
                        String msgString="Please Login and add driver details";
                        CustomToast.showToast(getActivity(),msgString,1);
                    }*/
            });

            //calander
            calendar.setCalendarListener(calendarListener);
            Calendar current = Calendar.getInstance();
            Calendar yearAfter = Calendar.getInstance();
            yearAfter.add(Calendar.DATE, 365);
            calendar.setCurrentMonth(current);
            calendar.setSelectableDateRange(current, yearAfter);

            SimpleDateFormat df=new SimpleDateFormat("HH:mm");
            current.add(Calendar.MINUTE,60);
            String formatedtime=df.format(current.getTime());
            txtpickuptime.setText(formatedtime);

            SimpleDateFormat df1=new SimpleDateFormat("HH:mm");
            String formatedtime1=df1.format(current.getTime());
            txtreturntime.setText(formatedtime1);


            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
            String currentDateandTime = sdf.format(new Date());
            txtstartdate.setText(currentDateandTime);

            cmP_DATE_FORMAT=1;
            current.add(Calendar.DATE,(cmP_DATE_FORMAT));
            String enddate = sdf.format(current.getTime());
            txtenddate.setText(enddate);

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            StarDate = sdfDate.format(new Date());
            EndDate = sdfDate.format(current.getTime());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private final CalendarListener calendarListener = new CalendarListener()
    {
        @Override
        public void onFirstDateSelected(@NonNull final Calendar startDate)
        {

        }

        @Override
        public void onDateRangeSelected(@NonNull final Calendar startDate, @NonNull final Calendar endDate)
        {

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd  yyyy");
            String formattedDate = sdf.format(startDate.getTime());
            txtstartdate.setText(formattedDate);
            String formattedDate1 = sdf.format(endDate.getTime());
            txtenddate.setText(formattedDate1);

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            StarDate = sdfDate.format(startDate.getTime());

            if (endDate != null)

                EndDate = sdfDate.format(endDate.getTime());
        }
    };
}