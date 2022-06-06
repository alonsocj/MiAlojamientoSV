package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.se.omapi.Session;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import sv.edu.ues.fia.eisi.mialojamientosv.JavaMailAPI;
import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.SplashScreen;
import sv.edu.ues.fia.eisi.mialojamientosv.StripeService;
import sv.edu.ues.fia.eisi.mialojamientosv.databinding.ActivityHotelBinding;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Favorito;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Habitacion;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Hotel;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Perfil;
import sv.edu.ues.fia.eisi.mialojamientosv.model.Reservacion;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.viewModel.Comunicacion;
import sv.edu.ues.fia.eisi.mialojamientosv.ui.viewModel.HotelViewModel;

public class HotelActivity extends AppCompatActivity implements Comunicacion, OnMapReadyCallback {

    ActivityHotelBinding binding;
    TextView tvNombreHotel, tvDireccionHotel, tvDescriptionHotel, tiempoReserva, tiempoReservaFin, precioReserva, camas, banios, personas, servicios, valoracion;
    String fecha, fechaFin, hoy;
    String reserva, reservaFin, precio;
    ImageView ivFotoHotel;
    LottieAnimationView botonf;
    Button pay;
    Hotel hotel;
    Perfil perfil;
    String id = "";
    StripeService paymentService;
    PaymentSheet paymentSheet;
    GoogleMap map;

    Session session=null;
    String rec, subject, textMessage;
    ProgressDialog pdialog = null;

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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

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

        tiempoReservaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTiempoReservaFin();
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentFlow();
            }
        });
    }

    //Envio de correo de la confirmación de la reservación.
    private void envioEmail(){
        JavaMailAPI javaMailAPI=new JavaMailAPI(this,"gustavopineda400@gmail.com","Reservacion elaborada","Su reservación ha sido satisfactoria");
        javaMailAPI.execute();
    }

    private void elementsInit() {
        botonf = binding.animationFavoriteHotel;
        tvNombreHotel = binding.tvHotelName;
        tvDireccionHotel = binding.tvAddress;
        tvDescriptionHotel = binding.tvHotelDescription;
        ivFotoHotel = binding.ivHotel;
        precioReserva = binding.precioReserva;
        camas = binding.cantCamas;
        banios = binding.cantBanios;
        personas = binding.cantPersonas;
        servicios = binding.extra;
        pay = binding.pay;
        tiempoReserva = binding.tiempoReserva;
        tiempoReserva.setText(fecha);
        tiempoReservaFin = binding.tiempoReservaFin;
        tiempoReservaFin.setText(fechaFin);
        valoracion = binding.tvHotelEvaluation;

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
        hotel = Hotel.find(Hotel.class, "ID_HOTEL = '" + id + "'", null).get(0);
        // Si es diferente de null llena los campos
        if (hotel != null) {
            Habitacion habitacion = Habitacion.find(Habitacion.class, "ID_HOTEL = '" + hotel.getId() + "'", null).get(0);
            String[] precioCalc = habitacion.getPrecioPorDia().split("");
            String precio = "";
            for (int i = precioCalc.length - 1; i >= 0; i--) {
                if (i == precioCalc.length - 3) {
                    precio += ".";
                }
                precio += precioCalc[i];
            }
            camas.setText("" + habitacion.getCantCamas());
            banios.setText("" + habitacion.getCantBat());
            personas.setText("" + habitacion.getCantPersonas());
            servicios.setText(habitacion.getServiciosExtra());
            StringBuilder precioFinal = new StringBuilder(precio);
            tvNombreHotel.setText(hotel.getTitulo());
            tvDescriptionHotel.setText(hotel.getDescripcion());
            tvDireccionHotel.setText(hotel.getDireccion());
            valoracion.setText(hotel.getEvaluaciones());
            loadImageView(hotel.getImagen(), ivFotoHotel);
            this.precio = precioFinal.reverse().toString();
            precioReserva.setText("$" + this.precio + " noche");

            paymentService = new StripeService(this, Integer.parseInt(habitacion.getPrecioPorDia()));
        }


        new HotelViewModel(HotelActivity.this).execute(3500, paymentService, this);
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            int diaI, diaF;
            int cantDias;
            Log.v("Fecha: ", reservaFin);
            String[] fechaI = reserva.split("/", 5);
            String[] fechaF = reservaFin.split("/", 5);
            int mesI = Integer.parseInt(fechaI[1]);
            int mesF = Integer.parseInt(fechaF[1]);
            if (mesI == mesF) {
                diaI = Integer.parseInt(fechaI[0]);
                diaF = Integer.parseInt(fechaF[0]);
                cantDias = diaF - diaI;
            } else {
                diaI = Integer.parseInt(fechaI[0]);
                diaF = Integer.parseInt(fechaF[0]);
                cantDias = (diaF - diaI) + (30 - mesF) + (mesI - 1);
            }

            Reservacion reservacion = new Reservacion();
            reservacion.setIdHotel(hotel);
            reservacion.setIdPerfil(perfil);
            reservacion.setFechaInicio(reserva);
            reservacion.setFechaFin(reservaFin);
            reservacion.setFechaRegistro(hoy);
            reservacion.setPrecioTotal(cantDias * (Float.parseFloat(this.precio)));
            reservacion.save();
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            envioEmail();
        }
    }

    private void loadImageView(String url, ImageView imageView) {
        Picasso.get().load(url).into(imageView);
    }

    private void paymentFlow() {
        PaymentSheet.CustomerConfiguration customerConfiguration = new PaymentSheet.CustomerConfiguration(paymentService.getFinalCustomerID(),
                paymentService.getFinalEphimeral());
        PaymentSheet.BillingDetails billingDetails = new PaymentSheet.BillingDetails(new PaymentSheet.Address("", "SV", hotel.getDireccion(), "", "", ""),
                null,null,null);
        PaymentSheet.Configuration config = new PaymentSheet.Configuration.Builder(hotel.getTitulo())
                .defaultBillingDetails(billingDetails).customer(customerConfiguration).build();
        /*(hotel.getTitulo(),
                new PaymentSheet.CustomerConfiguration(paymentService.getFinalCustomerID(), paymentService.getFinalEphimeral()));*/
        paymentSheet.presentWithPaymentIntent(
                paymentService.getFinalClientSecret(), config
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
        Calendar calendar = Calendar.getInstance();
        int anio, mes, dia;
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog date = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fecha = selectMonth(month) + " " + dayOfMonth;
                reserva = dayOfMonth + "/" + month + "/" + year;
                tiempoReserva.setText(fecha);
            }
        }, anio, mes, dia);
        date.getDatePicker().setMinDate(calendar.getTimeInMillis());
        date.show();

    }

    private void setTiempoReservaFin() {
        Calendar calendar = Calendar.getInstance();
        int anio, mes, dia;
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog date = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fechaFin = selectMonth(month) + " " + dayOfMonth;
                reservaFin = dayOfMonth + "/" + month + "/" + year;
                tiempoReservaFin.setText(fechaFin);
            }
        }, anio, mes, dia);
        date.getDatePicker().setMinDate(calendar.getTimeInMillis());
        date.show();
    }

    private void setTimeinit() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        String monthString = selectMonth(month);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        fecha = (monthString) + " " + day;
        fechaFin = (monthString) + " " + (day + 1);
        hoy = day + "/" + month + "/" + year;
        reserva = day + "/" + month + "/" + year;
        reservaFin = (day + 1) + "/" + month + "/" + year;
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(HotelActivity.this, R.raw.stylemap));

        // Se encuentra el hotel en la base de datos
        hotel = Hotel.find(Hotel.class, "ID_HOTEL = '" + id + "'", null).get(0);
        if (hotel != null) {
            LatLng ubicacion = new LatLng(Double.parseDouble(hotel.getLatitudH()), Double.parseDouble(hotel.getLongitudH()));
            map.addMarker(new MarkerOptions().position(ubicacion).title(hotel.getTitulo()));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f));
        }

    }
}