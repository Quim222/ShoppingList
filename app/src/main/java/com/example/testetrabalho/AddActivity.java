package com.example.testetrabalho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddActivity extends AppCompatActivity {

    EditText nome_input, quantidade_input;
    ImageView voltar;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);

        boolean isFirstRunAddPage = sharedPreferences.getBoolean("isFirstRunAddPage", true);

        if (isFirstRunAddPage) {
            // É a primeira vez que o usuário acessa a página de adicionar produtos, exiba a mensagem explicativa
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Página de Inserir Produto");
            builder.setMessage("- Nesta página, você pode voltar para trás, clicando na seta.\n" +
                    "- Para introduzir o produto e a quantidade desejada, basta pressionar o botão 'Adicionar'.");
            builder.setPositiveButton("OK", null);
            builder.show();

            // Após exibir a mensagem explicativa, atualize a flag para indicar que a mensagem já foi exibida
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstRunAddPage", false);
            editor.apply();
        }


        nome_input = findViewById(R.id.nomeProduto);
        quantidade_input = findViewById(R.id.quantidadeProduto);
        addButton = findViewById(R.id.addProduto);
        voltar = findViewById(R.id.voltar);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBase dataBase = new DataBase(AddActivity.this);
                dataBase.addProduto(nome_input.getText().toString().trim(),
                        Integer.valueOf(quantidade_input.getText().toString().trim()),0);
            }
        });

    }


}