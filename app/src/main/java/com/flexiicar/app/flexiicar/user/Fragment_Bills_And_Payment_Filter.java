package com.flexiicar.app.flexiicar.user;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.flexiicar.app.R;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Bills_And_Payment_Filter extends Fragment
{
    TextView txt_FilterStartDate,txt_FilterEndDate,txtclearfilter;
    ImageView backarrow;
    Handler handler = new Handler();
    public static Context context;
    public String id = "";
    DatePickerDialog datePickerDialog;
    LinearLayout lblSubmit;
    RangeSeekBar rangeSeekBar;
    Spinner Sp_transacType,Sp_BillStatus;
    String Trans_Typestr;
    String BillstatusStr;
    String MinVal="",MaxVal="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_bills_and_payments_filter, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            txt_FilterStartDate = view.findViewById(R.id.txt_FilterStartDate);
            txt_FilterEndDate = view.findViewById(R.id.txt_FilterEndDate);
            rangeSeekBar = view.findViewById(R.id.rangeSeekbar);
            Sp_transacType = view.findViewById(R.id.Sp_transacType);
            Sp_BillStatus = view.findViewById(R.id.Sp_BillStatus);
            lblSubmit = view.findViewById(R.id.lblSubmit);
            txtclearfilter= view.findViewById(R.id.txtclearfilter);
            backarrow = view.findViewById(R.id.BacktoBillPayment);

            Sp_BillStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int pos, long id)
                {
                    BillstatusStr = parent.getItemAtPosition(pos).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0)
                {

                }
            });

            txtclearfilter.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    txt_FilterEndDate.setText("");
                    txt_FilterStartDate.setText("");
                    Sp_transacType.setSelection(0);
                    Sp_BillStatus.setSelection(0);
                    rangeSeekBar.resetSelectedValues();
                }
            });

            Sp_transacType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int pos, long id)
                {
                    Trans_Typestr = parent.getItemAtPosition(pos).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0)
                {

                }
            });
            rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>()
            {
                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Integer minValue, Integer maxValue)
                {
                    Toast.makeText(getActivity(), minValue + "-" + maxValue, Toast.LENGTH_LONG).show();
                    MinVal = minValue.toString();
                    MaxVal = maxValue.toString();
                }
            });
            backarrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle AccountStatmentList=new Bundle();
                    AccountStatmentList.putBoolean("StatmentType",true);
                    NavHostFragment.findNavController(Fragment_Bills_And_Payment_Filter.this)
                            .navigate(R.id.action_BillsPayment_Filter_to_Bills_and_Payment,AccountStatmentList);
                }
            });
            txt_FilterStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // calender class's instance and get current date , month and year from calender
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year
                        int mMonth = c.get(Calendar.MONTH); // current month
                        final int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
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

                                        txt_FilterStartDate.setText(year + "-" + month + "-" + day);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            txt_FilterEndDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // calender class's instance and get current date , month and year from calender
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR); // current year
                        int mMonth = c.get(Calendar.MONTH); // current month
                        final int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
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

                                        txt_FilterEndDate.setText(year + "-" + month + "-" + day);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            id = sp.getString(getString(R.string.id), "");

            lblSubmit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    /*if (Sp_transacType.getSelectedItem().toString().equals(""))
                        Toast.makeText(getActivity(), "Please Select Transaction Type", Toast.LENGTH_LONG).show();
                    else if (Sp_BillStatus.getSelectedItem().toString().equals(""))
                        Toast.makeText(getActivity(), "Please Select Bill Status", Toast.LENGTH_LONG).show();
                    else if (txt_FilterStartDate.getText().toString().equals(""))
                        Toast.makeText(getActivity(), "Please Select Start Date", Toast.LENGTH_LONG).show();
                    else if (txt_FilterEndDate.getText().toString().equals(""))
                        Toast.makeText(getActivity(), "Please Select End Date", Toast.LENGTH_LONG).show();*/

                    Bundle AccountFilterList=new Bundle();
                    AccountFilterList.putInt("BillStatus",Sp_BillStatus.getSelectedItemPosition());
                    AccountFilterList.putString("FilterTransacType",Sp_transacType.getSelectedItem().toString());
                    AccountFilterList.putString("FilterStartDate",txt_FilterStartDate.getText().toString());
                    AccountFilterList.putString("FilterEndDate",txt_FilterEndDate.getText().toString());
                    AccountFilterList.putString("FilterToAmount",MinVal);
                    AccountFilterList.putString("FilterFromAmount",MaxVal);

                    Bundle AccountStatmentList=new Bundle();
                    AccountStatmentList.putBundle("AccountFilterList",AccountFilterList);
                    AccountStatmentList.putBoolean("StatmentType",false);

                    System.out.println(AccountStatmentList);
                    NavHostFragment.findNavController(Fragment_Bills_And_Payment_Filter.this)
                            .navigate(R.id.action_BillsPayment_Filter_to_Bills_and_Payment,AccountStatmentList);
                }
            });
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}