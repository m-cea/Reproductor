package ar.edu.unsada.reproductor;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {
        private static final int RC_SIGN_IN = 9001;
        private GoogleSignInClient mGoogleSignInClient;
        private Button atajo;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            SignInButton btnSignInWithGoogle = findViewById(R.id.btn_google);

            // Configurar el clic del botón para iniciar sesión con Google
            btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signInWithGoogle();
                }
            });

            // Configura el cliente de inicio de sesión con Google
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            atajo = findViewById(R.id.atajo);
            atajo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Login.this, Tabs.class);
                    startActivity(i);
                }
            });
        }

        // Método para iniciar el inicio de sesión con Google
        private void signInWithGoogle() {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

        // Procesar el resultado del inicio de sesión
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Inicio de sesión con éxito, obtén la cuenta de Google
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    // Error en el inicio de sesión con Google
                    Log.w(TAG, "Error al iniciar sesión con Google: " + e.getStatusCode());
                }
            }
        }

        // Autenticar con Firebase usando la cuenta de Google
        private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Inicio de sesión con Firebase exitoso, ir a la actividad de las pestañas (MainActivity)
                                Intent intent = new Intent(Login.this, Tabs.class);
                                startActivity(intent);
                                finish(); // Opcional: finalizar la actividad de inicio de sesión para que no se pueda volver atrás
                            } else {
                                // Error en el inicio de sesión con Firebase
                                Log.w(TAG, "Error al iniciar sesión con Firebase: " + task.getException());
                                // Mostrar un mensaje de error, si es necesario
                                Toast.makeText(Login.this, "Error en el inicio de sesión con Firebase", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }
