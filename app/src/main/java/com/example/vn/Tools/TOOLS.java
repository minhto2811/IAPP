package com.example.vn.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.vn.Models.User;
import com.example.vn.R;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class TOOLS {

    public static final String doMainDevice = "http://192.168.22.101:3000";
    public static final String  USER= "USER";
    public static final String  DEFAULT_ADDRESS= "DEFAULT_ADDRESS";
    public static final String  TOKEN= "TOKEN";

    private static final Gson gson = new Gson();



    public static String convertPrice(int price) {
        DecimalFormat formatter = new DecimalFormat("###,###");
        return formatter.format(price)+" ₫";
    }

    public static void saveUser(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(user);
        editor.putString(USER, json);
        editor.apply();
    }

    public static void clearUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(USER,null);
        return gson.fromJson(string, User.class);
    }

    public static void saveDefaulAddress(Context context,String id_address){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DEFAULT_ADDRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEFAULT_ADDRESS, id_address);
        editor.apply();
    }



    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN,null);
    }

    public static void clearDefaulAddress(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DEFAULT_ADDRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static String getDefaulAddress(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DEFAULT_ADDRESS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(DEFAULT_ADDRESS,null);
    }



    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "\\d{10}";
        return !phoneNumber.matches(regex);
    }

    public static boolean isValidEmail(String email) {
        String emailPattern = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.isEmpty() || !email.matches(emailPattern);
    }


    public static String covertToString(String value) {
        try {
            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean checkAllCarts;


    public static Dialog createDialog(Context context) {
        Dialog dialog = new Dialog(context);
        @SuppressLint("InflateParams")
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.layout_watting, null);
        Glide.with(context).asGif().load(R.drawable.spin).into((ImageView) view.findViewById(R.id.imv_watting));
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }



}



