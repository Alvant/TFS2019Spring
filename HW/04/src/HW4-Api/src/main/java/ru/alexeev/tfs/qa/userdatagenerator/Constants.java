package ru.alexeev.tfs.qa.userdatagenerator;


import java.io.File;
import java.util.Map;
import java.util.HashMap;


public class Constants {

    public final static String fontFileNameRegular = "OpenSans-Regular.ttf";
    public final static String fontFileNameBold = "OpenSans-Bold.ttf";

    public final static String fontsFolderName = "fonts";
    public final static File fontsFolder = new File(Constants.projectFolderPath, Constants.fontsFolderName);

    public final static Map<String, String> sexValues = new HashMap<String, String>() {
        {
            put("male", "М");
            put("female", "Ж");
        }
    };

    public final static String undefinedSexValue = "Н";
    public final static String undefinedCountry = "Unknown";

    protected final static String projectFolderPath = System.getProperty("user.dir");

    protected final static File outputFolder = new File(Constants.projectFolderPath, "output");
    protected final static File resourcesFolder = new File(Constants.projectFolderPath, "resources");

    protected final static int MAX_NUMBER_OF_LINES_IN_RESOURCE_FILE = 50;

    protected final static int personsCountEdgeLeft = 1;
    protected final static int personsCountEdgeRight = 30;
}
