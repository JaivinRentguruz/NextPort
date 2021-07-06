package com.flexiicar.app.apicall;

public class ApiEndPoint
{

    // ApiEndPoint
    public static final String BASE_URL_LOGIN = "https://api.flexiicar.com/api/login/";
    public static final String BASE_URL_BOOKING = "https://api.flexiicar.com/api/booking/";
    public static final String BASE_URL_PAYMENT = "https://api.flexiicar.com/api/Payment/";
    public static final String BASE_URL_CUSTOMER = "https://api.flexiicar.com/api/Customer/";
    public static final String BASE_URL_SETTINGS = "https://api.flexiicar.com/api/Settings/";
    public static final String BASE_URL_CHECKOUT = "https://api.flexiicar.com/api/CheckOut/";
    public static final String BASE_URL_CHECKIN= "https://api.flexiicar.com/api/CheckIn/";
    public static final String UPLOAD_BASE_URL = "";
//http://api.rentguruz.com/api/login/
    //LOGIN
    public static final String LOGIN_VERIFICATION = "LoginVerification";
    public static final String APP_INTIALIZATION = "AppInitialization";
    public static final String REGISTRATION = "Registration";
    public static final String CHANGEPASSWORD = "ChangePassword";

    //BOOKING
    public static final String LOCATION_LIST = "locationlist";
    public static final String BOOKING = "booking";
    public static final String LOCATION_SEARCH_LIST="locationsearchlist";
    public static final String LOCATION_SEARCH_BY_DISTANCE="LocationSearchByDistance";
    public static final String FILTERLIST="filterlist";
    public static final String GETDEFAULTCREDITCARD="GetDefaultCreditCard";
    public static final String GETDCREDITCARDLIST="GetCreditCardList";
    public static final String GETTERMSCONDITION="GetTermsCondition";
    public static final String GETPAYMENT="PaymentProcess";
    public static final String UPDATECREDITCARD="UpdateCreditCard";
    public static final String DELETECREDITCARD="DeleteCreditCard";
    public static final String ADDCREDITCARD="AddCreditCard";
    public static final String GETPICKUPLIST="GetPickupList";
    public static final String GETDELIVERYLIST="GetDeliveryList";
    public static final String CALCULATE_DISTANCE="CalculatesDistance";
    public static final String CANCELBOOKING="CancelBooking";
    //PAYMENT
    public static final String PAYMENTPROCESS="PaymentProcess";

    //CUSTOMER
    public static final String GETCUSTOMERPROFILE="CustomerProfile";
    public static final String UPDATECUSTOMERPROFILE="UpdateCustomerProfile";
    public static final String GETCUSTOMERSUMMARY="customerSummary";
    public static final String GETRESERVATIONLIST="ReservationList";
    public static final String GETAGREEMENTLIST="AgreementsList";
    public static final String GETCUSTOMERINSURANCE="CustomerInsurance";
    public static final String UPDATECUSTOMERINSURANCE="UpdateCustomerInsurance";
    public static final String DRIVINGLICENSE="DrivingLicense";
    public static final String UPDATEDRIVINGLICENSE="UpdateDrivingLicense";
    public static final String GETACCOUNTSTATEMENT="AccountStatementList";

    public static final String ACTIVITYTIMELINELIST="ActivityTimeLineList";
    public static final String ADDCUSTOMERACTIVITY="AddCustomerActivity";
    public static final String UPDATECUSTOMERACTIVITY="UpdateCustomerActivity";
    public static final String ACTIVITYTIMELINE = "ActivityTimeLine";

    public static final String FORGETPASSWORD = "ForgotPassword";
    public static final String ADDPROFILEPICTURE = "AddProfilePicture";
    public static final String REMOVEPROFILEPICTURE = "RemoveProfilePicture";

    public static final String ACCOUNTSTATEMENTSTATUSUPDATE = "AccountStatementStatusUpdate";

    //SETTINGS
    public static final String GETCOUNTRYLIST="CountryList";
    public static final String STATELIST="StateList";
    public static final String ACTIVITYTYPELIST="ActivityTypeList";
    public static final String INSURANCECOMPANYLIST="InsuranceCompanyList";
    //CheckOut
    public static final String GETSELFCHECKOUT="GetSelfCheckOut";
    public static final String UPDATESELFCHECKOUT="UpdateSelfCheckOut";

    //CheckIn
    public static final String GETSELFCHECKIN="GetSelfCheckIn";
    public static final String UPDATESELFCHECKIN="UpdateSelfCheckIn";
    public static final String GETAGREEMENTREPORT="GetAgreementReport";
    public static final String VERSION_API = "v1/";

}
