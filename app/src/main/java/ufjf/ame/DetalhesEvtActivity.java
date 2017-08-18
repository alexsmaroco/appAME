package ufjf.ame;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class DetalhesEvtActivity extends AppCompatActivity {

    private TextView txtTipoEvt;
    private TextView txtConfirmado;
    private TextView txtLatitude;
    private TextView txtLongitude;
    private TextView txtHora;
    private TextView txtDist;
    private TextView txtInfluenciaNec;
    private TextView txtInfluenciaTotal;
    private TextView txtSuporte;
    private TextView txtVotos;
    private Button btnSim;
    private Button btnNao;
    private Evento evt;
    private Usuario user;
    private boolean podeVotar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_evt);

        txtTipoEvt = (TextView) findViewById(R.id.txtTipoEmerg);
        txtConfirmado = (TextView) findViewById(R.id.txtConfirmado);
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtHora = (TextView) findViewById(R.id.txtHora);
        txtDist = (TextView) findViewById(R.id.txtDistancia);
        txtInfluenciaNec = (TextView) findViewById(R.id.txtInfluenciaNec);
        txtInfluenciaTotal = (TextView) findViewById(R.id.txtInfluenciaTotal);
        txtSuporte = (TextView) findViewById(R.id.txtSuporte);
        txtVotos = (TextView) findViewById(R.id.txtVotos);
        btnSim = (Button) findViewById(R.id.btnSim);
        btnNao = (Button) findViewById(R.id.btnNao);

        Location loc = getIntent().getParcelableExtra("location");
        evt = (Evento) getIntent().getSerializableExtra("evt");
        user = (Usuario) getIntent().getSerializableExtra("user");


        verificarUID();

        txtTipoEvt.setText(txtTipoEvt.getText() + " " + evt.getTipoEvt());
        String str = "Não";
        if(evt.isConfirmado()) {
            str = "Sim";
            podeVotar = false;
        }
        if(!podeVotar) {
            findViewById(R.id.layoutVotar).setEnabled(false);
            findViewById(R.id.layoutVotar).setVisibility(View.INVISIBLE);
        }
        txtConfirmado.setText(txtConfirmado.getText()+ " " + str);
        txtLatitude.setText(txtLatitude.getText() + " " + evt.getLoc().getLatitude());
        txtLongitude.setText(txtLongitude.getText() + " " + evt.getLoc().getLongitude());
        txtHora.setText(txtHora.getText() + " " + new Date(evt.getLoc().getTime()));
        if(loc != null) {
            txtDist.setText("Distancia aproximada: " + Math.round(distanciaEntre(evt.getLoc().getLatitude(), evt.getLoc().getLongitude(), loc.getLatitude(), loc.getLongitude())) + " metros");
        } else {
            txtDist.setText("");
        }
        txtInfluenciaNec.setText(txtInfluenciaNec.getText() + " " + evt.getInfluenciaNecessaria());
        txtInfluenciaTotal.setText(txtInfluenciaTotal.getText() + " " + evt.getInfluenciaTotal());
        str = "";
        for(String s: evt.getSuporte()) {
            str = str.concat(s + System.lineSeparator());
        }
        txtSuporte.setText(txtSuporte.getText() + System.lineSeparator() + str);

        str = "";
        for(String s: evt.getUsersName()) {
            str = str.concat(s + System.lineSeparator());
        }
        txtVotos.setText(txtVotos.getText() + System.lineSeparator() + str);

        btnSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(podeVotar) {
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference ref = db.getReference();
                    evt.setInfluenciaTotal(evt.getInfluenciaTotal()+user.getInfluencia()*user.getCodClasse());
                    evt.addUserId(user.getUid());
                    evt.addUserName(user.getName());
                    ref.child("events").child(evt.getId()).setValue(evt);
                    Toast.makeText(getApplicationContext(), "Voto computado!", Toast.LENGTH_LONG).show();
                    Intent it = new Intent(DetalhesEvtActivity.this, MainActivity.class);
                    startActivity(it);
                }
            }
        });

        btnNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(podeVotar) {
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference ref = db.getReference();
                    evt.setInfluenciaTotal(evt.getInfluenciaTotal()-(user.getInfluencia()*user.getCodClasse())/2);
                    evt.addUserId(user.getUid());
                    evt.addUserName(user.getName());
                    ref.child("events").child(evt.getId()).setValue(evt);
                    Toast.makeText(getApplicationContext(), "Voto computado!", Toast.LENGTH_LONG).show();
                    Intent it = new Intent(DetalhesEvtActivity.this, MainActivity.class);
                    startActivity(it);
                }
            }
        });
    }


    public void verificarUID() {
        for(String s: evt.getUsersId()) {
            if(s.equals(user.getUid())) {
                podeVotar = false;
                break;
            }
        }
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
}
