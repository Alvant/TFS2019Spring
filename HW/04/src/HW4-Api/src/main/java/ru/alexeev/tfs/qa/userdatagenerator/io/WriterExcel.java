package ru.alexeev.tfs.qa.userdatagenerator.io;


import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WritableSheet;
import jxl.write.Label;
import jxl.write.WriteException;
import ru.alexeev.tfs.qa.userdatagenerator.Logger;

import java.io.File;
import java.io.IOException;


// https://stackoverflow.com/questions/3454975/writing-to-excel-in-java
public class WriterExcel extends Writer {

    private WritableWorkbook book;
    private WritableSheet sheet;
    private File targetFile;
    private int currentRowIndex;
    private String sheetName = "Sheet 1";

    private String fileType = "excel";

    public WriterExcel(String folderPath, String fileName) {
        this.createFile(folderPath, fileName);
        this.createBook();

        this.currentRowIndex = 0;
    }

    @Override
    public void save() {
        try {
            this.book.write();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.closeBook();
        }

        Logger.log(String.format("writer %s: file saved, path: \"%s\"",
                this.getFileType(),
                this.targetFile.getPath()));
    }

    @Override
    protected void writeRow(String[] row) {
        try {
            for (int i = 0; i < row.length; i++) {
                String cellValueText = row[i];
                this.sheet.addCell(new Label(i, currentRowIndex, cellValueText));
            }

            this.currentRowIndex += 1;

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getFileType() {
        return this.fileType;
    }

    private void createFile(String folderPath, String fileName) {
        this.targetFile = new File(folderPath, fileName);

        if (this.targetFile.isDirectory()) {
            throw new IllegalArgumentException("Invalid file path");
        }
    }

    private void createBook() {
        try {
            this.book = Workbook.createWorkbook(this.targetFile);
            this.sheet = this.book.createSheet(this.sheetName, 0);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Got error while handling file");
        }
    }

    private void closeBook() {
        if (this.book != null) {
            try {
                this.book.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
    }
}

