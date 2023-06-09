package com.example.projetocar.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetocar.activitys.MainActivity;
import com.example.projetocar.R;
import com.example.projetocar.helper.FirebaseHelper;
import com.example.projetocar.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    //Declarando os componentes que tem na tela ActivityCadastro
    private EditText edt_nome;
    private EditText edt_email;
    private EditText edt_telefone;
    private EditText edt_senha;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        iniciarComponetes();

        configCliques();
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    public void validarDados(View view){
        String nome = edt_nome.getText().toString();
        String email = edt_email.getText().toString();
        String telefone = edt_telefone.getText().toString();
        String senha = edt_senha.getText().toString();

        if(!nome.isEmpty()){
            if(!email.isEmpty()){
                if(!telefone.isEmpty()){
                    if(!senha.isEmpty()){

                        progressBar.setVisibility(View.VISIBLE);

                        Usuario usuario = new Usuario();
                        usuario.setNome(nome);
                        usuario.setEmail(email);
                        usuario.setTelefone(telefone);
                        usuario.setSenha(senha);

                        cadastrarUsuario(usuario);

                    }else{
                        edt_senha.requestFocus();
                        edt_senha.setError("Preencha sua senha");
                    }

                }else{
                    edt_telefone.requestFocus();
                    edt_telefone.setError("Preencha seu telefone");
                }

            }else{
                edt_email.requestFocus();
                edt_email.setError("Preencha seu Email");
            }

        }else{
            edt_nome.requestFocus();
            edt_nome.setError("Preencha seu nome");
        }

    }

    private void cadastrarUsuario(Usuario usuario){
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(task -> {
           if(task.isSuccessful()){

                String id = task.getResult().getUser().getUid();

                usuario.setId(id);
                usuario.Salvar();

                //Levar para Tela Home
               startActivity(new Intent(this, MainActivity.class));
               finish();

           }else{
                //Validação de erro
               String erro = FirebaseHelper.validarErros(task.getException().getMessage());
               Toast.makeText(this, erro, Toast.LENGTH_SHORT).show();
           }
        });
    }

    private void iniciarComponetes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Criar Conta");

        edt_nome = findViewById(R.id.edt_nome);
        edt_email = findViewById(R.id.edt_email);
        edt_telefone = findViewById(R.id.edt_telefone);
        edt_senha = findViewById(R.id.edt_senha);
        progressBar = findViewById(R.id.progressBar);
    }
}