package com.example.slickkwear;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout registerPhone, registerName, registerPassword, registerConfirmPassword;
    private Button registerButton;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerName = (TextInputLayout) findViewById(R.id.register_name);
        registerPhone = (TextInputLayout) findViewById(R.id.register_phone);
        registerPassword = (TextInputLayout) findViewById(R.id.register_password);
        registerConfirmPassword =(TextInputLayout) findViewById(R.id.register_confirm_password);
        registerButton = findViewById(R.id.register_button);
        loadingBar = new ProgressDialog(this);

        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validateRegistrationData();
                    }
                }
        );
    }

    private void validateRegistrationData() {
        String name = registerName.getEditText().getText().toString();
        String phone = registerPhone.getEditText().getText().toString();
        String password = registerPassword.getEditText().getText().toString();
        String confirmPassword = registerConfirmPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Phone cannot be empty", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Confirm password cannot be empty", Toast.LENGTH_LONG).show();
        }
//        else if (password.length() < 8) {
//            Toast.makeText(this, "Password must be 8 or more characters", Toast.LENGTH_LONG).show();
//        } else if (!password.equals(confirmPassword)) {
//            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
//        }
        else {
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Please wait, account is being created...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

//            validateUserRegistration(name,phone, email, password);


            Intent intent = new Intent(getApplicationContext(), PhoneVerificationActivity.class);
            intent.putExtra("phoneNo", phone);
            startActivity(intent);
        }
    }
}