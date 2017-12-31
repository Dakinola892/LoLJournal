package com.danielakinola.loljournal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;

public class ChampPoolActivity extends AppCompatActivity {

    private static HashSet<ChampPoolChampion> topChampPool = new HashSet<>();
    private static HashSet<ChampPoolChampion> jungleChampPool = new HashSet<>();
    private static HashSet<ChampPoolChampion> midChampPool = new HashSet<>();
    private static HashSet<ChampPoolChampion> botChampPool = new HashSet<>();
    private static HashSet<ChampPoolChampion> supportChampPool = new HashSet<>();
    private SharedPreferences sharedPreferences;
    private String currentLane;
    private String laneData;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter sectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentLane = getIntent().getStringExtra("lane").toLowerCase();
        getLaneData();
        setChampPool();
        setContentView(R.layout.activity_champ_pool);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = findViewById(R.id.lane_fragments);
        viewPager.setAdapter(sectionsPagerAdapter);

        tabLayout = findViewById(R.id.tabs_lanes);
        tabLayout.setupWithViewPager(viewPager);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChampPoolActivity.this, ChampionListActivity.class);
                i.putExtra("from", "ChampPoolActivity");
                i.putExtra("currentLane", currentLane);
                startActivity(i);
                finish();
            }
        });
    }

    private void setChampPool() {
        try {
            JSONObject laneDataJSON = new JSONObject(laneData);
            for (String lane : getResources().getStringArray(R.array.lanes_array)) {
                lane = lane.toLowerCase();
                JSONObject laneObject = laneDataJSON.getJSONObject(lane);
                Iterator<String> laneObjectIterator = laneObject.keys();

                while (laneObjectIterator.hasNext()) {
                    String champName = laneObjectIterator.next();
                    ChampPoolChampion champPoolChampion = new ChampPoolChampion(
                            champName, ChampionReference.getChampionImage(champName));

                    JSONArray matchupsJSON = laneObject.getJSONArray(champName);
                    for (int i = 0; i < matchupsJSON.length(); i++) {
                        String matchupName = matchupsJSON.getString(i);
                        champPoolChampion.addMatchup(
                                new Champion(matchupName,
                                        ChampionReference.getChampionImage(matchupsJSON.getString(i))));
                    }

                    switch (lane) {
                        case "top":
                            topChampPool.add(champPoolChampion);
                            break;
                        case "jungle":
                            jungleChampPool.add(champPoolChampion);
                            break;
                        case "mid":
                            midChampPool.add(champPoolChampion);
                            break;
                        case "bottom":
                            botChampPool.add(champPoolChampion);
                            break;
                        case "support":
                            supportChampPool.add(champPoolChampion);
                            break;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getLaneData() {
        File file = new File(getFilesDir() + "/lane_data.json");
        if (file.exists() && file.length() > 2) {
            try {
                FileInputStream fileInputStream = openFileInput("lane_data.json");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String fileLine = "";
                while ((fileLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(fileLine);
                }
                laneData = stringBuilder.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_champ_pool, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private MatchupAdapter matchupAdapter;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int position = getArguments().getInt(ARG_SECTION_NUMBER);

            final int TOP_LANE = 0;
            final int JUNGLE = 1;
            final int MID_LANE = 2;
            final int BOT_LANE = 3;
            final int SUPPORT = 4;

            View rootView = inflater.inflate(R.layout.fragment_champ_pool, container, false);
            RecyclerView champPoolContainer = (RecyclerView) rootView;
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            champPoolContainer.setLayoutManager(layoutManager);
            champPoolContainer.setHasFixedSize(true);

            //matchupAdapter = new MatchupAdapter();
            //champPoolContainer.setAdapter(matchupAdapter);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            //champPool.trimToSize();
            return 5;
        }

    }
}
