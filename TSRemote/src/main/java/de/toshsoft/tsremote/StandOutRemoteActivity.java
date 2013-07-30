package de.toshsoft.tsremote;

import de.toshsoft.tsremote.configuration.ConfigurationActivity;
import de.toshsoft.tsremote.helper.DatabaseHelper;
import de.toshsoft.tsremote.helper.IrHelper;
import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * Copyright (c) 20013 Oliver Pahl
 * Distributed under the GNU GPL v2. For full terms see the file LICENSE.
 *
 */
public class StandOutRemoteActivity extends StandOutWindow {

    /**
     * Intent action: Change into configure mode
     */
    public static final String ACTION_CONFIGURE = "CONFIGURE";

    /**
     * Array list representing the list of added remotes
     */
    ArrayList<String> remotesList = new ArrayList<String>();

    @Override
    public String getAppName() {
        return getResources().getString(R.string.app_name);
    }

    @Override
    public int getAppIcon() {
        return R.drawable.ic_launcher;
    }

    @Override
    public String getTitle(int id) {
        return getAppName() + " " + id;
    }

    @Override
    public void createAndAttachView(int id, FrameLayout frame) {
        // create a new layout from body.xml
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View remoteView = inflater.inflate(R.layout.standout_activity_remote, frame, true);

        // Try to add the entries from the remote database to the spinner
        Spinner spinner = (Spinner)remoteView.findViewById(R.id.remote_list_spinner);
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        try {
            // Get all the remotes from the Database
            dbHelper.open();
            Cursor cur = dbHelper.getAllRemotes();
            if(cur.getCount() != 0)
            {
                while(cur.moveToNext()) {
                    // Get the names
                    remotesList.add(cur.getString(cur.getColumnIndex("_name")));
                }
                cur.close();
            }
            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // What to do once a spinner item was selected
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prepareRemote(remoteView, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing done here ATM
            }
        });

        // If the list is empty, open the configuration
        // otherwise continue populating
        if(remotesList.size() == 0)
        {
            // Close the remotes
            closeAll();

            // Open Configuration
            Intent dialogIntent = new Intent(getBaseContext(), ConfigurationActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(dialogIntent);
        }
        else
        {
            // Add the Add device note and set the adapter for the spinner
            // remotesList.add(getResources().getString(R.string.add_device_prompt));
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, remotesList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);

            // Let the close button close the remote
            Button closeButton = (Button)remoteView.findViewById(R.id.button_close);
            closeButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    // Close the Remote
                    IrHelper.getInstance().stopIrNative();
                    closeAll();
                }
            });
        }
    }

    // every window is initially same size
    @Override
    public StandOutLayoutParams getParams(int id, Window window) {
        return new StandOutLayoutParams(id, 430, 980,
                StandOutLayoutParams.AUTO_POSITION,
                StandOutLayoutParams.AUTO_POSITION, 100, 100);
    }

    @Override
    public int getFlags(int id) {
        return StandOutFlags.FLAG_BODY_MOVE_ENABLE
                | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
    }

    @Override
    public String getPersistentNotificationTitle(int id) {
        return getAppName();
    }

    @Override
    public String getPersistentNotificationMessage(int id) {
        return getResources().getString(R.string.config_message);
    }

    // Send a Configuration Intent
    @Override
    public Intent getPersistentNotificationIntent(int id) {
        return new Intent(StandOutRemoteActivity.this, StandOutRemoteActivity.class).putExtra("id", id).setAction(ACTION_CONFIGURE);
    }

    @Override
    public boolean onHide(int id, Window window) {
        return true;
    }

    @Override
    public boolean onFocusChange(int id, Window window, boolean focus) {
        // TODO: Implement focus listener to grey out the remote while it has no focus
        return super.onFocusChange(id, window, focus);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();

            if (ACTION_CONFIGURE.equals(action)) {
                // Close the remote, because we
                // are changing the configuration
                closeAll();

                // Start the Configuration Activity
                Intent dialogIntent = new Intent(getBaseContext(), ConfigurationActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(dialogIntent);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onCloseAll() {
        // Once we get closed we have to clean up
        // the remote db so we get no doubles on the
        // next call
        remotesList.clear();
        return super.onCloseAll();
    }

    private void prepareRemote(View remoteView, int listNumber) {

        // Check if the remote we want is available
        if(remotesList.size() < listNumber + 1)
            return;

        DatabaseHelper helper = new DatabaseHelper(remoteView.getContext());
        try {
            helper.open();

            // Now get the current remote from the DB
            Cursor currentRemote = helper.getRemote(remotesList.get(listNumber));
            currentRemote.moveToFirst();

            final String remoteName = currentRemote.getString(currentRemote.getColumnIndex(DatabaseHelper.DB_COLUMN_NAME));

            Iterator remoteIterator = DatabaseHelper.UI_TO_DB_MAP.keySet().iterator();
            while(remoteIterator.hasNext())
            {
                final String dbName = (String)remoteIterator.next();
                final int currentId = (DatabaseHelper.UI_TO_DB_MAP.get(dbName)).intValue();

                View currentButton = remoteView.findViewById(currentId);
                int currentButtonIndex = currentRemote.getColumnIndex(dbName);
                if(currentButtonIndex < 0)
                    continue;

                String currentButtonName = currentRemote.getString(currentButtonIndex);
                if(currentButton != null)
                {
                    currentButton.setVisibility(currentButtonName.compareTo("none") == 0 ? View.INVISIBLE : View.VISIBLE);
                    currentButton.setOnClickListener(new Button.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            IrHelper.getInstance().sendKeyNative(remoteName, dbName);
                        }
                    });
                }
            }

            currentRemote.close();
            helper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


