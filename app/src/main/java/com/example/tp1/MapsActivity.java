package com.example.tp1;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.tp1.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

        private GoogleMap mMap;
        private ActivityMapsBinding binding;
        private static final int LOCATION_REQUEST_CODE = 101;
        private FusedLocationProviderClient locationProviderClient;
        DBHelper Tp1bd;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Tp1bd  = new DBHelper(this);

            binding = ActivityMapsBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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



            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);

            }
            else {
                activerLocalisation();
                placeMarqueurEntreprise();
            }

        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == LOCATION_REQUEST_CODE) {
                if (isPermissionAuth(permissions, grantResults, android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                        isPermissionAuth(permissions, grantResults, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    activerLocalisation();

                }
            }
        }

        private boolean isPermissionAuth(String[] permissions, int[] grantResults, String accessLocation) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].compareToIgnoreCase(accessLocation) == 0){
                    return grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
            }
            return false;
        }

        @SuppressLint("MissingPermission")
        private void activerLocalisation() {
            locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12));
                        mMap.addMarker(new MarkerOptions().position(position).title("Collège Bois-de-Boulogne"));

                    }
                }
            });
            if(mMap != null){
                mMap.setMyLocationEnabled(true);
            }
        }
        //place les marqueurs aux adresses dans la bd
        private void placeMarqueurEntreprise(){
            Cursor resultatAdress;
            //liste adresse trouvé
            List<Address> listeAdresseTrouver;
            Log.d("tag","dans placer marqueur");
            if(Geocoder.isPresent()){
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                //va chercher toutes les adresses dans la bd
                resultatAdress = Tp1bd.obtenirAdresse();
                if(resultatAdress.getCount() < 0){
                    Log.d("tag","dans getcount 0");
                    Toast.makeText(MapsActivity.this,"aucune adresse trouve",Toast.LENGTH_SHORT).show();
                    return;
                }
                //on va boucler tant qu'il y aura une adresse
                while(resultatAdress.moveToNext()){
                    try{
                        //trouve des adresse correspondant à l'adresse mis en parametre
                     listeAdresseTrouver = geocoder.getFromLocationName(resultatAdress.getString(0),1);
                     if(listeAdresseTrouver.size() >0){
                         Address adresseTrouve = listeAdresseTrouver.get(0);
                         //prend la position de la premiere adresse
                         LatLng positionVal = new LatLng(adresseTrouve.getLatitude(),adresseTrouve.getLongitude());
                         Log.d("tag",resultatAdress.getString(0)+" Latitude "+adresseTrouve.getLatitude()+",laditude: "+
                                 adresseTrouve.getLongitude()+" postalCode "+adresseTrouve.getPostalCode());
                         mMap.addMarker(new MarkerOptions().position(positionVal).title("jobPlace"));
                         Toast.makeText(MapsActivity.this,"Le marqueur a été placé",Toast.LENGTH_SHORT).show();
                     }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }


        }


        @Override
        public boolean onMyLocationButtonClick() {
            Toast.makeText(getApplicationContext(), "localisation cliquée", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onMyLocationClick(@NonNull Location location) {
            Toast.makeText(getApplicationContext(), "localisation cliquée", Toast.LENGTH_SHORT).show();

        }
}