package com.helper.Helper2Go.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.view.MenuItem;

import com.helper.Helper2Go.R;
import com.helper.Helper2Go.ui.fragments.HomeFragment;
import com.helper.Helper2Go.ui.fragments.HomeTutorialFragment;
import com.helper.Helper2Go.ui.fragments.ProfileFragment;
import com.helper.Helper2Go.ui.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener{

    public static boolean flipAnimationFullCardFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        loadFragment(new HomeTutorialFragment());

        BottomNavigationView navigation = findViewById(R.id.bottom_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeTutorialFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_search:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_admin:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return false;
                case R.id.navigation_settings:
                    fragment = new SettingsFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        // transaction.addToBackStack(null);
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment){
        flipCard(fragment);
    }



    private boolean isShowingBackLayout = false;

    private boolean mShowingBack = false;
    private void flipCard(Fragment fragment) {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }
        // Flip to the back.
        mShowingBack = true;
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.right_in, R.animator.right_out,
                        R.animator.left_in, R.animator.left_out)
                .replace(R.id.frame_container, new FlipActivity.CardBackFragment())
                // Add this transaction to the back stack, allowing users to press Back
                // to get to the front of the card.
                .addToBackStack(null)
                // Commit the transaction.
                .commit();
    }

    @Override
    public void onBackStackChanged() {
        isShowingBackLayout = (getFragmentManager().getBackStackEntryCount() > 0);
    }

}
