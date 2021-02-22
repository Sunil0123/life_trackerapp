package com.example.lifetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class formFragment extends Fragment {
    private EditText full_name,email,company_id,department,birth_date,weight,hotspot_password;
    private ImageView profile;
    private Button submit;
    private ProgressBar progressBar_form,progressBar;
    private Uri image_upload_url;
    private FirebaseStorage storage;
    private ConstraintLayout constraintLayout;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    public formFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form, container, false);
   full_name = view.findViewById(R.id.editText_name);
   email = view.findViewById(R.id.editText_email);
   company_id = view.findViewById(R.id.editText_id);
   department = view.findViewById(R.id.editText_departmaent);
   birth_date = view.findViewById(R.id.editText_birth_date);
   weight = view.findViewById(R.id.editText_weight);
   profile = view.findViewById(R.id.upload_profile);
   submit = view.findViewById(R.id.form_submit_button);
   hotspot_password = view.findViewById(R.id.editText_hotspot_pass);
   progressBar = view.findViewById(R.id.progressBar1);
   constraintLayout = view.findViewById(R.id.constraintLayout);
   progressBar.setVisibility(View.VISIBLE);
   constraintLayout.setVisibility(View.INVISIBLE);
   progressBar_form = view.findViewById(R.id.progressBar_form);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        progressBar_form.setVisibility(View.INVISIBLE);
        submit.setEnabled(false);
        submit.setAlpha(0.3f);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar_form.setVisibility(View.VISIBLE);
                submitDetails();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage();
                submit.setEnabled(true);
                submit.setAlpha(1f);

            }


        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Life_Tracker");
        Query query1 = databaseReference.child(FirebaseAuth.getInstance().getUid()).child("User_Details");
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Full_Name").exists()){
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    getContext().startActivity(intent);
                    sign_inActivity.signinActivity.finish();
                }else{
                    constraintLayout.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    return view;
    }
    private  void submitDetails(){
        if(!full_name.equals("")){
            if(!email.equals("")){
                if(!company_id.equals("")){
                    if(!department.equals("")){
                        if(!birth_date.equals("")){
                            if(!weight.equals("")){
                                if(!hotspot_password.equals("")){

                                        if(image_upload_url !=null){

                                            progressBar_form.setVisibility(View.VISIBLE);
                                            submit.setEnabled(false);
                                            submit.setAlpha(0.3f);
                                            upload();

                                        }else {
                                            Toast.makeText(getContext(), "Profile Image Is Not Selected", Toast.LENGTH_SHORT).show();
                                        }

                                }else {
                                    Toast.makeText(getContext(), "Mobile Hotspot Field Is Empty", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getContext(), "Weight Field Is Empty ", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getContext(), "Birth date Field Is Empty", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Department Field Is Empty", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Company Id Field Is Empty", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getContext(), "Email Field Is Empty", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getContext(), "Full Name Field Is Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data !=null && data.getData() !=null){
            image_upload_url=data.getData();
            profile.setImageURI(image_upload_url);

        }
    }
    private void upload(){
        final String random_value= UUID.randomUUID().toString();
        final StorageReference sr = storageReference.child("profile_picture/"+random_value);
        sr.putFile(image_upload_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        Map<String,Object> profile_data = new HashMap<>();
                        final Map<String,Object>users = new HashMap<>();
                        profile_data.put("Company_Id",company_id.getText().toString());
                        profile_data.put("Full_Name",full_name.getText().toString());
                        profile_data.put("Email",email.getText().toString());
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("HI", Context.MODE_PRIVATE);
                        profile_data.put("Phone_Number",sharedPreferences.getString("phone","null"));
                        profile_data.put("Hotspot_Password",hotspot_password.getText().toString());
                        profile_data.put("Birth_Date",birth_date.getText().toString());
                        profile_data.put("weight",weight.getText().toString());
                        profile_data.put("Profile_Picture",url);
                        profile_data.put("Department",department.getText().toString());
                        profile_data.put("Firebase_Token", FirebaseInstanceId.getInstance().getToken());
                        users.put("UID", FirebaseAuth.getInstance().getUid());
                        users.put("Firebase_Token",FirebaseInstanceId.getInstance().getToken());
                        users.put("email",email.getText().toString());

                        DatabaseReference query = databaseReference.child(FirebaseAuth.getInstance().getUid()).child("User_Details");
                       query.updateChildren(profile_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    DatabaseReference databaseReference_user = FirebaseDatabase.getInstance().getReference("users");
                                    databaseReference_user.child(hotspot_password.getText().toString()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                                    Toast.makeText(getContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    progressBar_form.setVisibility(View.INVISIBLE);
                                   Intent intent = new Intent(getContext(),MainActivity.class);
                                   getContext().startActivity(intent);
                                   sign_inActivity.signinActivity.finish();


                                }else {
                                    progressBar_form.setVisibility(View.INVISIBLE);
                                    submit.setEnabled(true);
                                    submit.setAlpha(1f);
                                    String error = task.getException().getMessage();

                                }
                            }
                        });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error Uploading", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
