package ar.edu.unsada.reproductor;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Radio extends AppCompatActivity {

    private Button cerrar, play, pause;
    private TextView usuario;
    private FirebaseAuth mAuth;
    private RadioPlayer radioPlayer;
    private String streamUrl = "https://24073.live.streamtheworld.com/ROCKANDPOP_SC";

    //Gestion de permisos
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), aceptado -> {
                if (aceptado) Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show();
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

//Crea instancia de usuario
        mAuth = FirebaseAuth.getInstance();

        //Conexion vista-logica
        cerrar = findViewById(R.id.btn_cerrar);
        usuario = findViewById(R.id.nombreUsuario);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);

        //Sector información de usuario y cerrar
        usuario.setText("Bienvenid@ \n" + mAuth.getCurrentUser().getDisplayName());

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                irMain();
            }
        });



            radioPlayer = new RadioPlayer();

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verificarPermisos(v);
                }
            });

            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    radioPlayer.stopRadio();
                }
            });
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            radioPlayer.stopRadio();
            logout();
        }

    private void logout(){
        mAuth.signOut();
    }

    private void irMain(){
        Intent i = new Intent(Radio.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    //Metodo validador de permisos
    private void verificarPermisos(View v) {
        if (ContextCompat.checkSelfPermission(Radio.this, android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            radioPlayer.playRadio(streamUrl);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(Radio.this, Manifest.permission.INTERNET)) {
            Snackbar.make(v, "Permiso necesario para crear el archivo", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPermissionLauncher.launch(android.Manifest.permission.INTERNET);
                }
            });
        } else {
            requestPermissionLauncher.launch(Manifest.permission.INTERNET);
        }

        if (ContextCompat.checkSelfPermission(Radio.this, android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            radioPlayer.playRadio(streamUrl);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(Radio.this, Manifest.permission.ACCESS_NETWORK_STATE)) {
            Snackbar.make(v, "Permiso necesario para crear el archivo", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_NETWORK_STATE);
                }
            });
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if (ContextCompat.checkSelfPermission(Radio.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            radioPlayer.playRadio(streamUrl);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(Radio.this, Manifest.permission.RECORD_AUDIO)) {
            Snackbar.make(v, "Permiso necesario para crear el archivo", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO);
                }
            });
        } else {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

}
