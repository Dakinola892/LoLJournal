package com.danielakinola.loljournal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*final SharedPreferences laneSelector = getSharedPreferences("lane_selector", MODE_PRIVATE);
        String lane = laneSelector.getString("lane", null);
        checkFilesPresent();
        if (lane != null) {
            Intent i = new Intent(MainActivity.this, ChampPoolActivity.class);
            i.putExtra("lane", lane);
            startActivity(i);
        }*/

        setContentView(R.layout.activity_test);

        /*Button buttonJungle = findViewById(R.id.button_jungle);
        Button buttonTop = findViewById(R.id.button_top);
        Button buttonMid = findViewById(R.id.button_mid);
        Button buttonChampList = findViewById(R.id.buttonChampList);

        View.OnClickListener laneSelectListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                String lane = button.getText().toString().toLowerCase();

                Intent i = new Intent(MainActivity.this, ChampPoolActivity.class);
                i.putExtra("lane", lane);

                SharedPreferences.Editor editor = laneSelector.edit();
                editor.putString("lane", lane);
                editor.apply();

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
                startActivity(new Intent(MainActivity.this, ChampionListActivity.class));
            }
        });

    }

    public void checkFilesPresent() {
        String path = getFilesDir() + "/lane_data.json";
        try {
            boolean fileCreated = new File(path).createNewFile();
            if (fileCreated) {
                FileOutputStream fileOutputStream = openFileOutput("lane_data.json", MODE_PRIVATE);
                fileOutputStream.write(("" +
                        "{" +
                        "top:{}," +
                        "jungle:{}," +
                        "mid:{}," +
                        "bot:{}," +
                        "support:{}" +
                        "}").getBytes());
                fileOutputStream.close();
            }
            Log.d("file_created", String.valueOf(fileCreated));
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }


}
