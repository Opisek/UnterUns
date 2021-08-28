package net.opisek.unteruns.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.repositories.MainRepository;

public class PostcardQuestionsViewModel extends ViewModel {
    public String[] getQuestions() {
        String[] ans = new String[MainRepository.getInstance().getQuestions().length];
        for (int i = 0; i < ans.length;) {
            ans[i] = "Frage " + (++i);
        }
        return ans;
    }
}
