package com.example.testetrabalho;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList id_produto, nome_produto, quant_produto, preco;


    CustomAdapter(Activity activity, Context c, ArrayList id, ArrayList nome, ArrayList quant, ArrayList preco){
        this.activity = activity;
        this.context = c;
        this.id_produto = id;
        this.nome_produto = nome;
        this.quant_produto = quant;
        this.preco = preco;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.minhas_linhas, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String nomeProduto = (String) nome_produto.get(position);
        if (!nomeProduto.isEmpty()) {
            nomeProduto = nomeProduto.substring(0, 1).toUpperCase() + nomeProduto.substring(1);
        }

        holder.id_produto_txt.setText(String.valueOf(id_produto.get(position)));
        holder.nome_produto_txt.setText(String.valueOf(nomeProduto));
        holder.quant_produto_txt.setText(String.valueOf(quant_produto.get(position)));
        holder.preco_produto.setText(String.valueOf(preco.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(id_produto.get(position)));
                intent.putExtra("nome", String.valueOf(nome_produto.get(position)));
                intent.putExtra("quantidade", String.valueOf(quant_produto.get(position)));
                intent.putExtra("preco", String.valueOf(preco.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }


    @Override
    public int getItemCount() {
        return id_produto.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id_produto_txt, nome_produto_txt, quant_produto_txt, preco_produto;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id_produto_txt = itemView.findViewById(R.id.produto_id);
            nome_produto_txt = itemView.findViewById(R.id.nome);
            quant_produto_txt = itemView.findViewById(R.id.quantidade);
            preco_produto = itemView.findViewById(R.id.preco);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
