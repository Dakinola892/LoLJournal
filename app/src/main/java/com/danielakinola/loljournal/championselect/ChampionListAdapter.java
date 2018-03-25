package com.danielakinola.loljournal.championselect;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danielakinola.loljournal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dakin on 27/12/2017.
 */

public class ChampionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int INACTIVE = 0;
    private static final int ACTIVE = 1;

    private final ChampionSelectViewModel championSelectViewModel;
    private final ArrayList<String> initallySelectedChampions;
    private ArrayList<String> currentlySelectedChampions = new ArrayList<>();

    private final String[] champNames = {"Aatrox", "Ahri", "Akali", "Alistar", "Amumu", "Anivia", "Annie", "Ashe",
            "Aurelion Sol", "Azir", "Bard", "Blitzcrank", "Brand", "Braum", "Caitlyn", "Camille",
            "Cassiopeia", "Cho'Gath", "Corki", "Darius", "Diana", "DrMundo", "Draven", "Ekko", "Elise",
            "Evelynn", "Ezreal", "Fiddlesticks", "Fiora", "Fizz", "Galio", "Gangplank", "Garen",
            "Gnar", "Gragas", "Graves", "Hecarim", "Heimerdinger", "Illaoi", "Irelia", "Ivern", "Janna",
            "Jarvan IV", "Jax", "Jayce", "Jhin", "Jinx", "Kalista", "Karma", "Karthus", "Kassadin",
            "Katarina", "Kayn", "Kayle", "Kennen", "Kled", "Kindred", "Kha'Zix", "Kog'Maw", "LeBlanc",
            "Lee Sin", "Leona", "Lissandra", "Lucian", "Lulu", "Lux", "Malphite", "Malzahar", "Maokai",
            "Master Yi", "Miss Fortune", "Mordekaiser", "Morgana", "Nami", "Nasus", "Nautilus", "Nidalee",
            "Nocturne", "Nunu", "Olaf", "Orianna", "Ornn", "Pantheon", "Poppy", "Quinn", "Rakan",
            "Rammus", "Rek'Sai", "Renekton", "Rengar", "Riven", "Rumble", "Ryze", "Sejuani", "Shaco",
            "Shen", "Shyvana", "Singed", "Sion", "Sivir", "Skarner", "Sona", "Soraka", "Swain", "Syndra",
            "Tahm Kench", "Taliyah", "Talon", "Taric", "Teemo", "Thresh", "Tristana", "Trundle",
            "Tryndamere", "Twisted Fate", "Twitch", "Udyr", "Urgot", "Varus", "Vayne", "Veigar",
            "Vel'Koz", "Vi", "Viktor", "Vladimir", "Volibear", "Warwick", "Wukong", "Xayah", "Xerath",
            "Xin Zhao", "Yasuo", "Yorick", "Zac", "Zed", "Ziggs", "Zoe", "Zilean", "Zyra"};

    final private int[] championSquares = {
            R.drawable.aatrox_loading, R.drawable.ahri_loading, R.drawable.akali_loading,
            R.drawable.alistar_loading, R.drawable.amumu_loading, R.drawable.anivia_loading,
            R.drawable.annie_loading, R.drawable.ashe_loading, R.drawable.aureliansol_loading,
            R.drawable.azir_loading, R.drawable.bard_loading, R.drawable.blitzcrank_loading,
            R.drawable.brand_loading, R.drawable.braum_loading, R.drawable.caitlyn_loading,
            R.drawable.camille_loading, R.drawable.cassiopeia_loading, R.drawable.chogath_loading,
            R.drawable.corki_loading, R.drawable.darius_loading, R.drawable.diana_loading,
            R.drawable.drmundo_loading, R.drawable.draven_loading, R.drawable.ekko_loading,
            R.drawable.elise_loading, R.drawable.evelynn_loading, R.drawable.ezreal_loading,
            R.drawable.fiddlesticks_loading, R.drawable.fiora_loading, R.drawable.fizz_loading,
            R.drawable.galio_loading, R.drawable.gangplank_loading, R.drawable.garen_loading,
            R.drawable.gnar_loading, R.drawable.gragas_loading, R.drawable.graves_loading,
            R.drawable.hecarim_loading, R.drawable.heimerdinger_loading, R.drawable.illaoi_loading,
            R.drawable.irelia_loading, R.drawable.ivern_loading, R.drawable.janna_loading,
            R.drawable.jarvaniv_loading, R.drawable.jax_loading, R.drawable.jayce_loading,
            R.drawable.jhin_loading, R.drawable.jinx_loading, R.drawable.kalista_loading,
            R.drawable.karma_loading, R.drawable.karthus_loading, R.drawable.kassadin_loading,
            R.drawable.katarina_loading, R.drawable.kayn_loading, R.drawable.kayle_loading,
            R.drawable.kennen_loading, R.drawable.kled_loading, R.drawable.kindred_loading,
            R.drawable.khazix_loading, R.drawable.kogmaw_loading, R.drawable.leblanc_loading,
            R.drawable.leesin_loading, R.drawable.leona_loading, R.drawable.lissandra_loading,
            R.drawable.lucian_loading, R.drawable.lulu_loading, R.drawable.lux_loading,
            R.drawable.malphite_loading, R.drawable.malzahar_loading, R.drawable.maokai_loading,
            R.drawable.masteryi_loading, R.drawable.missfortune_loading, R.drawable.mordekaiser_loading,
            R.drawable.morgana_loading, R.drawable.nami_loading, R.drawable.nasus_loading,
            R.drawable.nautilus_loading, R.drawable.nidalee_loading, R.drawable.nocturne_loading,
            R.drawable.nunu_loading, R.drawable.olaf_loading, R.drawable.orianna_loading,
            R.drawable.ornn_loading, R.drawable.pantheon_loading, R.drawable.poppy_loading,
            R.drawable.quinn_loading, R.drawable.rakan_loading, R.drawable.rammus_loading,
            R.drawable.reksai_loading, R.drawable.renekton_loading, R.drawable.rengar_loading,
            R.drawable.riven_loading, R.drawable.rumble_loading, R.drawable.ryze_loading,
            R.drawable.sejuani_loading, R.drawable.shaco_loading, R.drawable.shen_loading,
            R.drawable.shyvana_loading, R.drawable.singed_loading, R.drawable.sion_loading,
            R.drawable.sivir_loading, R.drawable.skarner_loading, R.drawable.sona_loading,
            R.drawable.soraka_loading, R.drawable.swain_loading, R.drawable.syndra_loading,
            R.drawable.tahmkench_loading, R.drawable.taliyah_loading, R.drawable.talon_loading,
            R.drawable.taric_loading, R.drawable.teemo_loading, R.drawable.thresh_loading,
            R.drawable.tristana_loading, R.drawable.trundle_loading, R.drawable.tryndamere_loading,
            R.drawable.twistedfate_loading, R.drawable.twitch_loading, R.drawable.udyr_loading,
            R.drawable.urgot_loading, R.drawable.varus_loading, R.drawable.vayne_loading,
            R.drawable.veigar_loading, R.drawable.velkoz_loading, R.drawable.vi_loading,
            R.drawable.viktor_loading, R.drawable.vladimir_loading, R.drawable.volibear_loading,
            R.drawable.warwick_loading, R.drawable.wukong_loading, R.drawable.xerath_loading,
            R.drawable.teemo_loading,
            R.drawable.xinzhao_loading, R.drawable.yasuo_loading, R.drawable.yorick_loading,
            R.drawable.zac_loading, R.drawable.zed_loading, R.drawable.ziggs_loading,
            R.drawable.zoe_loading, R.drawable.zilean_loading, R.drawable.zyra_loading};



    ChampionListAdapter(ChampionSelectViewModel championSelectViewModel) {
        this.championSelectViewModel = championSelectViewModel;
        this.initallySelectedChampions = (ArrayList<String>) championSelectViewModel.getIntiallySelectedChampions();
    }

    @Override
    public int getItemViewType(int position) {
        if (initallySelectedChampions.contains(champNames[position])) {
            return INACTIVE;
        } else {
            return ACTIVE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_champion_list, parent, false);
        if (viewType == ACTIVE) {
            ActiveChampionViewHolder holder = new ActiveChampionViewHolder(rootView);
            holder.itemView.setOnClickListener(v ->
                    championSelectViewModel.onViewHolderClick(champNames[holder.getAdapterPosition()]));
            return holder;
        } else {
            InactiveChampionViewHolder holder = new InactiveChampionViewHolder(rootView);
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);  //set to grayscale
            holder.imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
            holder.imageView.setImageAlpha(128);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final String champName = champNames[position];
        final int championSquare = championSquares[position];
        if (viewHolder.getItemViewType() == INACTIVE) {
            InactiveChampionViewHolder holder = (InactiveChampionViewHolder) viewHolder;
            holder.textView.setText(champName);
            if (champName.length() > 10) {
                holder.textView.setTextSize(19f);
            }
            holder.imageView.setImageResource(championSquare);
            holder.imageView.setImageAlpha(128);


        } else {
            ActiveChampionViewHolder holder = (ActiveChampionViewHolder) viewHolder;
            holder.textView.setText(champName);
            if (champName.length() > 10) {
                holder.textView.setTextSize(22f);
            }
            holder.imageView.setImageResource(championSquare);
            if (currentlySelectedChampions.contains(champName)) {
                holder.selectorImageView.setVisibility(View.VISIBLE);
            } else {
                holder.selectorImageView.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return champNames.length;
    }

    public void setCurrentlySelectedChampions(List<String> currentlySelectedChampions) {
        this.currentlySelectedChampions = (ArrayList<String>) currentlySelectedChampions;
    }

    class ActiveChampionViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        final TextView textView;
        final ImageView selectorImageView;

        ActiveChampionViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_champion_portrait);
            textView = itemView.findViewById(R.id.text_champion_name);
            selectorImageView = itemView.findViewById(R.id.img_champion_selected);
        }

    }

    class InactiveChampionViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        final TextView textView;

        InactiveChampionViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_champion_portrait);
            textView = itemView.findViewById(R.id.text_champion_name);
        }

    }


}

