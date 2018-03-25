package com.danielakinola.loljournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.danielakinola.loljournal.champpool.ChampPoolActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonJungle = findViewById(R.id.btn_jungle);
        Button buttonTop = findViewById(R.id.btn_top);
        Button buttonMid = findViewById(R.id.btn_mid);
        Button buttonBot = findViewById(R.id.btn_bottom);
        Button buttonSupport = findViewById(R.id.btn_support);
        Button buttonChampList = findViewById(R.id.btn_champ_list);


        buttonTop.setOnClickListener(v -> navigateToChampPool(0));
        buttonJungle.setOnClickListener(v -> navigateToChampPool(1));
        buttonMid.setOnClickListener(v -> navigateToChampPool(2));
        buttonBot.setOnClickListener(v -> navigateToChampPool(3));
        buttonSupport.setOnClickListener(v -> navigateToChampPool(4));
        buttonChampList.setOnClickListener(v -> navigateToChampPool(0));

    }

    void navigateToChampPool(int lane) {
        Intent i = new Intent(MainActivity.this, ChampPoolActivity.class);
        i.putExtra("LANE", lane);
        Log.d("lane", "" + lane);
        startActivity(i);
    }



}
