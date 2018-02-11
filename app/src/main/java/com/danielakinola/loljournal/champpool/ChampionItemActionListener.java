package com.danielakinola.loljournal.champpool;

import android.view.View;

import com.danielakinola.loljournal.data.models.Champion;

public interface ChampionItemActionListener {
    void onChampionClicked(Champion champion);

    void onFavouriteChanged(Champion champion, View v);
}
