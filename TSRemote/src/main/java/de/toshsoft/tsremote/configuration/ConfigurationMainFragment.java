package de.toshsoft.tsremote.configuration;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import de.toshsoft.tsremote.helper.DatabaseHelper;
import de.toshsoft.tsremote.helper.IIrLearnObserver;
import de.toshsoft.tsremote.helper.IrHelper;
import de.toshsoft.tsremote.R;

/**
 *
 * Copyright (c) 20013 Oliver Pahl
 * Distributed under the GNU GPL v2. For full terms see the file LICENSE.
 *
 */
public class ConfigurationMainFragment extends Fragment implements IIrLearnObserver {

    /**
     * Array list representing the list of added remotes
     */
    ArrayAdapter<String> remotesListAdapter = null;

    private AlertDialog learingAlertDialog;;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.configuration_main_fragment, container, false);

        final ListView list = (ListView)rootView.findViewById(R.id.configuration_remotes_list);
        final DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext());

        // The Alert Dialog for learning
        learingAlertDialog = new AlertDialog.Builder(rootView.getContext()).create();

        // Make a remote List
        ArrayList<String> remotesList = new ArrayList<String>();
        remotesListAdapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_list_item_1, remotesList);

        // Get all the remotes from the Database
        try {
            dbHelper.open();
            Cursor cur = dbHelper.getAllRemotes();
            if(cur.getCount() != 0)
            {
                while(cur.moveToNext()) {
                    // Get the names
                    remotesListAdapter.add(cur.getString(cur.getColumnIndex(DatabaseHelper.DB_COLUMN_NAME)));
                }
                cur.close();
            }
            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        list.setAdapter(remotesListAdapter);

        // Get a Preview of the remote to set the keys
        LinearLayout remotePreview = (LinearLayout)rootView.findViewById(R.id.theme_preview);
        final View remoteView = inflater.inflate(R.layout.standout_activity_remote, remotePreview, true);

        // Select the first item or ask
        // for creation of one
        if(list.getCount() > 0)
        {
            list.setSelection(0);
            TextView remoteId = (TextView)remoteView.findViewById(R.id.textview_remote_id);
            remoteId.setText(remotesListAdapter.getItem(0));
            remoteView.setVisibility(View.VISIBLE);

            // Now load the remote ...
            try {
                dbHelper.open();
                Cursor cur = dbHelper.getRemote(remotesListAdapter.getItem(0));

                // ... and set the keys
                if(cur.getCount() == 1)
                {
                    prepareRemote(remoteView, cur);
                }

                cur.close();
                dbHelper.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        {
            startAddRemoteWizard(rootView);
        }

        // Add the add remote to the list view
        remotesListAdapter.add(getResources().getString(R.string.add_device_prompt));

        // Set the OnClick listener for the ListView
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                // If the add remote prompt was pressed show the add remote dialog
                if(remotesListAdapter.getItem(pos) == getResources().getString(R.string.add_device_prompt))
                {
                    startAddRemoteWizard(rootView);
                }
                else
                {
                    list.setSelection(pos);
                    TextView remoteId = (TextView)remoteView.findViewById(R.id.textview_remote_id);
                    remoteId.setText(remotesListAdapter.getItem(pos));
                    remoteView.setVisibility(View.VISIBLE);

                    // Now load the remote ...
                    try {
                        dbHelper.open();
                        Cursor cur = dbHelper.getRemote(remotesListAdapter.getItem(pos));

                        // ... and set the keys
                        if(cur.getCount() == 1)
                        {
                            prepareRemote(remoteView, cur);
                        }

                        cur.close();
                        dbHelper.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Get the Spinner from the remote and make it invisible
        Spinner remoteName = (Spinner)remoteView.findViewById(R.id.remote_list_spinner);
        remoteName.setVisibility(View.INVISIBLE);

        // Get the Close Button from the remote because in config mode it
        // is used to delete the selected remote
        Button closeButton = (Button)remoteView.findViewById(R.id.button_close);
        closeButton.setText(getResources().getText(R.string.delete_remote_button_text));
        closeButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                if(remotesListAdapter.getCount() > 1)
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(rootView.getContext());
                    alert.setMessage(getResources().getString(R.string.query_are_you_sure));

                    alert.setPositiveButton(getResources().getString(R.string.answer_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            TextView remoteId = (TextView)remoteView.findViewById(R.id.textview_remote_id);

                            // First delet from the list
                            remotesListAdapter.remove(remoteId.getText().toString());

                            // then from the db
                            dbHelper.deleteRemote(remoteId.getText().toString());

                            // If there are no more, show the dialog
                            if(remotesListAdapter.getCount() == 1)
                            {
                                startAddRemoteWizard(rootView);
                            }
                            else if(remotesListAdapter.getCount() > 1)
                            {
                                // Update to the first remote
                                remoteId.setText(remotesListAdapter.getItem(0));
                            }
                        }
                    });

                    alert.setNegativeButton(getResources().getString(R.string.answer_no), null);
                    alert.show();
                }
            }
        });

        return rootView;
    }

    private void startAddRemoteWizard(final View rootView) {
        AlertDialog.Builder alert = new AlertDialog.Builder(rootView.getContext());
        final LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        final View alertView = inflater.inflate(R.layout.configuration_add_remote_wizard, (ViewGroup)rootView.getParent(), false);

        alert.setTitle(getResources().getString(R.string.add_new_remote_title));
        alert.setMessage(getResources().getString(R.string.add_new_remote_message));
        alert.setView(alertView);

        alert.setPositiveButton(getResources().getString(R.string.add_remote_ok_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext());
                String name = ((EditText)alertView.findViewById(R.id.remote_name)).getText().toString();
                String vendor = ((EditText)alertView.findViewById(R.id.remote_vendor)).getText().toString();
                String type = ((EditText)alertView.findViewById(R.id.remote_type)).getText().toString();
                if(!name.isEmpty())
                {
                    // Add the remote to the db...
                    dbHelper.insertRemote(name, vendor, type, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "");
                    remotesListAdapter.add(name);

                    // ...and refresh the list
                    LinearLayout remotePreview = (LinearLayout)rootView.findViewById(R.id.theme_preview);
                    final View remoteView = inflater.inflate(R.layout.standout_activity_remote, remotePreview, true);
                    ListView list = (ListView)rootView.findViewById(R.id.configuration_remotes_list);
                    list.setSelection(0);
                    TextView remoteId = (TextView)remoteView.findViewById(R.id.textview_remote_id);
                    remoteId.setText(name);
                    remoteView.setVisibility(View.VISIBLE);

                    // Move the Add Device to the end of the list
                    remotesListAdapter.remove(getResources().getString(R.string.add_device_prompt));
                    remotesListAdapter.add(getResources().getString(R.string.add_device_prompt));

                    // Now load the remote ...
                    try {
                        dbHelper.open();
                        Cursor cur = dbHelper.getRemote(remotesListAdapter.getItem(0));

                        // ... and set the keys
                        if(cur.getCount() == 1)
                        {
                            prepareRemote(remoteView, cur);
                        }

                        cur.close();
                        dbHelper.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    // TODO: There was no data entered, find a better way
                    // to redisplay the Settings and show the error
                    getActivity().finish();
                    Intent dialogIntent = new Intent(rootView.getContext(), ConfigurationActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    rootView.getContext().startActivity(dialogIntent);
                }
            }
        });

        alert.setNegativeButton(getResources().getString(R.string.add_remote_cancel_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(remotesListAdapter.getCount() == 1)
                {
                    // Close as there is no remote
                    getActivity().finish();
                }
            }
        });

        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(remotesListAdapter.getCount() == 1)
                {
                    // Close as there is no remote
                    getActivity().finish();
                }
            }
        });

        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        remotesListAdapter.clear();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        remotesListAdapter.clear();
        super.onDestroy();
    }

    private void prepareRemote(View remoteView, Cursor currentRemote) {
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
            currentButton.getBackground().setColorFilter(currentButtonName.compareTo("none") == 0 ? Color.RED : Color.GREEN, PorterDuff.Mode.MULTIPLY);
            currentButton.setOnLongClickListener(new Button.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    IrHelper.getInstance().learnKeyNative(remoteName, dbName, ConfigurationMainFragment.this, v);
                    learingAlertDialog.setMessage(String.format(getResources().getString(R.string.learn_string_alert_countdown), 3));
                    learingAlertDialog.show();
                    learingAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            learingAlertDialog.show();
                        }
                    });

                    TextView messageView = (TextView)learingAlertDialog.findViewById(android.R.id.message);
                    messageView.setGravity(Gravity.CENTER);

                    new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            learingAlertDialog.setMessage(String.format(getResources().getString(R.string.learn_string_alert_countdown), (millisUntilFinished / 1000)));
                        }

                        @Override
                        public void onFinish() {
                            learingAlertDialog.setMessage(getResources().getString(R.string.learn_string_alert));
                        }
                    }.start();

                    return true;
                }
            });
        }
    }

    @Override
    public void KeyLearned(final int status, final String remoteName, final String keyName, final View changedView) {
        if(getActivity() == null)
        {
            learingAlertDialog.hide();
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                View currentButton = (View)changedView.findViewById(((Number) DatabaseHelper.UI_TO_DB_MAP.get(keyName)).intValue());
                currentButton.getBackground().setColorFilter(status < 20 ? Color.RED : Color.GREEN, PorterDuff.Mode.MULTIPLY);

                DatabaseHelper dbHelper = new DatabaseHelper(changedView.getContext());
                dbHelper.updateKeyFromRemote(remoteName, keyName, status < 20 ? "none" : "set");

                learingAlertDialog.hide();
            }
        });
    }
}
