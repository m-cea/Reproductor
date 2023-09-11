package ar.edu.unsada.reproductor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;

import com.google.firebase.auth.FirebaseAuth;

public class tabRadio extends Fragment {
    private WebView pagina;
    private final String IP = "192.168.1.27:5000";
    private String baseUrl;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflar el diseño del Fragment
        View view = inflater.inflate(R.layout.fragment_tab_radio, container, false);

        pagina = view.findViewById(R.id.webRadio);

        // Cargar el contenido web desde servicio web Flask

        String url = "http://" + IP + "/";
        pagina.loadUrl(url);

        // Configurar el WebView para que muestre el contenido de tu servicio web de Flask
        pagina.getSettings().setJavaScriptEnabled(true);
        pagina.setWebViewClient(new WebViewClient() {
            // Capturar las nuevas URL cargadas en la WebView
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                baseUrl = url; // Almacena la nueva URL cargada en la variable base

                return super.shouldOverrideUrlLoading(view, url);
            }

        });

        // Cargar la página principal al iniciar la actividad
        pagina.loadUrl(baseUrl);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void irMain() {
        Intent i = new Intent(this.getContext(), Login.class);
        startActivity(i);
    }
}