package sv.edu.ues.fia.eisi.mialojamientosv;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Propietario;

public class GetNearbyPlacesData extends AsyncTask<Object,String,String> {

    String googlePlacesData;
    GoogleMap googleMap;
    String url;
    String API = "AIzaSyAKakDqcpt9_DzYy9SWkutteLyZ9x_1bdU";

    @Override
    protected String doInBackground(Object... objects) {
        googleMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();

        try{
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results"); /*Extraido de la api de Google*/

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject datosextraidos = jsonArray.getJSONObject(i);
                JSONObject locationObj = datosextraidos.getJSONObject("geometry").getJSONObject("location");

                String latitud = locationObj.getString("lat");
                String longitud = locationObj.getString("lng");

                JSONObject nameObject = jsonArray.getJSONObject(i);
                String name = nameObject.getString("name");
                String rating = nameObject.getString("rating");

                JSONArray photoArray = nameObject.getJSONArray("photos");
                JSONObject photo = photoArray.getJSONObject(0);
                String photo_reference = photo.getString("photo_reference");

                Hotel hotel = new Hotel();
                Propietario propietario = new Propietario();

                hotel.setIdHotel(""+i);
                hotel.setTitulo(name);
                hotel.setLatitudH(latitud);
                hotel.setLongitudH(longitud);
                hotel.setImagen("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="+photo_reference+"&key="+API);
                hotel.setDescripcion("Estrellas: " + rating);
                hotel.setPropietario(propietario);
                hotel.setDireccion("Direccion 1");
                hotel.save();



                /*Obtengo la latitud y longitud*/
                LatLng latLng = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));

                /*Asignamos un marcador*/
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(name);
                markerOptions.snippet("Estrellas: " + rating);
                markerOptions.position(latLng);

                googleMap.addMarker(markerOptions);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
