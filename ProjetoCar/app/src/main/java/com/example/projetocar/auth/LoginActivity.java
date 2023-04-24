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

public class LoginActivity extends AppCompatActivity {

    private EditText edt_email;
    private EditText edt_senha;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciarComponentes();

        configCliques();
    }

    public void validarDados(View view){
        String email = edt_email.getText().toString();
        String senha = edt_senha.getText().toString();

        if(!email.isEmpty()){
            if(!senha.isEmpty()){
                progressBar.setVisibility(View.VISIBLE);

                logar(email, senha);

            }else{
                edt_senha.requestFocus();
                edt_senha.setError("Preencha sua senha");
            }
        }else{
            edt_email.requestFocus();
            edt_email.setError("Preencha seu email");
        }
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    public void criarConta(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void recuperarConta(View view){
        startActivity(new Intent(this, RecuperarSenhaActivity.class));
    }

    private void logar(String email, String senha){
        FirebaseHelper.getAuth().signInWithEmailAndPassword(
                email, senha).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }else{
                        String erro = FirebaseHelper.validarErros(task.getException().getMessage());
                        Toast.makeText(this, erro, Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
        });
    }

    private void iniciarComponentes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Login");

        edt_email = findViewById(R.id.edt_email);
        edt_senha = findViewById(R.id.edt_senha);
        progressBar = findViewById(R.id.progressBar);
    }
}