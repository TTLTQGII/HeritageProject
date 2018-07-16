package com.hrtgo.heritagego.heritagego.until;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.hrtgo.heritagego.heritagego.Model.modelLanguage;
import java.util.Locale;

public class Language extends AppCompatActivity {
    private static Locale mLocale;

    // Save language is setted by user
    public static void Save(String Language, Activity activity){
        String mLangPrefer = modelLanguage.getLanguagePrefs();
        SharedPreferences mPrefs = activity.getSharedPreferences("LanguagePrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mPrefs.edit();
//        Log.e("localeLanguage", mLocale.getLanguage());
        //mEditor.putString(mLangPrefer, Language);
        //mEditor.apply();
    }

    public static void Load(Activity activity){
        String mLangPrefer = modelLanguage.getLanguagePrefs();
        SharedPreferences mPrefs = activity.getSharedPreferences("LanguagePrefs", Activity.MODE_PRIVATE);
        String mLanguage = mPrefs.getString(mLangPrefer,"en");
        Log.e("Language", mLanguage);
        Change(mLanguage, activity);
    }

    public static void Change(String Language, Activity activity){
        if(Language.equalsIgnoreCase("")){
            return;
        }

        mLocale = new Locale(Language);
        Save(Language, activity);
        Locale.setDefault(mLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        if(Build.VERSION.SDK_INT >= 17){
            config.setLocale(mLocale);
        }
        else {
            config.locale = mLocale;
        }
        activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());
    }



}
