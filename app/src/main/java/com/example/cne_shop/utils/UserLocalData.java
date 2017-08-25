package com.example.cne_shop.utils;

import android.content.Context;
import android.text.TextUtils;

import com.example.cne_shop.bean.User;
import com.example.cne_shop.contents.Contents;

/**
 * Created by 博 on 2017/7/23.
 */

public class UserLocalData {

    public static void putUser(Context context , User user){

        String user_json = JsonUtil.toJSON(user) ;
        PreferenceUtil.putString(context , Contents.USER_JSON , user_json);
    }

    public static void putToken(Context context , String token){

        PreferenceUtil.putString(context , Contents.TOKEN , token);
    }

    public static User getUser(Context context){
        String user_json = PreferenceUtil.getString(context , Contents.USER_JSON , null);
        if (!TextUtils.isEmpty(user_json)){

            return JsonUtil.fromJson(user_json , User.class) ;
        }
        return null ;
    }

    public static String getToken(Context context){
        String token = PreferenceUtil.getString(context , Contents.TOKEN , null);
        if (!TextUtils.isEmpty(token)){

            return JsonUtil.fromJson(token , String.class) ;
        }
        return null ;
    }

    public static void clearUser(Context context){
        PreferenceUtil.putString(context , Contents.USER_JSON , "");
    }

    public static void clearToken(Context context){
        PreferenceUtil.putString(context , Contents.TOKEN , "");
    }

}
