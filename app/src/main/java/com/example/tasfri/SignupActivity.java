package com.example.tasfri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText txName, txInstansi, txEmail, txPass;
    private Button btnSubmit, btnLogin;
    private CheckBox showPass;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initView();
    }

    private void initView(){
        txName = findViewById(R.id.txNewName);
        txInstansi = findViewById(R.id.txNewInstansi);
        txEmail = findViewById(R.id.txNewEmail);
        txPass = findViewById(R.id.txNewPassword);
        btnSubmit = findViewById(R.id.btn_submitUser);
        btnLogin = findViewById(R.id.btn_loginUser);
        showPass = findViewById(R.id.showNewPass);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btnLogin.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        showPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showNewPass :
                if(showPass.isChecked()){
                    //Saat Checkbox dalam keadaan Checked, maka password akan di tampilkan
                    txPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //Jika tidak, maka password akan di sembuyikan
                    txPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.btn_submitUser :
                //create user dengan firebase auth
                inputUsers();
                break;
            case R.id.btn_loginUser :
                //menuju login_activity
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
        }

    }


    private void inputUsers(){
        final String nameUser = txName.getText().toString().trim();
        final String instansiUser = txInstansi.getText().toString().trim();
        final String emailUser = txEmail.getText().toString().trim();
        final String passwordUser = txPass.getText().toString().trim();

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
        }
        else if (nameUser.isEmpty()){
            txName.setError("Name can't be empty");
        }
        else if (instansiUser.isEmpty()){
            txInstansi.setError("Agency can't be empty");
        }else{
            auth.createUserWithEmailAndPassword(emailUser, passwordUser)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference("user");
                                String userId = auth.getUid();

                                database.child(userId).setValue(new Users(instansiUser, nameUser, emailUser, passwordUser, "user"));
                                txEmail.setText("");
                                txName.setText("");
                                txPass.setText("");
                                auth.getCurrentUser().sendEmailVerification();
                                Log.d("wewUser", userId);
                                Toast.makeText(SignupActivity.this, "User added", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                finish();
                            } else if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this,
                                        "Can't sign up because " + task.getException().getMessage()
                                        , Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

}