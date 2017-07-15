package ufjf.ame;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mAuthUser == null) { // sem usuario, volta pro login
            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(it);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 501);
        }


        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        userAtual = new Usuario();

        db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference();

        btnCriarEvento = (Button) findViewById(R.id.btnCriarEvento);
        btnCriarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                if(loc != null) {
                    Intent it = new Intent(MainActivity.this, CriaEventoActivity.class);
                    it.putExtra("user", userAtual);
                    it.putExtra("location", loc);
                    startActivity(it);
                } else {
                    Toast.makeText(getApplicationContext(), "Não é possível obter sua localização atual", Toast.LENGTH_LONG);
                }
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

        ref.child("users").child(mAuthUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Recuperando usuario", mAuthUser.getDisplayName());
                userAtual = dataSnapshot.getValue(Usuario.class);
                /*
                userAtual.setUid(dataSnapshot.getValue(Usuario.class).getUid());
                userAtual.setName(dataSnapshot.getValue(Usuario.class).getName());
                userAtual.setInfluencia(dataSnapshot.getValue(Usuario.class).getInfluencia());
                userAtual.setCodClasse(dataSnapshot.getValue(Usuario.class).getCodClasse());
                */
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

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 501: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission. ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        String prov = lm.getBestProvider(new Criteria(), false);
                        lm.requestLocationUpdates(prov, 1000, 0, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
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
