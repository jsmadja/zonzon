package fr.jsmadja.zonzon;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.joda.time.DateMidnight;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class ZonZon {

    private void saveTo(String html, String dest) throws IOException {
        FileOutputStream fos = new FileOutputStream(dest);
        fos.write(html.getBytes(Charset.defaultCharset()));
        fos.close();
    }

    String fromXLSXToHtml(String file) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(file));
        Sheet rows = workbook.getSheetAt(0);
        return HtmlExporter.createHtml(rows, DateMidnight.now());
    }

    public static void main(String[] args) throws Exception {
        String html = new ZonZon().fromXLSXToHtml("data.xlsx");
        new ZonZon().saveTo(html, "data.html");
    }

}
