package com.example.projetocar.helper;

import com.example.projetocar.R;
import com.example.projetocar.model.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaList {

    public static List<Categoria> getList(boolean todas){

        List<Categoria> categoriaList = new ArrayList<>();
        if(todas) categoriaList.add(new Categoria(R.drawable.ic_todas_categorias, "Todas as Categorias"));
        categoriaList.add(new Categoria(R.drawable.ic_moto, "A - Motos e Triciclos"));
        categoriaList.add(new Categoria(R.drawable.ic_carro, "B - Carros de Passeios"));
        categoriaList.add(new Categoria(R.drawable.ic_caminhao, "C - Veiculos de Carga + 3,5 ton."));
        categoriaList.add(new Categoria(R.drawable.ic_van, "D Veiculos com + 8 pessoas."));
        categoriaList.add(new Categoria(R.drawable.ic_caminhao_grande, "E Veiculos de Carga + 6 ton."));


        return categoriaList;

    }

}
