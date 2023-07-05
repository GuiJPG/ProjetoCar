package com.example.projetocar.model;

import android.app.Activity;
import android.content.Intent;

import com.example.projetocar.activitys.MainActivity;
import com.example.projetocar.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Automovel implements Serializable {

    private String id;
    private String idUsuario;
    private String titulo;
    private double valor;
    private double valorVendido;
    private String placa;
    private String categoria;
    private String modelo;
    private String anoModelo;
    private String quilometragem;
    private String descricao;
    private long dataPublicacao;
    private Endereco endereco;
    private List<String> urlImagens = new ArrayList<>();

    public Automovel() {
        DatabaseReference automovelRef = FirebaseHelper.getDatabaseReference();
        this.setId(automovelRef.push().getKey());
    }

    public void salvar(boolean novoAutomovel){
        DatabaseReference meusAnunciosRef = FirebaseHelper.getDatabaseReference()
                .child("meus_automoveis")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());
        meusAnunciosRef.setValue(this);

        if(novoAutomovel){
            DatabaseReference dataAnuncioPublicado = meusAnunciosRef
                    .child("dataPublicacao");
            dataAnuncioPublicado.setValue(ServerValue.TIMESTAMP).addOnCompleteListener(task -> {
                //activity.finish();
                //Intent intent = new Intent(activity, MainActivity.class);
                //activity.startActivity(intent);
            });
        }else{
            //activity.finish();

        }

    }

    public void remove(){
        DatabaseReference meusAnunciosRef = FirebaseHelper.getDatabaseReference()
                .child("meus_automoveis")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());
        meusAnunciosRef.removeValue();

        for (int i = 0; i < getUrlImagens().size(); i++) {
            StorageReference storageReference = FirebaseHelper.getStorageReference()
                    .child("imagens")
                    .child("automoveis")
                    .child(getId())
                    .child("imagem" + i + ".jpeg");
            storageReference.delete();
            
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(String anoModelo) {
        this.anoModelo = anoModelo;
    }

    public String getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(String quilometragem) {
        this.quilometragem = quilometragem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(long dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<String> getUrlImagens() {
        return urlImagens;
    }

    public void setUrlImagens(List<String> urlImagens) {
        this.urlImagens = urlImagens;
    }

    public double getValorVendido() {
        return valorVendido;
    }

    public void setValorVendido(double valorVendido) {
        this.valorVendido = valorVendido;
    }
}
