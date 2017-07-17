package ufjf.ame;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private EventAdapter adapter;
    private ArrayList<Evento> arrayEventos = new ArrayList<>(); // lista de eventos, pegar do BD

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

        userAtual = null;
        db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference();

        ref.child("users").child(mAuthUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Recuperando usuario ", mAuthUser.getDisplayName());
                userAtual = dataSnapshot.getValue(Usuario.class);
                txtWelcome.setText("Bem Vindo ao AME, " + userAtual.getName() + "!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        txtWelcome = (TextView) findViewById(R.id.txtWelcome);


        btnCriarEvento = (Button) findViewById(R.id.btnCriarEvento);
        btnCriarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(loc == null) {
                        loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                if(loc != null && userAtual != null) {
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
                FirebaseAuth.getInstance().signOut();
                Intent it = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(it);
            }
        });


        DatabaseReference refEvt = db.getReference().child("events");
        refEvt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayEventos.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Evento evt = ds.getValue(Evento.class);
                    Log.d("Recuperou evento: ",evt.getId());
                    arrayEventos.add(evt);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listaEventos = (ListView) findViewById(R.id.listaEventos);
        listaEventos.setClickable(true);
        listaEventos.setEnabled(true);
        adapter = new EventAdapter(this, arrayEventos);
        adapter.setLoc(loc);
        listaEventos.setAdapter(adapter);
        listaEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if(loc == null) {
                        loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                if(userAtual != null) {
                    Intent it = new Intent(MainActivity.this, DetalhesEvtActivity.class);
                    it.putExtra("location", loc);
                    it.putExtra("evt", arrayEventos.get(position));
                    it.putExtra("user", userAtual);
                    startActivity(it);
                }

            }
        });


    }

    public void atualizaArrayEventos() {
        DatabaseReference ref = db.getReference();
        ref.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayEventos.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Evento evt = ds.getValue(Evento.class);
                    Log.d("Distancia: ", distanciaEntre(evt.getLoc().getLatitude(), evt.getLoc().getLongitude(),loc.getLatitude(), loc.getLongitude()) + " metros");
                    Log.d("Recuperou evento: ",evt.getId());
                    arrayEventos.add(evt);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public double distanciaEntre(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371;

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist;
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
                        lm.requestLocationUpdates(prov, 10000, 0, this);
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
        // atualiza lista de eventos proximos
        adapter.setLoc(loc);
        atualizaArrayEventos();

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

class EventAdapter extends ArrayAdapter<Evento> {
    private Context ctx;
    private ArrayList<Evento> evts;
    private Location loc;
    public EventAdapter(Context context, ArrayList<Evento> evt) {
        super(context, android.R.layout.simple_selectable_list_item, evt);
        this.ctx = context;
        this.evts = evt;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Pega o evento
        final Evento evt = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_evento, parent, false);
        }
        // Preenche campos
        TextView tipoEmerg = (TextView) convertView.findViewById(R.id.tipoEmerg);
        TextView confirmacao = (TextView) convertView.findViewById(R.id.isConf);
        tipoEmerg.setText(evt.getTipoEvt());
        String isConf = "Não confirmado";
        if (evt.isConfirmado()) {
            isConf = "Confirmado";
        }
        confirmacao.setText(isConf);

        // retorna para a lista exibir
        return convertView;

    }
}

