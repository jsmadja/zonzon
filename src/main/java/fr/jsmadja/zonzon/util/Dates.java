package fr.jsmadja.zonzon.util;

import com.google.common.base.Joiner;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.ocpsoft.prettytime.Duration;
import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.TimeUnit;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Dates {
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return DateTimeFormat.forPattern("dd/MM/YY").print(date);
    }

    public static LocalDate toDate(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC.getCode()) {
            return new LocalDate(cell.getDateCellValue());
        }
        if (cell.getCellType() == CellType.STRING.getCode()) {
            return LocalDate.parse(cell.getStringCellValue(), DateTimeFormat.forPattern("dd/MM/yy"));
        }
        return null;
    }

    public static String formatDelai(LocalDate date) {
        PrettyTime p = new PrettyTime(new Locale("fr"));
        p.setReference(LocalDate.now().toDate());
        List<Duration> durations = p.calculatePreciseDuration(date.toDate());

        String[] s = p.format(durations)
                .replaceAll("dans", "")
                .replaceAll("[0-9]+ heures", "")
                .replaceAll("[0-9]+ minutes", "")
                .trim()
                .split(" ");
        return Joiner.on(" ").join(s);
    }
}
