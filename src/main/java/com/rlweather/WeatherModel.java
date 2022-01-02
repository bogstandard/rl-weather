package com.rlweather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherModel {

    // openweather returns an array of one weather item
    // catch it but then return only the first
    // kinda confusing but shortens the codebase hugely
    @SerializedName("weather")
    @Expose
    private List<Weather> weather;

    public Weather getWeather() {
        return this.weather.get(0);
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public class Weather {
        @SerializedName("id")
        @Expose
        private int id;

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        // https://openweathermap.org/weather-conditions
        public boolean isRainingFromID() {
            if (this.id < 600) {
                return true;
            } else {
                return false;
            }
        }

        public boolean isSnowingFromID() {
            if (this.id < 700 && this.id >= 600) {
                return true;
            } else {
                return false;
            }
        }

        public boolean isThunderingFromID() {
            if (this.id < 300) {
                return true;
            } else {
                return false;
            }
        }
    }
}
