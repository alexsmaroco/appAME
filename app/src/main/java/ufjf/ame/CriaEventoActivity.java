package ufjf.ame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class CriaEventoActivity extends AppCompatActivity {
    private Button btnTirarFoto;
    private FloatingActionButton btnCriarEvento;
    private RadioGroup radioGroup;
    private ListView CBList;
    private ArrayList<String> CBGroup = new ArrayList<String>();
    private ImageView imgView;
    private Usuario user;
    private Location loc;
    private Bitmap img;
    private int lastIndex;
    private Activity act;

    private FirebaseDatabase db;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cria_evento);

        user = (Usuario) getIntent().getSerializableExtra("user");
        loc = getIntent().getParcelableExtra("location");

        act = this;
        img = null;


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
                abrirCamera();
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
                    Evento e = new Evento();
                    e.addUserId(user.getUid());
                    e.setInfluenciaNecessaria((float)Math.ceil( (6/user.getCodClasse()) * (15 / (user.getInfluencia()+1)) )); // quanto mais influente e de classe 'superior', menos influencia necessaria para confirmar, ex: admin(3) com 10 influencia: (6/3)*(15/11) < 3
                    e.setInfluenciaTotal(0); // o evento de um usuario recem registrado(cod = 1, influencia = 5) precisaria de (6/1)*(15/5) = 18 pnts

                    // Dados do radio button
                    RadioButton rSelecionado = (RadioButton) findViewById(radioId);
                    String emergencia = (String) rSelecionado.getText();
                    e.setTipoEvt(emergencia);
                    Local l = new Local();
                    l.setLatitude(loc.getLatitude());
                    l.setLongitude(loc.getLongitude());
                    l.setTime(loc.getTime());
                    e.setLoc(l);

                    if(img != null) {
                        e.setImg(img);
                    }

                    // pegando as checkboxes marcadas
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
                    e.setSuporte(suporteSelecionado);
                    UUID uid = UUID.randomUUID();
                    e.setId(uid.toString());

                    db = FirebaseDatabase.getInstance();
                    ref = db.getReference();
                    ref.child("events").child(uid.toString()).setValue(e).addOnCompleteListener(act, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful() && task.isComplete()) {
                                Log.d("Resposta bd: ", "Evento salvo no bd");
                                Intent it = new Intent(act, MainActivity.class);
                                startActivity(it);
                            }
                        }
                    });



                } else {
                    Toast.makeText(getApplicationContext(), "Selecione uma emergência!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void abrirCamera() {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it, 500);
    }

    protected void onActivityResult(int req, int res, Intent data) {
        if(req == 500) {
            if(res == Activity.RESULT_OK) {
                img = (Bitmap) data.getExtras().get("data");
                imgView.setImageBitmap(img);
            } else {
                Log.d("Erro_Foto", "Houve um erro ao tirar a foto");
            }
        }
    }

}
