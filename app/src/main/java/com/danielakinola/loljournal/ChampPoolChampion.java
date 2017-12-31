package com.danielakinola.loljournal;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dakinola892 on 25/12/2017.
 */

public class ChampPoolChampion extends Champion {
    private Set<Champion> matchups = new HashSet<>();

    public ChampPoolChampion(String name, int imgResource) {
        super(name, imgResource);
    }

    public ChampPoolChampion(Champion champion) {
        super(champion.getName(), champion.getImgResource());
    }

    public Set<Champion> getMatchups() {
        return matchups;
    }

    public void addMatchup(Champion champion) {
        matchups.add(champion);
    }

}
