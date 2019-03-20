import java.io.File;
import java.util.Map;
import java.util.HashMap;


public class Constants {

    // Set the field "true" if want to build JAR!
    protected static boolean isJar = true;

    protected static String projectFolderPath = System.getProperty("user.dir");
//            new File(Program.class
//            .getProtectionDomain()
//            .getCodeSource()
//            .getLocation()
//            .toString()  // toURI()
//    ).getParent();

    protected static File outputFolder = new File(Constants.projectFolderPath, "output");
    protected static File resourcesFolder = new File(Constants.projectFolderPath, "resources");

    protected static String fontsFolderName = "fonts";
    protected static File fontsFolder = new File(Constants.projectFolderPath, Constants.fontsFolderName);
    protected static String fontFileNameRegular = "OpenSans-Regular.ttf";
    protected static String fontFileNameBold = "OpenSans-Bold.ttf";

    protected static int MAX_NUMBER_OF_LINES_IN_RESOURCE_FILE = 50;

    protected static Map<String, String> sexValues = new HashMap<String, String>() {
        {
            put("male", "М");
            put("female", "Ж");
        }
    };

    protected static int personsCountEdgeLeft = 1;
    protected static int personsCountEdgeRight = 30;
}
