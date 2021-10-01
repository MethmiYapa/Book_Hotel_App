package com.example.bookhotelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegister extends AppCompatActivity implements View.OnClickListener {
    private EditText Familyname, Firstname, Password, Email, Country, Mobileno;
    private Button CreateAccount;
    private FirebaseAuth mAuth;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_user_register);
        Familyname = (EditText) findViewById(R.id.reglastname);
        Firstname = (EditText) findViewById(R.id.regfirstname);
        Password = (EditText) findViewById(R.id.password);
        Email = (EditText) findViewById(R.id.email);
        Country = (EditText) findViewById(R.id.CountryRegion);
        Mobileno = (EditText) findViewById(R.id.mobile);


        CreateAccount = (Button) findViewById(R.id.creteacc);
        CreateAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.creteacc:

                registeruser();
        }
    }

    private void registeruser() {
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        String familyname= Familyname.getText().toString().trim();
        String firstname= Firstname.getText().toString().trim();
        String country= Country.getText().toString().trim();
        String mobileno= Mobileno.getText().toString().trim();
        if (email.isEmpty()) {
            Email.setError("Please enter email");
            Email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Plase enter valid email");
            Email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Password.setError("Please enter password");
            Password.requestFocus();
            return;
        }

        if (password.length() < 8) {
            Password.setError("Minimum 8 characters");
            Password.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password).
         addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful()) {

                     Newuser newuser = new Newuser(familyname, firstname, password, email, country, mobileno);
                     FirebaseDatabase.getInstance().getReference("Newusers").
                             child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                             setValue(newuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful()) {
                                 Toast.makeText(UserRegister.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                             } else {
                                 Toast.makeText(UserRegister.this, "Unuccessfully", Toast.LENGTH_LONG).show();
                             }
                         }
                     });
                 }
                 else{
                     Toast.makeText(UserRegister.this, "Unuccessfully", Toast.LENGTH_LONG).show();
                 }

             }
         });

         }
}