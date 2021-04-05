package com.example.slickkwear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout registerPhone, registerName, registerPassword, registerConfirmPassword;
    private Button registerButton;
    private ProgressDialog loadingBar;

    private FirebaseFirestore userRef;
    private String  saveCurrentDate, saveCurrentTime;
    private TextView loginLink;
    private String generatedPassword, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userRef = FirebaseFirestore.getInstance();

        registerName = (TextInputLayout) findViewById(R.id.register_name);
        registerPhone = (TextInputLayout) findViewById(R.id.register_phone);
        registerPassword = (TextInputLayout) findViewById(R.id.register_password);
        registerConfirmPassword =(TextInputLayout) findViewById(R.id.register_confirm_password);
        registerButton = findViewById(R.id.register_button);
        loadingBar = new ProgressDialog(this);

        loginLink = (TextView) findViewById(R.id.register_login_link);
        loginLink.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
        );

        liveUserDataValidator();


        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnClickValidateRegistrationData();
                    }
                }
        );
    }

    private void liveUserDataValidator() {

        registerName.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if (TextUtils.isEmpty(charSequence))
                        {
                            registerName.setError("Name cannot be empty!");
                        }
                        else {
                            registerName.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );

        registerPhone.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if (TextUtils.isEmpty(charSequence))
                        {
                            registerPhone.setError("Number cannot be empty!");
                        }
                        else if ((charSequence.toString().startsWith("0") && charSequence.toString().length() == 10) || charSequence.toString().length() > 9 || charSequence.toString().length() < 9)
                        {
                            registerPhone.setError("Number format: 705...");
                        }
                        else {
                            registerPhone.setError(null);
//                            registerPhone.setEndIconDrawable(R.drawable.back_icon);
//                            registerPhone.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );

        registerPassword.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if (TextUtils.isEmpty(charSequence))
                        {
                            registerPassword.setError("Password cannot be empty!");
                        }
                        else if (charSequence.toString().length() < 8)
                        {
                            registerPassword.setError("Minimum of 8 characters required");
                        }
                        else if (!isValidPassword(charSequence))
                        {
                            registerPassword.setError("Must contain both numbers and letters");
                        }
                        else {
                            registerPassword.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );

        registerConfirmPassword.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        String password = registerPassword.getEditText().getText().toString();

                        if (TextUtils.isEmpty(charSequence))
                        {
                            registerConfirmPassword.setError("Password cannot be empty!");
                        }
                        else if (!charSequence.toString().equals(password))
                        {
                            registerConfirmPassword.setError("Passwords do not match!");
                        }
                        else {
                            registerConfirmPassword.setError(null);
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


    private void btnClickValidateRegistrationData() {
        String name = registerName.getEditText().getText().toString();
        String phone = registerPhone.getEditText().getText().toString();
        String password = registerPassword.getEditText().getText().toString();
        String confirmPassword = registerConfirmPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(name)) {
            registerName.setError("Name cannot be empty!");
        }
        else if (TextUtils.isEmpty(phone))
        {
            registerPhone.setError("Phone Number cannot be empty!");
        }
        else if (phone.startsWith("0") || phone.length() > 9 || phone.length() < 9)
        {
            registerPhone.setError("Phone Number format: 705...");
        }
        else if (TextUtils.isEmpty(password))
        {
            registerPassword.setError("Password cannot be empty!");
        }
        else if (password.length() < 8)
        {
            registerPassword.setError("Minimum of 8 characters required");
        }
        else if (!isValidPassword(password))
        {
            registerPassword.setError("Must contain both numbers and letters");
        }
        else if (TextUtils.isEmpty(confirmPassword))
        {
            registerConfirmPassword.setError("Password cannot be empty!");
        }
        else if (!confirmPassword.equals(password))
        {
            registerConfirmPassword.setError("Passwords do not match!");
        }
        else {
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            registerUser(name, phone, password);
//            checkIfUserExist();

        }
    }

//    private void checkIfUserExist() {
//    }

    private void registerUser(String name, String phone, String password) {

        userID = "254" + phone;

        userRef.collection("Users").document(userID).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            loadingBar.dismiss();
                            Toast.makeText(RegisterActivity.this, "User +" + userID + " exist!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            saveUserData(userID, name, password);
                        }

                    }
                }
        );
    }

    private void saveUserData(String userID, String name, String password) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("YYYY/MM/dd");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calendar.getTime());

//        String userID = saveCurrentDate + saveCurrentTime;
//        userID = userID.replaceAll("[^\\d]", "");
//        int randomNumber = new Random().nextInt(999999) + 100000;
//        userID = userID + randomNumber;

        Random rand = new Random();
        String OTP = String.format("%04d", rand.nextInt(10000));

        passWordHash(password);


        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("UseID", userID);
        productMap.put("UserName", name);
        productMap.put("UserPhoneNumber", userID);
        productMap.put("UserPassword", generatedPassword);
        productMap.put("UserOTP", OTP);
        productMap.put("UserStatus", "active");
        productMap.put("UserVerified", "false");
        productMap.put("CategoryDeleted", "false");

        userRef.collection("Users").document(userID).set(productMap)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        String baseUrl = "https://mysms.celcomafrica.com/api/services/sendsms/";
                                        int partnerId = 2881; // your ID here
                                        String apiKey = "d72d2587d85c517381ca0daa34ff4c9c"; // your API key
                                        String shortCode = "CELCOM_SMS"; // sender ID here e.g INFOTEXT, Celcom, e.t.c

                                        SmsGateway gateway = new SmsGateway(baseUrl, partnerId, apiKey, shortCode);

                                        String[] strings = {userID};
                                        String user_msg = OTP + ": is your Verification Code for Slickk Wear App.";

                                        try {
                                            String res = gateway.sendBulkSms(user_msg, strings);
                                            System.out.println(res);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }).start();

                                loadingBar.dismiss();
                                Intent intent = new Intent(getApplicationContext(), PhoneVerificationActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("userID", userID);
                                startActivity(intent);
                            }
                        }
                )
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingBar.dismiss();
                                Toast.makeText(getApplicationContext(), "Error! user not created", Toast.LENGTH_SHORT).show();
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