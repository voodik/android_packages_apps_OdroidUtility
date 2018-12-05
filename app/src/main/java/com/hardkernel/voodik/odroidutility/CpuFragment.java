package com.hardkernel.voodik.odroidutility;


import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CpuFragment extends PreferenceFragmentCompat {

    protected static final String TAG = Constants.TAG;
    public MainActivity mAcct;

    public static CpuFragment newInstance() {
        return new CpuFragment();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        mAcct = (MainActivity) getActivity();
        addPreferencesFromResource(R.xml.fragment_cpu);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListPreference cpu_gov_prefs = (ListPreference)findPreference("pref_cpu_gov");
        ListPreference dram_gov_prefs = (ListPreference)findPreference("pref_dram_gov");
        ListPreference dram_freq_prefs = (ListPreference)findPreference("pref_dram_freq");
        String[] cpu_gov_entries = getFromNode(Constants.SCALING_AVAILABLE_GOVERNORS).split(" ");
        String[] dram_gov_entries = getFromNode(Constants.DRAM_SCALING_AVAILABLE_GOVERNORS).split(" ");
        String[] dram_freq_entries = getFromNode(Constants.DRAM_SCALING_AVAILABLE_FREQUENCY).split(" ");
        cpu_gov_prefs.setEntries(cpu_gov_entries);
        cpu_gov_prefs.setEntryValues(cpu_gov_entries);
        dram_gov_prefs.setEntries(dram_gov_entries);
        dram_gov_prefs.setEntryValues(dram_gov_entries);
        dram_freq_prefs.setEntries(dram_freq_entries);
        dram_freq_prefs.setEntryValues(dram_freq_entries);

        cpu_gov_prefs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String value = (String) newValue;
                setValueToNode(Constants.GOVERNOR_NODE, value);
                Log.v(TAG, "onPreferenceChange: pref_cpu_gov " + value);
                return true;
            }
        });

        dram_gov_prefs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String value = (String) newValue;
                setValueToNode(Constants.DRAM_GOVERNOR_NODE, value);
                Log.v(TAG, "onPreferenceChange: pref_dram_freq " + value);
                return true;
            }
        });

        dram_freq_prefs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String value = (String) newValue;
                setValueToNode(Constants.DRAM_FREQUENCY_NODE, value);
                Log.v(TAG, "onPreferenceChange: pref_dram_gov " + value);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected String getFromNode(String node) {
        String value = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(node));
            value = bufferedReader.readLine();
            value = value.trim();
            value = value.replaceAll("\\s{2,}", " ");
            bufferedReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return value;
    }

    public static void setValueToNode(String value, String node) {
        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter(node));
            out.write(value);
            out.newLine();
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
