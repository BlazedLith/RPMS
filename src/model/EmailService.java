package model;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import model.Notifiable;
//The function to send Email
public class EmailService implements Notifiable{
    public static void sendEmail(String toEmail, String subject, String body) {
        final String fromEmail = "wasiqamir3@gmail.com";
        final String password = "pxgm zogn vifv dbym";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject(subject);
            msg.setText(body);

            Transport.send(msg);
            UIHelper.showInfo("Email sent successfully to " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
