package de.senft.smplweather.request;

public abstract class WeatherRequest {
    protected double latitude;
    protected double longitude;

    public double getLatitude() {
        // TODO
        return 37.77500916;
        // return latitude;
    }

    public double getLongitude() {
        // TODO
        return -122.41825867;
        // return longtitude;
    }

    public WeatherRequest(double lat, double lng) {
        this.latitude = lat;
        this.longitude = lng;
    }

    public abstract void getCurrentCondition(
            final WeatherRequestCallback callback);

    public abstract void getForecast(final WeatherRequestCallback callback);
}
