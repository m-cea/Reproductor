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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class tabRadio extends Fragment {
    private WebView pagina;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflar el diseño del Fragment
        View view = inflater.inflate(R.layout.fragment_tab_radio, container, false);

        pagina = view.findViewById(R.id.webRadio);

        // Cargar el contenido web desde servicio web Flask
        String url = "http://192.168.0.103:5000/";
        pagina.loadUrl(url);

        // WebViewClient para manejar la navegación dentro de la WebView
        pagina.setWebViewClient(new WebViewClient());

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