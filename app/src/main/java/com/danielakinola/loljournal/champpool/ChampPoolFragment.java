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
import com.danielakinola.loljournal.databinding.FragmentChampPoolBinding;

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
    private ViewModelFactory viewModelFactory;
    private ChampPoolViewModel champPoolViewModel;
    private ChampionAdapter championAdapter = new ChampionAdapter(champPoolViewModel.getChampions(lane).getValue(), champPoolViewModel);

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
        champPoolViewModel = ViewModelProviders.of(this, viewModelFactory).get(ChampPoolViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_champ_pool, container);
        FragmentChampPoolBinding fragmentChampPoolBinding = FragmentChampPoolBinding.bind(rootView);
        fragmentChampPoolBinding.setViewmodel(champPoolViewModel);
        fragmentChampPoolBinding.setLane(lane);

        setupRecyclerView();

        return rootView;
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.champ_pool_recyler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2); //TODO: Dagger this or XML this?
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(championAdapter);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }
}
