package com.example.asus.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordRepeat;
    private Button btnRegister;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etEmail = (EditText) findViewById(R.id.register_etEmail);
        etPassword = (EditText) findViewById(R.id.register_etPassword);
        btnRegister = (Button) findViewById(R.id.register_btnRegister);
        etPasswordRepeat = (EditText) findViewById(R.id.register_etPasswordRepeat);
        mProgress = new ProgressDialog(this);

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startRegister();
            }
        });



    }

    private void startRegister()
    {
        final String eMail = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String passwordRepeat = etPasswordRepeat.getText().toString().trim();

        if (!TextUtils.isEmpty(eMail) && !TextUtils.isEmpty(password) && TextUtils.equals(password, passwordRepeat))
        {
            mProgress.setMessage("Oturum Açılıyor ...");
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(eMail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    String user_id = mAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db = mDatabase.child("User").child(user_id);

                    current_user_db.child("email").setValue(mAuth.getCurrentUser().getEmail().toString());

                    mProgress.dismiss();

                    Intent credentialsIntent = new Intent(RegisterActivity.this, CredentialsActivity.class);
                    credentialsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(credentialsIntent);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Kayıt Olma Hatası !", Toast.LENGTH_SHORT).show();
                }
            });
        }else
        {
            Toast.makeText(getApplicationContext(),"Parolalar Eşleşmedi, Yeniden Girin", Toast.LENGTH_SHORT).show();
        }
    }
}
