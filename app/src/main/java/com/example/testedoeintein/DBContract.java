package com.example.testedoeintein;

import android.provider.BaseColumns;

public class DBContract {
    private DBContract(){}

    public static class DB implements BaseColumns {
        public static final String NOME_TABELA = "Casas";
        public static final String POS = "pos";
        public static final String COR = "cor";
        public static final String NACIO = "nacio";
        public static final String BEBIDA = "bebida";
        public static final String CIGARRO = "cigarro";
        public static final String ANIMAL = "animal";
    }
}