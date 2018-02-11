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

    /*@BindingAdapter("matchups")
    public static void setMatchupsListItems(RecyclerView recyclerView, List<Champion> champions) {
        ChampionAdapter adapter = (ChampionAdapter) recyclerView.getAdapter();
        adapter.setChampions((ArrayList<Champion>) champions);
    }*/

    /*@BindingAdapter("bitmapResource")
    public static void setChampionBitmap(ImageView imageView, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(imageView.getContext().getResources(), resourceId);
        bitmap = Bitmap.createBitmap(bitmap, 35,35,700, 700);
    }*/
}
