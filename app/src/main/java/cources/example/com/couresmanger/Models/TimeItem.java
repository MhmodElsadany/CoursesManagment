package cources.example.com.couresmanger.Models;

/**
 * Created by Google       Company on 06/03/2018..
 */

public class TimeItem {
    String day;
    String from;
    String too;

    public TimeItem(String day, String from, String too) {
        this.day = day;
        this.from = from;
        this.too = too;

    }

    public String getFro() {
        return from;
    }

    public String getDay() {
        return day;
    }

    public String getToo() {
        return too;
    }
}
