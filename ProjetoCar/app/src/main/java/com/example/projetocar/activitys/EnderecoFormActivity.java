package com.example.projetocar.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.projetocar.R;
import com.example.projetocar.adapter.AdapterEndereco;
import com.example.projetocar.helper.FirebaseHelper;
import com.example.projetocar.model.Endereco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnderecoFormActivity extends AppCompatActivity implements AdapterEndereco.OnClickListener {

    private AdapterEndereco adapterEndereco;
    private List<Endereco> enderecoList = new ArrayList<>();
    private SwipeableRecyclerView rv_enderecos;
    private  Endereco endereco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco_form);

        iniciarComponentes();
        configCliques();
        configRv();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaEndereco();
    }

    private void configRv(){
        rv_enderecos.setLayoutManager(new LinearLayoutManager(this));
        rv_enderecos.setHasFixedSize(true);
        adapterEndereco = new AdapterEndereco(enderecoList, this);
        rv_enderecos.setAdapter(adapterEndereco);

        rv_enderecos.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
            }

            @Override
            public void onSwipedRight(int position) {
                dialogRemoverEndereco(enderecoList.get(position));
            }
        });
    }

    private void recuperaEndereco(){
        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(FirebaseHelper.getIdFirebase());

        enderecoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    enderecoList.clear();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        endereco = ds.getValue(Endereco.class);
                        enderecoList.add(endereco);
                    }
                    //Verificar Erro do text_info
                    //text_info.setText("");
                }else{
                    //text_info.setText("Nenhum Endereço Cadastrado");
                }
                Collections.reverse(enderecoList);
                adapterEndereco.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dialogRemoverEndereco(Endereco endereco){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remover endereço");
        builder.setMessage("Deseja remover o endereço selecionado?");
        builder.setNegativeButton("Não",((dialog, which) ->{
            dialog.dismiss();
            adapterEndereco.notifyDataSetChanged();
        }));
        builder.setPositiveButton("Sim",((dialog, which)->{
            endereco.remover();
            enderecoList.remove(endereco);

            if(enderecoList.isEmpty()){
                //text_info.setText("Nenhum endereço cadastrado");
            }
            adapterEndereco.notifyDataSetChanged();
            dialog.dismiss();
        }));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.ib_adicionar).setOnClickListener(v ->startActivity(new Intent(this, EnderecoAddActivity.class)));

    }

    private void iniciarComponentes(){
        rv_enderecos = findViewById(R.id.rv_enderecos);
    }

    @Override
   public void OnClick(Endereco endereco) {
        //Intent intent = new Intent(this, EnderecoAddActivity.class);
        //intent.putExtra("enderecoSelecionado", endereco);
        //startActivity(intent);
    }
}