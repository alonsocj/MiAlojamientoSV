package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.stripe.util.StringUtils;

import java.util.HashMap;
import java.util.Locale;

import sv.edu.ues.fia.eisi.mialojamientosv.FCM;
import sv.edu.ues.fia.eisi.mialojamientosv.MainActivity;
import sv.edu.ues.fia.eisi.mialojamientosv.R;
import sv.edu.ues.fia.eisi.mialojamientosv.SplashScreen;
import sv.edu.ues.fia.eisi.mialojamientosv.homeLogin;

public class RegistroActivity extends AppCompatActivity {

    TextInputEditText nombre,genero,CorreoLogin, PasswordLogin;
    Button registro;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        /*ActionBar actionBar=getSupportActionBar();
        assert actionBar!=null;
        actionBar.setTitle("Registro");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        nombre=findViewById(R.id.EditNombrePerfil);
        genero=findViewById(R.id.EditGeneroPerfil);
        CorreoLogin=findViewById(R.id.EditUsuarioLogin);
        PasswordLogin=findViewById(R.id.EditPasswordLogin);
        registro=findViewById(R.id.botonResgistrarse);

        firebaseAuth=FirebaseAuth.getInstance();

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo=CorreoLogin.getText().toString();
                String pass=PasswordLogin.getText().toString();
                String nom=nombre.getText().toString();
                String gen=genero.getText().toString();

                if(nom.isEmpty()){
                    nombre.setError("Debe ingresar su nombre de usuario");
                    nombre.setFocusable(true);
                }else if(!(gen.equalsIgnoreCase("femenino") || gen.equalsIgnoreCase("masculino"))){
                    genero.setError("Debe indicar si su genero es femenino o masculino");
                    genero.setFocusable(true);
                }else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    CorreoLogin.setError("Correo inválido");
                    CorreoLogin.setFocusable(true);
                }else if (pass.length()<6){
                    PasswordLogin.setError("La contraseña debe ser mayor o iguall a 6 caracteres");
                    PasswordLogin.setFocusable(true);
                }else{
                    registrarse(correo,pass);
                }
            }

        });
    }
    private void registrarse(String correo, String pass) {
        firebaseAuth.createUserWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=firebaseAuth.getCurrentUser();

                            assert user != null; //se afirma que el usuario no es nulo
                            //obtenemos uid
                            String uid=user.getUid();
                            //datos a registrarse
                            String correo=CorreoLogin.getText().toString();
                            String pass=PasswordLogin.getText().toString();
                            String nom=nombre.getText().toString();
                            String gen=genero.getText().toString();


                            //Creamos un HashMap para mandar los datos a Firebase
                            HashMap<Object,String> DatosUsuario=new HashMap<>();
                            DatosUsuario.put("uid",uid);
                            DatosUsuario.put("correo",correo);
                            DatosUsuario.put("password",pass);
                            DatosUsuario.put("nombre",nom);
                            DatosUsuario.put("genero",gen);

                            FirebaseDatabase database= FirebaseDatabase.getInstance();
                            DatabaseReference reference=database.getReference("usuariosApp");
                            reference.child(uid).setValue(DatosUsuario);

                            Toast.makeText(RegistroActivity.this,"Se registró exitosamente",Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(RegistroActivity.this, homeLogin.class));
                        }else{
                            Toast.makeText(RegistroActivity.this,"No se pudo resgistrar, ya existe la cuenta",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }
}















