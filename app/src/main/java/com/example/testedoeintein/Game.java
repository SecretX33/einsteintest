package com.example.testedoeintein;

import android.util.Log;
import android.view.MenuItem;

import com.example.testedoeintein.Events.EventNotifyAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class Game {
    private static final String TAG = "Game";

    public static int venceu=0;
    public static ArrayList<Casa> casaArray = new ArrayList<Casa>();
    public static ArrayList<Boolean> arrayBool = new ArrayList<Boolean>();
    public static MenuItem itemDica;

    private static final int HITS_TO_WIN=15;
    private static final int HINTS=15;
    private static final int AMARELA=1, ALEMAO=1, AGUA=1, BLENDS=1, CACHORROS=1;
    private static final int AZUL=2, DINAMARQUES=2, CAFE=2, BLUEMASTER=2, CAVALOS=2;
    private static final int BRANCA=3, INGLES=3, CERVEJA=3, DUNHILL=3, GATOS=3;
    private static final int VERDE=4, NORUEGUES=4, CHA=4, PALLMALL=4, PASSAROS=4;
    private static final int VERMELHA=5, SUECO=5, LEITE=5, PRINCE=5;

    public static int getHits(){
        if(arrayBool.isEmpty()) for(int i=0; i<HINTS; i++) arrayBool.add(false);

        if(Game.casaArray == null || Game.casaArray.isEmpty()) {
            Log.d(TAG, "getHits: called method but casaArray is null or empty.");
            return 0;
        } else if(Game.casaArray.size()!=5){
            Log.wtf(TAG, "getHits: called but casaArray size isn't 5, it is actually " + Game.casaArray.size());
            return 0;
        } else if(arrayBool==null){
            Log.wtf(TAG, "getHits: called but arrayBool is NULL!");
            return 0;
        } else if(arrayBool.size()!=HINTS){
            Log.wtf(TAG, "getHits: called but arrayBool size isn't 15, it is actually " + arrayBool.size());
            return 0;
        }

        int count=0;

        // ===== REGRAS =====
        //Regra 1
        if(Game.casaArray.get(0).nacionalidade==NORUEGUES){
            arrayBool.set(0,true);
            count++;
        } else arrayBool.set(0,false);
        //Regra 2
        for(int i=0; i<5; i++) {
            Casa c = Game.casaArray.get(i);
            if (c.nacionalidade == INGLES && c.cor == VERMELHA) {
                arrayBool.set(1, true);
                count++;
                break;
            } else {
                arrayBool.set(1, false);
            }
        }
        //Regra 3
        for(int i=0; i<5; i++) {
            Casa c = Game.casaArray.get(i);
            if (c.nacionalidade == SUECO && c.animal == CACHORROS) {
                arrayBool.set(2, true);
                count++;
                break;
            } else {
                arrayBool.set(2, false);
            }
        }
        // Regra 4
        for(int i=0; i<5; i++) {
            Casa c = Game.casaArray.get(i);
            if (c.nacionalidade == DINAMARQUES && c.bebida == CHA) {
                arrayBool.set(3, true);
                count++;
                break;
            } else {
                arrayBool.set(3, false);
            }
        }
        //Regra 5
        for(int i=0; i<4; i++){
            Casa c = Game.casaArray.get(i);
            Casa c_direito = Game.casaArray.get(i+1);
            if(c.cor==VERDE && c_direito.cor==BRANCA){
                arrayBool.set(4,true);
                count++;
                break;
            } else {
                arrayBool.set(4, false);
            }
        }
        // Regra 6
        for(int i=0; i<5; i++) {
            Casa c = Game.casaArray.get(i);
            if (c.cor == VERDE && c.bebida == CAFE) {
                arrayBool.set(5, true);
                count++;
                break;
            } else {
                arrayBool.set(5, false);
            }
        }
        // Regra 7
        for(int i=0; i<5; i++) {
            Casa c = Game.casaArray.get(i);
            if (c.cigarro == PALLMALL && c.animal == PASSAROS) {
                arrayBool.set(6, true);
                count++;
                break;
            } else {
                arrayBool.set(6, false);
            }
        }
        // Regra 8
        for(int i=0; i<5; i++) {
            Casa c = Game.casaArray.get(i);
            if (c.cor == AMARELA && c.cigarro == DUNHILL) {
                arrayBool.set(7, true);
                count++;
                break;
            } else {
                arrayBool.set(7, false);
            }
        }
        //Regra 9
        if(Game.casaArray.get(2).bebida==LEITE){
            arrayBool.set(8, true);
            count++;
        } else {
            arrayBool.set(8, false);
        }
        //Regra 10
        for(int i=0; i<5; i++){
            Casa c_esquerda = new Casa(0);
            Casa c_direita = new Casa(0);
            Casa c = Game.casaArray.get(i);
            if(i>0) c_esquerda = Game.casaArray.get(i-1);
            if(i<4) c_direita = Game.casaArray.get(i+1);
            if(c.cigarro==BLENDS && (c_esquerda.animal==GATOS || c_direita.animal==GATOS)){
                arrayBool.set(9, true);
                count++;
                break;
            } else {
                arrayBool.set(9, false);
            }
        }
        //Regra 11
        for(int i=0; i<5; i++){
            Casa c_esquerda = new Casa(0);
            Casa c_direita = new Casa(0);
            Casa c = Game.casaArray.get(i);
            if(i>0) c_esquerda = Game.casaArray.get(i-1);
            if(i<4) c_direita = Game.casaArray.get(i+1);
            if(c.animal==CAVALOS && (c_esquerda.cigarro==DUNHILL || c_direita.cigarro==DUNHILL)){
                arrayBool.set(10, true);
                count++;
                break;
            } else {
                arrayBool.set(10, false);
            }
        }
        // Regra 12
        for(int i=0; i<5; i++) {
            Casa c = Game.casaArray.get(i);
            if (c.cigarro == BLUEMASTER && c.bebida == CERVEJA) {
                arrayBool.set(11, true);
                count++;
                break;
            } else {
                arrayBool.set(11, false);
            }
        }
        // Regra 13
        for(int i=0; i<5; i++){
            Casa c = Game.casaArray.get(i);
            if(c.nacionalidade==ALEMAO && c.cigarro==PRINCE){
                arrayBool.set(12,true);
                count++;
                break;
            } else{
                arrayBool.set(12,false);
            }
        }
        //Regra 14
        for(int i=0; i<5; i++){
            Casa c_esquerda = new Casa(0);
            Casa c_direita = new Casa(0);
            Casa c = Game.casaArray.get(i);
            if(i>0) c_esquerda = Game.casaArray.get(i-1);
            if(i<4) c_direita = Game.casaArray.get(i+1);
            if(c.nacionalidade==NORUEGUES && (c_esquerda.cor==AZUL || c_direita.cor==AZUL)){
                arrayBool.set(13, true);
                count++;
                break;
            } else {
                arrayBool.set(13, false);
            }
        }
        //Regra 15
        for(int i=0; i<5; i++){
            Casa c_esquerda = new Casa(0);
            Casa c_direita = new Casa(0);
            Casa c = Game.casaArray.get(i);
            if(i>0) c_esquerda = Game.casaArray.get(i-1);
            if(i<4) c_direita = Game.casaArray.get(i+1);
            if(c.cigarro==BLENDS && (c_esquerda.bebida==AGUA || c_direita.bebida==AGUA)){
                arrayBool.set(14, true);
                count++;
                break;
            } else {
                arrayBool.set(14, false);
            }
        }

        if(count>=HITS_TO_WIN && venceu==0) {
            casaArray.get(3).animal=5;
            EventBus.getDefault().post(new EventNotifyAdapter());
            venceu = 1;
        }

        return count;
    }
}
