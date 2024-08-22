package org.example;

public class Main {
    public static void main(String[] args) {
        var rateObserver = new ExchangeRateObserver("USD");
        rateObserver.startPublisher(
                60,
                new GaPublisher("apisecret", "measurementid")
        );
    }
}