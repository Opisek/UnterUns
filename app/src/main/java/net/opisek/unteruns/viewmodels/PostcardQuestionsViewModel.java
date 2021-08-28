package net.opisek.unteruns.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.repositories.MainRepository;

public class PostcardQuestionsViewModel extends ViewModel {
    MainRepository rep;
    private int questionsCount;
    private int questionsDone;
    public PostcardQuestionsViewModel() {
        rep = MainRepository.getInstance();
        questionsCount = rep.getPostcardQuestions().length;
        questionsDone = 0;
        for (int i = 0; i < questionsCount; i++) if (rep.getPostcardQuestionDone(i)) questionsDone++;
        if(questionsDone == questionsCount) getAllDone().setValue(true);
        rep.setPostcardQuestionDoneListener(new MainRepository.PostcardQuestionDoneListener() {
            @Override
            public void onDoneUpdated(int index) {
                setQuestionDone(index);
            }
        });
    }

    public String[] getQuestions() {
        String[] ans = new String[questionsCount];
        for (int i = 0; i < ans.length;) {
            ans[i] = "Frage " + (++i);
        }
        return ans;
    }

    MutableLiveData<Boolean>[] questionDone;
    public MutableLiveData<Boolean> getQuestionDone(int index) {
        if (questionDone == null) {
            questionDone = new MutableLiveData[rep.getPostcardQuestions().length];
            for (int i = 0; i < questionDone.length; i++) {
                questionDone[i] = new MutableLiveData<>();
                questionDone[i].setValue(rep.getPostcardQuestionDone(i));
            }
        }
        return questionDone[index];
    }

    MutableLiveData<Boolean> allDone;
    public MutableLiveData<Boolean> getAllDone() {
        if (allDone == null) {
            allDone = new MutableLiveData<>();
            allDone.setValue(false);
        }
        return allDone;
    }
    private void setQuestionDone(int index) {
        getQuestionDone(index).setValue(true);
        if(++questionsDone == questionsCount) getAllDone().setValue(true);
    }
}
