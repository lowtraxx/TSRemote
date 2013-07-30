package de.toshsoft.tsremote.configuration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import de.toshsoft.tsremote.R;

/**
 *
 * Copyright (c) 20013 Oliver Pahl
 * Distributed under the GNU GPL v2. For full terms see the file LICENSE.
 *
 */
public class ConfigurationAboutFragment extends Fragment {

    /**
     * Array list representing the list of added remotes
     */
    String[] themesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.configuration_about_fragment, container, false);
        TextView aboutText = (TextView)rootView.findViewById(R.id.about_text_view);
        aboutText.setText(Html.fromHtml("<b>TSRemote</b><br />--------------------------------------<br />(c)2013 by Oliver Pahl<br />Using StandOut from Mark Wei (https://github.com/pingpongboss/StandOut)<br />Using lib_sony_ir from BuzzBumbleBee (https://github.com/BuzzBumbleBee/lib_sony_ir)"));
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

