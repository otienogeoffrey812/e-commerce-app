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
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminCategoriesAddEditActivity extends AppCompatActivity {

    private TextInputLayout addCategoryName;
    private MaterialButton addCategoryButton;
    private ShapeableImageView addCategoryImage;
    private ProgressDialog loadingBar;

    private String categoryUniqueID, saveCurrentDate, saveCurrentTime, categoryName;
    private String downloadImageUrl;

    private static final int GalleryPick = 1;
    private Uri ImageUri;

    private StorageReference categoryImagesRef;
    private FirebaseFirestore categoryRef;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_categories_add_edit);

        toolbar = findViewById(R.id.main_toolbar);
//        toolbar.setTitle("Admin");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_icon);

        categoryImagesRef = FirebaseStorage.getInstance().getReference().child("Category Images");
        categoryRef = FirebaseFirestore.getInstance();

        addCategoryImage = (ShapeableImageView) findViewById(R.id.admin_add_category_image);
        addCategoryName = (TextInputLayout) findViewById(R.id.admin_add_category_name);
        addCategoryButton = (MaterialButton) findViewById(R.id.admin_add_category_btn);
        loadingBar = new ProgressDialog(this);

        addCategoryImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openGallery();
                    }
                }
        );
        addCategoryButton.setOnClickListener(
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
            addCategoryImage.setImageURI(ImageUri);
        }
    }

    private void validateProductData() {
        categoryName = addCategoryName.getEditText().getText().toString();

        if(ImageUri == null)
        {
            Toast.makeText(this, "Category Image cannot be empty !", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(categoryName))
        {
            Toast.makeText(this, "Category name cannot be empty !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            storeProductDetails();
        }

    }

    private void storeProductDetails() {
        loadingBar.setTitle("Adding Category");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("YYYY/MM/dd");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calendar.getTime());

        categoryUniqueID = saveCurrentDate + saveCurrentTime;
        categoryUniqueID = categoryUniqueID.replaceAll("[^\\d]", "");

        StorageReference filepath = categoryImagesRef.child(ImageUri.getLastPathSegment() + categoryUniqueID + "jpg");
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
        productMap.put("CategoryUniqueID", categoryUniqueID);
        productMap.put("DateCreated", saveCurrentDate);
        productMap.put("TimeCreated", saveCurrentTime);
        productMap.put("CategoryName", categoryName);
        productMap.put("CategoryImage", downloadImageUrl);
        productMap.put("CategoryStatus", "active");
        productMap.put("CategoryDeleted", "false");

        categoryRef.collection("Categories").document(categoryUniqueID).set(productMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadingBar.dismiss();
                        Toast.makeText(getApplicationContext(), "Category Added Successfully.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), AdminCategoriesActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingBar.dismiss();
                        Toast.makeText(getApplicationContext(), "Error!!!! Category not added.", Toast.LENGTH_SHORT).show();
                    }
                });


    }


}