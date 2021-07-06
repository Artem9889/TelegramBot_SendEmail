package mail;

import bot.TelegramBotSetting;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class EmailSetting {

    private String username;
    private String password;
    private Properties properties;

    public EmailSetting(String username, String password) {
        this.username = username;
        this.password = password;

        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
    }

    public void send(String subject, String fromEmail, String toEmail) {
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);

            //від кого лист
            message.setFrom(new InternetAddress(fromEmail));
            //кому лист
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //тема листа
            message.setSubject(subject);
            //вміст листа
            message.setText(TelegramBotSetting.text);
            //Отправка листа

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
