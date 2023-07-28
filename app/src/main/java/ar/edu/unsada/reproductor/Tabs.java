package ar.edu.unsada.reproductor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class Tabs extends AppCompatActivity {

    private Button cerrar;
    private TextView usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        //Conexion vista-logica

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

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
    }
}