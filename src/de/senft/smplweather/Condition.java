package de.senft.smplweather;

public class Condition {

    // Degrees in fahrenheit
    private int temperature;

    private int tempMin;
    private int tempMax;

    private String text;
    private int icon;
    private String date;

    public Condition(String text, int temperature, int min, int max, int icon,
            String date) {
        this.temperature = temperature;
        this.tempMin = min;
        this.tempMax = max;

        this.icon = icon;
        this.date = date;
        this.text = text;
    }

    public int getCelcius() {
        return toF(temperature);
    }

    public int getFahrenheit() {
        return temperature;
    }

    public int getMinC() {
        return toF(tempMin);
    }

    public int getMinF() {
        return tempMin;
    }

    public int getMaxC() {
        return toF(tempMax);
    }

    public int getMacF() {
        return tempMax;
    }

    public int getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    private int toF(int c) {
        return (int) Math.round(((c - 32) * 5) / 9);
    }
}