package com.example.testedoeintein.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Locale;

public class LocaleManager {
    private static final String TAG = "LocaleManager";
    private static final String DEFAULT_LANGUAGE="en";
    private static final String[] localeArray = {"en","pt"};
    private static final int SUPPORTED_LANGUAGES = 2;

    public static Context onAttach(Context context) {
        String lang = getPersistedData(context);
        return setLocale(context, lang);
    }

    public static Context onAttach(Context context, String defaultLanguage) {
        String lang = getPersistedData(context);
        return setLocale(context, lang);
    }

    public static String getLanguage(Context context) {
        return getPersistedData(context);
    }

    public static Context setLocale(Context context, @Nullable String language) {
        if(language==null) language=getPersistedData(context);
        else persist(context, language);
        //Log.d(TAG, "setLocale: language is "+language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }

        return updateResourcesLegacy(context, language);
    }

    private static String getPersistedData(Context context) {
        Log.d(TAG, "getPersistedData: called.");
        SharedPreferences pref = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        String localeCode = pref.getString("localeCode","default");
        Log.d(TAG, "getPersistedData() localeCode retrieved from file is: " + localeCode);
        if(localeCode==null){
            localeCode="default";
            Log.wtf(TAG, "getPersistedData: locale code is null, that should NEVER happen, what is going on?!");
        }

        if(localeCode.equals("default")){
            String temp = Locale.getDefault().getLanguage().toLowerCase();
            Log.d(TAG, "getPersistedData: system language is defined to " + temp);
            if(isSupportedLanguage(temp)) localeCode=temp;
            else localeCode=DEFAULT_LANGUAGE;
        }

        return localeCode;
    }

    @SuppressLint("ApplySharedPref")
    private static void persist(Context context, String language) {
        SharedPreferences pref = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("localeCode", language);
        editor.commit();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Log.d(TAG, "updateResources: called.");
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        Configuration config = new Configuration(configuration);
        config.setLocale(locale);

        return context.createConfigurationContext(config);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    private static boolean isSupportedLanguage(String localeCode){
        boolean isSupported = false;

        if(localeArray==null){
            Log.wtf(TAG, "isSupportedLanguage: localeArray is null!");
            return false;
        }
        try {
            for (int i = 0; localeArray[i] != null && i < SUPPORTED_LANGUAGES; i++) {
                if (localeArray[i].equals(localeCode.toLowerCase())) {
                    isSupported = true;
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){
            Log.e(TAG, "isSupportedLanguage: throwed expected exception", e);
        }
        return isSupported;
    }
}
