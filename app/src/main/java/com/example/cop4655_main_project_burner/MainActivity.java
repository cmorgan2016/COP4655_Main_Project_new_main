package com.example.cop4655_main_project_burner;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    Button login ,Reg, Google;
    FirebaseAuth auth;
    EditText emailin, passin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_reg);
        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        if (auth.getCurrentUser() == null) {

            login = findViewById(R.id.button);
            login.setOnClickListener(view -> {
                emailin =findViewById(R.id.editEmailAddress);
                passin=findViewById(R.id.editPassword);
                String email = emailin.getText().toString();
                String pass = passin.getText().toString();
                if(email.equals("") || pass.equals("")) {
                    Toast.makeText(getApplicationContext(), "Fill Fields",Toast.LENGTH_SHORT).show();
                } else{
                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(
                            task -> {
                                if(task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),  "Successfully Logged In", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this, Libaray.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext(),  "Login Failed", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            });
            Reg = findViewById(R.id.button2);
            Reg.setOnClickListener(view -> {
                emailin =findViewById(R.id.editEmailAddress);
                passin=findViewById(R.id.editPassword);
                String email = emailin.getText().toString();
                String pass = passin.getText().toString();
                if(email.matches("") || pass.matches("")){
                    Toast.makeText(MainActivity.this, "Fill Fields",Toast.LENGTH_SHORT).show();
                } else{
                        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
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
                        });
            }
            });

            }


    else{
        Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, Libaray.class);
            startActivity(intent);
            finish();
        }

    Google = findViewById(R.id.button3);
            Google.setOnClickListener(view -> {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            Intent intent = new Intent(MainActivity.this, Libaray.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    });
    }

}
