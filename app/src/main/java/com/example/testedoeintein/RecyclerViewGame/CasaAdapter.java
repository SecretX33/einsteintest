package com.example.testedoeintein.RecyclerViewGame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testedoeintein.Casa;
import com.example.testedoeintein.Events.EventUpdateMenuIcon;
import com.example.testedoeintein.Game;
import com.example.testedoeintein.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;

public class CasaAdapter extends RecyclerView.Adapter<CasaAdapter.ViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {
    private static final String TAG = "Adapter";
    private Context context;
    private ArrayList<ViewHolder> holderArray;
    private int viewWidth;


    public CasaAdapter(@NonNull Context context){
        this.context = context;
        holderArray = new ArrayList<>();

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float logicalDensity = displayMetrics.density;
        viewWidth = (int) Math.floor((displayMetrics.widthPixels-35*logicalDensity) / 5);
        //Log.d(TAG, "CasaAdapter: displayMetrics.widthPixels = " + displayMetrics.widthPixels);
        //Log.d(TAG, "CasaAdapter: viewWidth has been set to: " + viewWidth);
    }

    @Override @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_item,parent,false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = viewWidth;
        view.setLayoutParams(params);
        //Log.d(TAG, "onCreateViewHolder: Parent width is " + params.width);
        //Log.d(TAG, "onCreateViewHolder: Param width is " + view.getLayoutParams().width);

        ViewHolder holder = new ViewHolder(view);
        holderArray.add(holder);
        return holder;
    }

    @Override @SuppressLint("DefaultLocale")
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Casa c = Game.casaArray.get(position);
        holder.casa.setText(String.format("%s %d",context.getResources().getString(R.string.casa),position+1));
        holder.cor.setSelection(c.cor);
        holder.nacionalidade.setSelection(c.nacionalidade);
        holder.bebida.setSelection(c.bebida);
        holder.cigarro.setSelection(c.cigarro);
        holder.animal.setSelection(c.animal);
    }

    @Override
    public int getItemCount() {
        return Game.casaArray.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        Log.d(TAG, "onRowMoved: item moved from " + fromPosition + " to " + toPosition + ".");
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(holderArray, i, i + 1);
                Collections.swap(Game.casaArray, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(holderArray, i, i - 1);
                Collections.swap(Game.casaArray, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        updateCasaArray();
    }

    @Override
    public void onRowSelected(ViewHolder ViewHolder) { }

    @Override
    public void onRowClear(ViewHolder ViewHolder) { }

    @SuppressLint("DefaultLocale")
    private void updateCasaArray(){
        for(int i=0; i<holderArray.size() && i<5; i++){
            holderArray.get(i).casa.setText(String.format("%s %d",context.getResources().getString(R.string.casa),i+1));
            Game.casaArray.get(i).pos = i;
            Game.casaArray.get(i).cor = holderArray.get(i).cor.getSelectedItemPosition();
            Game.casaArray.get(i).nacionalidade = holderArray.get(i).nacionalidade.getSelectedItemPosition();
            Game.casaArray.get(i).bebida = holderArray.get(i).bebida.getSelectedItemPosition();
            Game.casaArray.get(i).cigarro = holderArray.get(i).cigarro.getSelectedItemPosition();
            Game.casaArray.get(i).animal = holderArray.get(i).animal.getSelectedItemPosition();
        }
        EventBus.getDefault().post(new EventUpdateMenuIcon());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layout;
        TextView casa;
        Spinner cor;
        Spinner nacionalidade;
        Spinner bebida;
        Spinner cigarro;
        Spinner animal;

        @SuppressWarnings("deprecation")
        AdapterView.OnItemSelectedListener spinnerCorClick = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN){
                    switch (position) {
                        case 0:
                            layout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.border_grey));
                            break;
                        case 1:
                            layout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.border_yellow));
                            break;
                        case 2:
                            layout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.border_blue));
                            break;
                        case 3:
                            layout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.border_white));
                            break;
                        case 4:
                            layout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.border_green));
                            break;
                        case 5:
                            layout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.border_red));
                            break;
                    }
                } else {
                    switch (position) {
                        case 0:
                            layout.setBackground(ContextCompat.getDrawable(context, R.drawable.border_grey));
                            break;
                        case 1:
                            layout.setBackground(ContextCompat.getDrawable(context, R.drawable.border_yellow));
                            break;
                        case 2:
                            layout.setBackground(ContextCompat.getDrawable(context, R.drawable.border_blue));
                            break;
                        case 3:
                            layout.setBackground(ContextCompat.getDrawable(context, R.drawable.border_white));
                            break;
                        case 4:
                            layout.setBackground(ContextCompat.getDrawable(context, R.drawable.border_green));
                            break;
                        case 5:
                            layout.setBackground(ContextCompat.getDrawable(context, R.drawable.border_red));
                            break;
                    }
                }
                updateCasaArray();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        };

        AdapterView.OnItemSelectedListener spinnerItemClick = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NonNull AdapterView<?> parent, View view, int position, long id) {
                updateCasaArray();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        };

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.parent_layout);
            casa = itemView.findViewById(R.id.tvCasa);
            cor = itemView.findViewById(R.id.spinnerCor);
            nacionalidade = itemView.findViewById(R.id.spinnerNacio);
            bebida = itemView.findViewById(R.id.spinnerBebida);
            cigarro = itemView.findViewById(R.id.spinnerCigarro);
            animal = itemView.findViewById(R.id.spinnerAnimal);

            cor.setOnItemSelectedListener(spinnerCorClick);
            nacionalidade.setOnItemSelectedListener(spinnerItemClick);
            bebida.setOnItemSelectedListener(spinnerItemClick);
            cigarro.setOnItemSelectedListener(spinnerItemClick);
            animal.setOnItemSelectedListener(spinnerItemClick);
        }
    }
}
