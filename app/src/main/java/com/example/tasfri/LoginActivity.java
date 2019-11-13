package com.example.tasfri;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogin;
    private CheckBox showPass;
    private EditText txEmail, txPass;
    private FirebaseAuth auth;
    FirebaseUser user;
    private TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        txEmail = findViewById(R.id.txEmail);
        txPass = findViewById(R.id.txPass);
        btnLogin = findViewById(R.id.btn_login);
        showPass = findViewById(R.id.showPass);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        showPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        forgotPass = findViewById(R.id.forgot_pass);
        forgotPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login : login();
                break;
            case R.id.showPass :
                if(showPass.isChecked()){
                    //Saat Checkbox dalam keadaan Checked, maka password akan di tampilkan
                    txPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //Jika tidak, maka password akan di sembuyikan
                    txPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.forgot_pass : startActivity(new Intent(LoginActivity.this, ForgotpassActivity.class));
                break;
        }
    }



    private void login(){
        //menampung imputan user
        final String emailUser = txEmail.getText().toString().trim();
        final String passwordUser = txPass.getText().toString().trim();
        //validasi email dan password
        // jika email kosong
        if (emailUser.isEmpty()) {
            txEmail.setError("Email can't be empty");
        }
        // jika email not valid
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()) {
            txEmail.setError("Email is incorrect");
        }
        // jika password kosong
        else if (passwordUser.isEmpty()) {
            txPass.setError("Password can't be empty");
        }
        //jika password kurang dari 6 karakter
        else if (passwordUser.length() < 6) {
            txPass.setError("Password must consist of at least 6 characters");
        } else {
            auth.signInWithEmailAndPassword(emailUser, passwordUser)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // ketika gagal locin maka akan do something
                            if (task.isSuccessful() && !auth.getCurrentUser().isEmailVerified()){
                                auth.getCurrentUser().sendEmailVerification();
                                Toast.makeText(LoginActivity.this, "Verify your account, please check your mailbox", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                            }else {
                                if (task.isSuccessful() && auth.getCurrentUser().isEmailVerified()) {
                                    txEmail.setText("");
                                    txPass.setText("");
                                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                }
                                else {
                                    FirebaseAuth.getInstance().signOut();
                                    Toast.makeText(LoginActivity.this,
                                            "Can't login because " + task.getException().getMessage()
                                            , Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
    }

}

