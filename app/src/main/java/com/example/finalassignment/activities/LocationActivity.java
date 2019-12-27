package com.example.finalassignment.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.finalassignment.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Marker marker;
    private TextView tvDistance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        tvDistance=findViewById(R.id.tvDistance);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        // return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney, Australia, and move the camera.
        LatLng nepal = new LatLng(27.7052, 85.3294);
        mMap.addMarker(new MarkerOptions().position(nepal).title("Marker in softwarica"));


        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                              int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
        Location location=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location==null)return;

        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        marker= mMap.addMarker(new MarkerOptions()
                .position(myLatLng).title("me")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(Location location) {
        Location Location = new Location("");
        Location.setLatitude(27.70);
        Location.setLongitude(85.33);



        float distance=location.distanceTo(Location);

        tvDistance.setText("Distance:"+distance+"k/m");

        if(marker == null) return;
        marker.remove();
        LatLng myLatLng =new LatLng(location.getLatitude(),location.getLongitude());
        marker= mMap.addMarker(new MarkerOptions()
                .position(myLatLng)
                .title("me")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

