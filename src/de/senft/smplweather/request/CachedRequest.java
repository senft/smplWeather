package de.senft.smplweather.request;

import android.content.Context;
import android.content.SharedPreferences;

public class CachedRequest {

    // TODO save/load icons

    private static final String PREFS_NAME = "smpl_weather_last_query";

    private static final String KEY_TIME = "time";
    private static final String KEY_CITYNAME = "cityname";
    private static final String KEY_PREFIX_MIN_TEMP = "min_temp_fore";
    private static final String KEY_PREFIX_MAX_TEMP = "max_temp_fore";
    private static final String KEY_PREFIX_DATE_FORE = "date_fore";
    private static final String KEY_CURRENT_TEMP = "current_temp";

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public CachedRequest(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
    }

    public int getCurrentTemp() {
        return settings.getInt(KEY_CURRENT_TEMP, 0);
    }

    public String getDateFore(int day) {
        return settings.getString(KEY_PREFIX_DATE_FORE + day, "");
    }

    public long getTime() {
        // Make sure the default value is faaaar in the past (so if this is not
        // set yet, we still do an update)
        return settings.getLong(KEY_TIME, Long.MIN_VALUE);
    }

    public String getCityName() {
        return settings.getString(KEY_CITYNAME, "");
    }

    public int getMaxTempFore(int day) {
        return settings.getInt(KEY_PREFIX_MAX_TEMP + day, 0);
    }

    public int getMinTempFore(int day) {
        return settings.getInt(KEY_PREFIX_MIN_TEMP + day, 0);
    }

    public void setCurrentTemp(int temp) {
        editor.putInt(KEY_CURRENT_TEMP, temp);
        editor.commit();
    }

    public void setDateFore(int day, String date) {
        editor.putString(KEY_PREFIX_DATE_FORE + day, date);
        editor.commit();
    }

    public void setTime(long time) {
        editor.putLong(KEY_TIME, time);
        editor.commit();
    }

    public void setCityName(String location) {
        editor.putString(KEY_CITYNAME, location);
        editor.commit();
    }

    public void setMaxTempFore(int day, int temp) {
        editor.putInt(KEY_PREFIX_MAX_TEMP + day, temp);
        editor.commit();
    }

    public void setMinTempFore(int day, int temp) {
        editor.putInt(KEY_PREFIX_MIN_TEMP + day, temp);
        editor.commit();
    }
}