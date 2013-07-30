package de.toshsoft.tsremote.configuration;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import de.toshsoft.tsremote.R;

/**
 *
 * Copyright (c) 20013 Oliver Pahl
 * Distributed under the GNU GPL v2. For full terms see the file LICENSE.
 *
 */
public class ConfigurationRemoteDatabaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.configuration_remote_database_fragment, container, false);
        return rootView;
    }
}
