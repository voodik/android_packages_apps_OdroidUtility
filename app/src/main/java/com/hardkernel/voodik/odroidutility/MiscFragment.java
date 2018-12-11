package com.hardkernel.voodik.odroidutility;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemProperties;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;

public class MiscFragment extends PreferenceFragmentCompat {

    protected static final String TAG = Constants.TAG;

    protected SharedPreferences.OnSharedPreferenceChangeListener mListener;
    SwitchPreferenceCompat pref_bt;
    SwitchPreferenceCompat pref_bt_sink;
    SwitchPreferenceCompat pref_gps;
    SwitchPreferenceCompat pref_shut;
    SwitchPreferenceCompat pref_hdmi_audio;
    SwitchPreferenceCompat pref_hdmi_cec;
    SwitchPreferenceCompat pref_adb;
    SwitchPreferenceCompat pref_wlan_ps;
    SwitchPreferenceCompat pref_fbtft;

    public static MiscFragment newInstance() {
        return new MiscFragment();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.fragment_misc);
        pref_bt = (SwitchPreferenceCompat) findPreference(getString(R.string.pref_enable_bluetooth));
        pref_bt_sink = (SwitchPreferenceCompat) findPreference(getString(R.string.pref_enable_bluetooth_sink));
        pref_gps = (SwitchPreferenceCompat) findPreference(getString(R.string.pref_enable_gps));
        pref_shut = (SwitchPreferenceCompat) findPreference(getString(R.string.pref_force_shutdown_without_dialog));
        pref_hdmi_audio = (SwitchPreferenceCompat) findPreference(getString(R.string.pref_force_hdmi_audio_output));
        pref_hdmi_cec = (SwitchPreferenceCompat) findPreference(getString(R.string.pref_hdmi_cec_switch_tv_input_after_bootup));
        pref_adb = (SwitchPreferenceCompat) findPreference(getString(R.string.pref_enable_adb_over_network_at_boot));
        pref_wlan_ps = (SwitchPreferenceCompat) findPreference(getString(R.string.pref_disable_wlan_ps));
        pref_fbtft = (SwitchPreferenceCompat) findPreference(getString(R.string.pref_enable_fbtft));

        pref_bt.setChecked(!SystemProperties.getBoolean(Constants.BT_PROP, true));
        pref_bt_sink.setChecked(SystemProperties.getBoolean(Constants.BT_SINK_PROP, true));
        pref_gps.setChecked(!SystemProperties.getBoolean(Constants.GPS_PROP, true));
        pref_shut.setChecked(SystemProperties.getBoolean(Constants.SHUT_PROP, false));
        pref_hdmi_audio.setChecked(SystemProperties.getBoolean(Constants.FORCE_HDMI_AUDIO_PROP, false));
        pref_hdmi_cec.setChecked(SystemProperties.getBoolean(Constants.FORCE_HDMI_INPUT_PROP, true));
        pref_adb.setChecked((SystemProperties.getInt(Constants.ADB_OVER_NET_PROP, 0) > 0));
        pref_wlan_ps.setChecked(SystemProperties.getBoolean(Constants.WLAN_NO_PS_PROP, false));
        pref_fbtft.setChecked(SystemProperties.getBoolean(Constants.FBTFT_PROP, false));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        pref_bt.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean value = (Boolean) newValue;
                SystemProperties.set(Constants.BT_PROP, value ? "false" : "true");
                Log.v(TAG, "onPreferenceChange: pref_bt " + newValue);
                return true;
            }
        });

        pref_bt_sink.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean value = (Boolean) newValue;
                SystemProperties.set(Constants.BT_SINK_PROP, value ? "true" : "false");
                Log.v(TAG, "onPreferenceChange: pref_bt_sink " + newValue);
                return true;
            }
        });

        pref_gps.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean value = (Boolean) newValue;
                SystemProperties.set(Constants.GPS_PROP, value ? "false" : "true");
                Log.v(TAG, "onPreferenceChange: pref_gps " + newValue);
                return true;
            }
        });

        pref_shut.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean value = (Boolean) newValue;
                SystemProperties.set(Constants.SHUT_PROP, value ? "true" : "false");
                Log.v(TAG, "onPreferenceChange: pref_shut " + newValue);
                return true;
            }
        });

        pref_hdmi_audio.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean value = (Boolean) newValue;
                SystemProperties.set(Constants.FORCE_HDMI_AUDIO_PROP, value ? "true" : "false");
                Log.v(TAG, "onPreferenceChange: pref_hdmi_audio " + newValue);
                return true;
            }
        });

        pref_hdmi_cec.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean value = (Boolean) newValue;
                SystemProperties.set(Constants.FORCE_HDMI_INPUT_PROP, value ? "true" : "false");
                Log.v(TAG, "onPreferenceChange: pref_hdmi_cec " + newValue);
                return true;
            }
        });

        pref_adb.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean value = (Boolean) newValue;
                SystemProperties.set(Constants.ADB_OVER_NET_PROP, value ? "5555" : "0");
                Log.v(TAG, "onPreferenceChange: pref_adb " + newValue);
                return true;
            }
        });

        pref_wlan_ps.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean value = (Boolean) newValue;
                SystemProperties.set(Constants.WLAN_NO_PS_PROP, value ? "true" : "false");
                Log.v(TAG, "onPreferenceChange: pref_wlan_ps " + newValue);
                return true;
            }
        });

        pref_fbtft.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean value = (Boolean) newValue;
                SystemProperties.set(Constants.FBTFT_PROP, value ? "1" : "0");
                Log.v(TAG, "onPreferenceChange: pref_fbtft " + newValue);
                return true;
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "DisplayFragment onResume");
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListener);
    }

    @Override
    public void onPause() {
        Log.v(TAG, "DisplayFragment onPause");
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListener);
        super.onPause();
    }
}
