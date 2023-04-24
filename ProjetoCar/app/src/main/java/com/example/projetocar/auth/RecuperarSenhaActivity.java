package com.example.projetocar.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetocar.R;
import com.example.projetocar.helper.FirebaseHelper;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private EditText edt_email;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        iniciaComponentes();

        configCliques();
    }


    public void validarDados(View view){
        String email = edt_email.getText().toString();

        if(!email.isEmpty()){
            progressBar.setVisibility(View.VISIBLE);
            enviarEmail(email);

        }else{
            edt_email.requestFocus();
            edt_email.setError("Preencha seu email");
        }
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void enviarEmail(String email){
        FirebaseHelper.getAuth().sendPasswordResetEmail(email).addOnCompleteListener(task ->{
           if(task.isSuccessful()){
               Toast.makeText(this,"Verifique seu Email", Toast.LENGTH_SHORT).show();
           }else{
               String erro = FirebaseHelper.validarErros(task.getException().getMessage());
               Toast.makeText(this, erro, Toast.LENGTH_SHORT).show();


           }
           progressBar.setVisibility(View.GONE);
        });
    }

    private void iniciaComponentes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Recuperar Senha");

        edt_email = findViewById(R.id.edt_email);
        progressBar = findViewById(R.id.progressBar);
    }
}