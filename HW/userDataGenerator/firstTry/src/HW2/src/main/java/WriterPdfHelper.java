import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import org.apache.pdfbox.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class WriterPdfHelper {

    protected int fontSizeHeaders = 16;
    protected int fontSizeRegular = 12;

    private String[] fontFileNames = {
            Constants.fontFileNameRegular, Constants.fontFileNameBold
    };

    public WriterPdfHelper() {
        this.checkFontFilesAvailabilityIfNotJar();
    }

    public Font getFontRegular(int fontSize) {
        return this.getFont(Constants.fontFileNameRegular, fontSize);
    }

    public Font getFontBold(int fontSize) {
        return this.getFont(Constants.fontFileNameBold, fontSize);
    }

    private void checkFontFilesAvailabilityIfNotJar() {
        if (Constants.isJar == true) {
            return;
        }

        for (String fontFilePathName : this.fontFileNames) {
            String fontFilePath = this.getFontFilePath(fontFilePathName);

            try {
                File file = new File(fontFilePath);

                if (!file.exists() || file.isDirectory()) {
                    throw new FileNotFoundException(String.format(
                            "Font file not found on path \"%s\"", file.getPath()));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFontFilePath(String fontFileName) {
        if (Constants.isJar == true) {
            return new File(Constants.fontsFolderName, fontFileName).getPath();
        } else {
            return new File(Constants.fontsFolder, fontFileName).getPath();
        }
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

    private Font getFontFromResource(String fontFileName, int fontSize) {
        byte[] bytes;
        BaseFont bf = null;
        Font font;

        try {
            bytes = IOUtils.toByteArray(
                    getClass()//.getClassLoader()
                            .getResourceAsStream(this.getFontFilePath(fontFileName)));
        } catch (IOException e) {
            e.printStackTrace();
            bytes = new byte[0];
        }

        try {
            bf = BaseFont.createFont(fontFileName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, bytes, null);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        font = new Font(bf, fontSize);

        return font;
    }

    private Font getFont(String fontFileName, int fontSize) {
        if (Constants.isJar == false) {
            return this.getFontFromFile(fontFileName, fontSize);
        } else {
            return this.getFontFromResource(fontFileName, fontSize);
        }
    }
}
