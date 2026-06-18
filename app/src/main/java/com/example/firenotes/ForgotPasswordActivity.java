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

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailId;
    Button sendRestLinkBtn;
    ProgressBar progressBar;
    TextView loginbtn;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailId = findViewById(R.id.email_edit_text);
        sendRestLinkBtn = findViewById(R.id.forgot_pass_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginbtn = findViewById(R.id.login_text_button);

        sendRestLinkBtn.setOnClickListener(v-> sendLink());
        loginbtn.setOnClickListener(v->startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class)));

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

    void sendLink(){
        String email = emailId.getText().toString();
        boolean isValidate = validateData(email);
        if (!isValidate){
            return;
        }

        sendLinkThroughFirebase(email);
    }

    boolean validateData(String email){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailId.setError("Email ID is Invalid!");
            return false;
        }
        return true;
    }

    void sendLinkThroughFirebase(String email){
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    //Login Successful
                    Utility.showToast(ForgotPasswordActivity.this, "Reset Password Link has been sent to your Email ID");
                }
                else {
                    //Login Failed
                    Utility.showToast(ForgotPasswordActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            sendRestLinkBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            sendRestLinkBtn.setVisibility(View.VISIBLE);
        }
    }

}