package com.example.slickkwear;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneVerificationActivity extends AppCompatActivity {

    private TextInputLayout verify1, verify2, verify3, verify4;
    private Button verificationButtton;
    private TextView textViewMessage;;
    String phoneNo;
    String mVerificationId;
    FirebaseAuth mAuth;

    private static final int REQ_USER_CONSENT = 200;
//    private static final int READ_SMS = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        verify1 = (TextInputLayout) findViewById(R.id.verify_1);
        verify2 = (TextInputLayout) findViewById(R.id.verify_2);
        verify3 = (TextInputLayout) findViewById(R.id.verify_3);
        verify4 = (TextInputLayout) findViewById(R.id.verify_4);

        verificationButtton = (Button) findViewById(R.id.verification_button);
        verificationButtton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        verificationCodeSubmit();
                    }
                }
        );

//        checkPermission(
//                Manifest.permission.READ_SMS,
//                READ_SMS);

//        phoneNo = "+254"+getIntent().getStringExtra("phoneNo");

//        mAuth= FirebaseAuth.getInstance();
        startSmsUserConsent();
        verificationCode();
    }


    // Function to check and request permission
//    public void checkPermission(String permission, int requestCode)
//    {
//
//        // Checking if permission is not granted
//        if (ContextCompat.checkSelfPermission(
//                PhoneVerificationActivity.this,
//                permission)
//                == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat
//                    .requestPermissions(
//                            PhoneVerificationActivity.this,new String[] { permission },requestCode);
//        }
//        else {
//                   //Permission already granted
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults)
//    {
//        super
//                .onRequestPermissionsResult(requestCode,
//                        permissions,
//                        grantResults);
//
//        if (requestCode == READ_SMS) {
//
//            // Checking whether user granted the permission or not.
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                // Permission granted
//
////                String message = Intent.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
////                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//            }
//            else {
//                //Permission denied
//            }
//        }
//    }

    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                textViewMessage.setText(
//                        String.format("%s - %s", getString(R.string.received_message), message));

                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        Pattern pattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String OTP = matcher.group(0);
            
            char n1 = OTP.charAt(0);
            char n2 = OTP.charAt(1);
            char n3 = OTP.charAt(2);
            char n4 = OTP.charAt(3);

            verify1.getEditText().setText(n1+"");
            verify2.getEditText().setText(n2+"");
            verify3.getEditText().setText(n3+"");
            verify4.getEditText().setText(n4+"");

//            Toast.makeText(this, ""+n1+","+n2+","+n3+","+n4, Toast.LENGTH_SHORT).show();
            verificationCodeSubmit();
        }
    }

    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
//                        Toast.makeText(PhoneVerificationActivity.this, "Message received !!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure() {

                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }


    private void verificationCode() {

        verify1.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() == 1)
                        {
                            verify2.getEditText().requestFocus();
                            verify1.setError(null);
                        }
                        else {
                            verify1.getEditText().requestFocus();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );

        verify2.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() == 1)
                        {
                            verify3.getEditText().requestFocus();
                            verify2.setError(null);
                        }
                        else {
                            verify1.getEditText().requestFocus();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );

        verify3.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() == 1)
                        {
                            verify4.getEditText().requestFocus();
                            verify3.setError(null);
                        }
                        else {
                            verify2.getEditText().requestFocus();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );

        verify4.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() == 0)
                        {
                            verify3.getEditText().requestFocus();

                        }
                        else {
                            verify4.setError(null);
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );

    }


    private void verificationCodeSubmit() {
        String v1 = verify1.getEditText().getText().toString();
        String v2 = verify2.getEditText().getText().toString();
        String v3 = verify3.getEditText().getText().toString();
        String v4 = verify4.getEditText().getText().toString();

        if (TextUtils.isEmpty(v1))
        {
            verify1.setError("Required!");
        }
        if (TextUtils.isEmpty(v2))
        {
            verify2.setError("Required!");
        }
        if (TextUtils.isEmpty(v3))
        {
            verify3.setError("Required!");
        }
        if (TextUtils.isEmpty(v4))
        {
            verify4.setError("Required!");
        }
        
        if (!TextUtils.isEmpty(v1) && !TextUtils.isEmpty(v2) && !TextUtils.isEmpty(v3) && !TextUtils.isEmpty(v4))
        {
            Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
        }

    }
}