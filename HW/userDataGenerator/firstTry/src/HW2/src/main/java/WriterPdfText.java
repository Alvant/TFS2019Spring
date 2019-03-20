/*
    Создать кроме Excel-файла еще PDF-файл.
    Просто экспорт excel в pdf нельзя.
    Также вывести в лог сообщение о создании файла и для PDF.
    Строки в PDF можно не нумеровать
 */

// https://www.baeldung.com/java-pdf-creation


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class WriterPdfText extends Writer {

    private Document document;
    private File targetFile;

    private WriterPdfHelper pdfHelper;

    private Rectangle pageSize = PageSize.A4;

    private String wordsSeparator = ", ";
    private String rowsSeparator = "\n";

    private String fileType = "pdf (text)";

    public WriterPdfText(String folderPath, String fileName) {
        this.pdfHelper = new WriterPdfHelper();

        this.createFile(folderPath, fileName);
        this.createDocument(this.targetFile);
    }

    @Override
    public void save() {
        document.close();

        Logger.log(String.format("writer %s: file saved, path: \"%s\"",
                this.getFileType(),
                this.targetFile.getPath()));
    }

    @Override
    public void writeHeaders(String[] headers) {
        this.writeRow(headers, this.pdfHelper.getFontBold(this.pdfHelper.fontSizeHeaders));
    }

    @Override
    protected void writeRow(String[] row) {
        this.writeRow(row, this.pdfHelper.getFontRegular(this.pdfHelper.fontSizeRegular));
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
            document.open();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Got error while handling file");
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Got error while handling file");
        }
    }

    private void writeRow(String[] row, Font fontDefault) {
        Phrase phrase = new Phrase();

        for (int i = 0; i < row.length; i++) {
            String textValue = row[i];

            textValue += (i < row.length - 1) ? this.wordsSeparator : this.rowsSeparator;

            phrase.add(new Chunk(textValue, fontDefault));
        }

        try {
            document.add(phrase);
        } catch (DocumentException e) {
            e.printStackTrace();
            Logger.log(String.format("writer %s: fail to add chunk", this.getFileType()));
        }
    }
}