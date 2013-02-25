package com.javathinking.batch.event;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.MimeMessage;

/**
 * @author paul
 */
public class EmailNotificationHandlerSpringVelocityImpl implements NotificationHandler<EmailNotification> {

    private static final Logger log = Logger.getLogger(EmailNotificationHandlerSpringVelocityImpl.class);
    private JavaMailSender mailSender;
    private VelocityEngine velocityEngine;

    /**
     * Sends the email.
     * <p/>
     * If the notification specifies a velocity template, then that template will be
     * merged with the notification parameters and the result will form the body
     * of the email - otherwise, the notification content will be used as the body.
     */
    public boolean process(final EmailNotification notification) {
        log.debug(notification);
        if (mailSender != null) {
            MimeMessagePreparator preparator = new MimeMessagePreparator() {

                public void prepare(MimeMessage mimeMessage) throws Exception {
                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, notification.hasAttachments());
                    for (String to : notification.getTo()) {
                        message.addTo(to);
                    }
                    for (String name : notification.getAttachments().keySet()) {
                        message.addAttachment(name, notification.getAttachments().get(name));
                    }

                    message.setFrom(notification.getFrom());
                    message.setSubject(notification.getSubject());
                    String text = notification.getContent();
                    if (StringUtils.isNotBlank(notification.getTemplate())) {
                        text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, notification.getTemplate(), notification.getParameters());
                    }
                    message.setText(text, true);

                }
            };
            try {
                this.mailSender.send(preparator);
                return true;
            } catch (MailSendException mse) {
                log.error("Could not send email :" + notification, mse);
            }
        }
        return false;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
}
