package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.SplashScreen;
import sv.edu.ues.fia.eisi.mialojamientosv.StripeService;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityHotelBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Favorito;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Perfil;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.viewModel.Comunicacion;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.viewModel.HotelViewModel;

public class HotelActivity extends AppCompatActivity implements Comunicacion {

    ActivityHotelBinding binding;
    TextView tvNombreHotel, tvDireccionHotel, tvDescriptionHotel;
    ImageView ivFotoHotel;
    LottieAnimationView botonf;
    Button pay;
    Hotel hotel;
    Perfil perfil;
    int id = 0;
    StripeService paymentService;
    PaymentSheet paymentSheet;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        botonf = binding.animationFavoriteHotel;
        tvNombreHotel = binding.tvHotelName;
        tvDireccionHotel = binding.tvAddress;
        tvDescriptionHotel = binding.tvHotelDescription;
        ivFotoHotel = binding.ivHotel;
        pay = binding.pay;

        if (savedInstanceState != null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("idHotel");
            }
        } else {
            id = (int) getIntent().getSerializableExtra("idHotel");
        }
        // Se encuentra el hotel en la base de datos
        hotel = Hotel.find(Hotel.class, "ID_HOTEL = " + id, null).get(0);
        // Si es diferente de null llena los campos
        if (hotel != null) {
            tvNombreHotel.setText(hotel.getTitulo());
            tvDescriptionHotel.setText(hotel.getDescripcion());
            tvDireccionHotel.setText(hotel.getDireccion());
            loadImageView(hotel.getImagen(), ivFotoHotel);


            paymentService = new StripeService(this, 9000);
        }


        new HotelViewModel(HotelActivity.this).execute(3500, paymentService, this);


        //Se recupera el perfil que esta logeado, por el momento es un numero fijo
        perfil = Perfil.find(Perfil.class, "ID_PERFIL = " + 1, null).get(0);
        //Se comprueba si existe le favorito en ese caso se pone la animación ya activa
        int i1 = Favorito.find(Favorito.class, "HOTEL = " + hotel.getId() + " AND PERFIL = " + perfil.getId(), null).size();
        if (i1 != 0) {
            botonf.setProgress(1);
        }

        botonf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //comprueba si ya existe el favorito
                int i1 = Favorito.find(Favorito.class, "HOTEL = " + hotel.getId() + " AND PERFIL = " + perfil.getId(), null).size();
                //Si no existe crea el favorito y activa la animación
                if (i1 == 0) {
                    Favorito favorito = new Favorito(1, perfil, hotel);
                    favorito.save();
                    botonf.playAnimation();
                    Toast.makeText(view.getContext(), "Se ha agregado el hotel a tus favoritos", Toast.LENGTH_LONG).show();
                    Log.d("prueba", i1 + "");
                } else {
                    // Si existe el favorito lo elimina y desactiva la animación
                    Favorito favoritoold = Favorito.find(Favorito.class, "HOTEL = " + hotel.getId() + " AND PERFIL = " + perfil.getId(), null).get(0);
                    favoritoold.delete();
                    botonf.setProgress(0);
                    Log.d("prueba", i1 + "");
                    Toast.makeText(view.getContext(), "Hotel eliminado tus favoritos", Toast.LENGTH_LONG).show();
                }
            }
        });
        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {

            onPaymentResult(paymentSheetResult);

        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentFlow();
            }
        });
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImageView(String url, ImageView imageView) {
        Picasso.get().load(url).into(imageView);
    }

    private void paymentFlow() {
        paymentSheet.presentWithPaymentIntent(
                paymentService.getFinalClientSecret(), new PaymentSheet.Configuration("Pagar a " + hotel.getTitulo(),
                        new PaymentSheet.CustomerConfiguration(paymentService.getFinalCustomerID(), paymentService.getFinalEphimeral()))
        );
    }

    @Override
    public void togglePaymentButton(boolean status) {
        pay.setEnabled(status);
    }
}