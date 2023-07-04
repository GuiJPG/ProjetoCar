package com.example.projetocar.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.projetocar.R;
import com.example.projetocar.activitys.FormCarroActivity;
import com.example.projetocar.adapter.AdapterListaAutomovel;
import com.example.projetocar.helper.FirebaseHelper;
import com.example.projetocar.model.Automovel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment implements AdapterListaAutomovel.OnclickListener {

    private AdapterListaAutomovel adapterListaAutomovel;
    private List<Automovel> automovelList = new ArrayList<>();

    private SwipeableRecyclerView rv_automoveis;
    private Button btn_inserir;
    private ProgressBar progressBar;
    private TextView text_info;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        iniciarComponentes(view);

        configRv();

        configCliques();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        recuperaAutomoveis();
    }

    private void recuperaAutomoveis(){
        if(FirebaseHelper.getAutenticado()){
            DatabaseReference automoveisRef = FirebaseHelper.getDatabaseReference()
                    .child("meus_automoveis")
                    .child(FirebaseHelper.getIdFirebase());

            automoveisRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        automovelList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            Automovel automovel = ds.getValue(Automovel.class);
                            automovelList.add(automovel);

                        }
                        text_info.setText("");
                        Collections.reverse(automovelList);
                        adapterListaAutomovel.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }else{
                        text_info.setText("Nenhum Automovel Cadastrado");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            text_info.setText("");
            progressBar.setVisibility(View.GONE);
        }
    }

    private void configRv(){
        rv_automoveis.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_automoveis.setHasFixedSize(true);
        adapterListaAutomovel = new AdapterListaAutomovel(automovelList, this);
        rv_automoveis.setAdapter(adapterListaAutomovel);

        rv_automoveis.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                //Delete
                showDialogDelete(automovelList.get(position));
            }

            @Override
            public void onSwipedRight(int position) {
                //Edit
                 showDialogEdit(automovelList.get(position));
            }
        });
    }

    private void showDialogDelete(Automovel automovel){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setTitle("Automovel Vendido?");
        alertDialog.setNegativeButton("Não", ((dialog, whitch) -> {

            dialog.dismiss();
            adapterListaAutomovel.notifyDataSetChanged();
        })).setPositiveButton("Sim",((dialog, whitch) -> {
            Intent intent = new Intent(requireActivity(), FormCarroActivity.class);
            intent.putExtra("automovelSelecionado", automovel);
            startActivity(intent);
            adapterListaAutomovel.notifyDataSetChanged();
        }));

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    private void showDialogEdit(Automovel automovel){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setTitle("Deseja Editar o automovel?");
        alertDialog.setNegativeButton("Não", ((dialog, whitch) -> {

            dialog.dismiss();
            adapterListaAutomovel.notifyDataSetChanged();
        })).setPositiveButton("Sim",((dialog, whitch) -> {
            Intent intent = new Intent(requireActivity(), FormCarroActivity.class);
            intent.putExtra("automovelSelecionado", automovel);
            startActivity(intent);

            adapterListaAutomovel.notifyDataSetChanged();
        }));

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    private void configCliques(){
        btn_inserir.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), FormCarroActivity.class));
        });
    }

    private void iniciarComponentes(View view){
        rv_automoveis = view.findViewById(R.id.rv_automoveis);
        btn_inserir = view.findViewById(R.id.btn_inserir);
        progressBar = view.findViewById(R.id.progressBar);
        text_info = view.findViewById(R.id.text_info);
    }

    @Override
    public void OnClick(Automovel automovel) {

    }
}