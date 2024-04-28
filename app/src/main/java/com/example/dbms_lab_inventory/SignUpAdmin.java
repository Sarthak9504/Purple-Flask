package com.example.dbms_lab_inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpAdmin extends AppCompatActivity {

    private TextInputEditText college_name;
    private TextInputEditText dep_email;
    private TextInputEditText state;
    private TextInputEditText city;
    private TextInputEditText name;
    private TextInputEditText password;
    private TextInputEditText con_password;
    private TextInputEditText prn;
    private Button signUpBtn;
    private TextView signInBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference user_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_admin);

        init();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpAdmin.this.onClick(signInBtn);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpAdmin.this.onClick(signUpBtn);
            }
        });

    }

    private void onClick(Object o){
        if(o.equals(signInBtn)){
            startActivity(new Intent(SignUpAdmin.this,LoginActivity.class));
        }
        else{
            if(checker()){
                String college = college_name.getText().toString().trim();
                String email = dep_email.getText().toString().trim();
                String state_text = state.getText().toString().trim();
                String city_text = city.getText().toString().trim();
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(dep_email.getText().toString().trim(), con_password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putInt("isLogged", 1);
                            myEdit.putString("college_name", college_name.getText().toString().trim());
                            myEdit.putString("admin_email", dep_email.getText().toString().trim());
                            myEdit.putString("admin_name", name.getText().toString().trim());
                            myEdit.putString("admin_state", state.getText().toString().trim());
                            myEdit.putString("admin_city", city.getText().toString().trim());
                            myEdit.putString("admin_prn", prn.getText().toString().trim());
                            myEdit.putInt("Lab_count", 0);
                            myEdit.putInt("Equipment_count", 0);
                            myEdit.apply();

                            Toast.makeText(SignUpAdmin.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            Log.d("name",name.getText().toString());
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            user_ref = firebaseDatabase.getReference("College or University");
                            user_ref.child(college).child("Admin").setValue(new AdminUtility(name.getText().toString(),email, state_text, city_text,prn.getText().toString()));

                            startActivity(new Intent(SignUpAdmin.this, AdminDashboard.class));
                            finish();
                        }
                        else {
                            Toast.makeText(SignUpAdmin.this, "Registration Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private boolean checker(){
        if(college_name.getText().toString().isEmpty()){
            college_name.setError("Field cannot be empty");
            college_name.requestFocus();
            return false;
        }
        if(dep_email.getText().toString().isEmpty()){
            dep_email.setError("Field cannot be empty");
            dep_email.requestFocus();
            return false;
        }
        if(state.getText().toString().isEmpty()){
            state.setError("Field cannot be empty");
            state.requestFocus();
            return false;
        }
        if(city.getText().toString().isEmpty()){
            city.setError("Field cannot be empty");
            city.requestFocus();
            return false;
        }
        if(name.getText().toString().isEmpty()){
            name.setError("Field cannot be empty");
            name.requestFocus();
            return false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Field cannot be empty");
            password.requestFocus();
            return false;
        }
        if(con_password.getText().toString().isEmpty()){
            con_password.setError("Field cannot be empty");
            con_password.requestFocus();
            return false;
        }
        if(prn.getText().toString().isEmpty()){
            prn.setError("Field cannot be empty");
            prn.requestFocus();
            return false;
        }
//        else if(!dep_email.getText().toString().isEmpty()){
//            if(!check_mail_vit()){
//                return false;
//            }
//        }
        else if(!password.getText().toString().equals(con_password.getText().toString())){
            con_password.setError("Confirm password should be same as password");
            con_password.requestFocus();
            return false;
        }
        return true;
    }

//    private boolean check_mail_vit(){
//        String mail = dep_email.getText().toString().trim();
//        if(!mail.substring(mail.length()-8).equals("vit.edu")){
//            dep_email.setError("Required Vit mail");
//            dep_email.requestFocus();
//            return false;
//        }
//        return true;
//    }


    private void init(){
        college_name = findViewById(R.id.editTextCollege);
        dep_email = findViewById(R.id.editTextDep_id);
        state = findViewById(R.id.editTextState);
        city = findViewById(R.id.editTextCity);
        password = findViewById(R.id.editTextPass);
        con_password = findViewById(R.id.editTextCon_Pass);
        signUpBtn = findViewById(R.id.signUpButton);
        signInBtn = findViewById(R.id.signInBtn);
        name = findViewById(R.id.editTextName);
        prn = findViewById(R.id.editTextPRN);
    }
}