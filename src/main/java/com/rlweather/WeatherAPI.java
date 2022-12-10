package com.rlweather;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.client.RuneLite;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class WeatherAPI {
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
    private OkHttpClient okHttpClient;

    @Inject
    private Gson gson;

    @Inject
    private WeatherAPI(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        log.debug("Weather API starting");
    }

    public void zeroStaleness() {
        this.staleness = MAX_STALENESS + 100;
        sendMessage("Weather location config change detected, refreshing..");
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
                .append(chat)
                .build();

        chatMessageManager.queue(
                QueuedMessage.builder()
                        .type(ChatMessageType.CONSOLE)
                        .runeLiteFormattedMessage(message)
                        .build());
    }

    // Called on gametick (~0.6 seconds), updates every MAX_STALENESS increment
    @Inject
    public void update() {
        if(location.equals("") || apiKey.equals("")) {
            return;
        } else if(staleness > MAX_STALENESS) {
            log.debug("Weather data stale, refreshing from API");

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host("api.openweathermap.org")
                    .addPathSegment("data")
                    .addPathSegment("2.5")
                    .addPathSegment("weather")
                    .addQueryParameter("q", location)
                    .addQueryParameter("units", "metric")
                    .addQueryParameter("cnt", "10")
                    .build();

            Request getRequest = new Request.Builder()
                    .url(httpUrl)
                    .header("User-Agent", RuneLite.USER_AGENT + " (rl-weather")
                    .header("x-api-key", apiKey)
                    .build();

            okHttpClient.newCall(getRequest).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    log.info(call.toString(), response.body().toString());

                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            WeatherModel weatherModel = gson.fromJson(response.body().string(), WeatherModel.class);
                            isRaining = weatherModel.getWeather().isRainingFromID();
                            isSnowing = weatherModel.getWeather().isSnowingFromID();
                            isThundering = weatherModel.getWeather().isThunderingFromID();
                            if(!isHealthy.orElse(false)) {
                                sendMessage("Now connected to weather in "+ location);
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
                            error = response.body().string();
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
                    response.close();
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    log.error("Error in call" + call.toString());
                    log.error(e.toString());
                    e.printStackTrace();
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
