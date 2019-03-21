package ru.alexeev.tfs.qa.userdatagenerator;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.*;
import ru.alexeev.tfs.qa.userdatagenerator.attribute.generate.*;
import ru.alexeev.tfs.qa.userdatagenerator.database.DatabaseAddress;
import ru.alexeev.tfs.qa.userdatagenerator.database.DatabaseHandler;
import ru.alexeev.tfs.qa.userdatagenerator.database.DatabaseUser;
import ru.alexeev.tfs.qa.userdatagenerator.io.Writer;
import ru.alexeev.tfs.qa.userdatagenerator.io.WriterExcel;
import ru.alexeev.tfs.qa.userdatagenerator.io.WriterPdfTable;
import ru.alexeev.tfs.qa.userdatagenerator.io.WriterPdfText;
import ru.alexeev.tfs.qa.userdatagenerator.pojo.PojoUser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


// java version: 11.0.2
public class DataGenerator {

    private static final String dataGeneratingSiteUrl = "https://randomuser.me/api/";

    private DatabaseHandler databaseHandler;

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

    private Map<String, Integer> attributeIndices = new LinkedHashMap<String, Integer>() {
        {
            put("first-name", 0);
            put("last-name", 1);
            put("patronic-name", 2);
            put("age", 3);
            put("sex", 4);
            put("birth-date", 5);
            put("inn", 6);
            put("postal-index", 7);
            put("country", 8);
            put("region", 9);
            put("city", 10);
            put("street", 11);
            put("house", 12);
            put("apartment", 13);
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

    public DataGenerator() throws java.sql.SQLException {
        Logger.log("data generator: initialize");

        this.initializeAttributeGeneratorsCompositions();
        this.initializeAttributeGenerators();

        this.initializePersonsCount();
        this.initializeData();

        this.connectToDatabase();

        this.initializeAttributeGeneratorsRanges();
        this.initializeAttributeGeneratorsCompositionsValues();
        this.initializeAttributeGeneratorsValues();
    }

    public void generate() throws java.sql.SQLException, java.text.ParseException {
        Logger.log(String.format("data generator: start generating data, for \"%s\" person(s)", this.personsCount));

        this.createOutputDirectory();

        try {
            Logger.log("data generator: try generate using web site");
            this.generateByInternet();
            this.writeDataToDatabase();
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

    private void connectToDatabase() throws java.sql.SQLException {
        this.databaseHandler = new DatabaseHandler();
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

    private DatabaseUser[] getDatabaseUsers() throws java.sql.SQLException {
        return this.databaseHandler.selectRandomUsers(this.personsCount);
    }

    private void transformUser(IUser[] users, int index) {
        Attribute[] currentRow = this.data[index];
        IUser currentUser = users[index];

        int attributeIndex = 0;

        currentRow[attributeIndex++] = (Attribute) currentUser.getFirstName();
        currentRow[attributeIndex++] = (Attribute) currentUser.getLastName();

        // in case no patronic name
        AttributeText patronicName = currentUser.getPatronicName();
        currentRow[attributeIndex++] = (Attribute) (patronicName != null ? patronicName : new AttributeText(""));

        currentRow[attributeIndex++] = (Attribute) currentUser.getAge();
        currentRow[attributeIndex++] = (Attribute) currentUser.getGender();

        // in case date format in api changes and date parsing crashes
        AttributeDate birthDate = currentUser.getBirthDate();
        currentRow[attributeIndex++] = (Attribute) (birthDate);

        // in case no INN
        AttributeInn inn = currentUser.getInn();
        currentRow[attributeIndex++] = (Attribute) (
                inn != null ? inn : this.attributeGenerators.get(this.attributeNames.get("inn")).generate());

        currentRow[attributeIndex++] = (Attribute) currentUser.getPostalIndex();
        currentRow[attributeIndex++] = (Attribute) currentUser.getCountry();
        currentRow[attributeIndex++] = (Attribute) currentUser.getRegion();
        currentRow[attributeIndex++] = (Attribute) currentUser.getCity();
        currentRow[attributeIndex++] = (Attribute) currentUser.getStreet();

        // this one may alos be null
        AttributeNumber house = currentUser.getHouse();
        currentRow[attributeIndex++] = (Attribute) (
                house != null ? house : this.attributeGenerators.get(this.attributeNames.get("house")).generate());

        // this one too
        AttributeNumber apartment = currentUser.getApartment();
        currentRow[attributeIndex++] = (Attribute) (
                apartment != null ? apartment : this.attributeGenerators.get(this.attributeNames.get("apartment")).generate());
    }

    private void transformPojoUser(PojoUser[] pojoUsers, int index) {
        this.transformUser(pojoUsers, index);
    }

    private void transformDatabaseUser(DatabaseUser[] databaseUsers, int index) {
        this.transformUser(databaseUsers, index);
    }

    private void transformPojoUsers(PojoUser[] pojoUsers) {
        for (int i = 0; i < this.personsCount; i++) {
            this.transformPojoUser(pojoUsers, i);
        }
    }

    private void transformDatabaseUsers(DatabaseUser[] databaseUsers) {
        for (int i = 0; i < this.personsCount; i++) {
            this.transformDatabaseUser(databaseUsers, i);
        }
    }

    private void generateByOwnMeans() throws java.sql.SQLException {
        Logger.log("data generator: generate using own means (database, if not empty, or resource files)");

        if (!this.isDatabaseEmpty()) {
            this.generateByDatabase();
        } else {
            Logger.log("data generator: database seems to be empty :(");
            this.generateByResourceFiles();
        }
    }

    private void generateByResourceFiles() {
        Logger.log("data generator: generate users " +
                "using resource files and random choice");

        for (int i = 0; i < this.personsCount; i++) {
            this.generatePersonRow(i);
        }
    }

    private void generateByInternet() throws IOException {
        Logger.log("data generator: generate users using web api");

        PojoUser[] pojoUsers = this.getPojoUsers();
        this.transformPojoUsers(pojoUsers);
    }

    private void generateByDatabase() throws java.sql.SQLException {
        Logger.log("data generator: select users from database");

        DatabaseUser[] databaseUsers = this.getDatabaseUsers();
        this.reducePersonsCountIfNeeded(databaseUsers.length);

        this.transformDatabaseUsers(databaseUsers);
    }

    private void writeDataToDatabase() throws java.sql.SQLException, ParseException {
        for (int i = 0; i < this.data.length; i++) {
            Attribute[] currentRow = data[i];

            Date birthdate = new SimpleDateFormat(
                    AttributeDate.getDateFormat()).parse(
                            currentRow[this.attributeIndices.get("birth-date")].toString()
            );
            String sex = currentRow[this.attributeIndices.get("sex")].toString() == Constants.sexValues.get("female") ?
                    DatabaseUser.getSexFemale() :
                    DatabaseUser.getSexMale();

            String patronicName = currentRow[this.attributeIndices.get("patronic-name")].toString();

            if (patronicName == "") {
                patronicName = null;
            }

            DatabaseUser user = new DatabaseUser(
                    currentRow[this.attributeIndices.get("last-name")].toString(),
                    currentRow[this.attributeIndices.get("first-name")].toString(),
                    patronicName,
                    birthdate,
                    sex,
                    currentRow[this.attributeIndices.get("inn")].toString()
            );
            DatabaseAddress address = new DatabaseAddress(
                    currentRow[this.attributeIndices.get("postal-index")].toString(),
                    currentRow[this.attributeIndices.get("country")].toString(),
                    currentRow[this.attributeIndices.get("region")].toString(),
                    currentRow[this.attributeIndices.get("city")].toString(),
                    currentRow[this.attributeIndices.get("street")].toString(),
                    Integer.parseInt(currentRow[this.attributeIndices.get("house")].toString()),
                    Integer.parseInt(currentRow[this.attributeIndices.get("apartment")].toString())
            );

            if (this.databaseHandler.isUserExists(
                    user.getLastName().toString(),
                    user.getFirstName().toString(),
                    patronicName == null ? patronicName : user.getPatronicName().toString())) {
                this.databaseHandler.updateUser(user, address);
            } else {
                this.databaseHandler.insertUser(user, address);
            }
        }
    }

    private boolean isDatabaseEmpty() throws java.sql.SQLException {
        return this.databaseHandler.isDatabaseEmpty();
    }

    private void reducePersonsCountIfNeeded(int newPersonsCount) {
        if (newPersonsCount < this.personsCount) {
            this.personsCount = newPersonsCount;
            this.reduceData(newPersonsCount);
        } else if (newPersonsCount == this.personsCount) {
            return;
        } else {
            throw new IllegalArgumentException(String.format(
                    "New personsCount \"%d\" is bigger than current value \"%d\"",
                    newPersonsCount, this.personsCount));
        }
    }

    private void reduceData(int newPersonsCount) {
        Attribute[][] newData = new Attribute[this.personsCount][this.data[0].length];
        System.arraycopy(
                this.data, 0,
                newData, 0,
                newPersonsCount
        );
        this.data = newData;
    }
}
