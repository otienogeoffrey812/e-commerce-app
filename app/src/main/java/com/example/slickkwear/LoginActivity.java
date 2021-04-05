package com.example.slickkwear;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slickkwear.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private TextView register_link, forgotPassword;
    private TextInputLayout loginPhone, loginPassword;
    private MaterialCheckBox remember_me_checkbox;
    private Button loginBtn;
    private ProgressDialog loadingBar;
    private FirebaseFirestore userRef;
    private String generatedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPhone = (TextInputLayout) findViewById(R.id.login_phone);
        loginPassword = (TextInputLayout) findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.login_button);
        remember_me_checkbox = (MaterialCheckBox) findViewById(R.id.remember_me_checkbox);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);

        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        register_link = (TextView) findViewById(R.id.register_link);
        register_link.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(intent);
                    }
                }
        );

        forgotPassword.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                        startActivity(intent);

                    }
                }
        );

        userRef = FirebaseFirestore.getInstance();

        validateLiveUserData();

        loginBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        validateOnBtnClick();

                    }
                }
        );
    }
    private void validateLiveUserData(){
        loginPhone.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if (TextUtils.isEmpty(charSequence))
                        {
                            loginPhone.setError("Number cannot be empty!");
                        }
                        else if ((charSequence.toString().startsWith("0") && charSequence.toString().length() == 10) || charSequence.toString().length() > 9 || charSequence.toString().length() < 9)
                        {
                            loginPhone.setError("Number format: 705...");
                        }
                        else {
                            loginPhone.setError(null);
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );

        loginPassword.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if (TextUtils.isEmpty(charSequence))
                        {
                            loginPassword.setError("Password cannot be empty!");
                        }
                        else {
                            loginPassword.setError(null);
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );

    }

    private void validateOnBtnClick(){
        String phone = loginPhone.getEditText().getText().toString();
        String password = loginPassword.getEditText().getText().toString();

        String userID = "254"+phone;

        passWordHash(password);

        if (TextUtils.isEmpty(phone))
        {
            loginPhone.setError("Phone Number cannot be empty!");
        }
        else if (phone.startsWith("0") || phone.length() > 9 || phone.length() < 9)
        {
            loginPhone.setError("Phone Number format: 705...");
        }
        else if (TextUtils.isEmpty(password))
        {
            loginPassword.setError("Password cannot be empty!");
        }
        else
        {
            loadingBar.setTitle("Login in");
            loadingBar.setMessage("Please wait ...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            userRef.collection("Users").document(userID).get()
                    .addOnSuccessListener(
                            new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists())
                                    {
                                        if (userID.equals(documentSnapshot.getString("UserPhoneNumber")) && generatedPassword.equals(documentSnapshot.getString("UserPassword")))
                                        {
                                            loadingBar.dismiss();

                                            if (documentSnapshot.getString("UserVerified").equals("true"))
                                            {
                                                loginUser(userID, password);
                                            }
                                            else
                                            {
                                                Intent intent = new Intent(getApplicationContext(), PhoneVerificationActivity.class);
                                                startActivity(intent);
                                            }

                                        }
                                        else{

                                            loadingBar.dismiss();
                                            Toast.makeText(LoginActivity.this, "Phone number or Password is incorrect!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    else{
                                        loadingBar.dismiss();
                                        Toast.makeText(LoginActivity.this, "User +"+userID+" does not exist!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
        }
    }

    private void loginUser(String userID, String password){

        if (remember_me_checkbox.isChecked())
        {
            Paper.book().write(Prevalent.UserRememberMe, "true");
        }
        else {
            Paper.book().write(Prevalent.UserRememberMe, "false");
        }

        Paper.book().write(Prevalent.UserLoggedIn, "true");
        Paper.book().write(Prevalent.UserPhoneKey, userID);
        Paper.book().write(Prevalent.UserPasswordKey, password);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
//        finish();
//        return true;
    }

    private void passWordHash(String password) {
        String passwordToHash = password;
        generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }

}