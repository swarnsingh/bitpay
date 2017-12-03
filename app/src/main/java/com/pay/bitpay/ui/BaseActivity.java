package com.pay.bitpay.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


import com.pay.bitpay.R;
import com.pay.bitpay.ui.otp.ViewListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Swarn Singh.
 */

public class BaseActivity extends AppCompatActivity implements ViewListener {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
    }

    /**
     * Add a fragment to the activity state.
     *
     * @param containerViewId Identifier of the container this fragment is
     *                        to be placed in.
     * @param fragment        The fragment to be added.  This fragment must not already
     *                        be added to the activity.
     * @param tag             Optional tag name for the fragment, to later retrieve the
     *                        fragment with {@link FragmentManager#findFragmentByTag(String)
     *                        FragmentManager.findFragmentByTag(String)}.
     */
    protected void addFragment(int containerViewId, Fragment fragment, @Nullable String tag) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(containerViewId, fragment, tag)
                .commit();
    }

    protected void replaceFragment(int fragmentId, Fragment fragment, @Nullable String tag) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(fragmentId, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    protected Fragment findFragment(int fragmentId) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(fragmentId);
        return fragment;
    }

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void addFragment(int containerViewId, Fragment fragment) {
        addFragment(containerViewId, fragment, fragment.getClass().getCanonicalName());
    }

    @Override
    public void replaceFragment(int containerViewId, Fragment fragment) {
        replaceFragment(containerViewId, fragment, fragment.getClass().getCanonicalName());
    }

    public void showProgressDialog() {
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        mProgressDialog.hide();
    }
}
