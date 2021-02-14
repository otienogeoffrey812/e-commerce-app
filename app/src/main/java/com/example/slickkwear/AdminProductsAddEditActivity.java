package com.example.slickkwear;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminProductsAddEditActivity extends AppCompatActivity {

    private TextInputLayout addProductName, addProductPrice, addProductDescription;
    private MaterialButton addProductButton;
    private ShapeableImageView addProductImage;
    private ProgressDialog loadingBar;

    private String productUniqueID, saveCurrentDate, saveCurrentTime, productName, productPrice, productDescription;
    private String downloadImageUrl;

    private static final int GalleryPick = 1;
    private Uri ImageUri;

    private StorageReference productImagesRef;
    private DatabaseReference productRef;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_products_add_edit);

        toolbar = findViewById(R.id.main_toolbar);
//        toolbar.setTitle("Admin");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_icon);

        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        addProductImage = (ShapeableImageView) findViewById(R.id.admin_add_product_image);
        addProductName = (TextInputLayout) findViewById(R.id.admin_add_product_name);
        addProductPrice = (TextInputLayout) findViewById(R.id.admin_add_product_price);
        addProductDescription = (TextInputLayout) findViewById(R.id.admin_add_product_description);
        loadingBar = new ProgressDialog(this);

        addProductButton = (MaterialButton) findViewById(R.id.admin_add_product_btn);

        addProductImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openGallery();
                    }
                }
        );

        addProductButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validateProductData();
                    }
                }
        );
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
     //Returns to the previous page
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            addProductImage.setImageURI(ImageUri);
        }
    }


    private void validateProductData() {
        productName = addProductName.getEditText().getText().toString();
        productPrice = addProductPrice.getEditText().getText().toString();
        productDescription = addProductDescription.getEditText().getText().toString();

        if(ImageUri == null)
        {
            Toast.makeText(this, "Product Image cannot be empty !", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(productName))
        {
            Toast.makeText(this, "Product name cannot be empty !", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(productPrice))
        {
            Toast.makeText(this, "Product price cannot be empty !", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(productDescription))
        {
            Toast.makeText(this, "Product description cannot be empty !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            storeProductDetails();
        }

    }

    private void storeProductDetails() {
        loadingBar.setTitle("Adding Product");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("YYYY/MM/dd");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productUniqueID = saveCurrentDate + saveCurrentTime;
        productUniqueID = productUniqueID.replaceAll("[^\\d]", "");

        StorageReference filepath = productImagesRef.child(ImageUri.getLastPathSegment() + productUniqueID + "jpg");
        final UploadTask uploadTask = filepath.putFile(ImageUri);

        uploadTask.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String message = e.toString();
                        Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
        ).addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                        Toast.makeText(getApplicationContext(), "Product Image Uploaded Successfully...", Toast.LENGTH_SHORT).show();

                        Task<Uri> uriTask = uploadTask.continueWithTask(
                                new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                                        if (!task.isSuccessful())
                                        {
                                            throw task.getException();
                                        }
                                        downloadImageUrl = filepath.getDownloadUrl().toString();
                                        return filepath.getDownloadUrl();

                                    }
                                }
                        ).addOnCompleteListener(
                                new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {

                                        if (task.isSuccessful())
                                        {
                                            downloadImageUrl = task.getResult().toString();
//                                            Toast.makeText(getApplicationContext(), "Getting Product Image Url Successfully.", Toast.LENGTH_SHORT).show();
                                            saveProductInfoToDatabase();
                                        }
                                    }
                                }
                        );

                    }
                }
        );
    }

    private void saveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("ProductUniqueID", productUniqueID);
        productMap.put("DateCreated", saveCurrentDate);
        productMap.put("TimeCreated", saveCurrentTime);
        productMap.put("ProductName", productName);
        productMap.put("ProductImage", downloadImageUrl);
        productMap.put("ProductPrice", productPrice);
        productMap.put("ProductDescription", productDescription);
        productMap.put("ProductCategory", "null");
        productMap.put("ProductStatus", "active");

        productRef.child(productUniqueID).updateChildren(productMap)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    loadingBar.dismiss();
                                    Toast.makeText(getApplicationContext(), "Product Added Successfully.", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), AdminProductsActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    loadingBar.dismiss();
                                    Toast.makeText(getApplicationContext(), "Error!!!! Product not added.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                );
    }


}