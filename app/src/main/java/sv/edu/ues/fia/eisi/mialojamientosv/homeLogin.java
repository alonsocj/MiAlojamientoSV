package sv.edu.ues.fia.eisi.mialojamientosv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

import sv.edu.ues.fia.eisi.mialojamientosv.ui.view.RegistroActivity;

public class homeLogin extends AppCompatActivity {

    TextInputEditText CorreoLogin, PasswordLogin;
    Button ingresar, registro;


    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_login);

        /*ActionBar actionBar=getSupportActionBar();
        assert actionBar!=null;
        actionBar.setTitle("Login");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        CorreoLogin=findViewById(R.id.EditUsuarioLogin);
        PasswordLogin=findViewById(R.id.EditPasswordLogin);
        ingresar=findViewById(R.id.botonIniciar);
        registro=findViewById(R.id.botonResgistrarse);

        firebaseAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(homeLogin.this);
        dialog = new Dialog(homeLogin.this);

        /*crearSolicitud();*/

        ingresar.setOnClickListener((view) ->{
            String correo=CorreoLogin.getText().toString();
            String pass=PasswordLogin.getText().toString();

            if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                CorreoLogin.setError("Correo inválido");
                CorreoLogin.setFocusable(true);
            }else if (pass.length()<6){
                PasswordLogin.setError("La contraseña debe ser mayor o iguall a 6 caracteres");
                PasswordLogin.setFocusable(true);
            }else{
                loguearse(correo,pass);
            }
            });
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(homeLogin.this, RegistroActivity.class));
            }
        });
    }
    private void loguearse(String correo, String pass) {
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user=firebaseAuth.getCurrentUser();

                            startActivity(new Intent(homeLogin.this, MainActivity.class));

                            assert user != null; //se afirma que el usuario no es nulo
                            finish();
                        }else{
                            progressDialog.dismiss();
                            Dialog_No_Incio();
                            //Toast.makeText(homeLogin.this,"Su correo y contraseña no son válidos",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(homeLogin.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void Dialog_No_Incio(){
        Button respuesta;
        dialog.setContentView(R.layout.no_session);
        respuesta=dialog.findViewById(R.id.respuesta);
        respuesta.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

}


























