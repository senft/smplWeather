package de.senft.smplweather;

public class Condition {

    // Degrees in fahrenheit
    private double fahrenheit;

    // Degrees in celcius
    private double celcius;

    private String text;
    private int icon;

    public Condition(String text, double fahrenheit, int icon) {
        this.fahrenheit = Math.round(fahrenheit);
        this.celcius = Math.round(this.fahrenheit / 1.8) - 32;

        this.icon = icon;

        this.text = text;
    }

    public double getCelcius() {
        return celcius;
    }

    public double getFahrenheit() {
        return fahrenheit;
    }

    public int getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }
}