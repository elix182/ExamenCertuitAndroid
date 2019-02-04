package com.certuit.pacheco.eliezer.examenclima;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

  private GoogleMap map;
  private FusedLocationProviderClient fusedLocationClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location);
    Toolbar toolbar = findViewById(R.id.toolbar_location);
    toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
  }

  @Override
  public void onBackPressed(){
    NavUtils.navigateUpFromSameTask(this);
    super.onBackPressed();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        break;
    }
    return super.onOptionsItemSelected(item);
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
    map = googleMap;
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      map.setMyLocationEnabled(true);
      FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
      fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
          if (location != null) {
            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15.5f));
          } else {
            showToast(getString(R.string.msg_cant_get_location));
          }
        }
      });
    }

    map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
      @Override
      public void onMapClick(LatLng point) {
        double latitude = point.latitude;
        double longitude = point.longitude;
        Intent intent = new Intent();
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude",longitude);
        setResult(RESULT_OK, intent);
        finish();
      }
    });
  }

  public void showToast(final String message){
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
      }
    });
  }
}