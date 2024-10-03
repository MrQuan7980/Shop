package com.example.mysql.database;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    private final SharedPreferences sharedPreferences;

    public Preference(Context context)
    {
        sharedPreferences = context.getSharedPreferences(Constant.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void putBoolean(String key, Boolean value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public boolean getBoolean(String key)
    {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public String getString(String key)
    {
        return sharedPreferences.getString(key, null);
    }

    public void clear()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
    }

    public void clearShopId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constant.KEY_USER_SHOP_ID);  // Xóa shopId
        editor.apply();
    }

}
