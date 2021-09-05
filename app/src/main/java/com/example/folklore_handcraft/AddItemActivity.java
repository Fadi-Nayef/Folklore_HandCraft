package com.example.folklore_handcraft;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    Location currentLocation;
    String addressString;

    FusedLocationProviderClient fusedLocationClient;

    // Initializing other items
    // from layout file
    TextView latitudeTextView, longitTextView;
    int PERMISSION_ID = 44;
    private double latitude;
    private double longitude;
    private Handler handler;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitTextView = findViewById(R.id.longitTextView);
        askForPermissionToUseLocation();

        configureLocationServices();
        askForLocation();


        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });

    }

    private void configureLocationServices() {
        fusedLocationClient  = LocationServices.getFusedLocationProviderClient(this);

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void askForPermissionToUseLocation(){
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 2);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
    @SuppressLint("MissingPermission")
    public void askForLocation() {

        LocationRequest locationRequest;
        LocationCallback locationCallback;

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(60000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                currentLocation = locationResult.getLastLocation();
                Log.i("Amplify_location", currentLocation.toString());

                Geocoder geocoder = new Geocoder(AddItemActivity.this, Locale.getDefault());

                try {
                    List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 10);
                    Log.e("Location","Your Addres is " + " " + addresses);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                    Log.i("Amplify_location", addresses.get(0).toString());
//                    addressString = addresses.get(0).getAddressLine(0);
//                    Log.i("Amplify_location", addressString);
//                } catch (IOException e) {
//                    e.printStackTrace();
                }
//            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }
}