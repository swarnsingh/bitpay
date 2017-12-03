package com.pay.bitpay.ui.otp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pay.bitpay.R;
import com.pay.bitpay.util.FocusControl;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * @author Swarn Singh.
 */

public class PhoneVerifyFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int RESOLVE_HINT = 1000;
    private GoogleApiClient mCredentialsApiClient;

    private static final String TAG = PhoneVerifyActivity.class.getSimpleName();

    @BindView(R.id.mobile_no_edit_txt)
    EditText mMobileNumberEditTxt;

    @BindView(R.id.otp_submit_btn)
    Button mSubmitBtn;

    private FocusControl mFocusControl;

    private ViewListener mViewListener;

    private boolean isHintRequestCalled = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCredentialsApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_verify, container, false);

        ButterKnife.bind(this, view);
        initViews();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mViewListener = (ViewListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ViewListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mViewListener = null;
    }

    private void initViews() {
        if (mViewListener != null) {
            mViewListener.setToolbarTitle("Verify Your mobile number");
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mFocusControl = new FocusControl(mMobileNumberEditTxt, getActivity());
        setSubmitEnabled(false);

        mMobileNumberEditTxt.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                onClick();
                mMobileNumberEditTxt.setOnTouchListener(null);
            }
            return true;
        });

        mMobileNumberEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    mSubmitBtn.setEnabled(true);
                } else {
                    mSubmitBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mMobileNumberEditTxt.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mMobileNumberEditTxt) {
            onClick();
        } else if (v == mSubmitBtn) {
            onSubmitPhoneNumber();
        }

    }

    private void onClick() {
        if (!isHintRequestCalled && TextUtils.isEmpty(getPhoneNumber())) {
            mMobileNumberEditTxt.requestFocus();
            requestHint();
        } else {
            mMobileNumberEditTxt.setCursorVisible(true);
            mMobileNumberEditTxt.setEnabled(true);
            focusPhoneNumber();
        }
    }

    private void onSubmitPhoneNumber() {
        if (getPhoneNumber().length() >= 10) {
            OTPConfirmFragment otpConfirmFragment = OTPConfirmFragment.newInstance(getPhoneNumber(), null);
            mViewListener.replaceFragment(R.id.otp_frame_container, otpConfirmFragment);
        } else if (getPhoneNumber().length() < 10) {
            mMobileNumberEditTxt.setError("Please Enter 10 digit mobile number");
        } else {
            mMobileNumberEditTxt.setError("Please Enter Mobile No.");
        }
    }

    // Construct a request for phone numbers and show the picker
    private void requestHint() {
        clearKeyboard();
        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder()
                        .setShowCancelButton(true)
                        .build())
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent =
                Auth.CredentialsApi.getHintPickerIntent(mCredentialsApiClient, hintRequest);
        try {
            getActivity().startIntentSenderForResult(intent.getIntentSender(), RESOLVE_HINT,
                    null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Could not start hint picker Intent", e);
        }
    }

    // Obtain the phone number from the result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESOLVE_HINT) {
            mMobileNumberEditTxt.setOnClickListener(this);
            mMobileNumberEditTxt.setOnTouchListener(null);

            isHintRequestCalled = true;
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                setPhoneNumber(credential.getId());
                Log.d("Credentials : ", credential.getId());
            } else {
                focusPhoneNumber();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Connected");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "GoogleApiClient is suspended with cause code: " + cause);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "GoogleApiClient failed to connect: " + connectionResult);
    }

    private void setPhoneNumber(String phoneNumber) {
        mMobileNumberEditTxt.setText(phoneNumber);
    }

    private String getPhoneNumber() {
        return mMobileNumberEditTxt.getText().toString();
    }

    private void focusPhoneNumber() {
        mFocusControl.showKeyboard();
    }

    private void clearKeyboard() {
        mFocusControl.hideKeyboard(mMobileNumberEditTxt);
    }

    private void setSubmitEnabled(boolean enabled) {
        mSubmitBtn.setEnabled(enabled);
    }
}
