package com.javathinking.batch.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows you to register named example EmailNotifications (a template) - which you can then ask for by name,
 * and get a new instance, with the properties copied over from the template
 *
 * @author paul
 */
public class EmailNotificationFactory {

    private Map<String, EmailNotification> typedefs = new HashMap();

    /**
     * Registers a new template email with the given name
     */
    public void addType(String name, EmailNotification example) {
        typedefs.put(name, example);
    }

    /**
     * Returns a copy of the named template
     */
    public EmailNotification get(String name) {
        EmailNotification example = typedefs.get(name);
        EmailNotification en = new EmailNotification();
        if (example != null) {
            en.setContent(example.getContent());
            en.setFrom(example.getFrom());
            en.setSubject(example.getSubject());
            for (String t : example.getTo()) {
                en.addTo(t);
            }
            for (String k : example.getParameters().keySet()) {
                en.addParameter(k, example.getParameters().get(k));
            }
        }
        return en;
    }
}
