package ru.alexeev.tfs.qa.userdatagenerator;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.*;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.generate.*;
import ru.alexeev.tfs.qa.userdatagenerator.io.Writer;
import ru.alexeev.tfs.qa.userdatagenerator.io.WriterExcel;
import ru.alexeev.tfs.qa.userdatagenerator.io.WriterPdfTable;
import ru.alexeev.tfs.qa.userdatagenerator.io.WriterPdfText;
import ru.alexeev.tfs.qa.userdatagenerator.pojo.PojoUser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


// java version: 11.0.2
public class DataGenerator {

    private static final String dataGeneratingSiteUrl = "https://randomuser.me/api/";

    private Attribute[][] data;
    private Map<String, AttributeGeneratorsComposition> attributeGeneratorsCompositions = new HashMap<String, AttributeGeneratorsComposition>();
    private Map<String, AttributeGenerator> attributeGenerators = new HashMap<String, AttributeGenerator>();

    private int personsCount;

    private Map<String, String> attributeNames = new LinkedHashMap<String, String>() {
        {
            put("first-name", "Имя");
            put("last-name", "Фамилия");
            put("patronic-name", "Отчество");
            put("age", "Возраст");
            put("sex", "Пол");
            put("birth-date", "Дата рождения");
            put("inn", "ИНН");
            put("postal-index", "Почтовый индекс");
            put("country", "Страна");
            put("region", "Область");
            put("city", "Город");
            put("street", "Улица");
            put("house", "Дом");
            put("apartment", "Квартира");
        }
    };

    private final String[] sexDependentAttributes = {
            attributeNames.get("first-name"),
            attributeNames.get("last-name"),
            attributeNames.get("patronic-name")
    };

    private final String[] sexDependentAttributesDataFileNamesPrefixes = {
            "first-names-",
            "last-names-",
            "patronic-names-"
    };
    private final String fileNameCountries = "countries.txt";
    private final String fileNameRegions = "regions.txt";
    private final String fileNameCities = "cities.txt";
    private final String fileNameStreets = "streets.txt";

    private final String targetFileNameExcel = "output.xls";
    private final String targetFileNamePdfAsText = "output-text.pdf";
    private final String targetFileNamePdfAsTable = "output-table.pdf";

    private final int yearBoundMin = 1900;
    private final int yearBoundMax = 2020;

    private final int postalIndexBoundMin = 100000;
    private final int postalIndexBoundMax = 200001;

    private final int houseBoundMin = 1;
    private final int houseBoundMax = 31;

    private final int apartmentBoundMin = 1;
    private final int apartmentBoundMax = 81;

    public DataGenerator() {
        Logger.log("data generator: initialize");

        this.initializeAttributeGeneratorsCompositions();
        this.initializeAttributeGenerators();

        this.initializePersonsCount();
        this.initializeData();

        this.initializeAttributeGeneratorsRanges();
        this.initializeAttributeGeneratorsCompositionsValues();
        this.initializeAttributeGeneratorsValues();
    }

    public void generate() {
        Logger.log(String.format("data generator: start generating data, for \"%s\" person(s)", this.personsCount));

        this.createOutputDirectory();

        try {
            Logger.log("data generator: try generate using web site");
            this.generateByInternet();
        } catch (IllegalStateException ex) {
            Logger.log(String.format(
                    "data generator: fail to generate via internet: \"%s\"",
                    ex.getMessage()));
            this.generateByOwnMeans();
        } catch (IOException ex) {
            Logger.log("data generator: internet connection problems, fail to generate via internet");
            this.generateByOwnMeans();
        }

        Logger.log("data generator: finish generating data");
    }

    public void writeDataToExcel() {
        this.writeData(new WriterExcel(Constants.outputFolder.getPath(), this.targetFileNameExcel));
    }

    public void writeDataToPdfAsText() {
        this.writeData(new WriterPdfText(Constants.outputFolder.getPath(), this.targetFileNamePdfAsText));
    }

