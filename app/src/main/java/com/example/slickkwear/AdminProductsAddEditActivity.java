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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.slickkwear.Model.SliderItem;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AdminProductsAddEditActivity extends AppCompatActivity {

    private TextInputLayout addProductName, addProductPrice, addProductDescription;
    private MaterialButton addProductButton;
    private ShapeableImageView addProductImage;
    private Spinner addProductCategory;
    private ProgressDialog loadingBar;

    private String productUniqueID, saveCurrentDate, saveCurrentTime, productName, productPrice, productDescription, productCategoryID;
    private String downloadImageUrl;

    private static final int GalleryPick = 1;
    private Uri ImageUri;

    private StorageReference productImagesRef;
    private FirebaseFirestore productRef;
    private Query query;

    private Toolbar toolbar;

    List<SpinnerItems> categories;

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
        productRef = FirebaseFirestore.getInstance();

        addProductImage = (ShapeableImageView) findViewById(R.id.admin_add_product_image);
        addProductName = (TextInputLayout) findViewById(R.id.admin_add_product_name);
        addProductPrice = (TextInputLayout) findViewById(R.id.admin_add_product_price);
        addProductDescription = (TextInputLayout) findViewById(R.id.admin_add_product_description);
        addProductCategory = (Spinner) findViewById(R.id.admin_add_product_category);
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

        categorySpinner();

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


    private void categorySpinner() {

        // Spinner click listener
//        addProductCategory.setOnItemSelectedListener(this);

        query = productRef.collection("Categories");
        // Spinner Drop down elements
        categories = new ArrayList<SpinnerItems>();

        categories.add(new SpinnerItems("Select category", ""));
        query.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot result : queryDocumentSnapshots)
                        {
                            categories.add(new SpinnerItems(result.getString("CategoryName"),result.getString("CategoryUniqueID")));
                        }

                    }
                }
        );


        // Creating adapter for spinner
        ArrayAdapter<SpinnerItems> dataAdapter = new ArrayAdapter<SpinnerItems>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        addProductCategory.setAdapter(dataAdapter);
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

        int cat_position =  addProductCategory.getSelectedItemPosition();
        productCategoryID = categories.get(cat_position).spinnerItemID;



        if(ImageUri == null)
        {
            Toast.makeText(this, "Product Image cannot be empty !", Toast.LENGTH_SHORT).show();
        }
        else  if (TextUtils.isEmpty(productCategoryID))
        {
            Toast.makeText(this, "Product Category cannot be empty !", Toast.LENGTH_SHORT).show();
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
        productMap.put("ProductCategory", productCategoryID);
        productMap.put("ProductStatus", "active");

        productRef.collection("Products").document(productUniqueID).set(productMap)
                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadingBar.dismiss();
                        Toast.makeText(getApplicationContext(), "Product Added Successfully.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), AdminProductsActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingBar.dismiss();
                        Toast.makeText(getApplicationContext(), "Error!!!! Product not added.", Toast.LENGTH_SHORT).show();
                    }
                });


    }


}