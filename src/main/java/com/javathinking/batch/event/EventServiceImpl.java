package com.javathinking.batch.event;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author paul
 */
public class EventServiceImpl implements EventService {

    private static Logger log = Logger.getLogger(EventServiceImpl.class);
    private Map<Class, NotificationHandler> notificationHandlers = new HashMap<Class, NotificationHandler>();
    private EventDao eventDao;

    protected EventServiceImpl() {

    }

    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public static void main(String[] args) {
        System.out.println("123" == String.valueOf(123));
    }

    /**
     * Logs the event and if the event has one or more notifications, then the notification
     * is processed by the associated notification handler.
     *
     * @param event
     */
    public void send(Event event) {
        eventDao.save(event);
        log.info(event.toString());
        NotificationHandler handler = null;
        if (event.hasNotification()) {
            for (Notification notification : event.getNotifications()) {
                handler = notificationHandlers.get(notification.getClass());
                if (handler != null) {
                    try {
                        if (!handler.process(notification)) {
                            // TODO what should be done if this returns false?
                            log.warn("Handler did not process notification : " + notification);
                        }
                    } catch (Exception e) {
                        log.error("Notification could not be processed : " + notification, e);
                    }
                }
            }
        }
    }

    public List<Event> list(Date from, Date to) {
        return eventDao.list(from, to);
    }

    public List<Event> list(String name, String messageContains) {
        return eventDao.list(name, messageContains);
    }

    public void registerNotificationHandler(Class cls, NotificationHandler nh) {
        notificationHandlers.put(cls, nh);


    }

    class X extends Thread {
        public native String getTime();
    }

}
