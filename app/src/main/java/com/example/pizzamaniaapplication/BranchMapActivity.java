package com.example.pizzamaniaapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;
import com.example.pizzamaniaapplication.models.Branch;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class BranchMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "BranchMapActivity";
    private static final int LOCATION_PERMISSION_REQUEST = 200;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private PizzaManiaDBOpenHelper dbHelper;

    private List<Branch> branches = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_map);

        dbHelper = new PizzaManiaDBOpenHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.branchMapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Check for location permission before proceeding
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }

        fetchBranchesAndShow();
    }

    private void fetchBranchesAndShow() {

        branches = getAllBranches();

        // Explicitly check location permission before accessing location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }

        // Get user location
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                showBranchMarkers(location);
            } else {
                Toast.makeText(this, "Could not fetch location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBranchMarkers(Location userLocation) {
        if (branches.isEmpty()) {
            Toast.makeText(this, "No branches found", Toast.LENGTH_SHORT).show();
            return;
        }

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        Branch nearestBranch = null;
        float nearestDistance = Float.MAX_VALUE;

        // Loop through all branches
        for (Branch branch : branches) {
            String[] parts = branch.getLocation().split(",");
            double lat = Double.parseDouble(parts[0]);
            double lon = Double.parseDouble(parts[1]);
            LatLng branchLatLng = new LatLng(lat, lon);

            // Calculate distance
            float[] results = new float[1];
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), lat, lon, results);
            float distance = results[0];

            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestBranch = branch;
            }

            mMap.addMarker(new MarkerOptions()
                    .position(branchLatLng)
                    .title(branch.getName())
                    .snippet(branch.getContact())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

            boundsBuilder.include(branchLatLng);
        }

        // Highlight nearest branch in GREEN
        if (nearestBranch != null) {
            String[] parts = nearestBranch.getLocation().split(",");
            double lat = Double.parseDouble(parts[0]);
            double lon = Double.parseDouble(parts[1]);
            LatLng nearestLatLng = new LatLng(lat, lon);

            mMap.addMarker(new MarkerOptions()
                            .position(nearestLatLng)
                            .title("Nearest: " + nearestBranch.getName())
                            .snippet(nearestBranch.getContact())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                    .showInfoWindow();

            boundsBuilder.include(nearestLatLng);
        }

        // Include user location
        LatLng userLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(userLatLng)
                .title("You")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        boundsBuilder.include(userLatLng);

        // Move camera to show all
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 150));
    }

    // Fetch branches from DB
    private List<Branch> getAllBranches() {
        List<Branch> branchList = new ArrayList<>();
        try {
            var db = dbHelper.getReadableDatabase();
            var cursor = db.rawQuery("SELECT branchId, name, location, contact, latitude, longitude FROM Branches", null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String loc = cursor.getDouble(4) + "," + cursor.getDouble(5); // lat,lon
                    String contact = cursor.getString(3);

                    branchList.add(new Branch(id, name, loc, contact));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error fetching branches: " + e.getMessage());
        }
        return branchList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchBranchesAndShow();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
