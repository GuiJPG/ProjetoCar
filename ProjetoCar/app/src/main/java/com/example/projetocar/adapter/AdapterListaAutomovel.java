package com.example.projetocar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetocar.R;
import com.example.projetocar.helper.Mascara;
import com.example.projetocar.model.Automovel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class AdapterListaAutomovel extends RecyclerView.Adapter<AdapterListaAutomovel.MyViewHolder> {


    private List<Automovel> automoveisList;
    private OnclickListener onclickListener;

    public AdapterListaAutomovel(List<Automovel> automoveisList, OnclickListener onclickListener) {
        this.automoveisList = automoveisList;
        this.onclickListener = onclickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.automovel_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Automovel automovel = automoveisList.get(position);

        Picasso.get().load(automovel.getUrlImagens().get(0)).into(holder.img_automovel);
        holder.text_titulo.setText(automovel.getTitulo());
        holder.text_ano.setText(automovel.getAnoModelo());
        holder.text_valor.setText("R$ " + Mascara.getValor(automovel.getValor()));
        holder.text_placa.setText(automovel.getPlaca());

        holder.itemView.setOnClickListener(v -> onclickListener.OnClick(automovel));

    }

    @Override
    public int getItemCount() {
        return automoveisList.size();
    }

    public interface OnclickListener{
        void OnClick(Automovel automovel);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img_automovel;
        TextView text_titulo;
        TextView text_ano;
        TextView text_valor;
        TextView text_placa;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_automovel = itemView.findViewById(R.id.img_automovel);
            text_titulo = itemView.findViewById(R.id.text_titulo);
            text_ano = itemView.findViewById(R.id.text_ano);
            text_valor = itemView.findViewById(R.id.text_valor);
            text_placa = itemView.findViewById(R.id.text_placa);

        }
    }

}
