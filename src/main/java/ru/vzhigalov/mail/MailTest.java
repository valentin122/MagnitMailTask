package ru.vzhigalov.mail;

import ru.vzhigalov.servise.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.search.FlagTerm;

public class MailTest {
    public List<String> getUnReadMessagesSubjects() throws MessagingException {
        Config config = new Config();
        config.init();
        String user = config.get("mail.login");
        String pass = config.get("mail.password");
        String protocol = config.get("mail.protocol");
        String host = config.get("mail.server");
        String port = config.get("mail.port");

        // Получить системные свойства
        Properties props = System.getProperties();
        props.put("mail.imap.host", host);
        props.put("mail.imap.port", port);
        props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.imap.socketFactory.fallback", "false");
        props.put("mail.debug", "false");

        // Настроить аутентификацию, получить session

        Authenticator auth = new EmailAuthenticator(user, pass);
        Session session = Session.getDefaultInstance(props, auth);

        // Получить store
        Store store = session.getStore(protocol);
        try {
            store.connect();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // Получить folder
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);

        // Получить каталог
        Message[] unreadMessages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        List<String> subjects = new ArrayList<>();
        for (int i = 0; i < unreadMessages.length; i++) {
            subjects.add(unreadMessages[i].getSubject());
            unreadMessages[i].setFlag(Flags.Flag.SEEN, true);
        }

        // Закрыть соединение
        folder.close(false);
        store.close();
        System.out.println(subjects);
        return subjects;
    }
}
