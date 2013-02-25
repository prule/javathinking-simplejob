package com.javathinking.batch.event;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author paul
 */
public class EmailNotification implements Notification {
    private List<String> to = new ArrayList<String>();
    private String from;
    private String subject;
    private String content;
    private String template;
    private Map parameters = new HashMap<String, Object>();
    private Map<String, File> attachments = new HashMap<String, File>();

    public List<String> getTo() {
        return to;
    }

    public void addTo(String email) {
        to.add(email);
    }

    public void addAttachment(String name, File obj) {
        attachments.put(name, obj);
    }

    public void addParameter(String name, Object obj) {
        parameters.put(name, obj);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public Map<String, File> getAttachments() {
        return attachments;
    }

    public boolean hasAttachments() {
        return !attachments.isEmpty();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
