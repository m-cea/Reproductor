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
    private SearchView busquedaRadio;

    private String baseUrl;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflar el diseño del Fragment
        View view = inflater.inflate(R.layout.fragment_tab_radio, container, false);

        pagina = view.findViewById(R.id.webRadio);
        busquedaRadio = view.findViewById(R.id.busquedaRadio);

        // Cargar el contenido web desde servicio web Flask
        String url = "http://192.168.0.103:5000/";
        pagina.loadUrl(url);
        if(url.equals("http://192.168.0.103:5000/")){
            busquedaRadio.setVisibility(View.GONE);
        }

        // Configurar el WebView para que muestre el contenido de tu servicio web de Flask
        pagina.getSettings().setJavaScriptEnabled(true);
        pagina.setWebViewClient(new WebViewClient() {
            // Capturar las nuevas URL cargadas en la WebView
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                baseUrl = url; // Almacena la nueva URL cargada en la variable base
                if(baseUrl.equals("http://192.168.0.103:5000/")){
                    busquedaRadio.setVisibility(View.GONE);
                }else{
                    busquedaRadio.setVisibility(View.VISIBLE);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            // Capturar errores al cargar una URL en la WebView
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // Aquí puedes mostrar un mensaje de error o tomar otras acciones
                pagina.loadUrl("http://192.168.0.103:5000/error");
            }
        });

        // Cargar la página principal al iniciar la actividad
        pagina.loadUrl(baseUrl);


        busquedaRadio.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Cargar la página de búsqueda en la WebView
                if (baseUrl.contains("AM")) {
                    pagina.loadUrl(baseUrl + "/reproducirAM/" + query); // Ejemplo: http://tu-servicio-web-flask.com?search=termino-busqueda
                } else if (baseUrl.contains("FM")) {
                    pagina.loadUrl(baseUrl + "/reproducirFM/" + query); // Ejemplo: http://tu-servicio-web-flask.com?search=termino-busqueda
                } else if (baseUrl.contains("Mundial")) {
                    pagina.loadUrl(baseUrl + "/Mundial/" + query); // Ejemplo: http://tu-servicio-web-flask.com?search=termino-busqueda
                }else if (baseUrl.contains("Musica")) {
                    pagina.loadUrl(baseUrl + "/Mundial/" + query); // Ejemplo: http://tu-servicio-web-flask.com?search=termino-busqueda
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // No se necesita realizar ninguna acción cuando el texto cambia
                return false;
            }


        });



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