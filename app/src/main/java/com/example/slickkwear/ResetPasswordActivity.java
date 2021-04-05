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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputLayout reset_password, reset_confirm_password;
    private Button reset_btn;
    private ProgressDialog loadingBar;
    private FirebaseFirestore userRef;
    private String userID, generatedPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        reset_password = (TextInputLayout) findViewById(R.id.reset_password);
        reset_confirm_password = (TextInputLayout) findViewById(R.id.reset_confirm_password);
        loadingBar = new ProgressDialog(this);

        reset_btn = (Button) findViewById(R.id.reset_password_btn);

        liveDataValidate();

        reset_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validateDataOnBtnClick();
                    }
                }
        );

        userRef = FirebaseFirestore.getInstance();
        userID = getIntent().getStringExtra("userID");

    }

    private void liveDataValidate(){

        reset_password.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if (TextUtils.isEmpty(charSequence))
                        {
                            reset_password.setError("Password cannot be empty!");
                        }
                        else if (charSequence.toString().length() < 8)
                        {
                            reset_password.setError("Minimum of 8 characters required");
                        }
                        else if (!isValidPassword(charSequence))
                        {
                            reset_password.setError("Must contain both numbers and letters");
                        }
                        else {
                            reset_password.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );

        reset_confirm_password.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        String password =  reset_password.getEditText().getText().toString();

                        if (TextUtils.isEmpty(charSequence))
                        {
                            reset_confirm_password.setError("Password cannot be empty!");
                        }
                        else if (!charSequence.toString().equals(password))
                        {
                            reset_confirm_password.setError("Passwords do not match!");
                        }
                        else {
                            reset_confirm_password.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );

    }
    private static boolean isValidPassword(CharSequence charSequence) {
        String n = ".*[0-9].*";
        String caps_l = ".*[A-Z].*";
        String small_l = ".*[a-z].*";
        return charSequence.toString().matches(n) && ( charSequence.toString().matches(caps_l) || charSequence.toString().matches(small_l));
    }

    private void validateDataOnBtnClick(){

        String password = reset_password.getEditText().getText().toString();
        String confirm_password = reset_confirm_password.getEditText().getText().toString();

        if (TextUtils.isEmpty(password))
        {
            reset_password.setError("Password cannot be empty!");
        }
        else if (password.length() < 8)
        {
            reset_password.setError("Minimum of 8 characters required");
        }
        else if (!isValidPassword(password))
        {
            reset_password.setError("Must contain both numbers and letters");
        }
        else if (TextUtils.isEmpty(confirm_password))
        {
            reset_confirm_password.setError("Password cannot be empty!");
        }
        else if (!confirm_password.equals(password))
        {
            reset_confirm_password.setError("Passwords do not match!");
        }
        else {
            loadingBar.setTitle("Resetting Password");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            saveNewPassword(password, confirm_password);

        }

    }

    private void saveNewPassword(String password, String confirm_password) {

        passWordHash(password);

        userRef.collection("Users").document(userID).update("UserPassword", generatedPassword)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loadingBar.dismiss();
                                Toast.makeText(ResetPasswordActivity.this, "Password reset successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                );
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