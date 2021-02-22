package com.example.lifetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class signinfragment extends Fragment {
    private Button button;
    private EditText editText;
    private FrameLayout frameLayout;

    public signinfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_signinfragment, container, false);
        frameLayout = getActivity().findViewById(R.id.frame_layout1);
        button = view.findViewById(R.id.phone_no_submitbtn);
        editText = view.findViewById(R.id.phone_no_edittxt);
        button.setEnabled(false);
        button.setAlpha(0.3f);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                button.setEnabled(true);
                button.setAlpha(1f);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().length()==10){
                verifivcationFragment verifivcationFragment = new verifivcationFragment();
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("HI", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("phone",editText.getText().toString());
                    editor.commit();
                changeFragment(verifivcationFragment);

                }
            }
        });
        return view;
    }
    private void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }


}
