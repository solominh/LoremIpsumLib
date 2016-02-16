package com.lorem_ipsum.modules.event_bus;

import de.greenrobot.event.EventBus;

/**
 * Created by hoangminh on 12/22/15.
 */
public class MyEventBus {

    private static EventBus custom = null;

    private MyEventBus() {
    }

    public static boolean hasInstance() {
        return custom != null;
    }

    public static EventBus getCustom() {
        if (custom == null)
            custom = EventBus.builder()
                    .logNoSubscriberMessages(false)
                    .sendNoSubscriberEvent(false)
                    .build();

        return custom;
    }

}
