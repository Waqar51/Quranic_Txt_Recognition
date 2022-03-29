package com.example.textrecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

public class GalleryActivity extends AppCompatActivity {

    TextView text_data;
    Button btnRetake;
    Button button_copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        text_data = findViewById(R.id.text_data);
        btnRetake = findViewById(R.id.btnRetake);
        button_copy = findViewById(R.id.button_copy);
        btnRetake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipBoard(text_data.getText().toString());
            }
        });


        Intent intent = getIntent();

        String result = intent.getStringExtra("Result");

        text_data.setText(result);

    }

    private void copyToClipBoard(String text)
    {
        ClipboardManager clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // create another method for clip data
        ClipData clip = ClipData.newPlainText("Copied Data", text);
        clipBoard.setPrimaryClip(clip);
        Toast.makeText(GalleryActivity.this, "Copied to clipboard !!!", Toast.LENGTH_SHORT).show();

    }
}