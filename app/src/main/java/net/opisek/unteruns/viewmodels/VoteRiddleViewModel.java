package net.opisek.unteruns.viewmodels;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.repositories.MainRepository;

import java.util.ArrayList;

public class VoteRiddleViewModel extends ViewModel {
    public boolean isVoteCorrect(ArrayList<Integer> vote) {
        for (int i = 0; i < MainRepository.getInstance().correctVote.length; i++) if (!vote.contains(MainRepository.getInstance().correctVote[i])) return false;
        return true;
    }

    public int getVote(String vote){
        if(vote.equals("Red")) return MainRepository.getInstance().redVote;
        if(vote.equals("Pink")) return MainRepository.getInstance().pinkVote;
        if(vote.equals("White")) return MainRepository.getInstance().whiteVote;
        return -1;
    }
}
