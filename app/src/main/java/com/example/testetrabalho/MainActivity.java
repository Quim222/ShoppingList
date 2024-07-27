package com.example.testetrabalho;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.Locale;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ImageView empty_imageview, delete_all;

    TextView no_data, totalTextView;
    private float total = 0.0f;
    DataBase dataBase;
    ArrayList<String> produto_id, nome_produto, quantidade_produto, preco_produto;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Dentro do método onCreate() da sua MainActivity

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);

        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            // É a primeira execução do aplicativo, exiba uma mensagem explicativa
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bem-vindo!");
            builder.setMessage("Bem-vindo à nossa aplicação. Aqui estão algumas instruções para começar:" +
                    "\n\n- Clique no botão '+' para introduzir um produto." +
                    "\n- Clique no botão de lixo para remover todos os produtos." +
                    "\n- Toque uma vez em um item para abrir os detalhes (para atualizar ou remover o produto)." +
                    "\n- A caixa ao lado de cada item serve para indicar se o produto já foi adquirido.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Segunda mensagem
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                    builder2.setTitle("");
                    builder2.setMessage("Quando você introduzir um produto, serão exibidos o nome, a quantidade e o valor do produto. " +
                            "O valor serve para quando você estiver verificando o produto em uma loja para inserir o respectivo valor.\n" +
                            "Dessa forma, você já saberá o valor total a pagar e, caso haja uma diferença, será informado sobre algum erro.\n" +
                            "\nEsperamos que você goste da nossa aplicação!");
                    builder2.setPositiveButton("OK", null);
                    builder2.show();
                }
            });
            builder.show();

            // Após exibir a mensagem explicativa, atualize a flag para indicar que o aplicativo já foi executado
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();
        }

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        empty_imageview = findViewById(R.id.empty_imageview);
        delete_all = findViewById(R.id.delete_all);
        no_data = findViewById(R.id.no_data);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        dataBase = new DataBase(MainActivity.this);
        produto_id = new ArrayList<>();
        nome_produto = new ArrayList<>();
        quantidade_produto = new ArrayList<>();
        preco_produto = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(MainActivity.this, this, produto_id, nome_produto, quantidade_produto, preco_produto);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            recreate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        produto_id.clear();
        nome_produto.clear();
        quantidade_produto.clear();
        preco_produto.clear();
        storeDataInArrays(); // Atualiza a lista de produtos ao retornar para a MainActivity
        customAdapter.notifyDataSetChanged(); // Notifica o adapter das mudanças na lista
        calcularTotal();
    }

    void storeDataInArrays() {
        Cursor cursor = dataBase.readAllData();
        if (cursor.getCount() == 0) {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                produto_id.add(cursor.getString(0));
                nome_produto.add(cursor.getString(1));
                quantidade_produto.add(cursor.getString(2));
                preco_produto.add(cursor.getString(3));
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
        calcularTotal();
    }

    void calcularTotal() {
        totalTextView = findViewById(R.id.total);
        total = 0.0f;

        for (String preco : preco_produto) {
            float valor = Float.parseFloat(preco);
            total += valor;
        }


        String formato = NumberFormat.getCurrencyInstance().format(total);
        totalTextView.setText(getString(R.string.total, formato));
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Apagar tudo?");
        builder.setMessage("Tem a certeza que deseja apagar os Dados todos?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataBase myDB = new DataBase(MainActivity.this);
                myDB.deleteAllData();
                // Refresh Activity
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // No action required
            }
        });
        builder.create().show();
    }
}
