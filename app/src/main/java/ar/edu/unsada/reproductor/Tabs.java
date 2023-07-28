package ar.edu.unsada.reproductor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;

public class Tabs extends AppCompatActivity {

    private Button cerrar;
    private TextView usuario;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        //Conexion vista-logica

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        cerrar = findViewById(R.id.btn_cerrar);
        usuario = findViewById(R.id.nombreUsuario);

        //Manejo de las tabs

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(new tabRadio(), "Radio");
        pagerAdapter.addFragment(new tabMusica(), "Musica");
        //pagerAdapter.addFragment(new tabConfig(), "Configuración");

        // Configura los colores del texto de las pestañas activas e inactivas
        int colorTabTextActive = ContextCompat.getColor(this, R.color.crimson_sky);
        int colorTabTextInactive = ContextCompat.getColor(this, R.color.lightblue);
        tabLayout.setTabTextColors(colorTabTextInactive, colorTabTextActive);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Obtener el usuario actualmente conectado
        FirebaseUser user = mAuth.getCurrentUser();

        //Saludo al usuario
        usuario.setText("Bienvenid@ \n" + user.getDisplayName());

        //Cerrar Sesion
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    public void logout() {
        // Revocar el acceso de la aplicación a la cuenta de Google tal que la proxima vez que se inicia la app deba loguear otra vez
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build());
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Navegar de vuelta a la actividad de inicio de sesión
                Intent intent = new Intent(Tabs.this, Login.class);
                startActivity(intent);
            }
        });
    }

}
