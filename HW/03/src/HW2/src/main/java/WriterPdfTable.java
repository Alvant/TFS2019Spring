/*
    Создать кроме Excel-файла еще PDF-файл.
    Просто экспорт excel в pdf нельзя.
    Также вывести в лог сообщение о создании файла и для PDF.
    Строки в PDF можно не нумеровать
 */

// https://www.baeldung.com/java-pdf-creation


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.InvalidParameterException;


public class WriterPdfTable extends Writer {

    private Document document;
    private PdfPTable table;

    private WriterPdfHelper pdfHelper;

    private File targetFile;

    private Rectangle pageSize = PageSize.A3.rotate();
    private int borderWidth = 2;
    private int tableWidthPercentage = 100;

    private String fileType = "pdf (table)";

    public WriterPdfTable(String folderPath, String fileName) {
        this.pdfHelper = new WriterPdfHelper();

        this.createFile(folderPath, fileName);
        this.createDocument(this.targetFile);
    }

    @Override
    public void save() {
        document.open();

        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new InvalidParameterException("Failed to add table to document");
        } finally {
            document.close();
        }

        Logger.log(String.format("writer %s: file saved, path: \"%s\"",
                this.getFileType(),
                this.targetFile.getPath()));
    }

    @Override
    public void writeHeaders(String[] headers) {
        this.createTable(headers.length);

        for (String headerName : headers) {
            PdfPCell header = new PdfPCell();

            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(this.borderWidth);
            header.setPhrase(new Phrase(headerName, this.pdfHelper.getFontBold(this.pdfHelper.fontSizeHeaders)));

            table.addCell(header);
        }
    }

    @Override
    protected void writeRow(String[] row) {
        for (String cellValueText : row) {
            PdfPCell cell = new PdfPCell();
            cell.setPhrase(new Phrase(cellValueText, this.pdfHelper.getFontRegular(this.pdfHelper.fontSizeRegular)));

            table.addCell(cell);
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

    private void createDocument(File targetFile) {
        this.document = new Document(this.pageSize, 10f, 10f, 10f, 0f);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(targetFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Got error while handling file");
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Got error while handling file");
        }
    }

    private void createTable(int columnsNumber) {
        this.table = new PdfPTable(columnsNumber);
        this.table.setWidthPercentage(this.tableWidthPercentage);
    }
}