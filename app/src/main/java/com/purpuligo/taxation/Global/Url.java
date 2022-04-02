package com.purpuligo.taxation.Global;

public class Url {

    //private static final String BASE_URL = "https://purpuligo.com/carsm"; //testing
    private static final String BASE_URL = "https://mis.taxception.in";   //production

    public static final String SEND_OTP_URL = BASE_URL + "/index.php/User_mobile/json_user_otp_generate";
    public static final String VERIFICATION_OTP_URL = BASE_URL + "/index.php/User_mobile/json_user_otp_verification";
    public static final String REGISTER_URL = BASE_URL + "/index.php/User_mobile/json_user_registration";

    public static final String DATA_SUBMIT_URL_ITR =  BASE_URL + "/index.php/User_mobile/json_itr_profile_data_submit";
    public static final String AMOUNT_URL = BASE_URL + "/index.php/User_mobile/tax_return_type_master_list";
    public static final String ORDER_ID =  BASE_URL + "/index.php/RPay/generate_order";
    public static final String ORDER_CONFIRM =  BASE_URL + "/index.php/RPay/tax_payment_details_update";
    public static final String BANK_URL = BASE_URL + "/index.php/User_mobile/bank_master_list";
    public static final String IMAGE_URL = BASE_URL +"/index.php/User_mobile/tax_itr_image_data_submit";
    public static final String PDF_URL = BASE_URL +"/index.php/User_mobile/tax_itr_image_data_submit2";

    public static final String LIST_URL = BASE_URL + "/index.php/User_mobile/tax_pair_list_user_wise";

    public static final String TEXT_PAIR_DETAILS = BASE_URL + "/index.php/User_mobile/tax_pair_list_details_view";
    public static final String COMMUNICATION_URL_LIST = BASE_URL + "/index.php/User_mobile/tax_admin_user_comunication_list";
    public static final String COMMUNICATION_SUBMIT = BASE_URL + "/index.php/User_mobile/admin_user_comunication_data_submit";

}
