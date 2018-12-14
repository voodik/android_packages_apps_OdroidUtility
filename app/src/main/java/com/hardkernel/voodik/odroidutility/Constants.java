package com.hardkernel.voodik.odroidutility;

import android.os.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

final class Constants {

    /** Log TAG */
    final static String TAG = "MNG";

    /** boot.ini path */
    final static String PATH_BOOTINI = "/storage/0000-3333/boot.ini";

    /** bootcmd */
    final static String BOOTCMD = "setenv bootcmd \"movi read kernel 0 40008000;bootz 40008000\"";

    /** sysfs nodes */
    final static String GOVERNOR_NODE = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
    final static String SCALING_AVAILABLE_GOVERNORS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";
    final static String DRAM_SCALING_AVAILABLE_GOVERNORS = "/sys/devices/platform/exynos5-devfreq-mif/devfreq/exynos5-devfreq-mif/available_governors";
    final static String DRAM_SCALING_AVAILABLE_FREQUENCY = "/sys/devices/platform/exynos5-devfreq-mif/devfreq/exynos5-devfreq-mif/available_frequencies";
    final static String DRAM_GOVERNOR_NODE = "/sys/devices/platform/exynos5-devfreq-mif/devfreq/exynos5-devfreq-mif/governor";
    final static String DRAM_FREQUENCY_NODE = "/sys/devices/platform/exynos5-devfreq-mif/devfreq/exynos5-devfreq-mif/max_freq";

    /** misc properties */
    final static String BT_PROP = "persist.disable_bluetooth";
    final static String BT_SINK_PROP = "persist.service.bt.a2dp.sink";
    final static String GPS_PROP = "persist.disable_location";
    final static String FORCE_HDMI_AUDIO_PROP = "persist.hdmi.audioforce";
    final static String FORCE_HDMI_INPUT_PROP = "persist.hdmi.switch_tv_input";
    final static String SHUT_PROP = "persist.pwbtn.shutdown";
    final static String ADB_OVER_NET_PROP = "persist.adb.tcp.port";
    final static String WLAN_NO_PS_PROP = "persist.no_wlan_ps";
    final static String FBTFT_PROP = "persist.enable_fbtft";
    private final static String EXTDIR = Environment.getExternalStorageDirectory().toString();
    final static String UPZIPFILE = "file://" + EXTDIR + "/update.zip";
    final static String UPSUMFILE = "file://" + EXTDIR + "/update.zip.md5sum";

    /** ethernet constants */
    final static int DHCP = 0;
    final static int STATIC_IP = 1;
    final static int NOPROXY = 0;
    final static int MANPROXY = 1;

    /** default boot.ini settings */
    final static Map<String, String> DEFMAP = new TreeMap<String, String>(){{
        put("hdmi_phy_res", "720p60hz");
        put("fb_x_res", "1280");
        put("fb_y_res", "720");
        put("androidboot.rotation", "0");
        put("edid", "0");
        put("hpd", "1");
        put("led_blink", "1");
        put("disable_dp", "false");
        put("disable_vu7", "false");
        put("touch_invert_x", "false");
        put("touch_invert_y", "false");
        put("test_mt_vid", "0000");
        put("test_mt_pid", "0000");
    }};

    /** resolutions map */
    final static Map<String, int[]> RES_DEFMAP = new HashMap<String, int[]>() {{
        put("480x320p60hz", new int[]{480, 320});
        put("480p60hz", new int[]{640, 480});
        put("480p59.94hz", new int[]{720, 480});
        put("576p50hz", new int[]{720, 576});
        put("480x800p60hz", new int[]{480, 800});
        put("800x480p60hz", new int[]{800, 480});
        put("848x480p60hz", new int[]{848, 480});
        put("600p60hz", new int[]{800, 600});
        put("1024x600p60hz", new int[]{1024, 600});
        put("768p60hz", new int[]{1024, 768});
        put("720p50hz", new int[]{1280, 720});
        put("720p60hz", new int[]{1280, 720});
        put("1280x768p60hz", new int[]{1280, 768});
        put("1152x864p75hz", new int[]{1152, 864});
        put("800p59hz", new int[]{1280, 800});
        put("960p60hz", new int[]{1280, 960});
        put("900p60hz", new int[]{1440, 900});
        put("1024p60hz", new int[]{1280, 1024});
        put("1400x1050p60hz", new int[]{1400, 1050});
        put("1360x768p60hz", new int[]{1360, 768});
        put("1600x900p60hz", new int[]{1600, 900});
        put("1600x1200p60hz", new int[]{1600, 1200});
        put("1920x800p60hz", new int[]{1920, 800});
        put("1792x1344p60hz", new int[]{1792, 1344});
        put("1080i50hz", new int[]{1920, 1080});
        put("1080i60hz", new int[]{1920, 1080});
        put("1080p23.976hz", new int[]{1920, 1080});
        put("1080p24hz", new int[]{1920, 1080});
        put("1080p30hz", new int[]{1920, 1080});
        put("1080p50hz", new int[]{1920, 1080});
        put("1080p60hz", new int[]{1920, 1080});
        put("1920x1200p60hz", new int[]{1920, 1200});
        put("1200x1920p60hz", new int[]{1200, 1920});
    }};

}
