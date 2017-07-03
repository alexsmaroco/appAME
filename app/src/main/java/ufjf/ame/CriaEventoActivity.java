package ufjf.ame;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CriaEventoActivity extends AppCompatActivity {
    private Button btnTirarFoto;
    private FloatingActionButton btnCriarEvento;
    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cria_evento);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        //Toast.makeText(this, "ID: " + radioGroup.getCheckedRadioButtonId(), Toast.LENGTH_SHORT).show();;


        btnTirarFoto = (Button) findViewById(R.id.btnTirarFoto);
        btnTirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // chamar camera
            }
        });

        btnCriarEvento = (FloatingActionButton) findViewById(R.id.btnCriarEvento);
        btnCriarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // coletar dados e criar evento
                Snackbar.make(v, "ID: " + radioGroup.getCheckedRadioButtonId(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}
