package com.nekolaboratory;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @author Yusuke Arakawa
 */

public class EmulatorDetector {

    private static final String[] QEMU_DRIVERS = {"goldfish"};
    private static final String[] GENY_FILES = {
            "/dev/socket/genyd",
            "/dev/socket/baseband_genyd"
    };
    private static final String[] PIPES = {
            "/dev/socket/qemud",
            "/dev/qemu_pipe"
    };
    private static final String[] X86_FILES = {
            "ueventd.android_x86.rc",
            "x86.prop",
            "ueventd.ttVM_x86.rc",
            "init.ttVM_x86.rc",
            "fstab.ttVM_x86",
            "fstab.vbox86",
            "init.vbox86.rc",
            "ueventd.vbox86.rc"
    };
    private static final String[] ANDY_FILES = {
            "fstab.andy",
            "ueventd.andy.rc"
    };
    private static final String[] NOX_FILES = {
            "fstab.nox",
            "init.nox.rc",
            "ueventd.nox.rc"
    };

    /**
     * Detects if app is currently running on emulator, or real device.
     *
     * @param context Apprication context
     * @return true for emulator, false for real devices
     */
    public static boolean isEmulator(Context context) {
        if (checkBasic()) return true;
        if (checkAdvanced()) return true;
        if (checkPackageName(context)) return true;

        return false;
    }

    private static boolean checkBasic() {
        int rating = 0;

        if (Build.PRODUCT.equals("sdk_x86_64") ||
                Build.PRODUCT.equals("sdk_google_phone_x86") ||
                Build.PRODUCT.equals("sdk_google_phone_x86_64") ||
                Build.PRODUCT.equals("sdk_google_phone_arm64") ||
                Build.PRODUCT.equals("vbox86p")) {
            rating++;
        }

        if (Build.MANUFACTURER.equals("unknown")) {
            rating++;
        }

        if (Build.BRAND.equals("generic") ||
                Build.BRAND.equalsIgnoreCase("android") ||
                Build.BRAND.equals("generic_arm64") ||
                Build.BRAND.equals("generic_x86") ||
                Build.BRAND.equals("generic_x86_64")) {
            rating++;
        }

        if (Build.DEVICE.equals("generic") ||
                Build.DEVICE.equals("generic_arm64") ||
                Build.DEVICE.equals("generic_x86") ||
                Build.DEVICE.equals("generic_x86_64") ||
                Build.DEVICE.equals("vbox86p")) {
            rating++;
        }

        if (Build.MODEL.equals("sdk") ||
                Build.MODEL.equals("Android SDK built for arm64") ||
                Build.MODEL.equals("Android SDK built for armv7") ||
                Build.MODEL.equals("Android SDK built for x86") ||
                Build.MODEL.equals("Android SDK built for x86_64")) {
            rating++;
        }

        if (Build.HARDWARE.equals("ranchu")) {
            rating++;
        }

        if (Build.FINGERPRINT.contains("sdk_google_phone_arm64") ||
                Build.FINGERPRINT.contains("sdk_google_phone_armv7")) {
            rating++;
        }

        boolean result = Build.FINGERPRINT.startsWith("generic")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.toLowerCase().contains("droid4x")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.HARDWARE.equals("goldfish")
                || Build.HARDWARE.equals("vbox86")
                || Build.PRODUCT.equals("sdk")
                || Build.PRODUCT.startsWith("google_sdk")
                || Build.PRODUCT.equals("sdk_x86")
                || Build.PRODUCT.equals("vbox86p")
                || Build.BOARD.toLowerCase().contains("nox")
                || Build.BOOTLOADER.toLowerCase().contains("nox")
                || Build.HARDWARE.toLowerCase().contains("nox")
                || Build.PRODUCT.toLowerCase().contains("nox")
                || Build.SERIAL.toLowerCase().contains("nox")
                || Build.HOST.contains("Droid4x-BuildStation")
                || Build.MANUFACTURER.startsWith("iToolsAVM")
                || Build.DEVICE.startsWith("iToolsAVM")
                || Build.MODEL.startsWith("iToolsAVM")
                || Build.BRAND.startsWith("generic")
                || Build.HARDWARE.startsWith("vbox86");

        if (result) return true;
        result |= Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic");
        if (result) return true;
        result |= "google_sdk".equals(Build.PRODUCT);
        if (result) return true;

        return rating >= 2;
    }

    private static boolean checkQEmuDrivers() {
        for (File drivers_file : new File[]{new File("/proc/tty/drivers"), new File("/proc/cpuinfo")}) {
            if (drivers_file.exists() && drivers_file.canRead()) {
                byte[] data = new byte[1024];
                try {
                    InputStream is = new FileInputStream(drivers_file);
                    is.read(data);
                    is.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                String driver_data = new String(data);
                for (String known_qemu_driver : QEMU_DRIVERS) {
                    if (driver_data.contains(known_qemu_driver)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean checkAdvanced() {
        boolean result = checkFiles(GENY_FILES)
                || checkFiles(ANDY_FILES)
                || checkFiles(NOX_FILES)
                || checkQEmuDrivers()
                || checkFiles(PIPES)
                || (checkFiles(X86_FILES));
        return result;
    }

    private static boolean checkFiles(String[] targets) {
        for (String pipe : targets) {
            File qemu_file = new File(pipe);
            if (qemu_file.exists()) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkPackageName(Context context) {
        final PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = packageManager.queryIntentActivities(intent, 0);
        for(ResolveInfo resolveInfo : availableActivities){
            if (resolveInfo.activityInfo.packageName.startsWith("com.bluestacks.")) {
                return true;
            }
        }
        List<ApplicationInfo> packages = packageManager
                .getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            String packageName = packageInfo.packageName;
            if (packageName.startsWith("com.vphone.")) {
                return true;
            } else if (packageName.startsWith("com.bignox.")) {
                return true;
            } else if (packageName.startsWith("me.haima.")) {
                return true;
            } else if (packageName.startsWith("com.bluestacks.")) {
                return true;
            } else if (packageName.startsWith("cn.itools.") && (Build.PRODUCT.startsWith("iToolsAVM"))) {
                return true;
            } else if (packageName.startsWith("com.kop.")) {
                return true;
            } else if (packageName.startsWith("com.kaopu.")) {
                return true;
            } else if (packageName.startsWith("com.microvirt.")) {
                return true;
            } else if (packageName.equals("com.google.android.launcher.layouts.genymotion")) {
                return true;
            }
        }
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos = manager.getRunningServices(30);
        for (ActivityManager.RunningServiceInfo serviceInfo : serviceInfos) {
            String serviceName = serviceInfo.service.getClassName();
            if (serviceName.startsWith("com.bluestacks.")) {
                return true;
            }
        }
        return false;
    }
}
