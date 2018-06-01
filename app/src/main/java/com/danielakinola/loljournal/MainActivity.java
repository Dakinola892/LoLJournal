package com.danielakinola.loljournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.danielakinola.loljournal.championselect.ChampionSelectActivity;
import com.danielakinola.loljournal.champpool.ChampPoolActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_top).setOnClickListener(v -> navigateToChampPool(0));
        findViewById(R.id.btn_jungle).setOnClickListener(v -> navigateToChampPool(1));
        findViewById(R.id.btn_mid).setOnClickListener(v -> navigateToChampPool(2));
        findViewById(R.id.btn_bottom).setOnClickListener(v -> navigateToChampPool(3));
        findViewById(R.id.btn_support).setOnClickListener(v -> navigateToChampPool(4));
    }

    void navigateToChampPool(int lane) {
        Intent i = new Intent(MainActivity.this, ChampPoolActivity.class);
        i.putExtra(ChampionSelectActivity.LANE, lane);
        //Log.d("lane", "" + lane);
        startActivity(i);
    }
}
