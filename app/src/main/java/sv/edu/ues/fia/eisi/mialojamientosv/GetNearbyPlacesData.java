package sv.edu.ues.fia.eisi.mialojamientosv;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GetNearbyPlacesData extends AsyncTask<Object,String,String> {

    String googlePlacesData;
    GoogleMap googleMap;
    String url;

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

                /*Obtengo la latitud y longitud*/
                LatLng latLng = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));

                /*Asignamos un marcador*/
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(name);
                markerOptions.position(latLng);

                googleMap.addMarker(markerOptions);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
