package de.toshsoft.tsremote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.widget.TextView;

import java.sql.SQLException;

import de.toshsoft.tsremote.configuration.ConfigurationActivity;
import de.toshsoft.tsremote.helper.DatabaseHelper;
import de.toshsoft.tsremote.helper.FileHelper;
import de.toshsoft.tsremote.helper.IrHelper;
import wei.mark.standout.StandOutWindow;

/**
 *
 * Copyright (c) 20013 Oliver Pahl
 * Distributed under the GNU GPL v2. For full terms see the file LICENSE.
 *
 */
public class RemoteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Try to enable IR
        FileHelper.fixPermissionsForIr();

        // Initialize the Ir
        new Thread(new Runnable() {
            public void run() {
                IrHelper.getInstance().startIrNative();
            }
        }).start();

        FileHelper.setDeviceSavePath(getFilesDir() + "/remote_data/");

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        try {
            // Get all the remotes from the Database
            dbHelper.open();
            Cursor cur = dbHelper.getAllRemotes();
            if(cur.getCount() != 0)
            {
                StandOutWindow.closeAll(this, StandOutRemoteActivity.class);
                StandOutWindow.show(this, StandOutRemoteActivity.class, StandOutWindow.DEFAULT_ID);
            }
            else
            {
                // Start the Configuration Activity
                Intent dialogIntent = new Intent(getBaseContext(), ConfigurationActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(dialogIntent);
            }
            cur.close();
            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        finish();
    }
}
