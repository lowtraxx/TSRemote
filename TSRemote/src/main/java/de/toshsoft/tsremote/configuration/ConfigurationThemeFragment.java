package de.toshsoft.tsremote.configuration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import de.toshsoft.tsremote.R;

/**
 *
 * Copyright (c) 20013 Oliver Pahl
 * Distributed under the GNU GPL v2. For full terms see the file LICENSE.
 *
 */
public class ConfigurationThemeFragment extends Fragment {

    /**
     * Array list representing the list of added remotes
     */
    String[] themesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.configuration_theme_fragment, container, false);

        themesList = getResources().getStringArray(R.array.themes_array);
        ListView list = (ListView)rootView.findViewById(R.id.configuration_themes_list);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_list_item_1, themesList);
        list.setAdapter(dataAdapter);

        LinearLayout themePreview = (LinearLayout)rootView.findViewById(R.id.theme_preview);
        View themeView = inflater.inflate(R.layout.standout_activity_remote, themePreview, true);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

