package de.senft.smplweather.request;

import it.restrung.rest.cache.RequestCache;
import it.restrung.rest.client.ContextAwareAPIDelegate;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.fortysevendeg.android.wunderground.api.service.WundergroundApiProvider;
import com.fortysevendeg.android.wunderground.api.service.request.Feature;
import com.fortysevendeg.android.wunderground.api.service.request.Query;
import com.fortysevendeg.android.wunderground.api.service.response.ForecastDayResponse;
import com.fortysevendeg.android.wunderground.api.service.response.ObservationResponse;
import com.fortysevendeg.android.wunderground.api.service.response.WundergroundResponse;

import de.senft.smplweather.Condition;
import de.senft.smplweather.R;

public class WundergroundReqest extends WeatherRequest {

    private final String API_KEY = "8e7609d0c8be4b6a";

    private Context context;

    public WundergroundReqest(Context context, double lat, double lng) {
        super(lat, lng);
        this.context = context;
    }

    @Override
    public void getCurrentCondition(final WeatherRequestCallback callback) {
        WundergroundApiProvider.getClient().query(
                new ContextAwareAPIDelegate<WundergroundResponse>(this.context,
                        WundergroundResponse.class,
                        RequestCache.LoadPolicy.NEVER) {

                    @Override
                    public void onResults(WundergroundResponse response) {

                        ObservationResponse current = response
                                .getCurrentObservation();
                        String icon = current.getIcon();
                        int temperature = (int) Math.round(current.getTempF());

                        Condition currentCondition = new Condition("",
                                temperature, 0, 0, iconStringToR(icon), "Now");

                        callback.onSuccess(new Condition[] { currentCondition });
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                }, API_KEY,
                Query.latLng(this.getLatitude(), this.getLongitude()),
                Feature.conditions);
    }

    @Override
    public void getForecast(final WeatherRequestCallback callback) {
        WundergroundApiProvider.getClient().query(
                new ContextAwareAPIDelegate<WundergroundResponse>(this.context,
                        WundergroundResponse.class,
                        RequestCache.LoadPolicy.NEVER) {

                    @Override
                    public void onResults(WundergroundResponse response) {

                        List<ForecastDayResponse> days = response.getForecast()
                                .getSimpleForecast().getForecastDay();

                        Condition[] forecast = new Condition[3];

                        // TODO should be .getFcText() but that's empty
                        // forecast[0] = new Condition(
                        // days.get(0).getConditions(), days.get(0)
                        // .getHigh().getFahrenheit());
                        // forecast[1] = new Condition(
                        // days.get(0).getConditions(), days.get(1)
                        // .getHigh().getFahrenheit());
                        // forecast[2] = new Condition(
                        // days.get(0).getConditions(), days.get(2)
                        // .getHigh().getFahrenheit());

                        for (int i = 0; i < 3; i++) {
                            ForecastDayResponse current = days.get(i);
                            String text = current.getConditions();
                            String weekday = current.getDate().getWeekday();

                            int max = (int) Math.round(current.getHigh()
                                    .getFahrenheit());

                            int min = (int) Math.round(current.getLow()
                                    .getFahrenheit());

                            String icon = current.getIcon();

                            forecast[i] = new Condition(text, 0, min, max,
                                    iconStringToR(icon), weekday);
                        }

                        callback.onSuccess(forecast);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                }, API_KEY,
                Query.latLng(this.getLatitude(), this.getLongitude()),
                Feature.forecast);
    }

    private int iconStringToR(String icon) {
        if (icon.equals("cloudy")) {
            return R.drawable.cloudy;

        } else if (icon.equals("fog")) {
            return R.drawable.fog;

        } else if (icon.equals("hazy")) {
            return R.drawable.hazy;

        } else if (icon.equals("clear")) {
            return R.drawable.clear;

        } else if (icon.equals("sunny")) {
            return R.drawable.sunny;

        } else if (icon.equals("rain") || icon.equals("chancerain")) {
            return R.drawable.rain;

        } else if (icon.equals("snow") || icon.equals("chancesnow")
                || icon.equals("flurries") || icon.equals("chanceflurries")) {
            return R.drawable.snow;

        } else if (icon.equals("sleet") || icon.equals("chancesleet")) {
            return R.drawable.sleet;

        } else if (icon.equals("tstorm") || icon.equals("chancetstorm")) {
            return R.drawable.storm;

        } else if (icon.equals("mostlycloudy") || icon.equals("partlycloudy")) {
            return R.drawable.mostlycloudy;

        } else if (icon.equals("mostlysunny") || icon.equals("partlysunny")) {
            return R.drawable.mostlysunny;
        }

        Log.d("smplWeather", "No icon for string: " + icon);
        return R.drawable.na;
    }
}