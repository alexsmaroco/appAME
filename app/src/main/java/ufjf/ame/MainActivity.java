package ufjf.ame;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser mAuthUser;
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
        userAtual = new Usuario();
        userAtual.setUid(mAuthUser.getUid());
        userAtual.setName(mAuthUser.getDisplayName());

        // pegar dados restantes do bd

        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        txtWelcome.setText("Bem Vindo ao AME, " + userAtual.getName() + "!");

        btnCriarEvento = (Button) findViewById(R.id.btnCriarEvento);
        btnCriarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, CriaEventoActivity.class);
                startActivity(it);
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

        listaEventos = (ListView) findViewById(R.id.listaEventos);
        /*
        EventAdapter adapter = new EventAdapter(this, arrayEventos);
        listaEvento.setAdapter(adapter);

         */
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
