package com.example.firstfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private Button buttonResetPassword;
    private EditText edtEmail;
    private TextView textViewBackToSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
        edtEmail = findViewById(R.id.resetPassword_Email);
        textViewBackToSignIn = findViewById(R.id.textViewBackToSignIn);
        buttonResetPassword.setOnClickListener(this);
        textViewBackToSignIn.setOnClickListener(this);
    }
    //checks if email is valid
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

        private void forgotPassword() {
            String email = edtEmail.getText().toString().trim();
            boolean validEmail = isEmailValid(email);

            if (validEmail) {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(ForgotPassword.this);
                                dialog.setMessage("A link has been sent to your email to reset your password.");
                                //dialog.setTitle("Dialog Box");
                                dialog.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                Toast.makeText(getApplicationContext(), "Ok is clicked", Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                            }
                                        });

                                AlertDialog alertDialog = dialog.create();
                                alertDialog.show();
                            }
                        }
                    });
            }else {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                //stop the function from executing further
            }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonResetPassword){
            forgotPassword();
        }
        if(v == textViewBackToSignIn){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }
}
