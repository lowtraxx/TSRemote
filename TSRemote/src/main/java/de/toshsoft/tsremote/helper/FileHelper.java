package de.toshsoft.tsremote.helper;

import java.io.File;
import java.io.IOException;

/**
 *
 * Copyright (c) 20013 Oliver Pahl
 * Distributed under the GNU GPL v2. For full terms see the file LICENSE.
 *
 */
public class FileHelper {
    private static FileHelper instance = null;
    private static String deviceSavePath = null;

    public static FileHelper getInstance() {
        if(instance == null) {
            instance = new FileHelper();
        }
        return instance;
    }

    protected FileHelper(){}

    public static void setDeviceSavePath(String deviceSavePath) {
        FileHelper.deviceSavePath = deviceSavePath;
        new File(deviceSavePath).mkdirs();
    }

    public static String getDeviceSavePath() {
        return deviceSavePath;
    }

    public static boolean deleteDirectoryWithName(String name) {
        if(deviceSavePath == null)
            return false;

        File path = new File(deviceSavePath, name);
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i = 0; i < files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectoryWithName(files[i].getName());
                }
                else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }

    public static boolean fixPermissionsForIr()
    {
        // IR Paths
        String[] irEnable = { "su", "-c", "chown system:sdcard_rw /sys/devices/platform/ir_remote_control/enable /dev/ttyHSL2"};
        String[] enablePermissions = { "su", "-c", "chmod 220 /sys/devices/platform/ir_remote_control/enable"};
        String[] devicePermissions = { "su", "-c", "chmod 660 /dev/ttyHSL2"};
        try {
            // Try to enable Infrared Devices
            Runtime.getRuntime().exec(irEnable);
            Runtime.getRuntime().exec(enablePermissions);
            Runtime.getRuntime().exec(devicePermissions);
        } catch (IOException e) {
            // Elevating failed
            return false;
        } finally {
            return true;
        }
    }
}
