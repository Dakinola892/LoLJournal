package com.danielakinola.loljournal.champpool;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.danielakinola.loljournal.data.models.Champion;

import java.util.ArrayList;
import java.util.List;

public class ChampPoolListBinding {
    @BindingAdapter("champs")
    public static void setChampPoolListItems(RecyclerView recyclerView, List<Champion> champions) {
        ChampionAdapter adapter = (ChampionAdapter) recyclerView.getAdapter();
        adapter.setChampions((ArrayList<Champion>) champions);
    }
}
