package ar.edu.unsada.reproductor;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
public class Radio extends AppCompatActivity {

    private Button cerrar, play, pause, stop;
    private TextView usuario, radio;
    private FirebaseAuth mAuth;
    private RadioPlayer radioPlayer;
    private String streamUrl = "https://24073.live.streamtheworld.com/ROCKANDPOP_SC";
    private ImageView actual, logo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        //Conexion vista-logica
        cerrar = findViewById(R.id.btn_cerrar);
        usuario = findViewById(R.id.nombreUsuario);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        radio = findViewById(R.id.nombreRadio);
        stop = findViewById(R.id.stop);
        logo = findViewById(R.id.logo);
        actual = findViewById(R.id.actual);

        //Crea instancia de usuario y de radioPlayer
        mAuth = FirebaseAuth.getInstance();
        radioPlayer = new RadioPlayer();
        logo.setVisibility(View.VISIBLE);
        actual.setVisibility(View.INVISIBLE);

        //Sector información de usuario y cerrar
        usuario.setText("Bienvenid@ \n" + mAuth.getCurrentUser().getDisplayName());

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                irMain();
            }
        });

        //Play y pause
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioPlayer.playRadio(streamUrl);
                radio.setText("Radio Esta");
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);
                actual.setVisibility(View.VISIBLE);
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioPlayer.pauseRadio();
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioPlayer.stopRadio();
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                radio.setText("");
                actual.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        radioPlayer.stopRadio();
        logout();
    }

    private void logout() {
        mAuth.signOut();
    }

    private void irMain() {
        Intent i = new Intent(Radio.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
