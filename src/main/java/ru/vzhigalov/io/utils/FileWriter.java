package ru.vzhigalov.io.utils;

import java.io.*;
import java.util.List;

public class FileWriter {

    public void writeSubjectsToFile(List<String> subjectNames) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("ListSubject.txt");
            for (String subject: subjectNames) {
                String preparedString = String.format("%s%s", subject, System.lineSeparator());
                fileOutputStream.write(preparedString.getBytes());
            }
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println("Error write file");
            e.printStackTrace();
        }
    }
}
