package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.eisi.mialojamientosv.GetNearbyPlacesData;
import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityMapaBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Propietario;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.viewModel.HotelViewModel;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnInfoWindowClickListener{

    private ActivityMapaBinding binding;
    BottomNavigationView navigationView;
    private GoogleMap nmap;
    GoogleApiClient mGoogleApiClient;
    double currentLatitud, currentLongitud;
    Location myLocation;
    boolean firstTime = true;

    private static final int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        navigationView = binding.bottomNavigation;
        navigationView.setSelectedItemId(R.id.mapa);

        //Obtengo el Support Map para obtener el mapa de la vista
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setUPGClient();

        /*
         * BadgeDrawable badgeDrawable = navigationView.getOrCreateBadge(R.id.mensajes);
         * badgeDrawable.setVisible(true);
         * badgeDrawable.setNumber(4);
         */

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setActivity(item);
                return true;
            }
        });
    }

    private void setUPGClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,0,this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @SuppressLint("NonConstantResourceId")
    public void setActivity(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.explore:
                startActivity(new Intent(MapaActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.favoritos:
                startActivity(new Intent(MapaActivity.this, FavoritosActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.mapa:
                break;
            case R.id.mensajes:
                startActivity(new Intent(MapaActivity.this, MensajesActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.perfil:
                startActivity(new Intent(MapaActivity.this, PerfilActivity.class));
                overridePendingTransition(0, 0);
                break;
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        //Pinta el mapa en la pantalla
        nmap = googleMap;
        nmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.stylemap));
        nmap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        myLocation = location;

        while (firstTime){
        if(myLocation !=null) {
            //Obtengo la latitud y longitud de donde me encuentro
            currentLatitud = location.getLatitude();
            currentLongitud = location.getLongitude();

            //Estilo de marker
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);

            //Esto realiza el Zoom a la ubicacion en donde se encuentra la persona, asignando la latitud, longitud y zoom respectivo
            nmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitud, currentLongitud), 15.0f));

            //Agrego el Marker en el Mapa
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(currentLatitud, currentLongitud));
            markerOptions.title("Tu ubicacion");
            markerOptions.icon(icon);

            //Agrego el marker en el mapa
            nmap.addMarker(markerOptions);
            /*Consumo de hoteles cercanos, referentes a la localizacion de la persona*/
            getNearbyHotels();
        }
        firstTime = false;
        }
    }

    private void getNearbyHotels() {
        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("location="+String.valueOf(currentLatitud)+","+String.valueOf(currentLongitud));
        stringBuilder.append("&radius=1500");
        stringBuilder.append("&keyword=hotel");
        stringBuilder.append("&type=hotel");
        stringBuilder.append("&key=AIzaSyAKakDqcpt9_DzYy9SWkutteLyZ9x_1bdU"); //Cambiar esta llave, No es aceptada.
        String url = stringBuilder.toString();
        Object dataTrasfer[] = new Object[2];
        dataTrasfer[0] = nmap;
        dataTrasfer[1] = url;

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(dataTrasfer);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        verificarpermisos();
    }

    /*Revisar este metodo en el Deny*/
    private void verificarpermisos() {
        int permissionLocation = ContextCompat.checkSelfPermission(MapaActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listaPermisos = new ArrayList<>();
        if(permissionLocation != PackageManager.PERMISSION_GRANTED){
            //No se dio el permiso

            listaPermisos.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if(!listaPermisos.isEmpty()){
                ActivityCompat.requestPermissions(this, listaPermisos.toArray(new String[listaPermisos.size()]),
                        REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
            //Se da el permiso
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            //Se autoriza el permiso de la localizacion y redirige a la ubicacion en donde se encuentra el usuario
            getMyLocation();
        } else {
            //No acepta el permiso y si se le da el Deny de no volver a preguntar
            Toast.makeText(this, "Debe de dar permiso en su dispositivo", Toast.LENGTH_SHORT).show();
            verificarpermisos();
            Intent intent = new Intent(MapaActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void getMyLocation(){

        if(mGoogleApiClient!=null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(MapaActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    //Obtiene la localizacion
                    myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this::onLocationChanged);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat.checkSelfPermission(MapaActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(MapaActivity.this, REQUEST_CHECK_SETTINGS_GPS);


                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    break;
                            }
                        }
                    });

                }
            }
        }
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {

        Hotel actual = null;

        try{
            actual = Hotel.find(Hotel.class, "TITULO = '" + marker.getTitle() +"'", null).get(0);
        }catch (Exception exception){
            exception.printStackTrace();
        }

        if(actual == null){

        }else{
            Context ctx = this;
            Intent intent = new Intent(ctx, HotelActivity.class);
            intent.putExtra("idHotel", actual.getIdHotel());
            ctx.startActivity(intent);
        }


    }
}