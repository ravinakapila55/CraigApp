package com.helper.Helper2Go.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.helper.Helper2Go.R;

public class FlipActivity extends AppCompatActivity
        implements FragmentManager.OnBackStackChangedListener {
    private boolean mShowingBack = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new CardFrontFragment())
                    .commit();
        } else {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, FlipActivity.class));
                return true;
            case R.id.action_flip:
                flipCard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void flipCard() {
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
                .replace(R.id.container, new CardBackFragment())
                // Add this transaction to the back stack, allowing users to press Back
                // to get to the front of the card.
                .addToBackStack(null)
                // Commit the transaction.
                .commit();
    }
    @Override
    public void onBackStackChanged() {
        mShowingBack = (getSupportFragmentManager().getBackStackEntryCount() > 0);
        // When the back stack changes, invalidate the options menu (action bar).
        invalidateOptionsMenu();
    }
    public static class CardFrontFragment extends Fragment {
        public CardFrontFragment() {}
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.front, container, false);
        }
    }
    public static class CardBackFragment extends Fragment {
        public CardBackFragment() {}
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.back, container, false);
        }
    }
}