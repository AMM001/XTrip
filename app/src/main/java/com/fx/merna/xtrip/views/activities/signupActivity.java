package com.fx.merna.xtrip.views.activities;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fx.merna.xtrip.R;
import com.fx.merna.xtrip.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signupActivity extends AppCompatActivity {


    EditText edtUserName,edtEmail,edtPassword;
    Button btnSignup;

    private FirebaseAuth mAuth;
    private ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtUserName =(EditText) findViewById(R.id.edtUserName);
        edtEmail = (EditText) findViewById(R.id.edtEmailField);
        edtPassword = (EditText) findViewById(R.id.edtPasswordField);
        btnSignup =(Button) findViewById(R.id.btnSignup);

        mAuth = FirebaseAuth.getInstance();
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.title_progress_title));
        mAuthProgressDialog.setMessage(getString(R.string.signup_message_progress_title));
        mAuthProgressDialog.setCancelable(false);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()) {
                    return;
                }

                String name=edtUserName.getText().toString();
                String email=edtEmail.getText().toString();
                String password=edtPassword.getText().toString();

                User newUser = new User(name,email,password);
                createNewUserWithEmailAndPassword(newUser);
            }
        });



    }
    private void createNewUserWithEmailAndPassword(final User newUser){


        mAuthProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {

                        }else{
                            mAuthProgressDialog.dismiss();
                            String uid=task.getResult().getUser().getUid();
                            newUser.setId(uid);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users").child(uid);
                            myRef.setValue(newUser);

                        }
                        mAuthProgressDialog.dismiss();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = edtUserName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            edtUserName.setError("Required.");
            valid = false;
        } else {
            edtUserName.setError(null);
        }

        String email = edtEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Required.");
            valid = false;
        } else {
            edtEmail.setError(null);
        }

        String password = edtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Required.");
            valid = false;
        } else {
            edtEmail.setError(null);
        }

        return valid;
    }
}
