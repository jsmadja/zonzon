package fr.jsmadja.zonzon.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.joda.time.DateMidnight;
import org.joda.time.format.DateTimeFormat;

public class Dates {
    public static String formatDate(DateMidnight date) {
        if (date == null) {
            return "";
        }
        return DateTimeFormat.forPattern("dd/MM/YY").print(date);
    }

    public static DateMidnight toDate(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC.getCode()) {
            return new DateMidnight(cell.getDateCellValue());
        }
        if (cell.getCellType() == CellType.STRING.getCode()) {
            return DateMidnight.parse(cell.getStringCellValue(), DateTimeFormat.forPattern("dd/MM/yy"));
        }
        return null;
    }
}
