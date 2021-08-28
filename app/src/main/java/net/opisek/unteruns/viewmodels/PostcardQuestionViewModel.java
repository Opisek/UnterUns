package net.opisek.unteruns.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.models.MorseModel;
import net.opisek.unteruns.repositories.MainRepository;

public class PostcardQuestionViewModel extends ViewModel {
    private MorseModel question;
    private MorseModel answer;

    public void setQuestion(int i) {
        question = MainRepository.getInstance().getQuestion(i);
        answer = MainRepository.getInstance().getAnswer(i);
        clearInput();
    }

    public int[] getMorseSequence() {
        return question.morse;
    }

    boolean canBeCorrect;
    int index;

    public void registerInput(int length) {
        if (!canBeCorrect) return;
        if (answer.morse[index] == length) {
            do {
                index++;
            } while (index < answer.morse.length && answer.morse[index] >= 2);
            if (index >= answer.morse.length) {
                getIsCorrect().setValue(true);
            }
        } else {
            canBeCorrect = false;
        }
    }

    public void clearInput() {
        canBeCorrect = true;
        index = 0;
    }

    private MutableLiveData<Boolean> isCorrect;
    public MutableLiveData<Boolean> getIsCorrect() {
        if (isCorrect == null) {
            isCorrect = new MutableLiveData<>();
            isCorrect.setValue(false);
        }
        return isCorrect;
    }
}
