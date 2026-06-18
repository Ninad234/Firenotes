package com.example.firenotes;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailId, passwordId;
    Button loginAccountBtn;
    ProgressBar progressBar;
    TextView signUpBtn, forgotPassBtn;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailId = findViewById(R.id.email_edit_text);
        passwordId = findViewById(R.id.password_edit_text);
        loginAccountBtn = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);
        signUpBtn = findViewById(R.id.signup_text_button);
        forgotPassBtn = findViewById(R.id.forgot_password_text);

        loginAccountBtn.setOnClickListener(v-> loginUSer());
        signUpBtn.setOnClickListener(v->startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));
        forgotPassBtn.setOnClickListener(v->startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

        View rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    hideKeyboard(currentFocus);
                }
            }
            v.performClick();
            return false;
        });
    }

    void loginUSer(){
        String email = emailId.getText().toString();
        String password = passwordId.getText().toString();

        boolean isValidate = validateData(email, password);

        if (!isValidate){
            return;
        }

        loginAccountInFirebase(email, password);
    }

    boolean validateData(String email, String password){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailId.setError("Email ID is Invalid!");
            return false;
        }
        if (password.length()<6 || password.length()>10){
            passwordId.setError("Password Length is Invalid");
            return false;
        }
        return true;
    }

    void loginAccountInFirebase(String email, String password){
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    //Login Successful
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    else{
                        Utility.showToast(LoginActivity.this, "Email Not Verified, Please Verify Your Email to Proceed");
                    }
                }
                else {
                    //Login Failed
                    Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginAccountBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            loginAccountBtn.setVisibility(View.VISIBLE);
        }
    }

}