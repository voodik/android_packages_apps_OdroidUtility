package com.hardkernel.voodik.odroidutility;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.System.lineSeparator;

public class BootIniHelper {
    private static final String TAG = Constants.TAG + " BootIniHelper";
    private Context context;
    private String bootinipath;
    private Map<String, String> mMap = new TreeMap<>();

    BootIniHelper(Context c, String bootinipath) {
        this.context = c;
        this.bootinipath = bootinipath;
        this.readini();
    }

    private void readini() {

        mMap.clear();

            File file = new File(bootinipath);
            if (!file.exists()) {
                Log.i(TAG, "readini: boot.ini does not exist, creting from template.");
                writeini_internal(Constants.DEFMAP);
                mMap = Constants.DEFMAP;
            } else {
                try {
                    FileInputStream fis = new FileInputStream(bootinipath);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();

                        if (line.isEmpty() || line.startsWith("#")) continue;

                        if (line.contains("bootcmd") || line.contains("bootargs")) continue;

                        if (line.contains("ODROIDXU-UBOOT-CONFIG")) continue;

                        if (line.startsWith("setenv")) {
                            String result[] = line.split("\\s+", 3);
                            String inikey = result[1].replaceAll("^['\"]*", "")
                                    .replaceAll("['\"]*$", "");
                            String inivalue = result[2].replaceAll("^['\"]*", "")
                                    .replaceAll("['\"]*$", "");

                            if (inikey.equals("rotation")) inikey = "androidboot.rotation";
                            mMap.put(inikey, inivalue);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

    }

    ArrayList<String> writeini() {
        ArrayList<String> output = writeini_internal(mMap);
        readini();
        return output;
    }

    private ArrayList<String> writeini_internal(Map<String, String> myMap){
        ArrayList<String> botiniText = new ArrayList<>();

        File file = new File(bootinipath);
        File bakfile = new File(bootinipath + ".bak");
        if (file.exists()) {

            if (bakfile.exists()) bakfile.delete();

            file.renameTo(bakfile);
        }

        InputStream fis = context.getResources().openRawResource(
                context.getResources().getIdentifier("bootiniheader",
                        "raw", context.getPackageName()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

//add header
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                botiniText.add(line);
            }
            botiniText.add(lineSeparator());
            reader.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//add values
        for (Map.Entry<String, String> entry : myMap.entrySet()) {
            String line;
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equals("disable_dp") || key.equals("edid") || key.equals("fb_x_res") || key.equals("hpd")) {
                line = lineSeparator() + "setenv " + key + " \"" + value + "\"";
            } else {
                line = "setenv " + key + " \"" + value + "\"";
            }
            botiniText.add(line);
        }

//add bootcmd
        botiniText.add(lineSeparator() + Constants.BOOTCMD);

//add bootargs
        botiniText.add(lineSeparator() + "setenv bootargs \"" + genbootargs() + "\"");

//add boot
        botiniText.add(lineSeparator() + "boot");

        FileWriter writer;
        try {
            writer = new FileWriter(bootinipath);
            for (String str : botiniText) {
                writer.write(str + lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return botiniText;
    }

    private String genbootargs() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : mMap.entrySet()) {
            sb.append(entry.getKey()).append("=${").append(entry.getKey()).append("} ");
        }
        return sb.toString().trim();
    }


    void syncprefs(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString("pref_resolution", this.get("hdmi_phy_res"));
        editor.putString("pref_rotation", this.get("androidboot.rotation"));
        editor.putBoolean("pref_edid", this.get("edid").equals("1"));
        editor.putBoolean("pref_hpd", this.get("hpd").equals("1"));
        editor.putBoolean("pref_dp", this.get("disable_dp").equals("true"));
        editor.putBoolean("pref_vu7", this.get("disable_vu7").equals("true"));
        editor.putBoolean("pref_touch_invert_x", this.get("touch_invert_x").equals("true"));
        editor.putBoolean("pref_touch_invert_y", this.get("touch_invert_y").equals("true"));
        editor.putString("pref_test_mt_vid", this.get("test_mt_vid"));
        editor.putString("pref_test_mt_pid", this.get("test_mt_pid"));

        editor.commit();
    }

    void set(String key, String value) {
        mMap.put(key, value);
    }

    public String get(String key) {
        return (mMap.get(key) != null)? mMap.get(key):Constants.DEFMAP.get(key);
    }

    Map<String, String> getmap() {
        return mMap;
    }


}
