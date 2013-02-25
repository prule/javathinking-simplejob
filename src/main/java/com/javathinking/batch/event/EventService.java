package com.javathinking.batch.event;

import java.util.Date;
import java.util.List;

/**
 * @author paul
 */
public interface EventService {

    void send(Event event);

    List<Event> list(Date from, Date to);

    List<Event> list(String name, String messageContains);

    void registerNotificationHandler(Class cls, NotificationHandler nh);

}
