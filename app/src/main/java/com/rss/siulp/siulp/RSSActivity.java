package com.rss.siulp.siulp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class RSSActivity extends AppCompatActivity {
    boolean ancona,italia;
    RecyclerView recyclerView;
    ReadRSS readRSS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.tamagno.it/"));
                startActivity(browserIntent);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        loadPreferences();
        readRSS = new ReadRSS(this,recyclerView,ancona,italia);
        readRSS.execute();
    }

    @Override
    public void onRestart()
    {  // After a pause OR at startup
        super.onRestart();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
       // readRSS.cancel(true);
       // readRSS.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AppPreferences.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_info){
            Intent intent = new Intent(this, AboutListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ancona = sharedPreferences.getBoolean("ancona",false);
        italia = sharedPreferences.getBoolean("italia",false);
    }
}
