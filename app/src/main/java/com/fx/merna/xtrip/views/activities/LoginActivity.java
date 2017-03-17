package com.fx.merna.xtrip.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fx.merna.xtrip.MainActivity;
import com.fx.merna.xtrip.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnSignin, btnFBSignin;
    TextView txtSignup;

    private FirebaseAuth mAuth;
    private ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnSignin = (Button) findViewById(R.id.btnLogin);
        txtSignup = (TextView) findViewById(R.id.linkSignUp);

        mAuth = FirebaseAuth.getInstance();
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.title_progress_title));
        mAuthProgressDialog.setMessage(getString(R.string.message_progress_title));
        mAuthProgressDialog.setCancelable(false);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString());
            }
        });

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),signupActivity.class);
                startActivity(intent);
            }
        });

    }

    public void signInWithEmailAndPassword(String email,String password) {


        if (!validateForm()) {
            return;
        }

        mAuthProgressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"faild",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            mAuthProgressDialog.dismiss();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                Log.i("MY_TAG",user.getUid());
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        mAuthProgressDialog.dismiss();
                    }
                });


    }

    private boolean validateForm() {
        boolean valid = true;

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
