package sv.edu.ues.fia.eisi.mialojamientosv;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import sv.edu.ues.fia.eisi.mialojamientosv.model.Chat;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Habitacion;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Propietario;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap googleMap;
    String url;
    String API = "AIzaSyAKakDqcpt9_DzYy9SWkutteLyZ9x_1bdU";

    @Override
    protected String doInBackground(Object... objects) {
        googleMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();

        try {
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);

            JSONArray jsonArray = jsonObject.getJSONArray("results"); /*Extraido de la api de Google*/

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject datosextraidos = jsonArray.getJSONObject(i);

                JSONObject locationObj = datosextraidos.getJSONObject("geometry").getJSONObject("location");

                String latitud = locationObj.getString("lat");
                String longitud = locationObj.getString("lng");

                JSONObject nameObject = jsonArray.getJSONObject(i);
                String name = nameObject.getString("name");
                String rating = nameObject.getString("rating");
                String direccion = nameObject.getString("vicinity");
                String idHotel = nameObject.getString("place_id");
                String evaluaciones = nameObject.getString("user_ratings_total");
                int precio = nameObject.getInt("user_ratings_total");


                JSONArray photoArray = nameObject.getJSONArray("photos");
                JSONObject photo = photoArray.getJSONObject(0);
                String photo_reference = photo.getString("photo_reference");

                Hotel actual = null;

                try {
                    actual = Hotel.find(Hotel.class, "ID_HOTEL = '" + idHotel + "'", null).get(0);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (actual != null) {

                } else {
                    Hotel hotel = new Hotel();
                    Propietario propietario = new Propietario();
                    Habitacion habitacion = new Habitacion();
                    Chat chat = new Chat();

                    //Guarda los datos de la habitacion en la base
                    if ((i % 2) == 0) {
                        habitacion.setCantCamas(2);
                        habitacion.setCantBat(2);
                        habitacion.setIdHabitacion(1);
                        habitacion.setServiciosExtra("Television , Cocina , Aire Acondicionado");
                        habitacion.setCantPersonas(2);
                    } else {
                        habitacion.setCantCamas(1);
                        habitacion.setCantBat(1);
                        habitacion.setIdHabitacion(1);
                        habitacion.setServiciosExtra("Television, Aire Acondicionado");
                        habitacion.setCantPersonas(1);
                    }
                    habitacion.setDisponibilidad(1);
                    habitacion.setPrecioPorDia(String.valueOf(precio * 10));

                    //Guarda los datos del hotel en la base
                    hotel.setIdHotel(idHotel);
                    hotel.setTitulo(name);
                    hotel.setLatitudH(latitud);
                    hotel.setLongitudH(longitud);
                    hotel.setImagen("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + photo_reference + "&key=" + API);
                    hotel.setDescripcion("Estrellas: " + rating);
                    hotel.setEvaluaciones("Evaluaciones: " + evaluaciones);
                    hotel.setPropietario(propietario);
                    hotel.setDireccion(direccion);
                    hotel.save();

                    chat.setIdChat(idHotel);
                    chat.setNombre(name);
                    chat.setEmisor(""+1);
                    chat.setReceptor(""+1);
                    chat.save();

                    habitacion.setIdHotel(hotel);
                    habitacion.save();

                }

                /*Obtengo la latitud y longitud*/
                LatLng latLng = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));

                /*Asignamos un marcador*/

               /* Marker marcador = googleMap.addMarker(
                 new MarkerOptions()
                         .title(name)
                         .position(latLng)
                          .snippet(rating + " ⭐ ," + direccion)
                );*/

                /*marcador.showInfoWindow();*/

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(name);
                markerOptions.snippet(rating + " ⭐ ," + direccion);
                markerOptions.position(latLng);
                googleMap.addMarker(markerOptions);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
