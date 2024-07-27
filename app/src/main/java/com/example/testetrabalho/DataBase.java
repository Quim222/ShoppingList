package com.example.testetrabalho;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

class DataBase extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ListaCompras.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "lista";
    private static final String COLLUMN_ID = "_id";
    private static final String COLLUMN_NOME = "nome_produto";
    private static final String COLLUMN_QUANTIDADE = "quantidade";

    private static final String COLLUMN_PRECO = "preco";



    DataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLLUMN_NOME + " TEXT, " +
                COLLUMN_QUANTIDADE + " INTEGER, " +
                COLLUMN_PRECO + " FLOAT); ";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    void addProduto(String nome, int quantidade, float preco) {
        SQLiteDatabase db = this.getWritableDatabase();
        String toast_feito = context.getString(R.string.toast_feito);
        String toast_erro = context.getString(R.string.toast_erro);

        // Obtenha o próximo ID disponível
        int nextID = getNextID();

        ContentValues cv = new ContentValues();
        cv.put(COLLUMN_ID, nextID);
        cv.put(COLLUMN_NOME, nome);
        cv.put(COLLUMN_QUANTIDADE, quantidade);
        cv.put(COLLUMN_PRECO, preco);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, toast_erro, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, toast_feito, Toast.LENGTH_SHORT).show();
        }

        db.close();
    }


    private int getNextID() {
        String query = "SELECT MAX(" + COLLUMN_ID + ") FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int nextID = 1;

        if (cursor.moveToFirst()) {
            nextID = cursor.getInt(0) + 1;
        }

        cursor.close();
        return nextID;
    }


    void updateData(String row_id, String nome, String quantidade, String preco){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String toast_erro = context.getString(R.string.toast_erro);
        String toast_update = context.getString(R.string.toast_update);

        cv.put(COLLUMN_NOME,nome);
        cv.put(COLLUMN_QUANTIDADE,quantidade);
        cv.put(COLLUMN_PRECO,preco);

        long result = db.update(TABLE_NAME,cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context,toast_erro,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,toast_update,Toast.LENGTH_SHORT).show();
        }
    }

    float getValor(String id){
        String query = "SELECT preco FROM " + TABLE_NAME + "where _id = "+id +";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        float valor = cursor.getFloat(0);

        cursor.close();
        return valor;
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db!= null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String toast_delete = context.getString(R.string.toast_delete);
        String toast_delete_erro = context.getString(R.string.toast_delete_erro);
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, toast_delete_erro, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, toast_delete, Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }



    public void decrementProductIDs(int deletedProductId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " +
                COLLUMN_ID + " = " + COLLUMN_ID + " - 1 " +
                "WHERE " + COLLUMN_ID + " > " + deletedProductId;
        db.execSQL(query);
        db.close();
    }

}
