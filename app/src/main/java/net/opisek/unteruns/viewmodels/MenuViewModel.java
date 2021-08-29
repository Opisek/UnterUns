package net.opisek.unteruns.viewmodels;

import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.repositories.MainRepository;

public class MenuViewModel extends ViewModel {
    public void setProgress(int route, int progress) {
        MainRepository.getInstance().continueFromSave(route, progress);
    }
}