    public void writeDataToPdfAsTable() {
        this.writeData(new WriterPdfTable(Constants.outputFolder.getPath(), this.targetFileNamePdfAsTable));
    }

    private void generatePersonRow(int rowIndex) {
        Attribute[] currentRow = this.data[rowIndex];

        Attribute sex = (Attribute) this.attributeGenerators.get(this.attributeNames.get("sex")).generate();
        Attribute birthDate = (Attribute) this.attributeGenerators.get(this.attributeNames.get("birth-date")).generate();

        int attributeIndex = 0;

        currentRow[attributeIndex++] = (Attribute) this.attributeGeneratorsCompositions
                .get(this.attributeNames.get("first-name"))
                .generate((Object) sex.toString());
        currentRow[attributeIndex++] = (Attribute) this.attributeGeneratorsCompositions
                .get(this.attributeNames.get("last-name"))
                .generate((Object) sex.toString());
        currentRow[attributeIndex++] = (Attribute) this.attributeGeneratorsCompositions
                .get(this.attributeNames.get("patronic-name"))
                .generate((Object) sex.toString());

        currentRow[attributeIndex++] = (Attribute) this.attributeGenerators
                .get(this.attributeNames.get("age"))
                .generate((Object) birthDate);

        currentRow[attributeIndex++] = sex;  // "sex"
        currentRow[attributeIndex++] = birthDate;  // "birth-date"

        // Simpler attributes
        String[] remainingAttributeKeys = {
                "inn", "postal-index", "country",
                "region", "city", "street",
                "house", "apartment"
        };

        for (String attributeKey : remainingAttributeKeys) {
            Attribute a = (Attribute) this.attributeGenerators
                    .get(this.attributeNames.get(attributeKey))
                    .generate();

            currentRow[attributeIndex++] = a;
        }
    }

    private void initializeAttributeGeneratorsCompositions() {
        for (String sexDependentAttribute : this.sexDependentAttributes) {
            Map<String, AttributeGenerator> attributeGeneratorsCurrent = new HashMap<String, AttributeGenerator>();
            AttributeGeneratorsComposition attributeGeneratorsCompositionCurrent;

            for (String sexValue : Constants.sexValues.values()) {
                attributeGeneratorsCurrent.put(sexValue, new AttributeGeneratorTextByValues());
            }

            attributeGeneratorsCompositionCurrent = new AttributeGeneratorsComposition(attributeGeneratorsCurrent);

            this.attributeGeneratorsCompositions.put(sexDependentAttribute, attributeGeneratorsCompositionCurrent);
        }
    }

    private void initializeAttributeGenerators() {
        this.attributeGenerators.put(this.attributeNames.get("age"), new AttributeGeneratorAge());
        this.attributeGenerators.put(this.attributeNames.get("sex"), new AttributeGeneratorTextByValues());
        this.attributeGenerators.put(this.attributeNames.get("birth-date"), new AttributeGeneratorDateByRange());
        this.attributeGenerators.put(this.attributeNames.get("inn"), new AttributeGeneratorInn());
        this.attributeGenerators.put(this.attributeNames.get("postal-index"), new AttributeGeneratorNumberByRange());
        this.attributeGenerators.put(this.attributeNames.get("country"), new AttributeGeneratorTextByValues());
        this.attributeGenerators.put(this.attributeNames.get("region"), new AttributeGeneratorTextByValues());
        this.attributeGenerators.put(this.attributeNames.get("city"), new AttributeGeneratorTextByValues());
        this.attributeGenerators.put(this.attributeNames.get("street"), new AttributeGeneratorTextByValues());
        this.attributeGenerators.put(this.attributeNames.get("house"), new AttributeGeneratorNumberByRange());
        this.attributeGenerators.put(this.attributeNames.get("apartment"), new AttributeGeneratorNumberByRange());
    }

