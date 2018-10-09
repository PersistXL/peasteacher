package com.peas.common.base;

import com.google.common.eventbus.Subscribe;

/**
 * Created by peaimage on 2016/5/9.
 */
public interface EventBusListener {

    @Subscribe
    void onEvent(Event event);
}
