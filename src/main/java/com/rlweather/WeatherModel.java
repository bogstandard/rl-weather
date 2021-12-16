package com.rlweather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherModel {

    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    private Double message;
    @SerializedName("cnt")
    @Expose
    private Integer cnt;
    @SerializedName("list")
    @Expose
    private java.util.List<WeatherList> list = null;
    @SerializedName("city")
    @Expose
    private City city;

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Double getMessage() {
        return message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public java.util.List<WeatherList> getList() {
        return list;
    }

    public void setList(java.util.List<WeatherList> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }


    public class City {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("coord")
        @Expose
        private Coord coord;
        @SerializedName("country")
        @Expose
        private String country;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Coord getCoord() {
            return coord;
        }

        public void setCoord(Coord coord) {
            this.coord = coord;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public class Coord {

            @SerializedName("lat")
            @Expose
            private Double lat;
            @SerializedName("lon")
            @Expose
            private Double lon;

            public Double getLat() {
                return lat;
            }

            public void setLat(Double lat) {
                this.lat = lat;
            }

            public Double getLon() {
                return lon;
            }

            public void setLon(Double lon) {
                this.lon = lon;
            }

        }

    }

    public class WeatherList {

        @SerializedName("dt")
        @Expose
        private Integer dt;
        @SerializedName("main")
        @Expose
        private Main main;
        @SerializedName("weather")
        @Expose
        private java.util.List<Weather> weather = null;
        @SerializedName("clouds")
        @Expose
        private Clouds clouds;
        @SerializedName("wind")
        @Expose
        private Wind wind;
        @SerializedName("sys")
        @Expose
        private Sys sys;
        @SerializedName("dt_txt")
        @Expose
        private String dtTxt;

        public Integer getDt() {
            return dt;
        }

        public void setDt(Integer dt) {
            this.dt = dt;
        }

        public Main getMain() {
            return main;
        }

        public void setMain(Main main) {
            this.main = main;
        }

        public java.util.List<Weather> getWeather() {
            return weather;
        }

        public void setWeather(java.util.List<Weather> weatherModel) {
            this.weather = weatherModel;
        }

        public Clouds getClouds() {
            return clouds;
        }

        public void setClouds(Clouds clouds) {
            this.clouds = clouds;
        }

        public Wind getWind() {
            return wind;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }

        public Sys getSys() {
            return sys;
        }

        public void setSys(Sys sys) {
            this.sys = sys;
        }

        public String getDtTxt() {
            return dtTxt;
        }

        public void setDtTxt(String dtTxt) {
            this.dtTxt = dtTxt;
        }



        public class Weather {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("main")
            @Expose
            private String main;
            @SerializedName("description")
            @Expose
            private String description;
            @SerializedName("icon")
            @Expose
            private String icon;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getMain() {
                return main;
            }

            public void setMain(String main) {
                this.main = main;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
            // https://openweathermap.org/weather-conditions
            public boolean isRainingFromID() {
                if(id < 600) {
                    return true;
                } else {
                    return false;
                }
            }
            public boolean isSnowingFromID() {
                if(id < 700 && id >= 600) {
                    return true;
                } else {
                    return false;
                }
            }
            public boolean isThunderingFromID() {
                if(id < 300) {
                    return true;
                } else {
                    return false;
                }
            }
        }


        public class Wind {

            @SerializedName("speed")
            @Expose
            private Double speed;
            @SerializedName("deg")
            @Expose
            private Double deg;

            public Double getSpeed() {
                return speed;
            }

            public void setSpeed(Double speed) {
                this.speed = speed;
            }

            public Double getDeg() {
                return deg;
            }

            public void setDeg(Double deg) {
                this.deg = deg;
            }

        }


        public class Clouds {

            @SerializedName("all")
            @Expose
            private Integer all;

            public Integer getAll() {
                return all;
            }

            public void setAll(Integer all) {
                this.all = all;
            }

        }

        public class Main {

            @SerializedName("temp")
            @Expose
            private Double temp;
            @SerializedName("temp_min")
            @Expose
            private Double tempMin;
            @SerializedName("temp_max")
            @Expose
            private Double tempMax;
            @SerializedName("pressure")
            @Expose
            private Double pressure;
            @SerializedName("sea_level")
            @Expose
            private Double seaLevel;
            @SerializedName("grnd_level")
            @Expose
            private Double grndLevel;
            @SerializedName("humidity")
            @Expose
            private Integer humidity;
            @SerializedName("temp_kf")
            @Expose
            private Float tempKf;

            public Double getTemp() {
                return temp;
            }

            public void setTemp(Double temp) {
                this.temp = temp;
            }

            public Double getTempMin() {
                return tempMin;
            }

            public void setTempMin(Double tempMin) {
                this.tempMin = tempMin;
            }

            public Double getTempMax() {
                return tempMax;
            }

            public void setTempMax(Double tempMax) {
                this.tempMax = tempMax;
            }

            public Double getPressure() {
                return pressure;
            }

            public void setPressure(Double pressure) {
                this.pressure = pressure;
            }

            public Double getSeaLevel() {
                return seaLevel;
            }

            public void setSeaLevel(Double seaLevel) {
                this.seaLevel = seaLevel;
            }

            public Double getGrndLevel() {
                return grndLevel;
            }

            public void setGrndLevel(Double grndLevel) {
                this.grndLevel = grndLevel;
            }

            public Integer getHumidity() {
                return humidity;
            }

            public void setHumidity(Integer humidity) {
                this.humidity = humidity;
            }

            public Float getTempKf() {
                return tempKf;
            }

            public void setTempKf(Float tempKf) {
                this.tempKf = tempKf;
            }

        }
        public class Sys {

            @SerializedName("pod")
            @Expose
            private String pod;

            public String getPod() {
                return pod;
            }

            public void setPod(String pod) {
                this.pod = pod;
            }

        }


    }
}