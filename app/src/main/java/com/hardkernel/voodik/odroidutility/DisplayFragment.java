package com.hardkernel.voodik.odroidutility;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.EditTextPreferenceDialogFragmentCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class DisplayFragment extends PreferenceFragmentCompat {

    private static final String DIALOG_FRAGMENT_TAG =
            "android.support.v7.preference.PreferenceFragment.DIALOG";

    protected static final String TAG = Constants.TAG;
    public MainActivity mAcct;
    String summary_touch_test_vid;
    String summary_touch_test_pid;

    public static DisplayFragment newInstance() {
        return new DisplayFragment();
    }

    @VisibleForTesting
    protected SharedPreferences.OnSharedPreferenceChangeListener mListener;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        mAcct = (MainActivity) getActivity();
        summary_touch_test_vid = getResources().getString(R.string.summary_touch_test_vid);
        summary_touch_test_pid = getResources().getString(R.string.summary_touch_test_pid);
//        SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
//        editor.apply();
        addPreferencesFromResource(R.xml.fragment_display);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
/*        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("pref_resolution")) {

                }

            }
        };*/
        Preference pref_res = findPreference(getString(R.string.pref_resolution));
        pref_res.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String value = (String) newValue;
                int[] res = Constants.RES_DEFMAP.get(value);
                if (res == null){
                    Log.e(TAG, "Invalid Display Resolution " + value);
                    value = "720p60hz";
                    res = Constants.RES_DEFMAP.get(value);
                }
                    mAcct.mBh.set("fb_x_res", Integer.toString(res[0]));
                    mAcct.mBh.set("fb_y_res", Integer.toString(res[1]));
                    mAcct.mBh.set("hdmi_phy_res", value);

                Log.v(TAG, "onPreferenceChange: pref_resolution " + value);
                return true;
            }
        });

        Preference pref_rot = findPreference(getString(R.string.pref_rotation));
        pref_rot.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String value = (String) newValue;
                mAcct.mBh.set("androidboot.rotation", value);
                Log.v(TAG, "onPreferenceChange: pref_rotation " + value);
                return true;
            }
        });

        Preference pref_edid = findPreference(getString(R.string.pref_edid));
        pref_edid.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                Boolean value = (Boolean) newValue;
                mAcct.mBh.set("edid", value ? "1" : "0");
                Log.v(TAG, "onPreferenceChange: pref_edid " + newValue);
                return true;
            }
        });

        Preference pref_hpd = findPreference(getString(R.string.pref_hpd));
        pref_hpd.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                Boolean value = (Boolean) newValue;
                mAcct.mBh.set("hpd", value ? "1" : "0");
                Log.v(TAG, "onPreferenceChange: pref_edid " + newValue);
                return true;
            }
        });

        Preference pref_dp = findPreference(getString(R.string.pref_dp));
        pref_dp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                Boolean value = (Boolean) newValue;
                mAcct.mBh.set("disable_dp", value ? "true" : "false");
                Log.v(TAG, "onPreferenceChange: pref_dp " + newValue);
                return true;
            }
        });

        Preference pref_vu7 = findPreference(getString(R.string.pref_vu7));
        pref_vu7.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                Boolean value = (Boolean) newValue;
                mAcct.mBh.set("disable_vu7", value ? "true" : "false");
                Log.v(TAG, "onPreferenceChange: pref_vu7 " + newValue);
                return true;
            }
        });

        Preference pref_touch_invert_x = findPreference(getString(R.string.pref_touch_invert_x));
        pref_touch_invert_x.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                Boolean value = (Boolean) newValue;
                mAcct.mBh.set("touch_invert_x", value ? "true" : "false");
                Log.v(TAG, "onPreferenceChange: pref_touch_invert_x " + newValue);
                return true;
            }
        });

        Preference pref_touch_invert_y = findPreference(getString(R.string.pref_touch_invert_y));
        pref_touch_invert_y.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                Boolean value = (Boolean) newValue;
                mAcct.mBh.set("touch_invert_y", value ? "true" : "false");
                Log.v(TAG, "onPreferenceChange: pref_touch_invert_y " + newValue);
                return true;
            }
        });

        Preference pref_test_mt_vid = findPreference(getString(R.string.pref_test_mt_vid));
        pref_test_mt_vid.setSummary(summary_touch_test_vid + " (" + mAcct.mBh.get("test_mt_vid").toUpperCase() + ")");
        pref_test_mt_vid.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String value = (String)newValue;
                String padded = new String(new char[4 - value.length()]).replace('\0', '0') + value;
                mAcct.mBh.set("test_mt_vid", padded);
                preference.setSummary(summary_touch_test_vid + " (" + padded.toUpperCase() + ")");
                Log.v(TAG, "onPreferenceChange: pref_test_mt_vid " + padded);
                return true;
            }
        });

        Preference pref_test_mt_pid = findPreference(getString(R.string.pref_test_mt_pid));
        pref_test_mt_pid.setSummary(summary_touch_test_pid + " (" + mAcct.mBh.get("test_mt_pid").toUpperCase() + ")");
        pref_test_mt_pid.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String value = (String) newValue;
                String padded = new String(new char[4 - value.length()]).replace('\0', '0') + value;
                mAcct.mBh.set("test_mt_pid", padded);
                preference.setSummary(summary_touch_test_pid + " (" + padded.toUpperCase() + ")");
                Log.v(TAG, "onPreferenceChange: pref_test_mt_pid " + padded);
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

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        // check if dialog is already showing
        if (getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
            return;
        }

        DialogFragment f = null;
        if (preference instanceof CustomEditTextPreference) {
            f = EditTextPreferenceDialog.newInstance(preference.getKey());
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
        if (f != null) {
            f.setTargetFragment(this, 0);
            f.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        }
    }

    public static class EditTextPreferenceDialog extends EditTextPreferenceDialogFragmentCompat {

        public static EditTextPreferenceDialog newInstance(String key) {
            final EditTextPreferenceDialog
                    fragment = new EditTextPreferenceDialog();
            final Bundle b = new Bundle(1);
            b.putString(ARG_KEY, key);
            fragment.setArguments(b);
            return fragment;
        }

        @Override
        protected void onBindDialogView(View view) {
            super.onBindDialogView(view);
            EditText edt = view.findViewById(android.R.id.edit);
            edt.setSelectAllOnFocus(true);
            InputFilter[] FilterArray = new InputFilter[2];
            FilterArray[0] = new HexadecimalInputFilter(true);
            FilterArray[1] = new InputFilter.LengthFilter(4);
                    edt.setFilters(FilterArray);
            edt.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

    }
}
