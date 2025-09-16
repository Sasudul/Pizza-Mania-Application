package com.example.pizzamaniaapplication;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity {

    private static final String TAG = "LocationActivity";
    private static final int LOCATION_PERMISSION_REQUEST = 100;

    private EditText locationEditText;
    private Button findMyLocationBtn;
    private FusedLocationProviderClient fusedLocationClient;
    private PizzaManiaDBOpenHelper dbHelper;

    // UI components for the side menu
    private DrawerLayout drawerLayout;
    private ImageView menuIcon, callIcon, profileIcon ,cartIcon;
    private AppCompatButton mainMenu;


    private TextView navHome, navOrder, navCart, navLogout, navCheckout ,navBranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // Initialize existing UI components
        locationEditText = findViewById(R.id.location_edit_text);
        findMyLocationBtn = findViewById(R.id.find_my_location_button);

        // Initialize new UI components for the drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menu_icon);
        profileIcon = findViewById(R.id.profile_icon);
        callIcon = findViewById(R.id.call_icon);
        cartIcon = findViewById(R.id.cart_icon);
        mainMenu = findViewById(R.id.special_deal_card);
        navHome = findViewById(R.id.nav_home);
        navOrder = findViewById(R.id.nav_order);
        navCart = findViewById(R.id.nav_cart);
        navLogout = findViewById(R.id.nav_logout);
        navCheckout = findViewById(R.id.nav_checkout);
        navBranch = findViewById(R.id.nav_branch);

        dbHelper = new PizzaManiaDBOpenHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        loadUserAddress();

        // Set click listener for the location button
        findMyLocationBtn.setOnClickListener(v -> checkGpsAndFetchLocation());

        // Set click listener to open the side menu
        menuIcon.setOnClickListener(v -> {
            if (drawerLayout != null) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Set click listeners for the navigation menu items
        navHome.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        navOrder.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(LocationActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        navCart.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(LocationActivity.this, CartActivity.class);
            startActivity(intent);
        });

        navBranch.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(LocationActivity.this, BranchMapActivity.class);
            startActivity(intent);
        });

        navLogout.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            SharedPreferences prefs = getSharedPreferences("PizzaManiaPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("LOGGED_IN_USER_ID");
            editor.apply();

            Intent intent = new Intent(LocationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        navCheckout.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(LocationActivity.this, ContactUsActivity.class);
            startActivity(intent);
        });

        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        callIcon.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this, ContactUsActivity.class);
            startActivity(intent);
        });

        cartIcon.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this, CartActivity.class);
            startActivity(intent);
        });

        mainMenu.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this, MenuActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserAddress() {
        SharedPreferences prefs = getSharedPreferences("PizzaManiaPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("LOGGED_IN_USER_ID", -1);

        if (userId != -1) {
            if (dbHelper != null && dbHelper.getReadableDatabase() != null) {
                String address = dbHelper.getUserAddress(dbHelper.getReadableDatabase(), userId);
                if (address != null && !address.isEmpty()) {
                    locationEditText.setText(address);
                }
            } else {
                Log.e(TAG, "DBHelper or readable database is null in loadUserAddress");
            }
        }
    }

    // ✅ New method: check if GPS is enabled
    private void checkGpsAndFetchLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new AlertDialog.Builder(this)
                    .setTitle("Enable GPS")
                    .setMessage("GPS is required to fetch your location. Enable it?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .setCancelable(false)
                    .show();
        } else {
            getCurrentLocation();
        }
    }

    // ✅ Updated method: use getCurrentLocation instead of getLastLocation
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        Log.d(TAG, "Location found: Lat: " + lat + ", Lon: " + lon);

                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            geocoder.getFromLocation(lat, lon, 1, new Geocoder.GeocodeListener() {
                                @Override
                                public void onGeocode(@NonNull List<Address> addresses) {
                                    if (addresses != null && !addresses.isEmpty()) {
                                        String addressText = addresses.get(0).getAddressLine(0);
                                        locationEditText.setText(addressText);
                                        saveAddressToDB(addressText);
                                    } else {
                                        Toast.makeText(LocationActivity.this, "Unable to fetch address", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(@Nullable String errorMessage) {
                                    Toast.makeText(LocationActivity.this, "Geocoding error: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            new Thread(() -> {
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                                    runOnUiThread(() -> {
                                        if (addresses != null && !addresses.isEmpty()) {
                                            String addressText = addresses.get(0).getAddressLine(0);
                                            locationEditText.setText(addressText);
                                            saveAddressToDB(addressText);
                                        } else {
                                            Toast.makeText(LocationActivity.this, "Unable to fetch address", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (IOException e) {
                                    runOnUiThread(() ->
                                            Toast.makeText(LocationActivity.this, "Unable to fetch address", Toast.LENGTH_SHORT).show());
                                }
                            }).start();
                        }
                    } else {
                        Toast.makeText(LocationActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, e ->
                        Toast.makeText(LocationActivity.this, "Failed to get location: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveAddressToDB(String address) {
        SharedPreferences prefs = getSharedPreferences("PizzaManiaPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("LOGGED_IN_USER_ID", -1);

        // 🔹 Detect branch based on address keywords
        int branchId = 1; // default Colombo
        if (address.toLowerCase().contains("galle")) {
            branchId = 2;
        } else if (address.toLowerCase().contains("matara")) {
            branchId = 3;
        }

        // Save branchId in SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("BRANCH_ID", branchId);
        editor.apply();

        if (userId != -1 && dbHelper != null && dbHelper.getWritableDatabase() != null) {
            ContentValues values = new ContentValues();
            values.put(PizzaManiaDBOpenHelper.COLUMN_USER_ADDRESS, address);

            int rowsAffected = dbHelper.getWritableDatabase().update(
                    PizzaManiaDBOpenHelper.TABLE_USERS,
                    values,
                    PizzaManiaDBOpenHelper.COLUMN_USER_ID + "=?",
                    new String[]{String.valueOf(userId)}
            );

            if (rowsAffected > 0) {
                Log.d(TAG, "Address + Branch saved. User ID: " + userId + ", Branch ID: " + branchId);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
