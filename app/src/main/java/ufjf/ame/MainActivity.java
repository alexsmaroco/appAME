package ufjf.ame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager lm;
    private Location loc;
    private FirebaseUser mAuthUser;
    private FirebaseDatabase db;
    private TextView txtWelcome;
    private Button btnLogout;
    private Button btnCriarEvento;
    private ListView listaEventos;
    private Usuario userAtual;
    //private ArrayList<Evento> arrayEventos; // lista de eventos, pegar do BD

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuthUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mAuthUser == null) { // sem usuario, volta pro login
            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(it);
        }

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0, this);
                loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,0,this);
                loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        userAtual = new Usuario();

        //GPS gps = new GPS((LocationManager)getSystemService(Context.LOCATION_SERVICE), this);
        Log.d("Loc: ",loc.getLatitude() + " " + loc.getLongitude());

        db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference();

        btnCriarEvento = (Button) findViewById(R.id.btnCriarEvento);
        btnCriarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, CriaEventoActivity.class);
                it.putExtra("user", userAtual);
                it.putExtra("location", loc);
                startActivity(it);
            }
        });

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // faz logout e volta pra tela de login
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FirebaseAuth.getInstance().signOut();
                Intent it = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(it);
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Recuperando usuario", mAuthUser.getDisplayName());
                userAtual.setUid(dataSnapshot.child("users").child(mAuthUser.getUid()).getValue(Usuario.class).getUid());
                userAtual.setName(dataSnapshot.child("users").child(mAuthUser.getUid()).getValue(Usuario.class).getName());
                userAtual.setInfluencia(dataSnapshot.child("users").child(mAuthUser.getUid()).getValue(Usuario.class).getInfluencia());
                userAtual.setCodClasse(dataSnapshot.child("users").child(mAuthUser.getUid()).getValue(Usuario.class).getCodClasse());

                txtWelcome.setText("Bem Vindo ao AME, " + userAtual.getName() + "!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listaEventos = (ListView) findViewById(R.id.listaEventos);
        /*
        EventAdapter adapter = new EventAdapter(this, arrayEventos);
        listaEvento.setAdapter(adapter);

         */
    }


    @Override
    public void onLocationChanged(Location location) {
        this.loc = location;
        Log.d("GPS: ", loc.getLatitude() + " " + loc.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}


// Classe para listar os eventos
/*
public class EventAdapter extends ArrayAdapter<Evento> {
    public EventAdapter(Context context, ArrayList<Evento> evt) {
        super(context, 0, evt);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Pega o evento
        Evento evt = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_evento, parent, false);
        }
        // Preenche campos
        TextView tipoEmerg = (TextView) convertView.findViewById(R.id.tipoEmerg);
        TextView confirmacao = (TextView) convertView.findViewById(R.id.confirmacao);
        Button btnDetalhes = (Button) convertView.findViewById(R.id.btnDetalhes);
        // Populate the data into the template view using the data object
        tipoEmerg.setText(evt.emerg);
        confirmacao.setText(evt.isConfirmado);
        // retorna para a lista exibir
        return convertView;
    }
}
*/
