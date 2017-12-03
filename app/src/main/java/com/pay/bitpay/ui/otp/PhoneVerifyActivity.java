package com.pay.bitpay.ui.otp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pay.bitpay.R;
import com.pay.bitpay.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneVerifyActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        addFragment(R.id.otp_frame_container, new PhoneVerifyFragment());

        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(PhoneVerifyFragment.class.getCanonicalName());

        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
