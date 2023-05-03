package com.example.projetocar.adapter.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projetocar.R;
import com.example.projetocar.activitys.EnderecoFormActivity;
import com.example.projetocar.activitys.PerfilActivity;
import com.example.projetocar.auth.LoginActivity;
import com.example.projetocar.helper.FirebaseHelper;
import com.example.projetocar.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MinhaContaFragment extends Fragment {

    private TextView text_sairConta;
    private TextView text_usuario;
    private ImageView img_perfil;

    private Usuario usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_minha_conta, container, false);

        iniciarComponente(view);

        configCliques(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperaUsuario();
    }

    private void recuperaUsuario(){
        if(FirebaseHelper.getAutenticado()){
            DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                    .child("usuarios")
                    .child(FirebaseHelper.getIdFirebase());
            usuarioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    usuario = snapshot.getValue(Usuario.class);
                    configConta();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void configConta(){
        text_usuario.setText(usuario.getNome());

        if(usuario.getImagemPerfil() != null){
            img_perfil.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.get().load(usuario.getImagemPerfil()).into(img_perfil);
        }

    }

    private void configCliques(View view){
        view.findViewById(R.id.menu_perfil).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), PerfilActivity.class)));

        view.findViewById(R.id.menu_endereco).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), EnderecoFormActivity.class)));

        text_sairConta.setOnClickListener(v ->{
            FirebaseHelper.getAuth().signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

    }

    private void iniciarComponente(View view){
        text_sairConta = view.findViewById(R.id.text_sairConta);
        text_usuario = view.findViewById(R.id.text_usuario);
        img_perfil = view.findViewById(R.id.img_perfil);
    }
}