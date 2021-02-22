package com.example.lifetracker.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.lifetracker.R;
import com.example.lifetracker.track_adapter;
import com.example.lifetracker.track_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    public static DatabaseReference databaseReference;
    public static List<track_model> modelList = new ArrayList<>();
    public static TextView symptomstxt,temp,oxygen,bpm,date,possibletxt,connecting;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        symptomstxt = view.findViewById(R.id.symptomstxt);
        temp = view.findViewById(R.id.temp_value);
        oxygen = view.findViewById(R.id.oxygen_value);
        bpm = view.findViewById(R.id.heart_value);
        date = view.findViewById(R.id.time_value);
        possibletxt = view.findViewById(R.id.possibletxt);
        connecting = view.findViewById(R.id.connecttxt);
        connecting.setVisibility(View.INVISIBLE);

        track_adapter track_adapter = new track_adapter(modelList);
     loadData(track_adapter,getContext());

        return view;
    }
    public  static void loadData(final track_adapter adapter, final Context context){
        databaseReference = FirebaseDatabase.getInstance().getReference("Life_Tracker");
        Query query = databaseReference.child(FirebaseAuth.getInstance().getUid()).child("Sensor_Value").orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    connecting.setVisibility(View.INVISIBLE);
                    modelList.clear();
                try {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot : dataSnapshot1.getChildren()) {

                            String Temperature = decode(dataSnapshot.child("Temperature").getValue().toString());
                            String Oxygen = decode(dataSnapshot.child("Oxygen_Level").getValue().toString());
                            String BPM = decode(dataSnapshot.child("Heart_Rate").getValue().toString());
                            String[] arr;
                            arr = symptoms(Temperature, Oxygen, BPM).split("-");
                            modelList.add(new track_model((dataSnapshot.child("time").getValue().toString()), Temperature, Oxygen, BPM, arr[0], arr[1]));

                    }
                }
                    Collections.reverse(modelList);
                    adapter.notifyDataSetChanged();
                temp.setText(modelList.get(0).getTemp());
                oxygen.setText(modelList.get(0).getOxygen());
                bpm.setText(modelList.get(0).getHeart_rate());
                date.setText(modelList.get(0).getDate());
                    symptomstxt.setText(modelList.get(0).getSymptoms());
                    possibletxt.setText(modelList.get(0).getPossible());
                }catch(Exception e) {
                    connecting.setVisibility(View.VISIBLE);
                }




            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static String decode(String value){
        String firtst = String.valueOf(value.charAt(0));
        String last = String.valueOf(value.charAt(value.length()-1));
        String data = value.substring(1,(value.length()-1));
        System.out.println(data);
        int divider = (Integer.parseInt(firtst)*10)+Integer.parseInt(last);
        String decrypt = String.valueOf(Integer.parseInt(data)/(divider));
      return decrypt;
    }
    public static String symptoms(String temp,String oxygen,String bpm){
        String symptoms="";
        String possible="";
        int temp1 = Integer.parseInt(temp);
        int oxygen1 = Integer.parseInt(oxygen);
        int bpm1 = Integer.parseInt(bpm);
        if(temp1>=36 && temp1<=38){
            symptoms += "Normal Body Temperature - ";
        }else if(temp1<36){
            symptoms += "Low Body Temperature - ";
            possible += "Low Body Temperature Causes Hypothermia ";
        }else {
            symptoms += "High Body Temperature - ";
            possible += "High Body Temperature Causes Fever ";
        }
        if(oxygen1>=91 && oxygen1<=100){
            symptoms += "Normal Oxygen Level - ";

        }else if(oxygen1<91){
            symptoms += "Low Oxygen Level - ";
            possible += "Low Oxygen Level Causes Hypoxemia ";
        }
        if(bpm1>=60 && bpm1<=100){
            symptoms += "Normal Body Temperature - ";
        }else if(bpm1<60){
            symptoms += "Low Heart Rate - ";
            possible += "Low Heart Rate Causes Bradycardia ";
        }else {
            symptoms += "High High Heart Rate - ";
            possible += "High High Heart Rate Causes Tachycardia ";
        }
       return symptoms+"-"+possible;
    }
}
