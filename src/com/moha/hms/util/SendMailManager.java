package com.moha.hms.util;

import javafx.scene.control.Alert;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

public class SendMailManager {
    public static void sendMail(List<String> mailContent) {
        Properties props = getMailProperties();

        try {
            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication("2002dedsec@gmail.com", "nimi nrec jvxc hhbj");
                }
            });
            session.setDebug(true);
            MimeMessage message = new MimeMessage(session);
            message.setSubject(mailContent.get(0));
            message.setText(mailContent.get(1));
            message.setFrom(new InternetAddress("2002dedsec@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailContent.get(2)));
            message.saveChanges();

            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", "2002dedsec@gmail.com", "nimi nrec jvxc hhbj");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            showAlert(Alert.AlertType.INFORMATION, mailContent.get(3));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Please check your internet connection");
        }
    }

    private static Properties getMailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.user", "2002dedsec@gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        return props;
    }

    private static void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }
}
