package com.danielakinola.loljournal.champpool;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;

public class BindingAdapters {
    /*@BindingAdapter("champs")
    public static void setChampPoolListItems(RecyclerView recyclerView, List<Champion> champions) {
        ChampionAdapter adapter = (ChampionAdapter) recyclerView.getAdapter();
        adapter.setChampions(champions);
        adapter.notifyDataSetChanged();
    }*/

    @BindingAdapter("portrait")
    public static void setPortrait(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter("favourite")
    public static void setFavourite(MaterialFavoriteButton materialFavoriteButton, boolean favourited) {
        materialFavoriteButton.setFavorite(favourited, false);
    }
}
