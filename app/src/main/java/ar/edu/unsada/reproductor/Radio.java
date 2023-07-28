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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Radio extends AppCompatActivity {

    private Button cerrar;
    private TextView usuario;
    private FirebaseAuth mAuth;
    private RadioPlayer radioPlayer;
    private WebView pagina;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        //Conexion vista-logica
        cerrar = findViewById(R.id.btn_cerrar);
        usuario = findViewById(R.id.nombreUsuario);
        pagina = findViewById(R.id.webview);

        //Crea instancia de usuario y de radioPlayer
        mAuth = FirebaseAuth.getInstance();
        radioPlayer = new RadioPlayer();

        //Sector información de usuario y cerrar
        usuario.setText("Bienvenid@ \n" + mAuth.getCurrentUser().getDisplayName());

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                irMain();
            }
        });

        // Cargar el contenido web desde servicio web Flask
        String url = "http://192.168.0.103:5000/";
        pagina.loadUrl(url);

        // WebViewClient para manejar la navegación dentro de la WebView
        pagina.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
