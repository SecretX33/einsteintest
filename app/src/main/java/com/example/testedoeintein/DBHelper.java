package com.example.testedoeintein;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
    private static final String TAG = "DBHelper";
    public static final int VERSAO_BANCO = 1;
    public static final String NOME_BANCO = "banco.db";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + DBContract.DB.NOME_TABELA +
            " (" + DBContract.DB._ID + " INT PRIMARY KEY," +
            DBContract.DB.POS + " INTEGER NOT NULL," +
            DBContract.DB.COR + " INTEGER NOT NULL," +
            DBContract.DB.NACIO + " INTEGER NOT NULL," +
            DBContract.DB.BEBIDA + " INTEGER NOT NULL," +
            DBContract.DB.CIGARRO + " INTEGER NOT NULL," +
            DBContract.DB.ANIMAL + " INTEGER NOT NULL" + ");";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.DB.NOME_TABELA;

    public DBHelper (Context context){
        super(context, DBContract.DB.NOME_TABELA, null, VERSAO_BANCO);
    }

    public void onCreate(SQLiteDatabase db){
        Log.d(TAG, "onCreate: creating database.");
        db.execSQL(SQL_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void clearDataFromDB(SQLiteDatabase db){
        Log.d(TAG, "clearDataFromDB: clearing DB entries.");
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}