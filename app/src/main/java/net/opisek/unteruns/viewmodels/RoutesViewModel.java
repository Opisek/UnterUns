package net.opisek.unteruns.viewmodels;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.opisek.unteruns.repositories.MainRepository;

import java.util.ArrayList;

public class RoutesViewModel extends ViewModel {
    public ArrayList<Pair<String, String>> getRoutes() {
        return MainRepository.getInstance().getRoutes();
    }

    public void pickRoute(int routeIndex) {
        MainRepository.getInstance().pickRoute(routeIndex);
        picked.setValue(true);
    }

    private MutableLiveData<Boolean> picked;

    public MutableLiveData<Boolean> getPicked() {
        if (picked == null) {
            picked = new MutableLiveData<>();
            picked.setValue(false);
        }
        return picked;
    }
}
