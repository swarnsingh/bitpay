package com.pay.bitpay.ui.otp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pay.bitpay.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Swarn Singh.
 */
public class OTPConfirmFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.otp_mobile_txt)
    TextView mOTPMobileTxtView;

    @BindView(R.id.otp_mobile_no_edit_txt)
    EditText mOTPMobileEditTxt;

    @BindView(R.id.otp_submit_btn)
    Button mSubmitBtn;

    private ViewListener mViewListener;

    public OTPConfirmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OTPConfirmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OTPConfirmFragment newInstance(String param1, String param2) {
        OTPConfirmFragment fragment = new OTPConfirmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otpconfirm, container, false);
        ButterKnife.bind(this, view);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        if (getArguments() != null) {
            String mobile = getArguments().getString(ARG_PARAM1);
            mOTPMobileTxtView.setText(mobile != null ? mobile : "");
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewListener.setToolbarTitle("Enter Verification Code");

        mSubmitBtn.setEnabled(false);

        mOTPMobileEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    mSubmitBtn.setEnabled(true);
                } else if (s.length() == 0){
                    mSubmitBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.otp_submit_btn)
    public void onClick() {
        if (!TextUtils.isEmpty(mOTPMobileEditTxt.getText().toString().trim())) {

        } else {
            mOTPMobileEditTxt.setError("Please Enter OTP");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViewListener) {
            mViewListener = (ViewListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ViewListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mViewListener = null;
    }

}
