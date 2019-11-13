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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AdminsignActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText txEmail, txPass, txName;
    private Button btnSubmit, btnLogin;
    private CheckBox showPass;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminsign);
        initView();

    }

    private void initView(){
        txEmail = findViewById(R.id.txNewEmailAdmin);
        txPass = findViewById(R.id.txNewPasswordAdmin);
        txName = findViewById(R.id.txNewNameAdmin);
        btnSubmit = findViewById(R.id.btn_submitAdmin);
        btnLogin = findViewById(R.id.btn_loginAdmin);
        showPass = findViewById(R.id.showNewPassAdmin);
        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        showPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showNewPassAdmin :
                if(showPass.isChecked()){
                    //Saat Checkbox dalam keadaan Checked, maka password akan di tampilkan
                    txPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //Jika tidak, maka password akan di sembuyikan
                    txPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.btn_submitAdmin :
                inputAdmin();
                break;
            case R.id.btn_loginAdmin :
                startActivity(new Intent(AdminsignActivity.this, LoginActivity.class));
                finish();
        }

    }


    private void inputAdmin(){
        final String emailAdmin = txEmail.getText().toString().trim();
        final String passwordAdmin = txPass.getText().toString().trim();
        final String nameAdmin = txName.getText().toString().trim();
        if (emailAdmin.isEmpty()) {
            txEmail.setError("Email can't be empty");
        }
        // jika email not valid
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailAdmin).matches()) {
            txEmail.setError("Email is incorrect");
        }
        // jika password kosong
        else if (passwordAdmin.isEmpty()) {
            txPass.setError("Password can't be empty");
        }
        //jika password kurang dari 6 karakter
        else if (passwordAdmin.length() < 6) {
            txPass.setError("Password must consist of at least 6 characters");
        }
        else if (nameAdmin.isEmpty()){
            txName.setError("Name can't be empty");
        }
        else{
            auth.createUserWithEmailAndPassword(emailAdmin, passwordAdmin)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference("user");
                                String userId = auth.getUid();
                                //String userId = database.push().getKey();
                                Map<String, Users> users = new HashMap<>();
                                //users.put(emailAdmin, new Users(nameAdmin, emailAdmin, passwordAdmin));
                                database.child(userId).setValue(new Users(nameAdmin, emailAdmin, passwordAdmin,"admin"));

                                txEmail.setText("");
                                txName.setText("");
                                txPass.setText("");

                                auth.getCurrentUser().sendEmailVerification();
                                Log.d("wewUser", userId);
                                Toast.makeText(AdminsignActivity.this, "User added", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(AdminsignActivity.this, LoginActivity.class));
                                finish();
                            } else if (!task.isSuccessful()) {
                                Toast.makeText(AdminsignActivity.this,
                                        "Can't sign up because " + task.getException().getMessage()
                                        , Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }

    }

}
