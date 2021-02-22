package com.example.lifetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class verifivcationFragment extends Fragment {
    String phone;
    String servercode;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    private TextView codesendtxt;
    private Button verifybtn;
    private EditText codeedittxt;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FrameLayout frameLayout;


    public verifivcationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verifivcation, container, false);
        frameLayout = getActivity().findViewById(R.id.frame_layout1);
        codesendtxt = view.findViewById(R.id.code_sendtxt);
        verifybtn = view.findViewById(R.id.verifybutton);
        codeedittxt = view.findViewById(R.id.verificationcodetxt);
        progressBar = view.findViewById(R.id.verificationprogressBar);
        verifybtn.setAlpha(0.3f);
        verifybtn.setEnabled(false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("HI", Context.MODE_PRIVATE);
      phone = sharedPreferences.getString("phone","null");
        codesendtxt.setText("Verification Code Has Been Send To +91 "+phone);
        mAuth = FirebaseAuth.getInstance();

        sendVerificationCodeToUser(phone);
        progressBar.setVisibility(View.INVISIBLE);
        codeedittxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (codeedittxt.getText().toString().length()==6) {
                    verifybtn.setEnabled(true);
                    verifybtn.setAlpha(1f);
                }else {
                    verifybtn.setEnabled(false);
                    verifybtn.setAlpha(0.3f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = codeedittxt.getText().toString();

                if (code.isEmpty() || code.length() < 6) {
                    codeedittxt.setError("Wrong OTP...");
                    codeedittxt.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
        return view;
    }
    private void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void sendVerificationCodeToUser(String phone){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity) getContext())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        private static final String TAG = "";

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + credential);
            progressBar.setVisibility(View.VISIBLE);
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);
            progressBar.setVisibility(View.INVISIBLE);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            servercode = verificationId;

            mResendToken = token;

            // ...
        }
    };


    private void verifyCode(String codeByUser) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(servercode, codeByUser);
        signInWithPhoneAuthCredential(credential);

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    private static final String TAG = "";

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            formFragment formFragment = new formFragment();

                            changeFragment(formFragment);
                            progressBar.setVisibility(View.INVISIBLE);


                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                codeedittxt.setError("Wrong OTP...");
                                codeedittxt.requestFocus();
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

}

