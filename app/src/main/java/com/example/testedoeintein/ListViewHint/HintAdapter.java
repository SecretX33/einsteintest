package com.example.testedoeintein.ListViewHint;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testedoeintein.Game;
import com.example.testedoeintein.R;


public class HintAdapter extends ArrayAdapter<Boolean>{
    private static final String TAG = "HintAdapter";
    private Context context;

    public HintAdapter(@NonNull Context context, int resource) {
        super(context, resource, Game.arrayBool);
        this.context = context;
    }

    @Override @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.layout_hintlist_item, parent, false);

            holder = new ViewHolder();
            holder.checkBox = convertView.findViewById(R.id.checkbox_hintitem);
            holder.hint = convertView.findViewById(R.id.textview_hintitem);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        boolean bool = Game.arrayBool.get(position);
        if(holder == null){
            Log.wtf(TAG, "getView: holder is null.");
            return convertView;
        }
        else if(holder.checkBox == null) Log.wtf(TAG, "getView: checkBox is null.");

        holder.checkBox.setChecked(bool);

        switch(position){
            case 0:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra1));
                break;
            case 1:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra2));
                break;
            case 2:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra3));
                break;
            case 3:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra4));
                break;
            case 4:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra5));
                break;
            case 5:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra6));
                break;
            case 6:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra7));
                break;
            case 7:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra8));
                break;
            case 8:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra9));
                break;
            case 9:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra10));
                break;
            case 10:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra11));
                break;
            case 11:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra12));
                break;
            case 12:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra13));
                break;
            case 13:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra14));
                break;
            case 14:
                holder.hint.setText(parent.getContext().getResources().getString(R.string.regra15));
                break;
        }

        return convertView;
    }

    private class ViewHolder {
        CheckBox checkBox;
        TextView hint;
    }
}
