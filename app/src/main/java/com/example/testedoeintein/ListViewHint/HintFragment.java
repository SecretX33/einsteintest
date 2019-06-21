package com.example.testedoeintein.ListViewHint;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.testedoeintein.Game;
import com.example.testedoeintein.R;

public class HintFragment extends Fragment {
    private static final String TAG = "HintFragment";
    private Context context;
    private ListView listView;
    private HintAdapter adapter;

    public HintFragment() { }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hint, container, false);
        if(Game.arrayBool.isEmpty()) for(int i = 0; i<15; i++) Game.arrayBool.add(false);
        initListView(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void initListView(View v) {
        listView = v.findViewById(R.id.listview_hint);
        adapter = new HintAdapter(context,R.layout.layout_hintlist_item);
        listView.setAdapter(adapter);
    }
}
