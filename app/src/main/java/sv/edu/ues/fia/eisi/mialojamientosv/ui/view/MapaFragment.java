package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.tasks.Task;

import sv.edu.ues.fia.eisi.mialojamientosv.R;


public class MapaFragment extends Fragment {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    double latitud = 0, longitud = 0;
    GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mapa, container, false);

        /*Posible error en esta linea*/
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        //Iniciamos la localizacion del cliente
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        
        //Verficamos los permisos del usuario
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Ha autorizado el permiso
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //Autoriza
                    if(location !=null){
                        latitud = location.getLatitude();
                        longitud = location.getLongitude();
                        //Sincronizamos el mapa
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {

                                //Alistamos el mapa
                                map = googleMap;

                                //Inicializamos la latitud y longitud
                                LatLng latLng = new LatLng(latitud, longitud);
                                //Creamos el marcador que se visualiza en el mapa
                                MarkerOptions options = new MarkerOptions().position(latLng).title("Tu ubicacion");

                                //Realizamos un acercamiento al mapa
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                                //Agregamos el marcador en el mapa
                                googleMap.addMarker(options);
                            }
                        });
                    }
                }
            });
        } else {
            //Cuando se niega el permiso
            ActivityCompat.requestPermissions(MapaFragment.this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Finalmente acepta el permiso
                //Pendiente [Se elimino el metodo por cuestiones de errores, se podra recolocarlo]
            }
        }
    }
}