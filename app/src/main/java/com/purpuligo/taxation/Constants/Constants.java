package com.purpuligo.taxation.Constants;

public interface Constants {
    interface SEND_OTP{
        String KEY_Phone = "telephone";
    }
    interface GET_DATA_SEND_OTP{
        String KEY_STATUS = "status";
        String KEY_MOBILE_NUMBER = "mobile_no";
    }
    interface VERIFY_OTP{
        String KEY_Phone = "telephone";
        String KEY_OTP = "otp";
    }
    interface GET_DATA_VERIFY_OTP{
        String KEY_MOBILE_NUMBER = "mobile_no";
        String KEY_STATUS = "status";
        String KEY_USER_ID = "user_id";
    }
    interface PROFILE_SUBMIT_DETAILS{
        String KEY_EMAIL_ID = "email_id";
        String KEY_USER_NAME = "user_name";
        String KEY_TELEPHONE = "telephone";
    }
    interface GET_PROFILE_DETAILS{
        String KEY_USER_ID = "user_id";
        String KEY_EMAIL_ID = "email_id";
    }
    interface ITR_SUBMIT_DETAILS{
        String KEY_USER_ID = "user_id2";
        String KEY_TYPE = "type";
        String KEY_CATEGORY = "category";
        String KEY_NAME = "name";
        String KEY_DOB = "dob";
        String KEY_ADDRESS = "address";
        String KEY_PIN_NO = "pin_no";
        String KEY_STATE = "state";
        String KEY_CITY_TOWN = "dist";
        String KEY_PAN_NO = "pan_no";
        String KEY_AADHAAR_NO = "aadhaar_no";
        String KEY_HOUSE_PROPERTY = "house_property_details";
        String KEY_INTEREST_HOUSE_PROPERTY = "interest_house_property";
        String KEY_INCOME = "income";
        String KEY_BANK_NAME = "bank_name";
        String KEY_ACCOUNT_HOLDER_NAME = "acc_holder_name";
        String KEY_IFSC_CODE = "bank_ifsc_code";
        String KEY_ACCOUNT_NO = "account_no";
        String KEY_FINANCIAL_YEAR = "financial_year";
        String KEY_DIVIDEND = "dividend";
        String KEY_HOUSE_TYPE = "house_type";
        String KEY_TAX_RELEVANT = "tax_80g";
        String KEY_IMAGE = "img";
    }
    interface  GET_ITR_SUBMIT_DETAILS{
        String KEY_TAX_PAIR_ID = "taxPairProfile_id";
        String KEY_ORDER_ID = "order_id";
        String KEY_CREATE_AT = "create_at";
        String KEY_ORDER_AMOUNT = "order_amount";
        String KEY_ORDER_CURRENCY = "order_currency";
    }
    interface IMAGE_DATA_SUBMIT{
        String KEY_TAX_PAIR_ID = "tax_transaction_id";
        String KEY_USER_ID = "user_id";
        String IMAGE_PATH = "img_path";

    }
    interface GET_DATA_ITR_SUBMIT{
        String KEY_TAX_PAIR_PROFILE_ID = "tax_pair_id";
    }
    interface ORDER_ID_GEN{
        String KEY_AMOUNT = "amount";
        String KEY_ORDER_USER_ID = "user_id";
        String KEY_TYPE = "type";
    }
    interface DETAILS_SHOW{
        String KEY_DOB = "dob";
        String KEY_TRANSACTION_ID = "tax_transaction_id";
        String KEY_FINANCIAL_YEAR = "financial_year";
        String KEY_INCOME = "income";
        String KEY_HOUSE_PROPERTY = "house_property_details";
        String KEY_HOUSE_INTEREST = "interest_house_property";
        String KEY_BANK = "bank_name";
        String KEY_ACC_NAME = "acc_holder_name";
        String KEY_IFCS = "bank_ifsc_code";
        String KEY_ACC_NUMBER = "account_no";
        String KEY_CATEGORY = "category";
        String KEY_TYPE = "type";
        String KEY_DATE = "submit_date";
        String KEY_EMAIL_ID = "email_id";
        String KEY_ADDRESS = "address";
        String KEY_PIN_NO = "pin_no";
        String KEY_DIST = "dist";
        String KEY_PAN_NUMBER = "pan_no";
        String KEY_AADHAAR = "aadhaar_no";
        String KEY_HOUSE_TYPE = "house_type";
        String KEY_DIVIDEND = "dividend";
        String KEY_TEX_RELEVANT = "tax_80g";

    }
    interface USER_SESSION_MANAGEMENT{
        String KEY_PREFER_NAME = "Taxation";
        //String KEY_IS_USER_LOGIN = "IsUserLoggedIn";
        String KEY_CUSTOMER_EMAIL = "customer_email";
        String KEY_CUSTOMER_ID = "customer_id";
        String KEY_CUSTOMER_NAME = "customer_name";
        String KEY_CUSTOMER_PHONE_NUMBER = "customer_phone";
    }
}
