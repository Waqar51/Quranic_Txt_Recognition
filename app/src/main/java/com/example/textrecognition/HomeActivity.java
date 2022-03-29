package com.example.textrecognition;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.card.MaterialCardView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    MaterialCardView btnTakePicture, btnExit;
    Bitmap bitmap;

    private static final int REQUEST_CAMERA_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // initialize them on create method (after initialization of views)

        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnExit = findViewById(R.id.btnBack);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        // ask run time permission to excess the camera

        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // permission is not granted than ask for the permission

            ActivityCompat.requestPermissions(HomeActivity.this, new String[]
                            {

                                    // Pass permission Name

                                    Manifest.permission.CAMERA
                            },
                    // pass the request Permission Code that can handle run time permission

                    REQUEST_CAMERA_CODE);

        }

        // Create click listener for this button
        // now call the button capture

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call over crop image activity that will get from the library that just imported
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(HomeActivity.this);
                // so that will launch the crop image activity and it will ask the user to click an image from the camera or select an image from the gallery and than crop the image
            }
        });


    }

    // than capture image result so call or override the on activity result Method


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // after the super call we check for the request code

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            // if it matches than just get crop image from the data

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            // check for the result code

            if (resultCode == RESULT_OK) {

                // create uri from the result

                Uri resultUri = result.getUri();

                // now create bitmap for this URI

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);

                    // call getTextFromImage inside the on activity result method after creation of bitmap

                    getTextFromImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    // for the OCR create Method

    private void getTextFromImage(Bitmap bitmap) {

        // call the text Recognition  from the vision API

        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();

        // check recognizer is not Operational

        if (!recognizer.isOperational()) {
            // show toast message some error occur

            Toast.makeText(HomeActivity.this, "Error !!!!", Toast.LENGTH_SHORT).show();
        } else {
            // extract text from the image
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();

            // And then traverse through each of the items inside this text block sparse array and add in string builder

            for (int i = 0; i < textBlockSparseArray.size(); i++) {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);

                // add value of text block in string builder

                stringBuilder.append(textBlock.getValue());

                // after add so build new line here maintain the line pattern

                stringBuilder.append("\n");

            }
            // attach the string from the string builder to our text view

            Intent intent = new Intent(HomeActivity.this, GalleryActivity.class);
            intent.putExtra("Result", stringBuilder.toString());
            startActivity(intent);
        }
    }

}
