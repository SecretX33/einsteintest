package com.example.testedoeintein;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Utilities {
    private static final String TAG = "Utilities";
    
    public static void sort(@NonNull ArrayList<Casa> casaArray){
        for (int j = 1; j < casaArray.size(); j++) {
            Casa key = casaArray.get(j);
            int i = j-1;
            while ((i > -1) && (casaArray.get(i).pos > key.pos)) {
                casaArray.set(i+1,casaArray.get(i));
                i--;
            }
            casaArray.set(i+1,key);
        }
        Log.d(TAG, "sort: sorting casaArray.");
    }
}
