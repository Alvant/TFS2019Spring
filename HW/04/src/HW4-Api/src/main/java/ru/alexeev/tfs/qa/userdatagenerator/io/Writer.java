package ru.alexeev.tfs.qa.userdatagenerator.io;


import ru.alexeev.tfs.qa.userdatagenerator.Logger;


public abstract class Writer {

    public void writeHeaders(String[] headers) {
        this.writeRow(headers);  // child may change the behaviour
    }

    public void writeRows(String[][] data) {
        Logger.log(String.format("writer %s: start writing file", this.getFileType()));

        for (String[] row : data) {
            this.writeRow(row);
        }

        Logger.log(String.format("writer %s: finish writing file", this.getFileType()));
    }

    public abstract void save();

    protected abstract void writeRow(String[] row);

    protected String getFileType() {
        return "";
    }
}
