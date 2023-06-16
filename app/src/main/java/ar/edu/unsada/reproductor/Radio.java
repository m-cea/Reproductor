package ar.edu.unsada.reproductor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Radio extends AppCompatActivity {

    private Button cerrar, play, pause;
    private TextView usuario, radio;
    private FirebaseAuth mAuth;
    private RadioPlayer radioPlayer;
    private String streamUrl = "https://24073.live.streamtheworld.com/ROCKANDPOP_SC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        //Crea instancia de usuario y de radioPlayer
        mAuth = FirebaseAuth.getInstance();
        radioPlayer = new RadioPlayer();

        //Conexion vista-logica
        cerrar = findViewById(R.id.btn_cerrar);
        usuario = findViewById(R.id.nombreUsuario);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        radio = findViewById(R.id.nombreRadio);

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

}
