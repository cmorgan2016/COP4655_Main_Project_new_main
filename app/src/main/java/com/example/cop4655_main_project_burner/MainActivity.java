package com.example.cop4655_main_project_burner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button login ,Reg;
    FirebaseAuth auth;
    EditText emailin, passin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_reg);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {

            login = findViewById(R.id.button);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emailin =findViewById(R.id.editEmailAddress);
                    passin=findViewById(R.id.editPassword);
                    String email = emailin.getText().toString();
                    String pass = passin.getText().toString();
                    if(email.matches("") || pass.matches("")) {
                        Toast.makeText(MainActivity.this, "Fill Fields",Toast.LENGTH_SHORT).show();
                    } else{
                        auth.signInWithEmailAndPassword(email, pass);
                                        new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()) {
                                                    Toast.makeText(MainActivity.this,  "Successfully Logged In", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(MainActivity.this, Libaray.class);
                                                    startActivity(intent);
                                                    finish();
                                                }else {
                                                    Toast.makeText(MainActivity.this,  "Login Failed", Toast.LENGTH_LONG).show();
                                                }
                                            }

                        };
                    }
                }
            });
            Reg = findViewById(R.id.button2);
            Reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emailin =findViewById(R.id.editEmailAddress);
                    passin=findViewById(R.id.editPassword);
                    String email = emailin.getText().toString();
                    String pass = passin.getText().toString();
                    if(email.matches("") || pass.matches("")){
                        Toast.makeText(MainActivity.this, "Fill Fields",Toast.LENGTH_SHORT).show();
                    } else{
                            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),
                                                "Registration successful!", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(MainActivity.this, Libaray.class);
                                                 startActivity(intent);
                                    }
                                    else {

                                        // Registration failed
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Registration failed!!"
                                                        + " Please try again later",
                                                Toast.LENGTH_LONG)
                                                .show();

                                        // hide the progress bar
                                    }
                                }
                            });
                }
                }
            });
        }
    else{
        Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show();
        }
}}
