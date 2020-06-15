package com.example.firstfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private Button buttonLogin;
    private EditText edt_Email;
    private EditText edt_Password;
    private TextView textViewSignUp;
    private TextView textViewForgotPassword;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = findViewById(R.id.buttonLogin);
        edt_Email = findViewById(R.id.loginEmail);
        edt_Password = findViewById(R.id.loginPassword);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        textViewForgotPassword = findViewById(R.id.forgotPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        progressDialog = new ProgressDialog(this);
        buttonLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(this);


    }

    //checks if email is valid
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();



    }

    private void userLogin(){
        String email = edt_Email.getText().toString().trim();
        String password = edt_Password.getText().toString().trim();
        boolean validEmail = isEmailValid(email);



        if (validEmail) {

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                //stop the function from executing further
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
                //stop the function from executing further
                return;
            }
            //if the parameters are correct continue to show the progress dialog
            progressDialog.setMessage("Signing in...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //user is successfully registered and logged in
                                //Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                //Toast.makeText(LoginActivity.this, "Could not log in. Please try again", Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }
                        }
                    });

        }

        else {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            //stop the function from executing further
        }

    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogin){
            userLogin();
            //finish();
        }
        if(v == textViewSignUp){
            //finish();
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        }
        if(v == textViewForgotPassword){
            //finish();
            startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
        }
    }
}
