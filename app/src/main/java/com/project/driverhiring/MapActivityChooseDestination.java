package com.project.driverhiring;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.driverhiring.databinding.ActivityMapChooseDestinationBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivityChooseDestination extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapChooseDestinationBinding binding;
    RelativeLayout confirmLocationBt;
    String destinationLatitude = "", destinationLongitude = "";
    private GpsTracker gpsTracker;
    double userLatitudeDouble, userLongitudeDouble;
    String userLatitude = "", userLongitude = "";
    private static final int DEFAULT_ZOOM = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMapChooseDestinationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // obtaining current location

        try {
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //  ActivityCompat.requestPermissions(getParent(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION, 101);
            } else {
                gpsTracker = new GpsTracker(getApplicationContext());
                if (gpsTracker.canGetLocation()) {

                    userLatitudeDouble = gpsTracker.getLatitude();
                    userLongitudeDouble = gpsTracker.getLongitude();

                    userLatitude = String.valueOf(userLatitudeDouble);
                    userLongitude = String.valueOf(userLongitudeDouble);

                    Log.i("lat", userLatitude);
                    Log.i("lat", userLongitude);


                    // apiCall("8.9076","77.0549");
                    //                   apiCall(String.valueOf(latitude),String.valueOf(longitude));
                    Geocoder geocoder;
                    List<Address> addresses = null;
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(userLatitudeDouble, userLongitudeDouble, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // locationTextView.setText(addresses.get(0).getLocality());

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        confirmLocationBt = findViewById(R.id.bt_confirm_location);

        confirmLocationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (destinationLatitude.equals("") || destinationLongitude.equals("")) {
                    Toast.makeText(MapActivityChooseDestination.this, "Please Choose Your Destination", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("destination_lat", destinationLatitude);
                    editor.putString("destination_longitude", destinationLongitude);
                    editor.putString("user_latitude", userLatitude);
                    editor.putString("user_longitude", userLongitude);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), DriverListActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        try {
            LatLng myLocation = new LatLng(userLatitudeDouble, userLongitudeDouble);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(userLatitudeDouble,
                            userLongitudeDouble),DEFAULT_ZOOM));
        } catch (Exception e) {

        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng));
                destinationLatitude = String.valueOf(latLng.latitude);
                destinationLongitude = String.valueOf(latLng.longitude);

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    Address obj = addresses.get(0);
                    String add = obj.getAddressLine(0);
                    add = add + "\n" + obj.getCountryName();
                    add = add + "\n" + obj.getCountryCode();
                    add = add + "\n" + obj.getAdminArea();
                    add = add + "\n" + obj.getPostalCode();
                    add = add + "\n" + obj.getSubAdminArea();
                    add = add + "\n" + obj.getLocality();
                    add = add + "\n" + obj.getSubThoroughfare();

                    Log.i("IGA", "Address" + add);
                    // Toast.makeText(this, "Address=>" + add,
                    // Toast.LENGTH_SHORT).show();
                    Toast.makeText(MapActivityChooseDestination.this, obj.getLocality() + " selected", Toast.LENGTH_SHORT).show();
                    // TennisAppActivity.showDialog(add);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    // Function to check and request permission.
    public void checkPermissions(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(MapActivityChooseDestination.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(getApplicationContext(), "Permission Already granted", Toast.LENGTH_SHORT).show();
        }

    }
}