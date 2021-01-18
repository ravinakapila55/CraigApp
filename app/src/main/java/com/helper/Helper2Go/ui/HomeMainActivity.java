package com.helper.Helper2Go.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.ui.fragments.HomeTutorialFragment;
import com.helper.Helper2Go.ui.fragments.MyJobsFragment;
import com.helper.Helper2Go.ui.fragments.ProfileFragment;
import com.helper.Helper2Go.ui.fragments.SearchFragment;
import com.helper.Helper2Go.ui.fragments.SettingsFragment;
import com.helper.Helper2Go.utils.MyApplication;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Locale;

public class HomeMainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    public static boolean is_search = false;

    public static String selected_skill = "Skill 1";
    public static String selected_tool = "Tool 1";
    public static String selected_duration = "";
    public static String end_duration = "";
    public static String selected_distance = "";
    public static String selected_max_distance = "";

    public static BottomNavigationView navigation;
    private boolean mShowingBack = false;

    String which_fragment = "";
    String from = "";

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        activity = this;
        Log.e("LanguageSelected ", MyApplication.getInstance().useString("lang"));

        if (getIntent().hasExtra("from"))
        {
            from=getIntent().getExtras().getString("from");
            Log.e("FromHome ",from);
        }
        if (savedInstanceState == null)
        {
            which_fragment = "Home";
            getNotiData();
//         loadFirstTime(new HomeTutorialFragment(), "Home");
        }
        else
        {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        navigation = findViewById(R.id.bottom_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Configuration config = newBase.getResources().getConfiguration();
//        SharedPreferences sharedPreferences = App.getGlobalPrefs();

        String language =MyApplication.getInstance().useString("lang");



        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        config.setLocale(new Locale(language));

        Resources resources = newBase.getResources();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            newBase = newBase.createConfigurationContext(config);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            newBase = newBase.createConfigurationContext(config);

        } else {
            Resources resources = newBase.getResources();
            config.locale = locale;
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }*/
        super.attachBaseContext(newBase);
    }


    String key="";
    public void getNotiData()
    {
        if (getIntent().hasExtra("noti_type"))
        {
            key=getIntent().getExtras().getString("noti_type");
            Log.e("KeyValues ",key);

            Bundle bundle = new Bundle();
            Fragment fragment;
            mShowingBack = false;
            which_fragment = "Admin";
            bundle.putString("type", key);
            fragment = new MyJobsFragment();

            HomeMainActivity.is_search = false;
            FragmentManager fm = getSupportFragmentManager();
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.container, fragment);
            fragmentTransaction.addToBackStack("Admin");
            fragmentTransaction.commit();
            fm.executePendingTransactions();
            Log.e("HomeActivity", "Fragment_Count : " + getSupportFragmentManager().getBackStackEntryCount());
        }
        else {
            key="direct";

            loadFirstTime(new HomeTutorialFragment(), "Home");
        }

        if (from.equalsIgnoreCase("job"))
        {
            Fragment fragment;

            mShowingBack = false;
            which_fragment = "Admin";
            fragment = new MyJobsFragment();
            loadFragment(fragment, "Admin");
        }

        else {
            key="direct";

            loadFirstTime(new HomeTutorialFragment(), "Home");
        }


    }


    public static Activity getInstance()
    {
        return activity;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            Fragment fragment;
            switch (item.getItemId())
            {
                case R.id.navigation_home:
                    mShowingBack = false;
                    which_fragment = "Home";
                    fragment = new HomeTutorialFragment();
                    loadFirstTime(fragment, "Home");
                    return true;
                case R.id.navigation_profile:
                    mShowingBack = false;
                    which_fragment = "Profile";
                    fragment = new ProfileFragment();
                    loadFragment(fragment, "Profile");
                    return true;
                case R.id.navigation_search:
                    mShowingBack = false;
                    which_fragment = "Search";
                    fragment = new SearchFragment();
                    loadFragment(fragment, "Search");
                    return true;
                case R.id.navigation_admin:
                    mShowingBack = false;
                    which_fragment = "Admin";
                    fragment = new MyJobsFragment();
                    loadFragment(fragment, "Admin");
                    return true;
                case R.id.navigation_settings:
                    mShowingBack = false;
                    which_fragment = "Settings";
                    fragment = new SettingsFragment();
                    loadFragment(fragment, "Settings");
                    return true;
            }
            return true;
        }
    };

    private void loadFragment(Fragment fragment, String tag)
    {
        HomeMainActivity.is_search = false;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
        fm.executePendingTransactions();
        Log.e("HomeActivity", "Fragment_Count : " + getSupportFragmentManager().getBackStackEntryCount());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add(Menu.NONE, R.id.action_flip, Menu.NONE,
                mShowingBack ?
                        R.string.action_photo :
                        R.string.action_info);
        item.setIcon(mShowingBack ?
                R.drawable.bell :
                R.drawable.fb);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    private void flipCard(Fragment fragment)
    {
        if (mShowingBack)
        {
            getSupportFragmentManager().popBackStack();
            return;
        }
        // Flip to the back.
        // mShowingBack = true;
        getSupportFragmentManager()
        .beginTransaction()
         .setCustomAnimations(R.animator.right_in, R.animator.right_out, R.animator.left_in, R.animator.left_out)
                .replace(R.id.container, fragment)
                // Add this transaction to the back stack, allowing users to press Back
                // to get to the front of the card.
                .addToBackStack(null)
                // Commit the transaction.
                .commit();

        check = true;
    }


    boolean check = false;
    boolean checkForSecond = false;

    @Override
    public void onBackStackChanged() {
        //mShowingBack = (getSupportFragmentManager().getBackStackEntryCount() > 0);
    }

    public void replaceFragment(Fragment fragment) {
        flipCard(fragment);
    }


    public void replaceFragmentFromBack(Fragment fragment)
    {
        checkForSecond = true;
        if (check)
        {
            getSupportFragmentManager().popBackStack();
            return;
        }
    }

    public void changeFragment(Fragment fragment, int tabPosition) {
        navigation.setSelectedItemId(tabPosition);
        //loadFragment(fragment);
    }


    private void loadFirstTime(Fragment fragment, String tag) {
        HomeMainActivity.is_search = false;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
        fragmentTransaction.commit();
        fm.executePendingTransactions();
        Log.e("HomeActivity", "Fragment_Count : " + getSupportFragmentManager().getBackStackEntryCount());
    }


    @Override
    public void onBackPressed() {
        HomeMainActivity.is_search = true;
        if (which_fragment.equalsIgnoreCase("Home")) {
            //finish();
        } else {

            if(checkForSecond){
                checkForSecond = false;
                which_fragment = "Home";
                navigation.getMenu().findItem(R.id.navigation_admin).setChecked(false);
                navigation.getMenu().findItem(R.id.navigation_search).setChecked(false);
                navigation.getMenu().findItem(R.id.navigation_settings).setChecked(false);
                navigation.getMenu().findItem(R.id.navigation_profile).setChecked(false);
                navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
            }
            else {

                if (check) {
                    check = false;
                } else {

                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        for (int i = getSupportFragmentManager().getBackStackEntryCount(); i > 1; i--) {
                            getSupportFragmentManager().popBackStackImmediate();
                        }
                    }

                    which_fragment = "Home";
                    navigation.getMenu().findItem(R.id.navigation_admin).setChecked(false);
                    navigation.getMenu().findItem(R.id.navigation_search).setChecked(false);
                    navigation.getMenu().findItem(R.id.navigation_settings).setChecked(false);
                    navigation.getMenu().findItem(R.id.navigation_profile).setChecked(false);
                    navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
                }
            }
        }
        super.onBackPressed();

    }

}

