package net.opisek.unteruns.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.repositories.MainRepository;

public class CompassViewModel extends ViewModel {

    private MutableLiveData<Float> compassRotation;

    public void init() {
        if (compassRotation != null) {

        }
    }

    public MutableLiveData<Float> getCompassRotation() {
        compassRotation = MainRepository.getInstance().getCompassRotation();
        return compassRotation;
    }
}
