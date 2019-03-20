package ru.alexeev.tfs.qa.userdatagenerator;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;


public class Program {

    public static void main(String[] args) throws MalformedURLException, IOException, ProtocolException {
        DataGenerator dataGenerator = new DataGenerator();

        dataGenerator.generate();

        dataGenerator.writeDataToExcel();
        dataGenerator.writeDataToPdfAsText();
        dataGenerator.writeDataToPdfAsTable();

        System.exit(0);
    }
}
