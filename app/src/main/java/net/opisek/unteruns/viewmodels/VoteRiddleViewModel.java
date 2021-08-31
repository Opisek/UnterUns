package net.opisek.unteruns.viewmodels;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.repositories.MainRepository;

import java.util.ArrayList;

public class VoteRiddleViewModel extends ViewModel {
    public boolean isVoteCorrect(String vote) {
        return vote.equals(MainRepository.getInstance().correctVote);
    }
}
