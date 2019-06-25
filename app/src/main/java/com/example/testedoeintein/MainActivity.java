package com.example.testedoeintein;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testedoeintein.Config.ConfigFragment;
import com.example.testedoeintein.Events.EventGameRestart;
import com.example.testedoeintein.Events.EventSaveDataToDB;
import com.example.testedoeintein.Events.EventUpdateMenuIcon;
import com.example.testedoeintein.ListViewHint.HintFragment;
import com.example.testedoeintein.Locale.LocaleManager;
import com.example.testedoeintein.RecyclerViewGame.RecyclerViewFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private androidx.appcompat.widget.Toolbar toolbar;
    private RecyclerViewFragment frag;
    private HintFragment frag2;
    private String currentFrag;
    private boolean doubleClickBack;

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
        if(Game.venceu>0){
            Log.w(TAG, "onStop: clearing DB because player choose to review his choices and closed the app without restarting it using the button.");
            DBHelper helper = new DBHelper(getApplicationContext());
            SQLiteDatabase db = helper.getWritableDatabase();
            helper.clearDataFromDB(db);
            db.close();
            helper.close();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManager.setLocale(newBase,null));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");
        doubleClickBack=false;

        initLandscapeLayout();
        initToolbar();
        initFragments();
        firstRun();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        Game.itemDica = menu.findItem(R.id.itemDica);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.itemDica:
                abrirDica();
                return true;
            case R.id.itemReiniciar:
                reiniciarJogo(true);
                return true;
            case R.id.itemConf:
                abrirConfig();
                return true;
            case R.id.itemAjuda:
                AlertDialog.Builder msg_ajuda = new AlertDialog.Builder(this);
                msg_ajuda.setTitle(R.string.ajuda);
                msg_ajuda.setMessage(R.string.mensagem_ajuda);
                msg_ajuda.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
                msg_ajuda.show();
                return true;
            case R.id.itemSair:
                AlertDialog.Builder msg_sair = new AlertDialog.Builder(this);
                msg_sair.setTitle(R.string.sair);
                msg_sair.setMessage(R.string.mensagem_sair);
                msg_sair.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().post(new EventSaveDataToDB());
                        finish();
                        System.exit(0);
                    }
                });
                msg_sair.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
                msg_sair.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        int index = manager.getBackStackEntryCount();

        if(index>0){
            FragmentManager.BackStackEntry ultimo_frag = manager.getBackStackEntryAt(index-1);
            String tag = ultimo_frag.getName();

            if(ultimo_frag==null) {
                Log.wtf(TAG, "onBackPressed: fragment could not be found.");
                return;
            } else if(tag==null){
                Log.wtf(TAG, "onBackPressed: last fragment doesn't have a tag.");
                return;
            } else {
                currentFrag = tag;
                updateToolbar();
                Log.d(TAG, "onBackPressed: currentFragment is now \"" + currentFrag + "\".");
            }
            super.onBackPressed();
        } else{
            if(doubleClickBack) {
                super.onBackPressed();
                return;
            }
            this.doubleClickBack = true;
            Toast.makeText(this, getResources().getString(R.string.doubleClickBack), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleClickBack=false;
                }
            }, 2000);
            Log.d(TAG, "onBackPressed: there are no frag to back, currentFragment is \"" + currentFrag + "\".");
        }
    }

    private void initLandscapeLayout(){
        Log.d(TAG, "initLandscapeLayout: preparing layout.");
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    private void initToolbar(){
        Log.d(TAG, "initToolbar: initiating.");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initFragments(){
        Log.d(TAG, "initFragments: preparing fragment.");
        frag = new RecyclerViewFragment();
        frag2 = new HintFragment();
        currentFrag = "game";
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, frag,"game").commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void firstRun(){
        SharedPreferences pref = getSharedPreferences("prefs",Context.MODE_PRIVATE);
        boolean firstRun = pref.getBoolean("firstRun",true);

        if(firstRun){
            int delay=2200;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), R.string.mensagem_first_run_1, Toast.LENGTH_SHORT).show();
                }
            }, delay);

            delay+=2000+500;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), R.string.mensagem_first_run_2, Toast.LENGTH_LONG).show();
                }
            }, delay);

            delay+=3500+500;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), R.string.mensagem_first_run_3, Toast.LENGTH_LONG).show();
                }
            }, delay);

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("firstRun", false).apply();
        }
    }

    private void abrirDica(){
        Log.d(TAG, "abrirDica: called.");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if(currentFrag.equals("hint")){
            currentFrag = "game";
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            updateToolbar();
            transaction.replace(R.id.frameLayout, frag,"game").commit();
        }
        else if(currentFrag.equals("game") || currentFrag.equals("config")){
            if(currentFrag.equals("game")){
                if(!isFragmentOnTop("game")) transaction.addToBackStack(currentFrag);
                else Log.d(TAG, "abrirDica: tried to add fragment to top but it's already there.");
            }
            currentFrag = "hint";
            updateToolbar();
            transaction.replace(R.id.frameLayout, frag2,"hint").commit();
        }
        else{
            Log.wtf(TAG, "abrirDica: that should never happen, what could cause that?! currentFrag = " + currentFrag);
            return;
        }
        manager.executePendingTransactions();
    }

    private void abrirConfig(){
        Log.d(TAG, "abrirConfig: called.");

        if (!currentFrag.equals("config")) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            Log.d(TAG, "abrirConfig: opening settings screen.");
            if(!isFragmentOnTop(currentFrag)) {
                transaction.addToBackStack(currentFrag);
            } else{
                Log.d(TAG, "abrirConfig: tried to add fragment to top but it's already there.");
            }
            currentFrag = "config";
            updateToolbar();
            transaction.replace(R.id.frameLayout, new ConfigFragment()).commit();
            manager.executePendingTransactions();
        } else{
            Log.d(TAG, "abrirConfig: called but user is already on config.");
        }
    }

    private boolean isFragmentOnTop(@NonNull String tag){
        FragmentManager manager = getSupportFragmentManager();
        int index = manager.getBackStackEntryCount();

        if(index>0) {
            FragmentManager.BackStackEntry ultimo_frag = manager.getBackStackEntryAt(index - 1);

            if (ultimo_frag == null) {
                Log.wtf(TAG, "isFragmentOnTop: fragment could not be found.");
            } else{
                String ultimo_frag_nome = ultimo_frag.getName();
                if (ultimo_frag_nome == null) Log.wtf(TAG, "isFragmentOnTop: last fragment doesn't have a tag.");
                else return ultimo_frag_nome.equals(tag);
            }
        }
        return false;
    }

    private void updateToolbar(){
        if(currentFrag==null) Log.w(TAG, "updateToolbar: called but currentFrag is NULL!");
        else if(getSupportActionBar()!=null){
            switch (currentFrag) {
                case "game":
                    getSupportActionBar().setTitle(R.string.app_name);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setDisplayShowHomeEnabled(false);
                    break;
                case "hint":
                    getSupportActionBar().setTitle(R.string.dicas);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    break;
                case "config":
                    getSupportActionBar().setTitle(R.string.settings);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    break;
                default:
                    Log.wtf(TAG, "updateToolbar: tried to update toolbar title but currentFrag value is unexpected, currentFrag = " + currentFrag);
                    break;
            }
        }
        else Log.wtf(TAG, "updateToolbar: called but there is no Action Bar set.");
    }

    public void updateMenuIcon(){
        switch(Game.getHits()){
            case 0:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_0));
                break;
            case 1:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_1));
                break;
            case 2:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_2));
                break;
            case 3:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_3));
                break;
            case 4:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_4));
                break;
            case 5:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_5));
                break;
            case 6:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_6));
                break;
            case 7:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_7));
                break;
            case 8:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_8));
                break;
            case 9:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_9));
                break;
            case 10:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_10));
                break;
            case 11:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_11));
                break;
            case 12:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_12));
                break;
            case 13:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_13));
                break;
            case 14:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_14));
                break;
            case 15:
                Game.itemDica.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_regras_15));
                break;
        }

        if(Game.venceu==1){
            Game.venceu=2;
            reiniciarJogo(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.parabens);
            builder.setMessage(R.string.mensagem_parabens);
            builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    reiniciarJogo(true);
                }
            });
            builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { }
            });
            builder.show();
        }
    }

    private void reiniciarJogo(boolean notifyAdapter) {
        Log.d(TAG, "reiniciarJogo: restart option selected.");
        DBHelper helper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        helper.clearDataFromDB(db);
        db.close();
        helper.close();
        if(notifyAdapter){
            Game.venceu=0;
            EventBus.getDefault().post(new EventGameRestart());
        }
    }

    @Subscribe
    public void onEvent(EventUpdateMenuIcon event){
        updateMenuIcon();
    }
}