    private void initializePersonsCount() {
        this.personsCount = Utils.randomInt(Constants.personsCountEdgeLeft, Constants.personsCountEdgeRight);
    }

    private void initializeData() {
        this.data = new Attribute[this.personsCount][this.attributeGenerators.size() + this.attributeGeneratorsCompositions.size()];
    }

    private void initializeAttributeGeneratorsRanges() {
        this.attributeGenerators.get(this.attributeNames.get("birth-date")).initializeRange(
                (Object) new GregorianCalendar(this.yearBoundMin, 0, 1),
                (Object) new GregorianCalendar(this.yearBoundMax, 1, 1)
        );

        this.attributeGenerators.get(this.attributeNames.get("postal-index")).initializeRange(
                (Object) this.postalIndexBoundMin,
                (Object) this.postalIndexBoundMax
        );

        this.attributeGenerators.get(this.attributeNames.get("house")).initializeRange(
                (Object) this.houseBoundMin,
                (Object) this.houseBoundMax
        );

        this.attributeGenerators.get(this.attributeNames.get("apartment")).initializeRange(
                (Object) this.apartmentBoundMin,
                (Object) this.apartmentBoundMax
        );
    }

    private void initializeAttributeGeneratorsCompositionsValues() {
        for (int i = 0; i < this.sexDependentAttributes.length; i++) {
            String sexDependentAttribute = this.sexDependentAttributes[i];
            String sexDependentAttributeDataFileNamePrefix = this.sexDependentAttributesDataFileNamesPrefixes[i];

            for (String sexValue : Constants.sexValues.values()) {
                this.attributeGeneratorsCompositions.get(sexDependentAttribute).get(sexValue).initializeValues(
                        (Object[]) Utils.readSexDependentData(sexValue, sexDependentAttributeDataFileNamePrefix)
                );
            }
        }
    }

    private void initializeAttributeGeneratorsValues() {
        this.attributeGenerators.get(this.attributeNames.get("sex")).initializeValues(
                (Object[]) Constants.sexValues.values().toArray()
        );

        this.attributeGenerators.get(this.attributeNames.get("country")).initializeValues(
                (Object[]) Utils.readResourceFile(this.fileNameCountries)
        );

        this.attributeGenerators.get(this.attributeNames.get("region")).initializeValues(
                (Object[]) Utils.readResourceFile(this.fileNameRegions)
        );

        this.attributeGenerators.get(this.attributeNames.get("city")).initializeValues(
                (Object[]) Utils.readResourceFile(this.fileNameCities)
        );

        this.attributeGenerators.get(this.attributeNames.get("street")).initializeValues(
                (Object[]) Utils.readResourceFile(this.fileNameStreets)
        );
    }

