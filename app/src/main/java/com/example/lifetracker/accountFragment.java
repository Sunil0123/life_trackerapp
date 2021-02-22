package com.example.lifetracker;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class accountFragment extends Fragment {
    private ImageView profile;
    private TextView name,id,email,department,birth_date,phone,hotspot_password,weight;
    private DatabaseReference databaseReference;
    private Button button;
    private Dialog dialog;


    public accountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        name= view.findViewById(R.id.nametxt);
        id= view.findViewById(R.id.idtxt);
        email= view.findViewById(R.id.emailtxt);
        department= view.findViewById(R.id.departmenttxt);
        birth_date= view.findViewById(R.id.birth_datetxt);
        phone= view.findViewById(R.id.phonetxt);
        hotspot_password= view.findViewById(R.id.hotspottxt);
        weight= view.findViewById(R.id.weighttxt);
        profile= view.findViewById(R.id.profile);
        button= view.findViewById(R.id.editbtn);

        dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.editdetailsdialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText edit_name = dialog.findViewById(R.id.edit_name);
        final EditText edit_department = dialog.findViewById(R.id.edit_department);
        final EditText edit_id = dialog.findViewById(R.id.edit_id);
        final EditText edit_hotspot = dialog.findViewById(R.id.edit_hotspot);
        final EditText edit_birth = dialog.findViewById(R.id.edit_birth);
        final EditText edit_weight = dialog.findViewById(R.id.edit_weight);
        final Button submit_btn = dialog.findViewById(R.id.dialog_submit_btn);

        databaseReference = FirebaseDatabase.getInstance().getReference("Life_Tracker");
        final DatabaseReference databaseReference1 = databaseReference.child(FirebaseAuth.getInstance().getUid()).child("User_Details");
       databaseReference1.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               name.setText(snapshot.child("Full_Name").getValue().toString());
               id.setText(snapshot.child("Company_Id").getValue().toString());
               email.setText(snapshot.child("Email").getValue().toString());
               department.setText(snapshot.child("Full_Name").getValue().toString());
               birth_date.setText(snapshot.child("Birth_Date").getValue().toString());
               phone.setText(snapshot.child("Phone_Number").getValue().toString());
               hotspot_password.setText(snapshot.child("Hotspot_Password").getValue().toString());
               weight.setText(snapshot.child("weight").getValue().toString());

               edit_name.setText(snapshot.child("Full_Name").getValue().toString());
               edit_id.setText(snapshot.child("Company_Id").getValue().toString());
               edit_department.setText(snapshot.child("Department").getValue().toString());
               edit_name.setText(snapshot.child("Full_Name").getValue().toString());
               edit_birth.setText(snapshot.child("Birth_Date").getValue().toString());
               edit_hotspot.setText(snapshot.child("Hotspot_Password").getValue().toString());
               edit_weight.setText(snapshot.child("weight").getValue().toString());

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.show();
           }
       });
       submit_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Map<String,Object>update = new HashMap<>();
               update.put("Company_Id",edit_id.getText().toString());
               update.put("Full_Name",edit_name.getText().toString());
               update.put("Hotspot_Password",edit_hotspot.getText().toString());
               update.put("Birth_Date",edit_birth.getText().toString());
               update.put("weight",edit_weight.getText().toString());
               update.put("Department",edit_department.getText().toString());
               databaseReference1.updateChildren(update);
           }
       });
        return view;
    }
}
