package net.opisek.unteruns.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.repositories.MainRepository;

public class PostcardsViewModel extends ViewModel {
    MutableLiveData<Boolean> allDone;
    public MutableLiveData<Boolean> getAllDone() {
        if (allDone == null) {
            allDone = new MutableLiveData<>();
            allDone.setValue(false);
        }
        return allDone;
    }
    public void checkAllDone() {
        getAllDone().setValue(MainRepository.getInstance().getAllPostcardQuestionsDone());
    }
}
