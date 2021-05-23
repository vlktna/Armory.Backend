package blur.tech.armory.dto.service;

import blur.tech.armory.ArmoryApplication;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

@Service
public class MailSender {

    public void sendEmail(String mailTo, String subject, String text) throws IOException, MessagingException {
        Properties prop = new Properties();
        prop.load(ArmoryApplication.class.getClassLoader().getResourceAsStream("mail.properties"));

        Session mailSession = Session.getDefaultInstance(prop);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress("bigarmory@yandex.ru"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
        message.setSubject(subject);
        message.setText(text);

        Transport tr = mailSession.getTransport();
        tr.connect("bigarmory@yandex.ru", "bigarmory1q2w3e");
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }

}
