package com.danielakinola.loljournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class ChampionListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_list);

        //champions = initMap();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(ChampionListActivity.this, 4));
        final ChampionListAdapter championListAdapter = new ChampionListAdapter();
        recyclerView.setAdapter(championListAdapter);

        Button buttonChooseChamps = findViewById(R.id.button_choose_champs);
        buttonChooseChamps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lane = getIntent().getStringExtra("lane");
                String laneJSONString = getLaneJSONString(lane);

                try {
                    JSONObject laneJSON = new JSONObject(laneJSONString);
                    Iterator<String> champNamesIterator = laneJSON.keys();
                    ArrayList<String> champNames = new ArrayList<>();
                    while (champNamesIterator.hasNext()) {
                        champNames.add(champNamesIterator.next());
                    }

                    for (String champName : championListAdapter.getChosenChampions()) {
                        if (!champNames.contains(champName)) {
                            laneJSON.put(champName, new JSONArray());
                        }
                    }

                    FileOutputStream fileOutputStream = openFileOutput(String.format("%s.json", lane), MODE_PRIVATE);
                    fileOutputStream.write(laneJSON.toString().getBytes());
                    fileOutputStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    Intent i = new Intent(ChampionListActivity.this, ChampPoolActivity.class);
                    i.putExtra("lane", lane);
                    finish();
                }


            }
        });
    }

    @NonNull
    public String getLaneJSONString(String lane) {
        try {
            FileInputStream fileInputStream = openFileInput(String.format("%s.json", lane));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String fileLine = "";
            while ((fileLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(fileLine);
            }
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


}
