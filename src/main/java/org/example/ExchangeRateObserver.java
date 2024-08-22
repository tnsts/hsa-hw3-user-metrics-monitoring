package org.example;

import org.json.JSONArray;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExchangeRateObserver {

    private static final String EVENT_NAME_TEMPLATE = "exr_uah%s";
    private static final String URL_TEMPLATE = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json&valcode=%s";
    private final String resourceUrl;
    private final String eventName;

    private final CustomHttpClient httpClient;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public ExchangeRateObserver(String currencyCode) {
        this.resourceUrl = String.format(URL_TEMPLATE, currencyCode);
        this.eventName = String.format(EVENT_NAME_TEMPLATE, currencyCode.toLowerCase());
        this.httpClient = new CustomHttpClient();
    }

    public double getExchangeRate() {
        String result = httpClient.executeGet(resourceUrl);
        return (new JSONArray(result)).getJSONObject(0).getDouble("rate");
    }

    public void startPublisher(int fixedRateMin, GaPublisher gaPublisher) {
        Runnable publisherTask = () -> {
            double exchangeRate = getExchangeRate();
            System.out.println(exchangeRate);
            gaPublisher.publishNewValue(eventName, exchangeRate);
        };
        executorService.scheduleAtFixedRate(publisherTask, 0, fixedRateMin, TimeUnit.MINUTES);
    }
}
