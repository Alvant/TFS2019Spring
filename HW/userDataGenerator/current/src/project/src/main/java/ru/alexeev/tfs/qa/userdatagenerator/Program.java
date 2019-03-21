package ru.alexeev.tfs.qa.userdatagenerator;


import ru.alexeev.tfs.qa.userdatagenerator.database.DatabaseHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;


public class Program {

    public static void main(String[] args) throws
            MalformedURLException,
            IOException,
            ProtocolException,
            java.sql.SQLException,
            java.text.ParseException {

        DataGenerator dataGenerator = new DataGenerator();

        dataGenerator.generate();

        dataGenerator.writeDataToExcel();
        dataGenerator.writeDataToPdfAsText();
        dataGenerator.writeDataToPdfAsTable();

        System.exit(0);
    }
}
