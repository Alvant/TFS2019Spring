/*
    Сгенерить данные для 1-30 человек (количество берется определяется в программе рандомно).
    Все текстовые данные на русском языке

    Заголовки таблицы - название данных пользователей.
    Порядок заголовков:
        Имя,
        фамилия,
        отчество,
        возраст,
        пол (М или Ж),
        дата рождения,
        Инн,
        почтовый индекс,
        страна,
        область,
        город,
        улица,
        дом,
        квартира

    Добавить в проект файлы формата .txt в папку resources
    для генерации следующих атрибутов:
        Имя,
        фамилия,
        отчество,
        Страна,
        область,
        город,
        улица.
    Каждый файл должен содержать список значений,
    которые будут использоваться для заполнения таблицы.
    Для каждого атрибута отдельный файл.
    Пример: вы создали файл «Countries.txt», внутри него информация выглядит
        Россия
        Белорусь
        США
        Китай
        Индия
    Количество значений в файле - не менее 30.
    Адрес, который вы генерите может быть несуществующим.

    Имя, Фамилия и Отчество должны соответствовать полу.
    Для каждого пола можно создавать отдельные файлы с атрибутами.

    Возраст должен считаться исходя из даты рождения.

    Индекс - рандомное значение в диапазоне от 100000 до 200000
 */


import java.util.GregorianCalendar;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class DataGenerator {

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

    private String[] sexDependentAttributes = {
            attributeNames.get("first-name"),
            attributeNames.get("last-name"),
            attributeNames.get("patronic-name")
    };

    private String[] sexDependentAttributesDataFileNamesPrefixes = {
            "first-names-",
            "last-names-",
            "patronic-names-"
    };
    private String fileNameCountries = "countries.txt";
    private String fileNameRegions = "regions.txt";
    private String fileNameCities = "cities.txt";
    private String fileNameStreets = "streets.txt";

    private String targetFileNameExcel = "output.xls";
    private String targetFileNamePdfAsText = "output-text.pdf";
    private String targetFileNamePdfAsTable = "output-table.pdf";

    private int yearBoundMin = 1900;
    private int yearBoundMax = 2020;

    private int postalIndexBoundMin = 100000;
    private int postalIndexBoundMax = 200001;

    private int houseBoundMin = 1;
    private int houseBoundMax = 31;

    private int apartmentBoundMin = 1;
    private int apartmentBoundMax = 81;

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

        for (int i = 0; i < this.personsCount; i++) {
            this.generatePersonRow(i);
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
}
