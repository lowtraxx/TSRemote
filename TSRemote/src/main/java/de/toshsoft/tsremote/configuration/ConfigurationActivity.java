package de.toshsoft.tsremote.configuration;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.sql.SQLException;
import java.util.Locale;

import de.toshsoft.tsremote.R;
import de.toshsoft.tsremote.helper.DatabaseHelper;
import de.toshsoft.tsremote.StandOutRemoteActivity;
import wei.mark.standout.StandOutWindow;

/**
 *
 * Copyright (c) 20013 Oliver Pahl
 * Distributed under the GNU GPL v2. For full terms see the file LICENSE.
 *
 */
public class ConfigurationActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onDestroy() {
        checkDatabase();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // Does not work ATM
        // checkDatabase();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Remove all StandOut Windows
        StandOutWindow.closeAll(this, StandOutRemoteActivity.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration_activity);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position)
            {
                case 0:
                {
                    fragment = new ConfigurationMainFragment();
                    break;
                }

                case 1:
                {
                    fragment = new ConfigurationThemeFragment();
                    break;
                }

                case 2:
                {
                    fragment = new ConfigurationRemoteDatabaseFragment();
                    break;
                }

                case 3:
                {
                    fragment = new ConfigurationAboutFragment();
                    break;
                }

                default:
                {
                    fragment = new ConfigurationMainFragment();
                    break;
                }
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_configuration).toUpperCase(l);

                case 1:
                    return getString(R.string.title_theme).toUpperCase(l);

                case 2:
                    return getString(R.string.title_device_database).toUpperCase(l);

                case 3:
                    return getString(R.string.title_about).toUpperCase(l);
            }
            return null;
        }
    }

    private void checkDatabase() {
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
            cur.close();
            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
