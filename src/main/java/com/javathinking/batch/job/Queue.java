package com.javathinking.batch.job;

import com.javathinking.batch.job.TaskResult.Result;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author paul
 */
@Entity
public class Queue implements Serializable {


    @Transient
    private XStream xstream = new XStream(new DomDriver());
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();
    private String type;
    private String discriminator;
    @Column(length = 2000)
    private String content;
    @Column
    @Enumerated(EnumType.STRING)
    private Result endresult;
    @Transient
    private Object data;

    @Column
    private Long triggeredBy;

    public Queue(String type, String discriminator, Object data, Result result, Queue triggeredBy) {
        this.type = type;
        this.data = data;
        this.discriminator = discriminator;
        this.endresult = result;
        this.triggeredBy = triggeredBy != null ? triggeredBy.getId() : null;
    }

    public Queue() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @PrePersist
    public void prepersist() {
        this.content = xstream.toXML(data);
    }

    @PostLoad
    public void postload() {
        if (content != null) {
            this.data = xstream.fromXML(content);
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Result getEndresult() {
        return endresult;
    }

    public void setEndresult(Result endresult) {
        this.endresult = endresult;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(Long triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public Object getData() {
        return data;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Queue)) {
            return false;
        }
        Queue other = (Queue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.javathinking.simplejob.Queue[id=" + id + "]";
    }
}
