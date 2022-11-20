package com.project.mymart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public Context context;
    public SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    private static final String NAME = "NAME";
    private static final String EMAIL = "EMAIL";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("LOGIN", PRIVATE_MODE);
        editor=sharedPreferences.edit();
    }

    public void createsession(String name,String email){
        editor.putBoolean("LOGIN",true);
        editor.putString("NAME",name);
        editor.putString("EMAIL",email);
        editor.apply();
    }
    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }

    public void checklogin(){
        if (!this.isLogin()){
            Intent intent=new Intent(context,Login_activity.class);
            context.startActivity(intent);
            ((MainActivity2) context).finish();
        }
    }

    public HashMap<String, String> getuserDetail(){

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(NAME,sharedPreferences.getString(NAME,null));
        hashMap.put(EMAIL,sharedPreferences.getString(EMAIL,null));

        return hashMap;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent intent=new Intent(context,Login_activity.class);
        context.startActivity(intent);
        ((MainActivity2) context).finish();
    }
}
