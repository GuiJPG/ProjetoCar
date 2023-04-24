package com.example.projetocar.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projetocar.R;
import com.example.projetocar.activitys.FormCarroActivity;


public class HomeFragment extends Fragment {

    private Button btn_inserir;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        iniciarComponentes(view);

        configCliques();

        return view;
    }

    private void configCliques(){
        btn_inserir.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), FormCarroActivity.class));
        });
    }

    private void iniciarComponentes(View view){
        btn_inserir = view.findViewById(R.id.btn_inserir);
    }
}