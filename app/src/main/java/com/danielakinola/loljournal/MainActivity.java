package com.danielakinola.loljournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.danielakinola.loljournal.championselect.ChampionSelectActivity;
import com.danielakinola.loljournal.champpool.ChampPoolActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_test);

        Button buttonJungle = findViewById(R.id.btn_jungle);
        Button buttonTop = findViewById(R.id.btn_top);
        Button buttonMid = findViewById(R.id.btn_mid);
        Button buttonChampList = findViewById(R.id.btn_champ_list);

        View.OnClickListener laneSelectListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                String lane = button.getText().toString().toLowerCase();

                Intent i = new Intent(MainActivity.this, ChampPoolActivity.class);
                i.putExtra("lane", lane);

                Log.d("lane", lane);
                startActivity(i);
            }
        };

        buttonJungle.setOnClickListener(laneSelectListener);
        buttonTop.setOnClickListener(laneSelectListener);
        buttonMid.setOnClickListener(laneSelectListener);
        buttonChampList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChampionSelectActivity.class));
            }
        });

    }



}
