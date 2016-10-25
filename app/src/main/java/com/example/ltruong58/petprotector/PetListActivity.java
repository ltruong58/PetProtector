package com.example.ltruong58.petprotector;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class PetListActivity extends AppCompatActivity {

    private ImageView petImageView;

    // This member variable store the URI to whatever image has been selected
    // Defaukt: none.png (R.drawable.none)
    private Uri imageURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        petImageView = (ImageView) findViewById(R.id.petImageView);

        // Constructs a full URI to any Android resource
         imageURI = getUriToResource(this, R.drawable.none);

        // Set the imageURI of the ImageView
        petImageView.setImageURI(imageURI);
    }

    public void selectPetImage(View view) {
        ArrayList<String> permList = new ArrayList<>();

        // Start by seeing if we have permission to camera
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(cameraPermission != PackageManager.PERMISSION_GRANTED)
            permList.add(Manifest.permission.CAMERA);

        int readExtStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(readExtStoragePermission != PackageManager.PERMISSION_GRANTED)
            permList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        int writeExtStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(writeExtStoragePermission != PackageManager.PERMISSION_GRANTED)
            permList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int requestCode = 100;

        // If the list have items
        if(permList.size() > 0)
        {
            String[] perms = new String[permList.size()];


            ActivityCompat.requestPermissions(this, permList.toArray(perms), requestCode);


        }
        if(cameraPermission == PackageManager.PERMISSION_GRANTED
                && readExtStoragePermission == PackageManager.PERMISSION_GRANTED
                && writeExtStoragePermission == PackageManager.PERMISSION_GRANTED)
        {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, requestCode);
        }
        else Toast.makeText(this, "Pet Protector requires permission",
                Toast.LENGTH_SHORT).show();
    }
    

    public static Uri getUriToResource(@NonNull Context context, @AnyRes int resId) throws Resources.NotFoundException {
        Resources res = context.getResources();

        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
        "://" + res.getResourcePackageName(resId)
        + '/' + res.getResourceTypeName(resId)
        + '/' + res.getResourceEntryName(resId));
    }

}
