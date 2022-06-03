package sv.edu.ues.fia.eisi.mialojamientosv.ui.viewModel;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.eisi.mialojamientosv.StripeService;

public class HotelViewModel extends AsyncTask<Object, Void, Boolean> {

    private Comunicacion comunicacion;
    private static List<String> acceso = new ArrayList<>();
    private Context context;

    public HotelViewModel(Comunicacion comunicacion) {
        this.comunicacion = comunicacion;
    }

    @Override
    protected void onPreExecute() {
        comunicacion.togglePaymentButton(false);
    }

    @Override
    protected void onPostExecute(Boolean status) {
        if(status) {
            this.comunicacion.togglePaymentButton(status);
        }else{
            Toast.makeText(context, "No puede realizar este pago", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        try {
            Thread.sleep((int) objects[0]);

            StripeService paymentService = (StripeService) objects[1];
            context = (Context) objects[2];

            return paymentService.getFinalClientSecret() != null;

        } catch (Exception e) {
        }
        return false;
    }
}
