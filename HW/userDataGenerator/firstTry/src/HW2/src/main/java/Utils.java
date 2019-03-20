import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;


public class Utils {

    private static Random generator = new Random();

    static public int randomInt(int bound) {
        return generator.nextInt(bound);
    }

    static public int randomInt(int boundLower, int boundUpper) {
        return Utils.randomInt(boundUpper - boundLower) + boundLower;
    }

    static public double randomGaussian(double mean, double std) {
        return generator.nextGaussian() * std + mean;
    }

    static public int[] concatenateArraysInt(int[] arrayA, int[] arrayB) {
        int[] arrayResult = new int[arrayA.length + arrayB.length];
        int[][] arraysToConcatenate = { arrayA, arrayB };
        int previouslyConcatenatedArraysCumulativeLength = 0;

        for (int i = 0; i < arraysToConcatenate.length; i++) {
            System.arraycopy(
                    arraysToConcatenate[i], 0,
                    arrayResult, previouslyConcatenatedArraysCumulativeLength,
                    arraysToConcatenate[i].length
            );

            previouslyConcatenatedArraysCumulativeLength += arraysToConcatenate[i].length;
        }

        return arrayResult;
    }

    static public int multiplyArraysInt(int[] arrayA, int[] arrayB) {
        if (arrayA.length != arrayB.length) {
            throw new IllegalArgumentException(String.format("Array lengths not equal"));
        }

        int result = 0;

        for (int i = 0; i < arrayA.length; i++) {
            result += arrayA[i] * arrayB[i];
        }

        return result;
    }

    static protected String[] readSexDependentData(String sex, String fileNamePrefix) {
        String fileNamePostfix = ".txt";

        if (sex == Constants.sexValues.get("female")) {
            return Utils.readResourceFile(fileNamePrefix + "female" + fileNamePostfix);
        } else if (sex == Constants.sexValues.get("male")) {
            return Utils.readResourceFile(fileNamePrefix + "male" + fileNamePostfix);
        } else {
            throw new IllegalArgumentException("Unknown sex value");
        }
    }

    static protected String[] readResourceFile(String fileName) {
        String[] linesAll = new String[Constants.MAX_NUMBER_OF_LINES_IN_RESOURCE_FILE];
        String[] linesNotEmpty;
        int lineNumber = 0;

        Scanner scanner = Utils.createScanner(fileName);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            linesAll[lineNumber++] = line;
        }

        linesNotEmpty = Utils.getAllLinesTillFirstEmptyOne(linesAll, lineNumber);

        return linesNotEmpty;
    }

    static private String[] getAllLinesTillFirstEmptyOne(String[] lines, int notEmptyLinesCount) {
        String[] notEmptyLines = new String[notEmptyLinesCount];

        for (int i = 0; i < notEmptyLinesCount; i++) {
            notEmptyLines[i] = lines[i];
        }

        return notEmptyLines;
    }

    static private Scanner createScanner(String fileName) {
        Scanner scanner;
        File resourceFile = new File(Constants.resourcesFolder, fileName);

        if (resourceFile.isDirectory()) {
            throw new IllegalArgumentException(
                    String.format("\"%s\" is not a resource file but a directory", resourceFile.getPath()));
        }

        try {
            scanner = new Scanner(resourceFile, "UTF-8");
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(
                    String.format("\"%s\" resource file not exists", resourceFile.getPath()));
        }

        return scanner;
    }
}
