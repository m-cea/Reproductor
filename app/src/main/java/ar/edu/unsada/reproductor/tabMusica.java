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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class tabMusica extends Fragment {
    private Button cerrar;
    private TextView usuario;
    private FirebaseAuth mAuth;
    private WebView pagina;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el diseño del Fragment
        View view = inflater.inflate(R.layout.fragment_tab_musica, container, false);

        // Aquí puedes obtener referencias a las vistas dentro del diseño del Fragment
        // y configurar cualquier lógica adicional que necesites


        //Conexion vista-logica
        pagina = view.findViewById(R.id.webMusica);

        // Cargar el contenido web desde servicio web Flask
        String ip = "192.168.1.27:5000";
        String url = "http://" + ip +"/Musica";
        pagina.loadUrl(url);

        // WebViewClient para manejar la navegación dentro de la WebView
        pagina.setWebViewClient(new WebViewClient());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logout();
    }

    private void logout() {
        try{
            mAuth.signOut();
        }catch(Exception e){
            Toast.makeText(requireContext(), "Se encuentra en modo de prueba", Toast.LENGTH_LONG).show();
        }
    }

    private void irMain() {
        Intent i = new Intent(this.getContext(), Login.class);
        startActivity(i);
    }
}