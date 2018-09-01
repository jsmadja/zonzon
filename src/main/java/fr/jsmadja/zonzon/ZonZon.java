package fr.jsmadja.zonzon;

import fr.jsmadja.zonzon.domain.Cabinet;
import fr.jsmadja.zonzon.exporters.HtmlExporter;
import org.joda.time.LocalDate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class ZonZon {

    private static void saveTo(String html, String dest) throws IOException {
        FileOutputStream fos = new FileOutputStream(dest);
        fos.write(html.getBytes(Charset.defaultCharset()));
        fos.close();
    }

    public static void main(String[] args) throws Exception {
        Cabinet cabinet = new Cabinet();
        cabinet.charge("Donnees Brutes.xlsx", LocalDate.now());

        String html = new HtmlExporter().export(cabinet);
        saveTo(html, "data.html");
    }

}
