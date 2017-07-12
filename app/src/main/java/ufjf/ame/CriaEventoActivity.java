package ufjf.ame;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class CriaEventoActivity extends AppCompatActivity {
    private Button btnTirarFoto;
    private FloatingActionButton btnCriarEvento;
    private RadioGroup radioGroup;
    private ListView CBList;
    private ArrayList<String> CBGroup = new ArrayList<String>();
    private ImageView imgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cria_evento);

        CBGroup.add("Bombeiros");
        CBGroup.add("Primeiros socorros");
        CBGroup.add("Polícia");
        CBGroup.add("Ambulância");

        CBList = (ListView) findViewById(R.id.CBList);
        CBList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, CBGroup));
        CBList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        imgView = (ImageView) findViewById(R.id.imageView);

        btnTirarFoto = (Button) findViewById(R.id.btnTirarFoto);
        btnTirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // chamar camera e jogar foto no imgView
            }
        });

        btnCriarEvento = (FloatingActionButton) findViewById(R.id.btnCriarEvento);
        btnCriarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // coletar dados e criar evento

                int radioId = radioGroup.getCheckedRadioButtonId();
                if(radioId != -1) { // verificar se há imagem tambem
                    //criando evento
                    //Evento e = new Evento();

                    // Dados do radio button
                    RadioButton rSelecionado = (RadioButton) findViewById(radioId);
                    String emergencia = (String) rSelecionado.getText();

                    // pegando os checkbox marcados
                    ArrayList<String> suporteSelecionado = new ArrayList<String>();
                    SparseBooleanArray checked = CBList.getCheckedItemPositions();
                    int size = checked.size();
                    for (int i = 0; i < size; i++) {
                        int key = checked.keyAt(i);
                        boolean isChecked = checked.get(key);
                        if (isChecked) {
                            suporteSelecionado.add(CBGroup.get(key));
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Selecione uma emergência!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}
