package com.example.binbuddy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.binbuddy.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker customMarker;
    private Marker currentLocationMarker;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 1. Location Permissions and Current Location:
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            showCurrentLocationWithMarker();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        // 2. Map Click Listener:
        mMap.setOnMapClickListener(latLng -> {
            if (customMarker != null) {
                customMarker.remove();
            }
            String address = getAddressFromLatLng(latLng);
            customMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(address != null ? address : "Custom Marker")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            if (address != null) {
                Toast.makeText(this, "Address: " + address, Toast.LENGTH_SHORT).show();
            }
        });

        // 3. Load GeoJSON:
        try {
            loadGeoJsonFromResource(R.raw.derry_bins_locations); // Replace with your GeoJSON file name
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading GeoJSON", Toast.LENGTH_SHORT).show();
        }
    }


    private void showCurrentLocationWithMarker() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                if (currentLocationMarker != null) {
                    currentLocationMarker.remove();
                }

                String address = getAddressFromLatLng(currentLatLng);

                currentLocationMarker = mMap.addMarker(new MarkerOptions()
                        .position(currentLatLng)
                        .title(address != null ? address : "Current Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

                if (address != null) {
                    Toast.makeText(this, "Current Location: " + address, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Unable to fetch address", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Unable to fetch current location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCurrentLocationWithMarker();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void loadGeoJsonFromResource(int resourceId) throws IOException, JSONException {
        InputStream inputStream = getResources().openRawResource(resourceId);
        String json = readJsonFromInputStream(inputStream);
        displayGeoJson(json);
    }

    private String readJsonFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

    private void displayGeoJson(String json) throws JSONException {
        JSONObject geoJson = new JSONObject(json);
        JSONArray features = geoJson.getJSONArray("features");

        for (int i = 0; i < features.length(); i++) {
            JSONObject feature = features.getJSONObject(i);
            JSONObject geometry = feature.getJSONObject("geometry");
            String type = geometry.getString("type");

            switch (type) {
                case "Point":
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    double longitude = coordinates.getDouble(0);
                    double latitude = coordinates.getDouble(1);
                    LatLng latLng = new LatLng(latitude, longitude);

                    String title = "";
                    if (feature.has("properties")) {
                        JSONObject properties = feature.getJSONObject("properties");
                        if (properties.has("name")) {
                            title = properties.getString("name");
                        }
                    }

                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(title)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    break;
                case "LineString":
                    JSONArray lineCoordinates = geometry.getJSONArray("coordinates");
                    PolylineOptions polylineOptions = new PolylineOptions();
                    for (int j = 0; j < lineCoordinates.length(); j++) {
                        JSONArray point = lineCoordinates.getJSONArray(j);
                        double lineLongitude = point.getDouble(0);
                        double lineLatitude = point.getDouble(1);
                        polylineOptions.add(new LatLng(lineLatitude, lineLongitude));
                    }
                    mMap.addPolyline(polylineOptions.color(Color.BLUE));
                    break;
                case "Polygon":
                    JSONArray polygonCoordinates = geometry.getJSONArray("coordinates");
                    PolygonOptions polygonOptions = new PolygonOptions();
                    JSONArray exteriorRing = polygonCoordinates.getJSONArray(0);
                    for (int j = 0; j < exteriorRing.length(); j++) {
                        JSONArray point = exteriorRing.getJSONArray(j);
                        double polygonLongitude = point.getDouble(0);
                        double polygonLatitude = point.getDouble(1);
                        polygonOptions.add(new LatLng(polygonLatitude, polygonLongitude));
                    }
                    mMap.addPolygon(polygonOptions.fillColor(Color.argb(128, 0, 255, 0)).strokeColor(Color.GREEN));
                    break;
                // Add more cases for other geometry types as needed
            }
        }
    }
}