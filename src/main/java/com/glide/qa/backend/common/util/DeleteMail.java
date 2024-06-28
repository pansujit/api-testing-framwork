package com.glide.qa.backend.common.util;

import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

/**
 * This class contains all logic about delete emails.
 *
 * @author sujitpandey
 *
 */
public class DeleteMail {

  /**
   * This method contains the logic for delete emails.
   *
   */
  public static void cleanup() {

    String userName = "abc@yahoo.com"; // change accordingly
    String password = "1Xaaaaaa"; // change accordingly
    Properties properties = new Properties();

    String host = "imap.gmail.com";
    String port = "993";

    // server setting
    properties.put("mail.imap.host", host);
    properties.put("mail.imap.port", port);

    // SSL setting
    properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    properties.setProperty("mail.imap.socketFactory.fallback", "false");
    properties.setProperty("mail.imap.socketFactory.port", String.valueOf(port));

    Session session = Session.getDefaultInstance(properties);

    try {
      // connects to the message store
      Store store = session.getStore("imap");
      store.connect(userName, password);

      // opens the inbox folder
      Folder folderInbox = store.getFolder("INBOX");
      folderInbox.open(Folder.READ_WRITE);

      // opens the trash folder
      Folder folderBin = store.getFolder("[Gmail]/Corbeille");
      folderBin.open(Folder.READ_WRITE);

      // fetches new messages from server
      Message[] arrayMessages = folderInbox.getMessages();

      // Copy messages from inbox to Trash
      folderInbox.copyMessages(arrayMessages, folderBin);

      arrayMessages = folderBin.getMessages();

      for (int i = 0; i < arrayMessages.length; i++) {
        Message message = arrayMessages[i];
        message.setFlag(Flags.Flag.DELETED, true);

      }

      // expunges the folder to remove messages which are marked deleted
      boolean expunge = true;
      folderBin.close(expunge);
      folderInbox.close(expunge);

      // disconnect
      store.close();
    } catch (Exception ex) {

      ex.printStackTrace();
    }
  }

  public static void main(String[] args) {
    cleanup();
  }
}
