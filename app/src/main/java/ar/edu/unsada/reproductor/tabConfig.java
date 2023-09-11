package ar.edu.unsada.reproductor;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class tabConfig extends Fragment {

    private Button cerrar;
    private ImageButton mail;
    private TextView usuario, textHoraA, textHoraE;
    private TimePicker horaA, horaE;
    private FirebaseAuth mAuth;
    private Switch timerA, timerE;
    private Calendar calendar;
    private int selectedHourA, selectedMinuteA, selectedHourE, selectedMinuteE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del Fragment
        View view = inflater.inflate(R.layout.fragment_tab_config, container, false);

        //Conexion vista logica

        timerA = view.findViewById(R.id.timerApagado);
        timerE = view.findViewById(R.id.timerEncendido);
        horaA = view.findViewById(R.id.timePickerA);
        horaE = view.findViewById(R.id.timePickerE);
        textHoraA = view.findViewById(R.id.textHoraA);
        textHoraE = view.findViewById(R.id.textHoraE);
        mail = view.findViewById(R.id.mail);
        //Toma la hora de apagado si existe

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        int shutdownHour = sharedPreferences.getInt("shutdownHour", -1); // -1 es un valor predeterminado en caso de que no se haya almacenado
        int shutdownMinute = sharedPreferences.getInt("shutdownMinute", -1);

        // Agregar un listener al Switch de cierre
        timerA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Aquí puedes responder al cambio de estado (on/off) del Switch de cierre
                if (isChecked) {
                    textHoraA.setVisibility(View.VISIBLE);
                    horaA.setVisibility(View.VISIBLE);
                    selectedHourA = horaA.getHour();
                    selectedMinuteA = horaA.getMinute();
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("shutdownHour", selectedHourA);
                    editor.putInt("shutdownMinute", selectedMinuteA);
                    editor.apply();
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            // Realizar la comprobación de la hora actual aquí
                            calendar = Calendar.getInstance();
                            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                            int currentMinute = calendar.get(Calendar.MINUTE);

                            // Comparar con la hora y los minutos programados
                            if (currentHour == shutdownHour && currentMinute == shutdownMinute) {
                                // La hora actual coincide con la hora programada, por lo que debes cerrar la aplicación
                                cerrarApp();
                            }

                            // Programar la siguiente ejecución después de un minuto
                            handler.postDelayed(this, 60 * 1000); // 60 segundos = 1 minuto
                        }
                    };
                    // Iniciar la comprobación
                    handler.post(runnable);
                } else {
                    textHoraA.setVisibility(View.GONE);
                    horaA.setVisibility(View.GONE);
                    selectedHourA = -1;
                    selectedMinuteA = -1;
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("shutdownHour", selectedHourA);
                    editor.putInt("shutdownMinute", selectedMinuteA);
                    editor.apply();
            }
        }
        });

        // Agregar un listener al Switch de encendido
        timerE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        textHoraE.setVisibility(View.VISIBLE);
                        horaE.setVisibility(View.VISIBLE);
                        selectedHourE = horaE.getHour();
                        selectedMinuteE = horaE.getMinute();

                        // Obtener el contexto desde el fragmento
                        Context context = requireContext();

                        // Usar el contexto para obtener el servicio del sistema
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                        // Obtener la hora actual en milisegundos
                        Calendar calendarActual = Calendar.getInstance();
                        long tiempoActual = calendarActual.getTimeInMillis();

                        // Crear un nuevo objeto Calendar con la hora deseada
                        Calendar calendarDeseada = Calendar.getInstance();
                        calendarDeseada.set(Calendar.HOUR_OF_DAY, selectedHourE);
                        calendarDeseada.set(Calendar.MINUTE, selectedMinuteE);

                        // Obtener el tiempo deseado en milisegundos
                        long tiempoAlarma = calendarDeseada.getTimeInMillis();

                        // Asegúrate de que la hora deseada sea en el futuro
                        if (tiempoAlarma <= tiempoActual) {
                            // Si la hora deseada ya pasó, agrega un día (24 horas) para programar la alarma para el próximo día.
                            tiempoAlarma += 24 * 60 * 60 * 1000; // Sumar 24 horas en milisegundos
                        }

                        // Crea un Intent que inicie tu actividad principal
                        Intent intent = new Intent(context, Tabs.class);

                        // Crea un PendingIntent que inicie la actividad
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        // Configura la alarma
                        alarmManager.set(AlarmManager.RTC_WAKEUP, tiempoAlarma, pendingIntent);

                    } else {
                        textHoraE.setVisibility(View.GONE);
                        horaE.setVisibility(View.GONE);
                        selectedHourE = -1;
                        selectedMinuteE = -1;
                    }
        }
        });

        //Mail al equipo desarrollador

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent con la acción ACTION_SENDTO
                Intent intent = new Intent(Intent.ACTION_SENDTO);

                // Especificar el URI de correo electrónico (con la dirección deseada)
                Uri uri = Uri.parse("mailto:" + "mariamartaplaysense@gmail.com");
                intent.setData(uri);

                // Agregar un asunto (opcional)
                intent.putExtra(Intent.EXTRA_SUBJECT, "Quiero informar");

                // Agregar el contenido del correo (opcional)
                intent.putExtra(Intent.EXTRA_TEXT, "Hola equipo desarrollador! Quiero comentarles que...");

                // Verificar si hay una aplicación de correo electrónico disponible para manejar el intent
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(requireContext(), "No se dispone de app para envío de correo", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
            finishAffinity(requireActivity());
        }
    }

    private void irMain() {
        Intent i = new Intent(this.getContext(), Login.class);
        startActivity(i);
    }

    private void cerrarApp() {
        // Obtener la actividad principal desde el fragment
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            // Llamar a finishAffinity() desde la actividad
            activity.finishAffinity();
        }
    }

}