package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GaPublisher {

    public final String URL_TEMPLATE = "https://www.google-analytics.com/mp/collect?api_secret=%s&measurement_id=%s";
    public final String resourceUrl;

    public final String CLIENT_ID = "000000.000000";

    public final CustomHttpClient httpClient;

    public GaPublisher(String apiSecret, String measurementId) {
        this.resourceUrl = String.format(URL_TEMPLATE, apiSecret, measurementId);
        this.httpClient = new CustomHttpClient();
    }

    public void publishNewValue(String event, double value) {
        GaEventCollector eventCollector = new GaEventCollector(CLIENT_ID);
        eventCollector.addEvent(event, String.valueOf(value));
        httpClient.executePost(resourceUrl, eventCollector);
    }

    private static class GaEventCollector {
        private final String client_id;
        private final List<Event> events;

        public GaEventCollector(String client_id) {
            this.client_id = client_id;
            this.events = new ArrayList<>();
        }

        public String getClient_id() {
            return client_id;
        }

        public List<Event> getEvents() {
            return events;
        }

        public void addEvent(String name, String value) {
            events.add(new Event(name, Map.of("value", value)));
        }

        private static class Event {
            private final String name;
            private final Map<String, String> params;

            public Event(String name, Map<String, String> params) {
                this.name = name;
                this.params = params;
            }

            public String getName() {
                return name;
            }

            public Map<String, String> getParams() {
                return params;
            }
        }
    }
}
