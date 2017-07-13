package ufjf.ame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnRegistrar;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(it);
                }
            }
        };

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();
                signInUser(email, senha);
            }
        });

        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = edtNome.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String senha = edtSenha.getText().toString().trim();
                registerUser(nome, email, senha);
            }
        });

    }

    private void registerUser(final String nome, String email, String senha) {
        if (!validarDados(nome,email, senha)) {
            Toast.makeText(this, "Email inválido ou nome/senha com menos de 4 caracteres", Toast.LENGTH_LONG);
        } else {
            final Context t = this;
            mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(t, "Algo errado ao registrar, tente mais tarde", Toast.LENGTH_LONG);
                    } else {
                        // adiciona o nome do usuario
                        final FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest update = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
                        user.updateProfile(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Log.d("Update profile", "Profile atualizada com sucesso");
                                    // salvando no bd
                                    Usuario usuario = new Usuario();
                                    usuario.setName(nome);
                                    usuario.setUid(user.getUid());
                                    usuario.setCodClasse(1);
                                    usuario.setInfluencia(5);
                                    DatabaseReference ref = db.getReference();
                                    ref.child("users").child(usuario.getUid()).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent it = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(it);
                                        }
                                    });
                                } else {
                                    task.getException().printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void signInUser(String email, String senha) {
        if (!validarDados("default", email, senha)) {
            Toast.makeText(this, "Email e/ou senha inválido(s)", Toast.LENGTH_LONG).show();
        } else {
            final Context t = this;
            mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(t, "Email e/ou senha incorreto(s). Tente novamente", Toast.LENGTH_LONG).show();
                    } else {
                        Intent it = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(it);
                    }
                }
            });
        }
    }

    private boolean validarDados(String nome, String email, String senha) {
        String EMAIL_REGEX = "^(.+)@(.+).com$";
        if (!email.matches(EMAIL_REGEX)) {
            return false;
        }
        if (senha.length() < 4) {
            return false;
        }
        if(nome.length() < 4) {
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

}