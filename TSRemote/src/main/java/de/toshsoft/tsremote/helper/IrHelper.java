package de.toshsoft.tsremote.helper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 *
 * Copyright (c) 20013 Oliver Pahl
 * Distributed under the GNU GPL v2. For full terms see the file LICENSE.
 *
 */
public class IrHelper {
    private static IrHelper instance = null;
    private static volatile boolean isIrStarted = false;
    private static volatile Vector<String> commandVector = new Vector<String>();
    private static Thread IrSenderThread;

    // Load the IR library
    static {
        System.loadLibrary("jni_sonyopenir_tsremote");
    }

    // Native Library Functions
    private native int startIR();
    private native int stopIR();
    private native int learnKey(String filename);
    private native int sendKey(String filename);

    // Protect from callers
    protected IrHelper() {}

    public static IrHelper getInstance() {
        if(instance == null) {
            instance = new IrHelper();
        }
        return instance;
    }

    public int startIrNative()
    {
        if(!isIrStarted)
        {
            isIrStarted = true;

            IrSenderThread = new Thread(new CommandSender());
            IrSenderThread.start();

            return startIR();
        }

        return -1;
    }

    public int stopIrNative()
    {
        if(isIrStarted)
        {
            isIrStarted = false;
            return stopIR();
        }

        return -1;
    }

    public void resetIR() {

        stopIrNative();
        startIrNative();
    }

    public int learnKeyNative(final String remoteName, final String keyName, final IIrLearnObserver observer, final View v)
    {
        String deviceSavePath = FileHelper.getDeviceSavePath();
        if(deviceSavePath == null)
            return -1;

        if(!isIrStarted)
            startIrNative();

        String remoteDataPath = deviceSavePath + remoteName + "/";
        new File(remoteDataPath).mkdir();

        // Remove the old file
        final String buttonPath = remoteDataPath + keyName + ".btn";
        String[] rmCommand = {"rm", buttonPath};
        try {
            Runtime.getRuntime().exec(rmCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Do the learning
        new Thread(new Runnable() {
            public void run() {
                int status = learnKey(buttonPath);
                observer.KeyLearned(status, remoteName, keyName, v);
            }
        }).start();

        return 0;
    }

    public int sendKeyNative(String remoteName, String keyName)
    {
        String deviceSavePath = FileHelper.getDeviceSavePath();
        if(deviceSavePath == null)
            return -1;

        if(!isIrStarted)
            startIrNative();

        String keyPath = deviceSavePath + remoteName + "/" + keyName + ".btn";
        commandVector.add(keyPath);

        return 0;
    }

    // Thread that sends the commands
    private class CommandSender implements Runnable {

        CommandSender(){}

        @Override
        public void run() {

            while (isIrStarted) {
                if (commandVector.size() > 0) {
                    String cmd = commandVector.get(0);
                    File keyFile = new File(cmd);
                    if(keyFile.canRead())
                    {
                        Log.v("TSRemote CommandSender", "Sending command " + cmd);
                        sendKey(cmd);
                    }

                    commandVector.remove(0);

                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
