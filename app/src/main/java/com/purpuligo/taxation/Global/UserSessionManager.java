package com.purpuligo.taxation.Global;

import android.content.Context;
import android.content.SharedPreferences;

import com.purpuligo.taxation.Constants.Constants;

public class UserSessionManager {
     SharedPreferences sharedPreferences;
     SharedPreferences.Editor editor;

    public UserSessionManager(Context context){
        sharedPreferences = context.getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    //login
    public void setLogin(boolean login){
        editor.putBoolean("KEY_LOGIN",login);
        editor.commit();
    }

    public boolean getLogin(){
        return sharedPreferences.getBoolean("KEY_LOGIN",false);
    }


    //phone number
    public void setPhoneNumber(String phoneNumber){ editor.putString("KEY_PHONE_NUMBER",phoneNumber);
        editor.commit(); }
    public String getPhoneNumber(){ return sharedPreferences.getString("KEY_PHONE_NUMBER",""); }

    //user id
    public void setUserID(String userId){ editor.putString("KEY_USER_ID",userId);
        editor.commit(); }
    public String getUserID(){ return sharedPreferences.getString("KEY_USER_ID","");}

    //name
    public void setUserName(String userName){ editor.putString("KEY_USER_NAME",userName);
        editor.commit(); }
    public String getUserName(){ return sharedPreferences.getString("KEY_USER_NAME","");}

    //email id
    public void setEmailId(String emailId){ editor.putString("KEY_USER_EMAIL_ID",emailId);
        editor.commit(); }
    public String getEmailId(){ return sharedPreferences.getString("KEY_USER_EMAIL_ID","");}

//    public void createUserSession(String customer_email,String phone_number,String customer_id,String customer_name){
//        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,customer_email);
//        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_PHONE_NUMBER,phone_number);
//        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_ID,customer_id);
//        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_NAME,customer_name);
//        editor.commit();
//    }
//    public void logoutUser(){
//        editor.clear();
//        editor.commit();
//    }

}
