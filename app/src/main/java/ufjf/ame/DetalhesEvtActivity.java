package ufjf.ame;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetalhesEvtActivity extends AppCompatActivity {

    private TextView txtTipoEvt;
    private TextView txtConfirmado;
    private TextView txtLatitude;
    private TextView txtLongitude;
    private TextView txtDist;
    private TextView txtInfluenciaNec;
    private TextView txtInfluenciaTotal;
    private TextView txtSuporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_evt);

        txtTipoEvt = (TextView) findViewById(R.id.txtTipoEmerg);
        txtConfirmado = (TextView) findViewById(R.id.txtConfirmado);
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtDist = (TextView) findViewById(R.id.txtDistancia);
        txtInfluenciaNec = (TextView) findViewById(R.id.txtInfluenciaNec);
        txtInfluenciaTotal = (TextView) findViewById(R.id.txtInfluenciaTotal);
        txtSuporte = (TextView) findViewById(R.id.txtSuporte);

        Location loc = getIntent().getParcelableExtra("location");
        Evento evt = (Evento) getIntent().getSerializableExtra("evt");

        txtTipoEvt.setText(txtTipoEvt.getText() + " " + evt.getTipoEvt());
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
