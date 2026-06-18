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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class CreateAccountActivity extends AppCompatActivity {

    EditText emailId, passwordId, confirmPassword;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtn;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailId = findViewById(R.id.email_edit_text);
        passwordId = findViewById(R.id.password_edit_text);
        confirmPassword = findViewById(R.id.confirm_password_edit_text);
        createAccountBtn = findViewById(R.id.create_account_button);
        progressBar = findViewById(R.id.progress_bar);
        loginBtn = findViewById(R.id.login_text_button);

        createAccountBtn.setOnClickListener(v-> createAccount());
        loginBtn.setOnClickListener(v-> startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class)));

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

    void createAccount(){
        String email = emailId.getText().toString();
        String password = passwordId.getText().toString();
        String confirmpass = confirmPassword.getText().toString();

        boolean isValidate = validateData(email, password, confirmpass);

        if (!isValidate){
            return;
        }

        createAccountInFirebase(email, password);

    }

    boolean validateData(String email, String password, String confirmPass){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailId.setError("Email ID is Invalid!");
            return false;
        }
        if (password.length()<6 || password.length()>10){
            passwordId.setError("Password Length is Invalid");
            return false;
        }
        if (!password.equals(confirmPass)){
            confirmPassword.setError("Password not Matched");
            return false;
        }
        return true;
    }

    void createAccountInFirebase(String email, String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()){
                            //created account successfully
                            Utility.showToast(CreateAccountActivity.this, "Account Created Successfully! Please Verify Your Email to Proceed");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }
                        else {
                            //create account unsuccessful
                            Utility.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
                }
        );

    }

    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }
}