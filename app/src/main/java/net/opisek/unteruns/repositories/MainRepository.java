package net.opisek.unteruns.repositories;

import androidx.lifecycle.MutableLiveData;

import net.opisek.unteruns.models.GpsModel;

public class MainRepository {

    private static MainRepository instance;

    public static MainRepository getInstance() {
        if (instance == null) instance = new MainRepository();
        return instance;
    }

    private MutableLiveData<Float> compassRotation;

    public MutableLiveData<Float> getCompassRotation() {
        compassRotation = GpsModel.getInstance().getCompassRotation();
        return compassRotation;
    }
}
