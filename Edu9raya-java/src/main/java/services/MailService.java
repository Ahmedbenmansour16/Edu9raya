package services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class MailService {
    // IMPORTANT: For Gmail, use an App Password, not your regular password!
    // See: https://myaccount.google.com/apppasswords
    private static final String EMAIL = "edu9raya@gmail.com";
    private static final String PASSWORD = "YOUR_APP_PASSWORD_HERE"; // <-- Replace with your Gmail App Password

    public static void sendMail(String recipient, String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
        } catch (AuthenticationFailedException e) {
            System.err.println("Authentication failed: Please check your Gmail address and App Password. See https://myaccount.google.com/apppasswords");
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
} 