package ru.vzhigalov;

import ru.vzhigalov.io.utils.FileWriter;
import ru.vzhigalov.dao.Dao;
import ru.vzhigalov.mail.MailTest;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws MessagingException, SQLException {

        MailTest mailTest = new MailTest();
        FileWriter fileWriter = new FileWriter();
        Dao dao = new Dao();

        List<String> subjects = mailTest.getUnReadMessagesSubjects();

        dao.initConfigConnectionToDb();
        dao.dbCreateAndClear();
        dao.dbInsert(subjects);
        List<String> subjectsFromDb = dao.getDataFromDb();

        fileWriter.writeSubjectsToFile(subjectsFromDb);

    }
}
