package com.shivamprajapati.waterwareconnect;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    public static String fileName="MyFile";

    public static void saveSharedSettingsIsLoggedIn(Context context,String settingName,String settingValue){
        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(settingName,settingValue).commit();
    }

    public static void saveSharedSettingsCompletedOrders(Context context,String settingName,String settingValue){
        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(settingName,settingValue).commit();
    }


    public static void saveSharedSettingsShopName(Context context,String settingName,String settingValue){
        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(settingName,settingValue).commit();
    }
    public static String readSharedSettingsIsLoggedIn(Context context,String settingName,String defaultVal){

        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sharedPreferences.getString(settingName,defaultVal);

    }
    public static String readSharedSettingsShopName(Context context,String settingName,String defaultVal){

        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sharedPreferences.getString(settingName,defaultVal);

    }
    public static String readSharedSettingsCompletedOrders(Context context,String settingName,String defaultVal){

        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sharedPreferences.getString(settingName,defaultVal);

    }


}
