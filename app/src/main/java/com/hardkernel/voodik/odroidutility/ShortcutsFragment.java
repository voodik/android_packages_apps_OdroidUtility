package com.hardkernel.voodik.odroidutility;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ShortcutsFragment extends PreferenceFragmentCompat {
    protected static final String TAG = Constants.TAG;

    private List<String> mShortcutApp = new ArrayList<>();
    private List<Preference> mShortcutAppPref = new ArrayList<>();

    private static final int RESULT_SHKT_APP1 = 1001;
    private static final int RESULT_SHKT_APP2 = 1002;
    private static final int RESULT_SHKT_APP3 = 1003;
    private static final int RESULT_SHKT_APP4 = 1004;

    SharedPreferences preferences;
    ContentResolver cr;
    SharedPreferences.Editor editor;

    public static ShortcutsFragment newInstance() {
        return new ShortcutsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.fragment_shortcuts);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = getActivity().getSharedPreferences("Shortcuts", Context.MODE_PRIVATE);
        cr = getActivity().getContentResolver();

        String ShortcutApp;
        for (int i = 0; i < 4; i++) {
            String key = "pref_hk_" + Integer.toString(i+1);
            mShortcutAppPref.add(i, findPreference(key));
//            Settings.System.putString(cr,"key","xuynya");
            ShortcutApp = preferences.getString(key, "");
            Log.v(TAG, "onCreatePreferences: " + key + " " + ShortcutApp);
            mShortcutApp.add(i, ShortcutApp);
        }
        updateDebuggerOptions();
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mShortcutAppPref.get(0)) {
            Intent intent = new Intent(getActivity(), AppPicker.class);
            intent.putExtra(AppPicker.EXTRA_DEBUGGABLE, false);
            startActivityForResult(intent, RESULT_SHKT_APP1);
        } else if (preference == mShortcutAppPref.get(1)) {
            Intent intent = new Intent(getActivity(), AppPicker.class);
            intent.putExtra(AppPicker.EXTRA_DEBUGGABLE, false);
            startActivityForResult(intent, RESULT_SHKT_APP2);
        } else if (preference == mShortcutAppPref.get(2)) {
            Intent intent = new Intent(getActivity(), AppPicker.class);
            intent.putExtra(AppPicker.EXTRA_DEBUGGABLE, false);
            startActivityForResult(intent, RESULT_SHKT_APP3);
        } else if (preference == mShortcutAppPref.get(3)) {
            Intent intent = new Intent(getActivity(), AppPicker.class);
            intent.putExtra(AppPicker.EXTRA_DEBUGGABLE, false);
            startActivityForResult(intent, RESULT_SHKT_APP4);
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_SHKT_APP1) {
            if (resultCode == Activity.RESULT_OK) {
                mShortcutApp.set(0, data.getAction());
            }
        } else if (requestCode == RESULT_SHKT_APP2) {
            if (resultCode == Activity.RESULT_OK) {
                mShortcutApp.set(1, data.getAction());
            }
        } else if (requestCode == RESULT_SHKT_APP3) {
            if (resultCode == Activity.RESULT_OK) {
                mShortcutApp.set(2, data.getAction());
            }
        } else if (requestCode == RESULT_SHKT_APP4) {
            if (resultCode == Activity.RESULT_OK) {
                mShortcutApp.set(3, data.getAction());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        updateDebuggerOptions();
    }

    private void updateDebuggerOptions() {
        editor = preferences.edit();
        String ShortcutApp;
        Preference ShortcutAppPref;

        for (int i = 0; i < 4; i++) {
            String key = "pref_hk_" + Integer.toString(i+1);
            ShortcutApp = mShortcutApp.get(i);
            ShortcutAppPref = mShortcutAppPref.get(i);
            editor.putString(key, ShortcutApp);

            if (ShortcutApp != null && ShortcutApp.length() > 0) {
                String label;
                try {
                    ApplicationInfo ai = getActivity().getPackageManager().getApplicationInfo(ShortcutApp,
                            PackageManager.GET_META_DATA);
                    CharSequence lab = getActivity().getPackageManager().getApplicationLabel(ai);
                    label = lab != null ? lab.toString() : ShortcutApp;
                } catch (PackageManager.NameNotFoundException e) {
                    label = ShortcutApp;
                }
                ShortcutAppPref.setSummary(getResources().getString(R.string.shortcut_app_set, label));

            } else {
                ShortcutAppPref.setSummary(getResources().getString(R.string.shortcut_app_not_set));

            }
            editor.commit();
        }
    }
}
