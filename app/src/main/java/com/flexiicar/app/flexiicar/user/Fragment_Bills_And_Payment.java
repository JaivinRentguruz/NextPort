package com.flexiicar.app.flexiicar.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.flexiicar.app.flexiicar.booking.Fragment_Payment_checkout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.flexiicar.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.flexiicar.app.apicall.ApiEndPoint.GETACCOUNTSTATEMENT;

public class Fragment_Bills_And_Payment extends Fragment
{
    ImageView backarrow;
    Handler handler = new Handler();
    public static Context context;
    public String id = "";
    EditText edt_search;
    LinearLayout filter_icon;
    Boolean StatmentType;
    Bundle AccountFilterList;
    TextView txtDiscard;
    JSONArray getAccountstatementlist = new JSONArray();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_bills_and_payments, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        backarrow=view.findViewById(R.id.payment_to_userdetails);
        edt_search=view.findViewById(R.id.edt_searchAccountStatment);
        filter_icon=view.findViewById(R.id.filter_iconForAC);
        txtDiscard=view.findViewById(R.id.txtDiscardAc);

        StatmentType = getArguments().getBoolean("StatmentType");
        System.out.println(StatmentType);

        backarrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavHostFragment.findNavController(Fragment_Bills_And_Payment.this)
                        .navigate(R.id.action_Bills_and_Payment_to_User_Details);
            }
        });
        txtDiscard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(Fragment_Bills_And_Payment.this)
                        .navigate(R.id.action_Bills_and_Payment_to_User_Details);
            }
        });
        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");

        if (StatmentType)
        {
            String bodyParam = "";

            try
            {
                bodyParam+="customerId="+id;

                System.out.println(bodyParam);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            AndroidNetworking.initialize(getActivity());
            Fragment_Payment_checkout.context = getActivity();

            ApiService ApiService = new ApiService(GetAccountstatementlist, RequestType.GET,
                    GETACCOUNTSTATEMENT, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);

        }
        else
        {
            try {
                AccountFilterList = getArguments().getBundle("AccountFilterList");

                int billStatus = AccountFilterList.getInt("BillStatus");
                String StartDateStr = AccountFilterList.getString("FilterStartDate");
                String EndDateStr = AccountFilterList.getString("FilterEndDate");
                String FromAmountStr = AccountFilterList.getString("FilterFromAmount");
                String ToAmountStr = AccountFilterList.getString("FilterToAmount");
                String TransacTypeStr = AccountFilterList.getString("FilterTransacType");

                JSONObject bodyParam = new JSONObject();
                try {
                    bodyParam.accumulate("CustomerID", Integer.parseInt(id));
                    bodyParam.accumulate("BillID", 0);
                    if(billStatus > 0)
                        bodyParam.accumulate("BillStatus", (billStatus-1));
                    bodyParam.accumulate("FilterStartDate", StartDateStr);
                    bodyParam.accumulate("FilterEndDate", EndDateStr);
                    bodyParam.accumulate("FilterToAmount", ToAmountStr);
                    bodyParam.accumulate("FilterFromAmount", FromAmountStr);
                    bodyParam.accumulate("FilterTransacType", TransacTypeStr);
                    System.out.println(bodyParam);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                ApiService ApiService = new ApiService(GetAccountstatementlist, RequestType.POST,
                        GETACCOUNTSTATEMENT, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        edt_search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                try
                {
                    final RelativeLayout rlAccountStatement = getActivity().findViewById(R.id.rl_AccountStatement);
                    int len;
                    len = getAccountstatementlist.length();
                    rlAccountStatement.removeAllViews();

                    for (int j = 0; j < len; j++)
                    {
                        final JSONObject test = (JSONObject) getAccountstatementlist.get(j);
                        final String transacType = test.getString("transacType");

                        if (transacType.contains(charSequence))
                        {
                            final int billID = test.getInt("billID");
                            final int billStatus = test.getInt("billStatus");
                            final String forDate = test.getString("forDate");
                            final String billNumber = test.getString("billNumber");
                            final double totalAmount = test.getDouble("totalAmount");
                            final double paid = test.getDouble("paid");
                            final double balance = test.getDouble("balance");
                            final int forTranID = test.getInt("forTranID");
                            final String createdOn = test.getString("createdOn");
                            final int modifiedBy = test.getInt("modifiedBy");
                            final String path = test.getString("path");
                            final int type = test.getInt("type");
                            final String description = test.getString("description");

                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            lp.setMargins(0, 10, 0, 0);

                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.list_account_statement, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                            linearLayout.setId(200 + j);
                            linearLayout.setLayoutParams(lp);

                            LinearLayout layout_invoice = linearLayout.findViewById(R.id.layout_invoice);
                            LinearLayout Payment_Status_Layout = linearLayout.findViewById(R.id.Payment_Status_Layout);
                            final TextView Name = linearLayout.findViewById(R.id.txt_StatementName);
                            TextView txtOneTimeCharge = linearLayout.findViewById(R.id.txt_oneTimeCharge);
                            TextView txtDate = linearLayout.findViewById(R.id.txtDate);
                            TextView txtMonth = linearLayout.findViewById(R.id.txtMonth);
                            TextView txtinvoiceNo = linearLayout.findViewById(R.id.txt_invoiceNo);
                            TextView txtPaymentStatus = linearLayout.findViewById(R.id.txt_PaymentStatus);
                            TextView txt_TotalAmount = linearLayout.findViewById(R.id.text_totalAmount);

                            if (billStatus == 1)
                            {
                                txtPaymentStatus.setText("PAID");
                                Payment_Status_Layout.setBackgroundResource(R.drawable.ic_rectangle_darkgreen);
                                Name.setTextColor(getResources().getColor(R.color.footerButtonBC));
                            } else {
                                txtPaymentStatus.setText("UNPAID");
                                Payment_Status_Layout.setBackgroundResource(R.drawable.ic_rectangle_red);
                                Name.setTextColor(getResources().getColor(R.color.btn2));
                            }
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                            Date date = dateFormat.parse(forDate);

                            SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
                            String Monthstr = sdf.format(date);

                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd,yyyy");
                            String datestr = sdf1.format(date);

                            txtMonth.setText(Monthstr);
                            txtDate.setText(datestr);

                            Name.setText(transacType);
                            txt_TotalAmount.setText(((String.format(Locale.US, "%.2f", totalAmount))));
                            txtinvoiceNo.setText(billNumber);
                            txtOneTimeCharge.setText(description);
                            rlAccountStatement.addView(linearLayout);

                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
    }

    OnResponseListener GetAccountstatementlist = new OnResponseListener()
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
                            getAccountstatementlist = resultSet.getJSONArray("t0050_Customer_Bill_Payment");

                            final RelativeLayout rlAccountStatement = getActivity().findViewById(R.id.rl_AccountStatement);
                            int len;
                            len = getAccountstatementlist.length();

                            for (int j = 0; j < len; j++)
                            {
                                final JSONObject test = (JSONObject) getAccountstatementlist.get(j);

                                final int billID = test.getInt("billID");
                                final int billStatus = test.getInt("billStatus");
                                final String forDate = test.getString("forDate");
                                final String billNumber = test.getString("billNumber");
                                final String transacType = test.getString("transacType");
                                final double totalAmount = test.getDouble("totalAmount");
                                final double paid = test.getDouble("paid");
                                final double balance = test.getDouble("balance");
                                final int forTranID = test.getInt("forTranID");
                                final String createdOn = test.getString("createdOn");
                                final int modifiedBy = test.getInt("modifiedBy");
                                final String path = test.getString("path");
                                final int type = test.getInt("type");
                                final String description = test.getString("description");

                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                lp.setMargins(0, 10, 0, 0);

                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.list_account_statement, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                linearLayout.setId(200 + j);
                                linearLayout.setLayoutParams(lp);

                                LinearLayout layout_invoice=linearLayout.findViewById(R.id.layout_invoice);
                                LinearLayout Payment_Status_Layout=linearLayout.findViewById(R.id.Payment_Status_Layout);
                                final TextView Name = linearLayout.findViewById(R.id.txt_StatementName);
                                TextView txtOneTimeCharge = linearLayout.findViewById(R.id.txt_oneTimeCharge);
                                TextView txtDate= linearLayout.findViewById(R.id.txtDate);
                                TextView txtMonth= linearLayout.findViewById(R.id.txtMonth);
                                TextView txtinvoiceNo = linearLayout.findViewById(R.id.txt_invoiceNo);
                                TextView txtPaymentStatus = linearLayout.findViewById(R.id.txt_PaymentStatus);
                                TextView txt_TotalAmount = linearLayout.findViewById(R.id.text_totalAmount);

                               /* if(transacType.equals("Invoice"))
                                {
                                    Name.setTextColor(getResources().getColor(R.color.DarkRed));
                                }
                                else if(transacType.equals("Toll Charge"))
                                {
                                    Name.setTextColor(getResources().getColor(R.color.blue));
                                }
                                else
                                    {
                                        Name.setTextColor(getResources().getColor(R.color.footerButtonBC));
                                }*/

                                if(billStatus==1)
                                {
                                    txtPaymentStatus.setText("PAID");
                                    Payment_Status_Layout.setBackgroundResource(R.drawable.ic_rectangle_darkgreen);
                                    Name.setTextColor(getResources().getColor(R.color.footerButtonBC));
                                }
                                else
                                {
                                    txtPaymentStatus.setText("UNPAID");
                                    Payment_Status_Layout.setBackgroundResource(R.drawable.ic_rectangle_red);
                                    Name.setTextColor(getResources().getColor(R.color.btn2));
                                }
                                layout_invoice.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        if(transacType.equals("Payment"))
                                        {
                                            Bundle PaymentBundle=new Bundle();
                                            PaymentBundle.putInt("billID",billID);
                                            PaymentBundle.putInt("billStatus",billStatus);
                                            PaymentBundle.putString("forDate",forDate);
                                            PaymentBundle.putString("billNumber",billNumber);
                                            PaymentBundle.putString("transacType",transacType);
                                            PaymentBundle.putDouble("paid",paid);
                                            PaymentBundle.putDouble("balance",balance);
                                            PaymentBundle.putInt("forTranID",forTranID);
                                            PaymentBundle.putString("createdOn",createdOn);
                                            PaymentBundle.putInt("modifiedBy",modifiedBy);
                                            PaymentBundle.putDouble("totalAmount",totalAmount);
                                            PaymentBundle.putString("path",path);
                                            Bundle Payment=new Bundle();
                                            Payment.putBundle("PaymentBundle",PaymentBundle);
                                            NavHostFragment.findNavController(Fragment_Bills_And_Payment.this)
                                                    .navigate(R.id.action_Bills_and_Payment_to_Payment_Reciept_2,Payment);
                                        }
                                        else if(transacType.equals("Deposit"))
                                        {

                                            Bundle PaymentBundle=new Bundle();
                                            PaymentBundle.putInt("billID",billID);
                                            PaymentBundle.putInt("billStatus",billStatus);
                                            PaymentBundle.putString("forDate",forDate);
                                            PaymentBundle.putString("billNumber",billNumber);
                                            PaymentBundle.putString("transacType",transacType);
                                            PaymentBundle.putDouble("paid",paid);
                                            PaymentBundle.putDouble("balance",balance);
                                            PaymentBundle.putInt("forTranID",forTranID);
                                            PaymentBundle.putString("createdOn",createdOn);
                                            PaymentBundle.putInt("modifiedBy",modifiedBy);
                                            PaymentBundle.putDouble("totalAmount",totalAmount);
                                            PaymentBundle.putString("path",path);
                                            Bundle Payment=new Bundle();
                                            Payment.putBundle("PaymentBundle",PaymentBundle);
                                            NavHostFragment.findNavController(Fragment_Bills_And_Payment.this)
                                                    .navigate(R.id.action_Bills_and_Payment_to_Traffic_Ticket_Image,Payment);
                                        }
                                       /* else if(transacType.equals("Refund"))
                                        {
                                            Bundle PaymentBundle=new Bundle();
                                            PaymentBundle.putInt("billID",billID);
                                            PaymentBundle.putInt("billStatus",billStatus);
                                            PaymentBundle.putString("forDate",forDate);
                                            PaymentBundle.putString("billNumber",billNumber);
                                            PaymentBundle.putString("transacType",transacType);
                                            PaymentBundle.putDouble("paid",paid);
                                            PaymentBundle.putDouble("balance",balance);
                                            PaymentBundle.putInt("forTranID",forTranID);
                                            PaymentBundle.putString("createdOn",createdOn);
                                            PaymentBundle.putInt("modifiedBy",modifiedBy);
                                            PaymentBundle.putDouble("totalAmount",totalAmount);
                                            PaymentBundle.putString("path",path);
                                            Bundle Payment=new Bundle();
                                            Payment.putBundle("PaymentBundle",PaymentBundle);
                                            NavHostFragment.findNavController(Fragment_Bills_And_Payment.this)
                                                    .navigate(R.id.action_Bills_and_Payment_to_Invoice_Image,Payment);
                                        }
                                        else if(transacType.equals("Toll Charge"))
                                        {
                                            Bundle PaymentBundle=new Bundle();
                                            PaymentBundle.putInt("billID",billID);
                                            PaymentBundle.putInt("billStatus",billStatus);
                                            PaymentBundle.putString("forDate",forDate);
                                            PaymentBundle.putString("billNumber",billNumber);
                                            PaymentBundle.putString("transacType",transacType);
                                            PaymentBundle.putDouble("paid",paid);
                                            PaymentBundle.putDouble("balance",balance);
                                            PaymentBundle.putInt("forTranID",forTranID);
                                            PaymentBundle.putString("createdOn",createdOn);
                                            PaymentBundle.putInt("modifiedBy",modifiedBy);
                                            PaymentBundle.putDouble("totalAmount",totalAmount);
                                            PaymentBundle.putString("path",path);
                                            Bundle Payment=new Bundle();
                                            Payment.putBundle("PaymentBundle",PaymentBundle);
                                            NavHostFragment.findNavController(Fragment_Bills_And_Payment.this)
                                                    .navigate(R.id.action_Bills_and_Payment_to_TollCharge_Image,Payment);
                                        }*/
                                    }
                                });
                                filter_icon.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        NavHostFragment.findNavController(Fragment_Bills_And_Payment.this)
                                                .navigate(R.id.action_Bills_and_Payment_to_BillsPayment_Filter);
                                    }
                                });

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                                Date date = dateFormat.parse(forDate);

                                SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
                                String Monthstr = sdf.format(date);

                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd,yyyy");
                                String datestr = sdf1.format(date);

                                txtMonth.setText(Monthstr);
                                txtDate.setText(datestr);

                                Name.setText(transacType);
                                txt_TotalAmount.setText(((String.format(Locale.US, "%.2f", totalAmount))));
                                txtinvoiceNo.setText(billNumber);
                                txtOneTimeCharge.setText(description);
                                rlAccountStatement.addView(linearLayout);
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
