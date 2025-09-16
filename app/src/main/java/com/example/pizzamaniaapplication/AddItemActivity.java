package com.example.pizzamaniaapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;
import com.example.pizzamaniaapplication.models.FileUtils;

public class AddItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private ImageView backIcon, itemImagePlaceholder;
    private EditText itemNameEditText, itemIDEditText, itemPriceEditText, descriptionEditText;
    private Button btnAddItems;

    private PizzaManiaDBOpenHelper dbHelper;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Initialize views
        backIcon = findViewById(R.id.backIcon);
        itemImagePlaceholder = findViewById(R.id.itemImagePlaceholder);
        itemNameEditText = findViewById(R.id.itemNameEditText);
        itemIDEditText = findViewById(R.id.itemIDEditText);
        itemPriceEditText = findViewById(R.id.itemPriceEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        btnAddItems = findViewById(R.id.btnAddItems);

        // Initialize DB helper
        dbHelper = new PizzaManiaDBOpenHelper(this);

        // Set click listeners
        backIcon.setOnClickListener(v -> finish());
        itemImagePlaceholder.setOnClickListener(v -> openImageSelection());
        btnAddItems.setOnClickListener(v -> addNewMenuItem());
    }

    private void openImageSelection() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageSelection();
            } else {
                Toast.makeText(this, "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            itemImagePlaceholder.setImageURI(imageUri);
        }
    }

    private void addNewMenuItem() {
        String itemName = itemNameEditText.getText().toString().trim();
        String itemPriceStr = itemPriceEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (itemName.isEmpty() || itemPriceStr.isEmpty() || description.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and add an image", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double itemPrice = Double.parseDouble(itemPriceStr);

            // Save the image to internal storage and get the path
            String imagePath = FileUtils.saveImageToInternalStorage(this, imageUri);

            dbHelper.insertMenuItem(itemName, description, itemPrice, imagePath, "Pizza");
            Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
        }
    }
}