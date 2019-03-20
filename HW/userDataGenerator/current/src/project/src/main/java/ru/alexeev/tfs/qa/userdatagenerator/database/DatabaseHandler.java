package ru.alexeev.tfs.qa.userdatagenerator.database;


import java.sql.*;


public class DatabaseHandler {
    protected static final String stringToDatabaseDateConvertFormat = "%d-%m-%Y";
    protected static final String javaDateToStringConvertFormat = "dd-MM-YYYY";

    private Connection conn;

    private static final String tablePersonsName = "persons";
    private static final String tableAddressesName = "address";

    public DatabaseHandler() throws java.sql.SQLException {
        this.conn = DriverManager.getConnection(
                DatabaseParameters.url,
                DatabaseParameters.login,
                DatabaseParameters.password);
    }

    public void main(String[] args) throws java.sql.SQLException {
        Connection conn = DriverManager.getConnection(
                DatabaseParameters.url,
                DatabaseParameters.login,
                DatabaseParameters.password);

        String query = "select * from persons";

        try {
            ResultSet rs = this.getQueryResults(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String surname = rs.getString("surname");
                String name = rs.getString("name");
                String middlename = rs.getString("middlename");
                Date birthdate = rs.getDate("birthdate");
                String gender = rs.getString("gender");
                String inn = rs.getString("inn");
                int addressId = rs.getInt("address_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isDatabaseEmpty() throws java.sql.SQLException {
        String usersCountColumnAlias = "users_count";
        String query = String.format(
                "SELECT count(*) AS %s FROM %s",
                usersCountColumnAlias,
                tablePersonsName
        );
        ResultSet rs = this.getQueryResults(query);

        while (rs.next()) {
            int usersCount = rs.getInt(usersCountColumnAlias);
            return usersCount == 0;
        }

        throw new IllegalStateException(String.format(
                "Probably no such table \"%s\" in database", tablePersonsName)
        );
    }

    public boolean isUserExists(String surname, String name, String middlename) throws java.sql.SQLException {
        String query = String.format(
                "SELECT * FROM %s WHERE surname = \"%s\" AND name = \"%s\"",
                tablePersonsName, surname, name
        );

        if (middlename == null) {
            query += " AND middlename is NULL;";
        } else {
            query += String.format(" AND middlename = \"%s\";", middlename);
        }

        ResultSet rs = this.getQueryResults(query);

        int usersCount = 0;

        while (rs.next()) {
            usersCount++;
        }

        if (usersCount == 1) {
            return true;
        } else if (usersCount == 0) {
            return false;
        } else {
            throw new IllegalStateException(String.format(
                    "There are \"%d\" users \"%s %s %s\" in database",
                    usersCount, surname, name, middlename));
        }
    }

    public void insertUser(DatabaseUser user, DatabaseAddress address) throws java.sql.SQLException {
        String columns = "surname, name, middlename, birthdate, gender, inn, address_id";

        Integer address_id = this.getAddressId(address);

        if (address_id == null) {
            address_id = this.insertAddressAndGetId(address);
        }

        // TODO: NULL
        String values = String.format(
                "\"%s\", \"%s\", %s, STR_TO_DATE(\"%s\", \"%s\"), \"%s\", \"%s\", %d",
                user.surname,
                user.name,
                user.middlename == null ? "NULL" : String.format("\"%s\"", user.middlename),
                user.convertDateToStringInDatabaseFormat(),
                stringToDatabaseDateConvertFormat,
                user.gender,
                user.inn,
                address_id
        );

        this.insertInTable(tablePersonsName, columns, values);
    }

    public void updateUser(DatabaseUser user, DatabaseAddress address) throws java.sql.SQLException {
        String sets = String.format(
                "birthdate = STR_TO_DATE(\"%s\", \"%s\"), gender = \"%s\", inn = \"%s\"",
                user.convertDateToStringInDatabaseFormat(),
                stringToDatabaseDateConvertFormat,
                user.gender,
                user.inn
        );

        Integer address_id = this.getAddressId(address);

        if (address_id == null) {
            address_id = this.insertAddressAndGetId(address);
        }

        sets += String.format(", address_id = %d", address_id);

        String condition = String.format(
                "surname = \"%s\" AND name = \"%s\"",
                user.surname, user.name);
        if (user.middlename == null) {
            condition += " AND middlename is NULL;";
        } else {
            condition += String.format(" AND middlename = \"%s\";", user.middlename);
        }

        this.updateInTable(tablePersonsName, sets, condition);
    }

    public DatabaseUser[] selectRandomUsers(int usersCount) throws java.sql.SQLException {
        DatabaseUser[] databaseUsers = new DatabaseUser[usersCount];
        String queryPersons = String.format(
                "SELECT * FROM %s ORDER BY RAND() LIMIT %d",
                tablePersonsName, usersCount);
        ResultSet rs = this.getQueryResults(queryPersons);

        int i = 0;

        while (rs.next() && i < usersCount) {
            DatabaseUser user = new DatabaseUser(
                    rs.getString("surname"),
                    rs.getString("name"),
                    rs.getString("middlename"),
                    rs.getDate("birthdate"),
                    rs.getString("gender"),
                    rs.getString("inn")
            );

            int addressId = rs.getInt("address_id");
            DatabaseAddress address = null;
            String queryAddress = String.format(
                    "SELECT * FROM %s WHERE id = %d",
                    tableAddressesName, addressId
            );

            ResultSet rsAddress = this.getQueryResults(queryAddress);

            while (rsAddress.next()) {
                address = new DatabaseAddress(
                        rsAddress.getString("postcode"),
                        rsAddress.getString("country"),
                        rsAddress.getString("region"),
                        rsAddress.getString("city"),
                        rsAddress.getString("street"),
                        rsAddress.getInt("house"),
                        rsAddress.getInt("flat")
                );
            }

            if (address == null) {
                throw new IllegalStateException(String.format(
                        "Address with id \"%s\" not found", addressId)
                );
            }

            user.setAddress(address);

            databaseUsers[i++] = user;
        }

        if (i < usersCount) {
            int retreivedUsersCount = i;
            DatabaseUser[] databaseUsersResult = new DatabaseUser[retreivedUsersCount];

            for (int j = 0; j < retreivedUsersCount; j++) {
                databaseUsersResult[j] = databaseUsers[j];
            }

            databaseUsers = databaseUsersResult;
        }

        return databaseUsers;
    }

    private ResultSet getQueryResults(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        return rs;
    }

    private int executeUpdateQuery(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        int result = stmt.executeUpdate(query);

        return result;
    }

    private Integer getAddressId(DatabaseAddress address) throws java.sql.SQLException {
        String query = String.format(
                "SELECT id FROM %s " +
                        "WHERE postcode = \"%s\" " +
                        "AND country = \"%s\" " +
                        "AND region = \"%s\" " +
                        "AND city = \"%s\" " +
                        "AND street = \"%s\" " +
                        "AND house = %d " +
                        "AND flat = %d;",
                tableAddressesName,
                address.postcode,
                address.country,
                address.region,
                address.city,
                address.street,
                address.house,
                address.flat
        );

        ResultSet rs = this.getQueryResults(query);

        int addressesCount = 0;
        Integer addressId = null;

        while (rs.next()) {
            addressesCount++;
            addressId = rs.getInt("id");
        }

        if (addressesCount == 1) {
            return addressId;
        } else if (addressesCount == 0) {
            return null;
        } else {
            throw new IllegalStateException(String.format(
                    "There are \"%d\" addresses \"%s %s %s %s %s %d %d\" in database",
                    addressesCount,
                    address.postcode,
                    address.country,
                    address.region,
                    address.city,
                    address.street,
                    address.house,
                    address.flat)
            );
        }
    }

    private Integer insertAddressAndGetId(DatabaseAddress address) throws java.sql.SQLException {
        String columns = "postcode, country, region, city, street, house, flat";
        String values = String.format(
                "\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", %d, %d",
                address.postcode,
                address.country,
                address.region,
                address.city,
                address.street,
                address.house,
                address.flat
        );

        this.insertInTable(tableAddressesName, columns, values);

        return this.getAddressId(address);
    }

    private void insertInTable(String tableName, String columns, String values) throws java.sql.SQLException {
        String query = String.format(
                "INSERT INTO %s (%s) VALUES (%s);",
                tableName, columns, values
        );

        this.executeUpdateQuery(query);
    }

    private void updateInTable(String tableName, String sets, String condition) throws java.sql.SQLException {
        String query = String.format(
                "UPDATE %s SET %s WHERE %s;",
                tableName, sets, condition
        );

        this.executeUpdateQuery(query);
    }
}
