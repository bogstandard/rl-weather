package com.rlweather;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class WeatherAPI {
    public final String URL = "http://api.openweathermap.org/";
    private final int MAX_STALENESS = 360; // Gameticks are ~0.6sec, 600 ~= every 3 minutes
                                            // Roughly req 14600 a month if continuous play.
                                            // (don't ever get near free api rate limits)
                                            // see https://openweathermap.org/price
                                            // we must be careful here!
    private String location = "";
    private String apiKey = "";
    private ChatMessageManager chatMessageManager;
    private int staleness = MAX_STALENESS;
    private boolean isSnowing = false;
    private boolean isRaining = false;
    private boolean isThundering = false;
    private Optional<Boolean> isHealthy = Optional.empty();

    private interface WeatherApiService {
        @GET("data/2.5/forecast")
        Call<WeatherModel> requestWeather(@Header("x-api-key") String apiKey, @Query("q") String location, @Query("units") String units, @Query("cnt") String count);
    }

    public WeatherAPI() {
        log.debug("Weather API starting");
    }

    public void setLocation(String location) {
        if(!this.location.equals(location)) {
            this.location = location;
            isHealthy = Optional.empty();
        }
    }

    public void setApiKey(String apiKey) {
        if(!this.apiKey.equals(apiKey)) {
            this.apiKey = apiKey;
            isHealthy = Optional.empty();
        }
    }

    public void setChatMessageManager(ChatMessageManager chatMessageManager) {
        this.chatMessageManager = chatMessageManager;
    }

    private void sendMessage(String chat) {
        final String message = new ChatMessageBuilder()
                .append(ChatColorType.HIGHLIGHT)
                .append(chat)
                .build();

        chatMessageManager.queue(
                QueuedMessage.builder()
                        .type(ChatMessageType.CONSOLE)
                        .runeLiteFormattedMessage(message)
                        .build());
    }

    // Called on gametick (~0.6 seconds), updates every MAX_STALENESS increment
    public void update() {
        if(location.equals("") || apiKey.equals("")) {
            return;
        } else if(staleness > MAX_STALENESS) {
            log.debug("Weather data stale, refreshing from API");
            // update weather here
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL) //This is the only mandatory call on Builder object.
                    .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                    .build();

            WeatherApiService weatherApiService = retrofit.create(WeatherApiService.class);

            weatherApiService.requestWeather(apiKey, location, "metric", "10").enqueue(new Callback<WeatherModel>() {
                @Override
                public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                    log.debug(call.toString(), response.toString());
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            log.debug(response.raw().toString());
                            List<WeatherModel.WeatherList.Weather> weatherEntries = response.body().getList().stream().flatMap(weatherItem -> weatherItem.getWeather().stream()).collect(Collectors.toList());
                            weatherEntries.forEach(entry -> log.debug(entry.getId().toString()));
                            isRaining = weatherEntries.stream().anyMatch(WeatherModel.WeatherList.Weather::isRainingFromID);
                            isSnowing = weatherEntries.stream().anyMatch(WeatherModel.WeatherList.Weather::isSnowingFromID);
                            isThundering = weatherEntries.stream().anyMatch(WeatherModel.WeatherList.Weather::isThunderingFromID);
                            if(!isHealthy.orElse(false)) {
                                sendMessage("Now connected to weather in "+location);
                            }
                            isHealthy = Optional.of(true);
                            log.debug("Updated weather --\n" +
                                    "isRaining: " + isRaining() + "\n" +
                                    "isSnowing: " + isSnowing() + "\n" +
                                    "isThundering: " + isThundering() + "\n");
                        } else {
                            log.error("Empty body response: " + response.code());
                        }
                    } else {
                        log.error("Responded with error code: " + response.code());
                        String error = "";
                        try {
                            error = response.errorBody().string();
                            log.error(error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            log.error("Disabling weather statuses due to broken API request");
                            isRaining = false;
                            isSnowing = false;
                            isThundering = false;
                            if(isHealthy.orElse(true)) {
                                sendMessage("Could not load weather data with given API key and/or location " + location);
                                sendMessage(error);
                                log.debug("API health dropped, sent message to console.");
                            } else {
                                log.debug("API has not been healthy, won't notify.");
                            }
                            isHealthy = Optional.of(false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<WeatherModel> call, Throwable t) {
                    log.error("Error in call" + call.toString());
                    log.error(t.toString());
                }
            });

            // reset staleness
            staleness = 0;
        } else {
            staleness ++;
        }
    }

    public boolean isSnowing() {
        return isSnowing;
    }
    public boolean isRaining() {
        return isRaining;
    }
    public boolean isThundering() {
        return isThundering;
    }
}
