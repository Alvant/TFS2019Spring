package ru.alexeev.tfs.qa.userdatagenerator.io;


import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import ru.alexeev.tfs.qa.userdatagenerator.Constants;

import java.io.File;


public class WriterPdfHelper {

    protected int fontSizeHeaders = 16;
    protected int fontSizeRegular = 12;

    public WriterPdfHelper() { }

    public Font getFontRegular(int fontSize) {
        return this.getFont(Constants.fontFileNameRegular, fontSize);
    }

    public Font getFontBold(int fontSize) {
        return this.getFont(Constants.fontFileNameBold, fontSize);
    }

    private String getFontFilePath(String fontFileName) {
        return new File(Constants.fontsFolder, fontFileName).getPath();
    }

    private Font getFontFromFile(String fontFileName, int fontSize) {
        return FontFactory.getFont(
                this.getFontFilePath(fontFileName),
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED,
                fontSize,
                Font.NORMAL,
                BaseColor.BLACK);
    }

    private Font getFont(String fontFileName, int fontSize) {
        return this.getFontFromFile(fontFileName, fontSize);
    }
}
