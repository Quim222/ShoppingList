package com.example.testetrabalho;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText nome_produto, quant_produto, preco_produto;
    Button updateBotao, delete_button;
    ImageView voltarButtom;

    String id, nome, quantidade, preco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);

        boolean isFirstRunUpdatePage = sharedPreferences.getBoolean("isFirstRunUpdatePage", true);

        if (isFirstRunUpdatePage) {
            // É a primeira vez que o usuário acessa a página de atualização, exiba a mensagem explicativa
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String boasVindas = getString(R.string.boasVindas);
            String boasVindas_txt = getString(R.string.boasVindas_texto);
            builder.setTitle(boasVindas);
            builder.setMessage(boasVindas_txt);
            builder.setPositiveButton("OK", null);
            builder.show();

            // Após exibir a mensagem explicativa, atualize a flag para indicar que a mensagem já foi exibida
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstRunUpdatePage", false);
            editor.apply();
        }

        voltarButtom = findViewById(R.id.voltar2);
        preco_produto = findViewById(R.id.preco_txt);
        nome_produto = findViewById(R.id.nomeProduto2);
        quant_produto = findViewById(R.id.quantidadeProduto2);
        updateBotao = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        voltarButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(nome);
        }

        updateBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBase dataBase = new DataBase(UpdateActivity.this);
                nome = nome_produto.getText().toString().trim();
                quantidade = quant_produto.getText().toString().trim();
                preco = preco_produto.getText().toString().trim();
                dataBase.updateData(id,nome,quantidade,preco);
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });



    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("nome") && getIntent().hasExtra("quantidade") && getIntent().hasExtra("preco") ){
            //Receber dados pela Intent
            id = getIntent().getStringExtra("id");
            nome = getIntent().getStringExtra("nome");
            quantidade = getIntent().getStringExtra("quantidade");
            preco = getIntent().getStringExtra("preco");

            //Guardar os dados
            nome_produto.setText(nome);
            quant_produto.setText(quantidade);
            preco_produto.setText(preco);
        }else{
            Toast.makeText(this, "Não existe nenhum valor!",Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Apagar " + nome + " ?");
        builder.setMessage("Tem a certeza que deseja apagar o produto: " + nome + " ?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataBase myDB = new DataBase(UpdateActivity.this);

                // Obtém o ID do produto antes de excluí-lo
                int productId = Integer.parseInt(id);

                // Exclui o produto do banco de dados
                myDB.deleteOneRow(id);

                // Decrementa os IDs dos produtos restantes
                myDB.decrementProductIDs(productId);

                finish();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}