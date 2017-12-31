package com.danielakinola.loljournal;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by white on 31/12/2017.
 */
public class ChampionViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView textView;

    public ChampionViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.img_champion_portrait0);
        textView = itemView.findViewById(R.id.text_champion_name);
    }

}
