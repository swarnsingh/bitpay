package com.pay.bitpay.ui.otp;

import android.support.v4.app.Fragment;

/**
 * @author Swarn Singh.
 */

public interface ViewListener {

    void setToolbarTitle(String title);

    void addFragment(int containerViewId, Fragment fragment);

    void replaceFragment(int containerViewId, Fragment fragment);
}
