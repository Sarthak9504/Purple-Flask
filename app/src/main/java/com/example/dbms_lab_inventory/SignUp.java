package com.example.dbms_lab_inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private TextInputEditText college_name;
    private TextInputEditText dep_email;
    private TextInputEditText state;
    private TextInputEditText city;
    private TextInputEditText password;
    private TextInputEditText con_password;
    private Button signUpBtn;
    private TextView signInBtn;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> dep_list;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference user_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        dep_dropdown();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp.this.onClick(signInBtn);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp.this.onClick(signUpBtn);
            }
        });
    }

    private void onClick(Object o){
        if(o.equals(signInBtn)){
            startActivity(new Intent(SignUp.this,LoginActivity.class));
        }
        else{
            if(checker()){
                String college = college_name.getText().toString().trim();
                String dep_name = autoCompleteTextView.getText().toString().trim();
                String email = dep_email.getText().toString().trim();
                String state_text = state.getText().toString().trim();
                String city_text = city.getText().toString().trim();
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(dep_email.getText().toString().trim(), con_password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putInt("isLogged",1);
                            myEdit.putString("college_name",college_name.getText().toString().trim());
                            myEdit.putString("Dep_name",autoCompleteTextView.getText().toString().trim());
                            myEdit.putString("Dep_email",dep_email.getText().toString().trim());
                            myEdit.apply();

                            firebaseDatabase = FirebaseDatabase.getInstance();
                            user_ref = firebaseDatabase.getReference("College/University");
                            user_ref.child(college).setValue(new RBUtility(dep_name,email,state_text,city_text));

                            Toast.makeText(SignUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this,MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignUp.this, "Registration Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void dep_dropdown(){
        String[] list = {"Computer Engineering","EnTC Engineering","Mechanical Engineering","InC Engineering","Chemical Engineering"};
        dep_list = new ArrayAdapter<>(this,R.layout.list_item,list);
        autoCompleteTextView.setAdapter(dep_list);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = dep_list.getItem(i);
            }
        });
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
        if (autoCompleteTextView.getText().toString().isEmpty()){
            autoCompleteTextView.setError("Department required");
            autoCompleteTextView.requestFocus();
            return false;
        }
        else if(!dep_email.getText().toString().isEmpty()){
            if(!check_mail_vit()){
                return false;
            }
        }
        else if(!password.getText().toString().equals(con_password.getText().toString())){
            con_password.setError("Confirm password should be same as password");
            con_password.requestFocus();
            return false;
        }
        return true;
    }

    private boolean check_mail_vit(){
        String mail = dep_email.getText().toString();
        if(!mail.substring(mail.length()-8).equals("vit.edu ")){
            dep_email.setError("Required Vit mail");
            dep_email.requestFocus();
            return false;
        }
        return true;
    }


    private void init(){
        college_name = findViewById(R.id.editTextCollege);
        dep_email = findViewById(R.id.editTextDep_id);
        state = findViewById(R.id.editTextState);
        city = findViewById(R.id.editTextCity);
        password = findViewById(R.id.editTextPass);
        con_password = findViewById(R.id.editTextCon_Pass);
        signUpBtn = findViewById(R.id.signUpButton);
        signInBtn = findViewById(R.id.signInBtn);
        autoCompleteTextView = findViewById(R.id.auto_text_view);
    }

}