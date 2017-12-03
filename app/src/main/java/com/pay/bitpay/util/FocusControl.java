package com.pay.bitpay.util;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author Swarn Singh.
 */

public class FocusControl {
    static final int POST_DELAY = 250;
    private Handler handler;
    private InputMethodManager manager;
    private View focus;

    /**
     * Keyboard focus controller
     * <p>
     * Shows and hides the keyboard. Uses a runnable to do the showing as there are race
     * conditions with hiding the keyboard that this solves.
     *
     * @param focus The view element to focus and hide the keyboard from
     */
    public FocusControl(View focus, Context context) {
        handler = new Handler();
        manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        this.focus = focus;
    }

    /**
     * Focus the view and show the keyboard.
     */
    public void showKeyboard() {
        handler.postDelayed(() -> {
            focus.requestFocus();
            manager.showSoftInput(focus, InputMethodManager.SHOW_IMPLICIT);
        }, POST_DELAY);
    }

    /**
     * Hide the keyboard.
     */
    public void hideKeyboard(View currentView) {
        if (currentView.equals(focus)) {
            manager.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
        }
    }
}
