public class Program {

    public static void main(String[] args) {
        DataGenerator dataGenerator = new DataGenerator();

        dataGenerator.generate();

        dataGenerator.writeDataToExcel();
        dataGenerator.writeDataToPdfAsText();
        dataGenerator.writeDataToPdfAsTable();
    }
}
