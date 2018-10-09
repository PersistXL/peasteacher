package com.peas.common.base;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import java.util.Map;

/**
 * Created by peaimage on 2016/5/9.
 */
public class EventBusCenter {

    private static final Map<String, EventBus> buses = Maps.newConcurrentMap();

    private static Object lock = new Object();

    private EventBusCenter() {

    }

    public static void post(String busLine, Event event) {
        checkBusLine(busLine);
        buses.get(busLine).post(event);
    }

    public static void register(String busLine, EventBusListener listener) {
        checkBusLine(busLine);
        buses.get(busLine).register(listener);
    }

    public static void register(String busLine, Class<? extends EventBusListener> listener) {
        try {
            register(busLine, listener.newInstance());
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    public static int size() {
        return buses.size();
    }


    public static void post(String busLine, String eventType, Object eventContent) {
        post(busLine, Events.newEvent(eventType, eventContent));
    }

    private static void checkBusLine(String busLine) {
        synchronized (lock) {
            if (!buses.containsKey(busLine)) {
                buses.put(busLine, new EventBus(busLine));
            }
        }
    }
}
