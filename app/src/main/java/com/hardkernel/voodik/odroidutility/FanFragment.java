package com.hardkernel.voodik.odroidutility;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

public class FanFragment extends PreferenceFragmentCompat {

    public static FanFragment newInstance() {
        return new FanFragment();
    }

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private AlertDialog.Builder mSpeedsDialog;
    private SwitchPreferenceCompat fanmodePref;
    private Preference fanspeedPref;
    private Preference fanpwmdutyPref;
    private SwitchPreferenceCompat fanpwmenablePref;
    private Preference fantemplevelsPref;
    private int mfanSpeed1;
    private int mfanSpeed2;
    private int mfanSpeed3;
    private int mfanSpeed4;


    @VisibleForTesting
    protected SharedPreferences.OnSharedPreferenceChangeListener mListener;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.fragment_fan);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = getActivity().getSharedPreferences("fan_prefs", Context.MODE_PRIVATE);

        fanmodePref = (SwitchPreferenceCompat)findPreference("pref_fan_1");
        fanspeedPref = findPreference("pref_fan_2");
        fanpwmdutyPref = findPreference("pref_fan_3");
        fanpwmenablePref = (SwitchPreferenceCompat)findPreference("pref_fan_4");
        fantemplevelsPref = findPreference("pref_fan_5");

        mfanSpeed1 = preferences.getInt("fan_speed_1",1);
        mfanSpeed2 = preferences.getInt("fan_speed_2",51);
        mfanSpeed3 = preferences.getInt("fan_speed_3",71);
        mfanSpeed4 = preferences.getInt("fan_speed_4",91);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        mSpeedsDialog = new AlertDialog.Builder(getActivity());
        mSpeedsDialog.setTitle("Fan speed");
        View dialogView = inflater.inflate(R.layout.dialog_fan_speeds, null);

        final NumberPicker pref_fan_speed1 = (NumberPicker) dialogView.findViewById(R.id.fan_speed1);
        final NumberPicker pref_fan_speed2 = (NumberPicker) dialogView.findViewById(R.id.fan_speed2);
        final NumberPicker pref_fan_speed3 = (NumberPicker) dialogView.findViewById(R.id.fan_speed3);
        final NumberPicker pref_fan_speed4 = (NumberPicker) dialogView.findViewById(R.id.fan_speed4);

        pref_fan_speed1.setMinValue(1);
        pref_fan_speed1.setMaxValue(100);
        pref_fan_speed1.setValue(mfanSpeed1);
        pref_fan_speed2.setMinValue(1);
        pref_fan_speed2.setMaxValue(100);
        pref_fan_speed2.setValue(mfanSpeed2);
        pref_fan_speed3.setMinValue(1);
        pref_fan_speed3.setMaxValue(100);
        pref_fan_speed3.setValue(mfanSpeed3);
        pref_fan_speed4.setMinValue(1);
        pref_fan_speed4.setMaxValue(100);
        pref_fan_speed4.setValue(mfanSpeed4);

        fanmodePref.setChecked(true);
        fanmodePref.setSummary("Enabled");

        fanspeedPref.setSummary("1%  51%  71%  91%");

        fanpwmdutyPref.setSummary("2");

        fanpwmenablePref.setChecked(true);
        fanpwmenablePref.setSummary("Enabled");

        fantemplevelsPref.setSummary("57°C  63°C  68°C");

        mSpeedsDialog.setView(dialogView)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        editor = preferences.edit();
                        // FIRE ZE MISSILES!
                        mfanSpeed1 = pref_fan_speed1.getValue();
                        mfanSpeed2 = pref_fan_speed2.getValue();
                        mfanSpeed3 = pref_fan_speed3.getValue();
                        mfanSpeed4 = pref_fan_speed4.getValue();
                        editor.putInt("fan_speed_1", mfanSpeed1);
                        editor.putInt("fan_speed_2", mfanSpeed2);
                        editor.putInt("fan_speed_3", mfanSpeed3);
                        editor.putInt("fan_speed_4", mfanSpeed4);
                        editor.commit();
                        Log.i("MNG","ok " + mfanSpeed1 + " " + mfanSpeed2 + " " + mfanSpeed3 + " " + mfanSpeed4);
                        String result = String.format("%s%%  %s%%  %s%%  %s%%",mfanSpeed1,mfanSpeed2,mfanSpeed3,mfanSpeed4);
//                        fanspeedPref.setSummary(speed1 + "%/" + speed2 + "%/" + speed3 + "%/" + speed4 + "%");
                        fanspeedPref.setSummary(result);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        pref_fan_speed1.setValue(mfanSpeed1);
                        pref_fan_speed2.setValue(mfanSpeed2);
                        pref_fan_speed3.setValue(mfanSpeed3);
                        pref_fan_speed4.setValue(mfanSpeed4);
                        Log.i("MNG","cancel " + mfanSpeed1 + " " + mfanSpeed2 + " " + mfanSpeed3 + " " + mfanSpeed4);
                        String result = String.format("%s%%  %s%%  %s%%  %s%%",mfanSpeed1,mfanSpeed2,mfanSpeed3,mfanSpeed4);
                        fanspeedPref.setSummary(result);
                    }
                });

        pref_fan_speed2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                pref_fan_speed3.setMinValue(newVal);
                pref_fan_speed3.setValue(newVal);
                Log.i("MNG","newVal "+newVal);
            }
        });

        final AlertDialog alertd = mSpeedsDialog.create();

        fanspeedPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                alertd.show();
                return true;
            }
        });

    }

}
