package id.ac.polinema.absensiprojectuas;

import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(final GoogleMap googleMap) {
        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);

        Toast.makeText(MapsActivity.this,
                "Lat : " + latitude + " Long : " + longitude,
                Toast.LENGTH_LONG).show();

        gMap = googleMap;

        // ASDASDASDASDASDASDASDASDDSASDADSAASDASDADSDS
        LatLng malang = new LatLng(latitude, longitude);
        gMap.addMarker(new MarkerOptions().position(malang).title("HARIS HOUSE YO !"));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(malang, 17));

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        gMap = googleMap;
//
//        // Menambah marker di Malang Jawa Timus Boss!
//        LatLng malang = new LatLng(-7.9409, 112.6024);
//        gMap.addMarker(new MarkerOptions().position(malang).title("HARIS HOUSE MEN !"));
//        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(malang, 12));
    }
}

