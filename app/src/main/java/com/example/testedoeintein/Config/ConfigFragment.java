package com.example.testedoeintein.Config;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.testedoeintein.Locale.LocaleManager;
import com.example.testedoeintein.MainActivity;
import com.example.testedoeintein.R;

public class ConfigFragment extends PreferenceFragmentCompat {
    private static final String TAG = "ConfigFragment";
    private Context context;
    private SharedPreferences pref;
    private ListPreference list;

    public ConfigFragment() { }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s);
        pref = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        list = (ListPreference) findPreference("item_linguagem");
        if(list == null) Log.wtf(TAG, "onCreatePreferences: LIST IS NULL MY FRIEND.");
        list.setValue(pref.getString("localeCode","default"));
        list.setSummary(list.getEntry());
        Log.d(TAG, String.format("onCreatePreferences: list.getEntry() = %s, list.getValue() = %s",list.getEntry(),list.getValue()));

        list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                list.setValue(o.toString());
                Log.d(TAG, "onPreferenceChange: o.toString() = " + o.toString());
                list.setSummary(list.getEntry());
                LocaleManager.setLocale(context, list.getValue().toLowerCase());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.aviso);
                builder.setMessage(R.string.mensagem_ao_trocar_lingua);
                builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Saving data to DB & wait 100ms to give time to save our settings
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent mStartActivity = new Intent(context, MainActivity.class);
                                int mPendingIntentId = 123456;
                                PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                                AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                if(mgr!=null) mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                                System.exit(0);
                            }
                        }, 100);
                    }
                });
                builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
                builder.show();
                return false;
            }
        });
    }

}
