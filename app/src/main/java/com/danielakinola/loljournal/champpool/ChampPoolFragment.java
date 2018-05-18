package com.danielakinola.loljournal.champpool;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.ViewModelFactory;
import com.danielakinola.loljournal.utils.ScreenUtils;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChampPoolFragment extends Fragment {
    private static final String LANE = "LANE";
    private int lane;
    @Inject
    ViewModelFactory viewModelFactory;
    private ChampPoolViewModel champPoolViewModel;
    private ChampionAdapter championAdapter;

    public ChampPoolFragment() {
    }

    public static ChampPoolFragment getInstance(int lane) {
        ChampPoolFragment champPoolFragment = new ChampPoolFragment();
        Bundle args = new Bundle();
        args.putInt(LANE, lane);
        champPoolFragment.setArguments(args);
        return champPoolFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lane = getArguments().getInt(LANE, 0);
        champPoolViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), viewModelFactory).get(ChampPoolViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = getLayoutInflater().inflate(R.layout.fragment_champ_pool, container, false);
        setupRecyclerView(rootView);

        /*FragmentChampPoolBinding fragmentChampPoolBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_champ_pool, container, false);
        View rootView = fragmentChampPoolBinding.getRoot();
        setupDataBinding(fragmentChampPoolBinding);*/
        //setupRecyclerView(recyclerView);
        return rootView;
    }


    private void setupRecyclerView(View rootView) {
        RecyclerView recyclerView = Objects.requireNonNull(rootView.findViewById(R.id.champ_pool_recycler_view));
        int spanCount = ScreenUtils.calculateNoOfColumns(Objects.requireNonNull(this.getContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), spanCount));
        championAdapter = new ChampionAdapter(champPoolViewModel);
        recyclerView.setAdapter(championAdapter);

        View emptyState = rootView.findViewById(R.id.empty_state);

        champPoolViewModel.getChampions(lane).observe(this, champions -> {
            if (champions == null || champions.isEmpty()) {
                emptyState.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                //TODO; find way to stop setting visibiity when already data?
                emptyState.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                championAdapter.setChampions(champions);
                championAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }
}
