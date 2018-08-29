package fr.jsmadja.zonzon;

import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;

import static org.junit.Assert.assertNotNull;

public class ZonZonTest {

    @Test
    public void test() throws Exception {
        String expected = Files.toString(new File("src/test/resources/data.html"), Charset.defaultCharset());
        String actual = new ZonZon().fromXLSXToHtml("src/test/resources/data.xlsx");
        assertNotNull(actual);
    }

}
