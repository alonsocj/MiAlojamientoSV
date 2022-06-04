package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import java.util.Calendar;

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
    TextView tvNombreHotel, tvDireccionHotel, tvDescriptionHotel, tiempoReserva;
    String fecha;
    ImageView ivFotoHotel;
    LottieAnimationView botonf;
    Button pay;
    Hotel hotel;
    Perfil perfil;
    String id = "";
    StripeService paymentService;
    PaymentSheet paymentSheet;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTimeinit();
        elementsInit();
        setTimeinit();
        favAnimation();

        infoHotelActual(savedInstanceState);

        //Se recupera el perfil que esta logeado, por el momento es un numero fijo
        perfil = Perfil.find(Perfil.class, "ID_PERFIL = " + 1, null).get(0);
        //Se comprueba si existe le favorito en ese caso se pone la animación ya activa
        int i1 = Favorito.find(Favorito.class, "HOTEL = " + hotel.getId() + " AND PERFIL = " + perfil.getId(), null).size();
        if (i1 != 0) {
            botonf.setProgress(1);
        }

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {

            onPaymentResult(paymentSheetResult);

        });
        tiempoReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTiempoReserva();
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentFlow();
            }
        });
    }

    private void elementsInit() {
        botonf = binding.animationFavoriteHotel;
        tvNombreHotel = binding.tvHotelName;
        tvDireccionHotel = binding.tvAddress;
        tvDescriptionHotel = binding.tvHotelDescription;
        ivFotoHotel = binding.ivHotel;
        pay = binding.pay;
        tiempoReserva = binding.tiempoReserva;
        tiempoReserva.setText(fecha);

    }

    private void infoHotelActual(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                id = null;
            } else {
                id = extras.getString("idHotel");
            }
        } else {
            id = (String) getIntent().getSerializableExtra("idHotel");
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

    private void favAnimation() {
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
    }

    private void setTiempoReserva() {

    }

    private void setTimeinit() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        String monthString = selectMonth(month);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        fecha = (monthString) + " " + day + "-" + (day + 1);
    }


    @Override
    public void togglePaymentButton(boolean status) {
        if (status) {
            pay.setEnabled(status);
            pay.setText("Reservar");
        } else {
            pay.setEnabled(status);
            pay.setText("...");
        }
    }

    private String selectMonth(int month) {
        String monthString = "";
        switch (month) {
            case 0:
                monthString = "Enero";
                break;
            case 1:
                monthString = "Febrero";
                break;
            case 2:
                monthString = "Marzo";
                break;
            case 3:
                monthString = "Abril";
                break;
            case 4:
                monthString = "Mayo";
                break;
            case 5:
                monthString = "Junio";
                break;
            case 6:
                monthString = "Julio";
                break;
            case 7:
                monthString = "Agosto";
                break;
            case 8:
                monthString = "Septiembre";
                break;
            case 9:
                monthString = "Octubre";
                break;
            case 10:
                monthString = "Noviembre";
                break;
            case 11:
                monthString = "Diciembre";
                break;
        }
        return monthString;
    }
}