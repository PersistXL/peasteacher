package com.peas.common.base;

/**
 * Created by peaimage on 2016/5/9.
 */
public class Events {

    private Events() {
    }

    public static Event newEvent(String eventType, String eventFrom, Object event) {
        return new Event(eventType, event, eventFrom);
    }

    public static Event newEvent(String eventType, Object event) {
        return new Event(eventType, event, null);
    }

    public static Event newEvent(Object event) {
        return new Event(null, event, null);
    }
}