    private String[][] transformDataToString() {
        String[][] result = new String[this.data.length][this.data[0].length];  // at least one row exists

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = this.data[i][j].toString();
            }
        }

        return result;
    }

    private String[] getAttributeNames() {
        Object[] attributeNamesRaw = this.attributeNames.values().toArray();
        String[] attributeNames = new String[attributeNamesRaw.length];

        for (int i = 0; i < attributeNames.length; i++) {
            attributeNames[i] = (String) attributeNamesRaw[i];
        }

        return attributeNames;
    }

    private void writeData(Writer writer) {
        writer.writeHeaders(this.getAttributeNames());
        writer.writeRows(this.transformDataToString());
        writer.save();
    }

    private void createOutputDirectory() {
        Constants.outputFolder.mkdirs();
    }

    private PojoUser[] getPojoUsers() throws IOException, IllegalStateException {
        String urlString = String.format(
                "%s?results=%s&nat=us&inc=gender,name,location,dob,phone,nat",
                DataGenerator.dataGeneratingSiteUrl,
                this.personsCount);

        Logger.log(String.format("data generator: going to connect to \"%s\"", urlString));

        URL url = new URL(urlString);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Content-Type", "application/json");

        int responseCode = con.getResponseCode();

        Logger.log(String.format("data generator: response code \"%s\"", responseCode));

        if (responseCode != HttpURLConnection.HTTP_OK) {
            String message = "response code not Ok, exit connection";

            Logger.log(String.format("data generator: %s", message));

            throw new IOException(message);
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        String jsonResponseString = response.body().string();
        Object responseInfo = new JsonParser().parse(jsonResponseString);
        JsonObject responseObject = (JsonObject) responseInfo;
        String results = (String) responseObject.get("results").toString();
        PojoUser[] pojoUsers = PojoUser.toArray(results);

        if (pojoUsers.length != this.personsCount) {
            throw new IllegalStateException(String.format(
                    "Got \"%s\" users, expected \"%s\"",
                    pojoUsers.length,
                    this.personsCount));
        }

        // TODO: is required or not?
        // con.disconnect();

        return pojoUsers;
    }

    private void transformPojoUser(PojoUser[] pojoUsers, int index) {
        Attribute[] currentRow = this.data[index];
        PojoUser currentUser = pojoUsers[index];

        int attributeIndex = 0;

        currentRow[attributeIndex++] = (Attribute) currentUser.getFirstName();
        currentRow[attributeIndex++] = (Attribute) currentUser.getLastName();

        // api doesn't generate patronic names
        AttributeText patronicName = currentUser.getPatronicName();
        currentRow[attributeIndex++] = (Attribute) (patronicName != null ? patronicName : new AttributeText(""));

        currentRow[attributeIndex++] = (Attribute) currentUser.getAge();
        currentRow[attributeIndex++] = (Attribute) currentUser.getGender();

        // in case date format in api changes and date parsing crashes
        AttributeDate birthDateProbablyNull = currentUser.getBirthDate();
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.YEAR, -1 * currentUser.getAge().getValue());
        AttributeDate birthDatePredictedIfActualOneIsNull = new AttributeDate(calendar);
        currentRow[attributeIndex++] = (Attribute) (birthDateProbablyNull != null ? birthDateProbablyNull : birthDatePredictedIfActualOneIsNull);

        // api doesn't generate INN
        AttributeInn inn = currentUser.getInn();
        currentRow[attributeIndex++] = (Attribute) (
                inn != null ? inn : this.attributeGenerators.get(this.attributeNames.get("inn")).generate());

        currentRow[attributeIndex++] = (Attribute) currentUser.getPostalIndex();
        currentRow[attributeIndex++] = (Attribute) currentUser.getCountry();
        currentRow[attributeIndex++] = (Attribute) currentUser.getRegion();
        currentRow[attributeIndex++] = (Attribute) currentUser.getCity();
        currentRow[attributeIndex++] = (Attribute) currentUser.getStreet();

        // this one is also null
        AttributeNumber house = currentUser.getHouse();
        currentRow[attributeIndex++] = (Attribute) (
                house != null ? house : this.attributeGenerators.get(this.attributeNames.get("house")).generate());

        // this one too
        AttributeNumber apartment = currentUser.getApartment();
        currentRow[attributeIndex++] = (Attribute) (
                apartment != null ? apartment : this.attributeGenerators.get(this.attributeNames.get("apartment")).generate());
    }

    private void transformPojoUsers(PojoUser[] pojoUsers) {
        for (int i = 0; i < this.personsCount; i++) {
            this.transformPojoUser(pojoUsers, i);
        }
    }

    private void generateByOwnMeans() {
        Logger.log("data generator: generate using own means (resource files and random choice)");

        for (int i = 0; i < this.personsCount; i++) {
            this.generatePersonRow(i);
        }
    }

    private void generateByInternet() throws IOException {
        PojoUser[] pojoUsers = this.getPojoUsers();
        this.transformPojoUsers(pojoUsers);
    }
}
