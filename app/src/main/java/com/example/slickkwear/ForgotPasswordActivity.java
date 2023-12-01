package com.example.slickkwear;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout forgot_password_phone;
    private Button forgot_password_button;
    private ProgressDialog loadingBar;

    private String userID;

    private FirebaseFirestore userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgot_password_phone = (TextInputLayout) findViewById(R.id.forgot_password_phone);
        forgot_password_button = (Button) findViewById(R.id.forgot_password_btn);

        loadingBar = new ProgressDialog(this);

        userRef = FirebaseFirestore.getInstance();

        validateLiveData();
        forgot_password_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        validateDataOnBtnClick();

                    }
                }
        );

    }


    private void validateLiveData(){
        forgot_password_phone.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if (TextUtils.isEmpty(charSequence))
                        {
                            forgot_password_phone.setError("Number cannot be empty!");
                        }
                        else if ((charSequence.toString().startsWith("0") && charSequence.toString().length() == 10) || charSequence.toString().length() > 9 || charSequence.toString().length() < 9)
                        {
                            forgot_password_phone.setError("Number format: 705...");
                        }
                        else {
                            forgot_password_phone.setError(null);
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );
    }


    private void validateDataOnBtnClick() {
        String phone = forgot_password_phone.getEditText().getText().toString();

        userID = "254"+phone;

        if (TextUtils.isEmpty(phone))
        {
            forgot_password_phone.setError("Phone Number cannot be empty!");
        }
        else if (phone.startsWith("0") || phone.length() > 9 || phone.length() < 9)
        {
            forgot_password_phone.setError("Phone Number format: 705...");
        }
        else {
            loadingBar.setTitle("Sending reset code");
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
                                        saveResetCode(userID);
                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(ForgotPasswordActivity.this, "User +" +userID +" does not exist!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
        }
    }

    private void saveResetCode(String userID) {

        Random rand = new Random();
        String OTP = String.format("%04d", rand.nextInt(10000));

        userRef.collection("Users").document(userID).update("UserOTP", OTP)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                sendResetCode(userID, OTP);
                            }
                        }
                );
    }

    private void sendResetCode(String userID, String OTP) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                // Send Alert

//                String baseUrl = "";
//                int partnerId = ;
//                String apiKey = "";
//                String shortCode = "";
//
//                SmsGateway gateway = new SmsGateway(baseUrl, partnerId, apiKey, shortCode);
//
//                String[] strings = {userID};
//                String user_msg = OTP + ": is your Password Reset Code for Slickk Wear App.";
//
//                try {
//                    String res = gateway.sendBulkSms(user_msg, strings);
//                    System.out.println(res);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }
        }).start();

        loadingBar.dismiss();
        Intent intent = new Intent(getApplicationContext(), ResetCodeActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

}