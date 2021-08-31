package net.opisek.unteruns.viewmodels;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.repositories.MainRepository;

public class InputQuestionViewModel extends ViewModel {

    private String answer;

    public void setAnswer(MainRepository.inputQuestionID id) {
        answer = MainRepository.getInstance().getInputQuestionAnswer(id).toLowerCase();
    }

    private MutableLiveData<Pair<Boolean, Integer>> isCorrect;
    public MutableLiveData<Pair<Boolean, Integer>> getIsCorrect() {
        if (isCorrect == null) {
            isCorrect = new MutableLiveData<>();
            isCorrect.setValue(new Pair(false, 0));
        }
        return isCorrect;
    }

    public void checkCorrect(String input) {
        isCorrect.setValue(new Pair(input.toLowerCase().equals(answer), isCorrect.getValue().second+1));
    }
}
