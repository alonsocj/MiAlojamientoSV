package sv.edu.ues.fia.eisi.mialojamientosv;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FCM extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("token","mi token "+token);
        guardarToken(token);
    }

    private void guardarToken(String token) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("token");
        ref.child("id").setValue(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getData().size()>0){
            String titulo = message.getData().get("Titulo");
            String detalle = message.getData().get("detalle");
            notificacion(titulo,detalle);
        }
    }
    private void notificacion(String titulo,String detalle){
        String id = "mensaje";
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(id,"nuevo",NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            nm.createNotificationChannel(nc);
        }
        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(titulo)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(detalle)
                .setContentIntent(clicknoti())
                .setContentInfo("nuevo");
        Random random = new Random();
        int idN = random.nextInt(8000);

        assert nm != null;
        nm.notify(idN,builder.build());
    }

    public PendingIntent clicknoti(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
    }
}
