package de.senft.smplweather.request;

import de.senft.smplweather.Condition;

public interface WeatherRequestCallback {
    public void onSuccess(Condition[] cond);

    public void onError();
}
