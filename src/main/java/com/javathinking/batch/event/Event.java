package com.javathinking.batch.event;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author paul
 */
@Entity
public class Event implements Serializable {
    @Column(length = 200)
    private String name;
    @Column(length = 2000)
    private String message;
    @Column(length = 20)
    private String discriminator;
    @Column(length = 100)
    private String thread;
    @Transient
    private List<Notification> notifications = new ArrayList();


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp = new Date();

    public Event() {
        thread = Thread.currentThread().getName();
    }

    public Event(String type, String message, String discriminator) {
        this();
        this.name = type;
        this.message = message;
        this.discriminator = discriminator;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getDiscriminator() {
        return discriminator;
    }


    public void addNotification(Notification notification) {
        this.notifications.add(notification);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }


    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }


    boolean hasNotification() {
        return !notifications.isEmpty();
    }

    public List<Notification> getNotifications() {
        return notifications;
    }


    @Override
    public String toString() {
        return "Event -> " + name + " / " + message + " / " + discriminator + " / " + notifications.size() + " notifications";
    }


}
