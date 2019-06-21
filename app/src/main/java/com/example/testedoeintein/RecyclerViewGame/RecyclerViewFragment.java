package com.example.testedoeintein.RecyclerViewGame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testedoeintein.Casa;
import com.example.testedoeintein.DBContract;
import com.example.testedoeintein.DBHelper;
import com.example.testedoeintein.Events.EventGameRestart;
import com.example.testedoeintein.Events.EventNotifyAdapter;
import com.example.testedoeintein.Events.EventSaveDataToDB;
import com.example.testedoeintein.Events.EventUpdateMenuIcon;
import com.example.testedoeintein.Game;
import com.example.testedoeintein.R;
import com.example.testedoeintein.Utilities;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class RecyclerViewFragment extends Fragment{
    private static final String TAG = "RecyclerViewFragment";
    private Context context;
    private View v;
    private CasaAdapter adapter;
    private RecyclerView recyclerView;
    private ItemTouchHelper touchHelper;
    private ItemTouchHelper.Callback callback;
    private LinearLayoutManager layoutManager;

    public RecyclerViewFragment() { }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        initRecyclerView();
        if(Game.casaArray.isEmpty()) getArrayFromDB();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveDataToDB();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }

    private void getArrayFromDB(){
        Log.d(TAG, "getArrayFromDB: populating array from DB.");
        DBHelper helper = new DBHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBContract.DB.NOME_TABELA,null);
        cursor.moveToFirst();

        if(Game.casaArray==null){
            Log.w(TAG, "getArrayFromDB: Game.casaArray is null, returning.");
            return;
        }
        Game.casaArray.clear();

        while(!cursor.isAfterLast()){
            Casa casa = new Casa(cursor.getInt(cursor.getColumnIndex(DBContract.DB.POS)),
                    cursor.getInt(cursor.getColumnIndex(DBContract.DB.COR)),
                    cursor.getInt(cursor.getColumnIndex(DBContract.DB.NACIO)),
                    cursor.getInt(cursor.getColumnIndex(DBContract.DB.BEBIDA)),
                    cursor.getInt(cursor.getColumnIndex(DBContract.DB.CIGARRO)),
                    cursor.getInt(cursor.getColumnIndex(DBContract.DB.ANIMAL)));

            Game.casaArray.add(casa);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        if(Game.casaArray.isEmpty()){
            Log.d(TAG, "getArrayFromDB: DB is empty!");
            db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            for(int i=0; i<5; i++){
                values.put(DBContract.DB.POS,i);
                values.put(DBContract.DB.COR,0);
                values.put(DBContract.DB.NACIO,0);
                values.put(DBContract.DB.BEBIDA,0);
                values.put(DBContract.DB.CIGARRO,0);
                values.put(DBContract.DB.ANIMAL,0);
                db.insert(DBContract.DB.NOME_TABELA,null,values);
                Game.casaArray.add(new Casa(i));
            }
        }
        helper.close();
        Utilities.sort(Game.casaArray);
        adapter.notifyDataSetChanged();
        EventBus.getDefault().post(new EventUpdateMenuIcon());
        Log.d(TAG, "getArrayFromDB: finished populating array.");
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: preparing recyclerview.");
        recyclerView = v.findViewById(R.id.recyclerView);
        adapter = new CasaAdapter(context);
        callback = new ItemMoveCallback(adapter);
        touchHelper = new ItemTouchHelper(callback);
        layoutManager = new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() { return false; }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        touchHelper.attachToRecyclerView(recyclerView);
        adapter.setHasStableIds(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void saveDataToDB(){
        Log.d(TAG, "saveDataToDB: called.");
        DBHelper helper = new DBHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = "pos = ?";

        for(int i=0; i<5; i++){
            ContentValues insert = new ContentValues();
            String[] whereArgs = new String[] {String.valueOf(Game.casaArray.get(i).pos)};
            insert.put(DBContract.DB.COR, Game.casaArray.get(i).cor);
            insert.put(DBContract.DB.NACIO, Game.casaArray.get(i).nacionalidade);
            insert.put(DBContract.DB.BEBIDA, Game.casaArray.get(i).bebida);
            insert.put(DBContract.DB.CIGARRO, Game.casaArray.get(i).cigarro);
            insert.put(DBContract.DB.ANIMAL, Game.casaArray.get(i).animal);

            db.update(DBContract.DB.NOME_TABELA, insert, where, whereArgs);
        }
        db.close();
        helper.close();
    }

    @Subscribe
    public void onEvent(EventSaveDataToDB event){ saveDataToDB(); }

    @Subscribe
    public void onEvent(EventNotifyAdapter event){ adapter.notifyDataSetChanged(); }

    @Subscribe
    public void onEvent(EventGameRestart event){
        Log.d(TAG, "onEvent (EventNotifyRecyclerView): called.");
        getArrayFromDB();
    }

}
