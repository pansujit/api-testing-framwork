package com.glide.qa.backend.common.util;

import java.io.File;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * This class contains all logic about sending emails.
 *
 * @author sujitpandey
 *
 */
public class EmailSender {
  private EmailSender() {

  }

  /**
   * This method contains logic to send email.
   */
  public static void emailSender() {
    final String username = "abc@yahoo.com"; // change accordingly
    final String password = "1Xaaaaaa"; // change accordingly

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props,
        new javax.mail.Authenticator() {
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
        });

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));

      message.setRecipients(Message.RecipientType.TO,
          InternetAddress.parse("abc@yahoo.com"));
      message.setRecipients(Message.RecipientType.CC,
          InternetAddress.parse("abc@yahoo.com"));
      message.setSubject("Test email with attachment");
      message.setText("This is a test email with an attachment");


      File folder = new File("target/test-reports/automationReport");
      File[] attachments = folder.listFiles();

      MimeBodyPart messageBodyPart = new MimeBodyPart();
      Multipart multipart = new MimeMultipart();
      messageBodyPart.setText("This is message body");
      multipart.addBodyPart(messageBodyPart);

      for (File attachment : attachments) {
        messageBodyPart = new MimeBodyPart();
        messageBodyPart.attachFile(attachment);
        multipart.addBodyPart(messageBodyPart);
      }

      message.setContent(multipart);

      Transport.send(message);


    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

