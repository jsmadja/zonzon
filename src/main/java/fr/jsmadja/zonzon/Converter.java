package fr.jsmadja.zonzon;

import com.google.common.base.Function;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateMidnight;

class Converter {
    static EtatMisEnExamen convertRowToEtatMisEnExamen(Row row, DateMidnight referenceDate) {
        String dossier = row.getCell(0).toString();
        String prevenu = row.getCell(1).getStringCellValue();
        Nature nature = Nature.parse(row.getCell(2).getStringCellValue());
        DateMidnight dateMandatDepotInitiale = Dates.toDate(row.getCell(3));
        DateMidnight dateDerniereGestionAlerte = Dates.toDate(row.getCell(4));
        return new EtatMisEnExamen(dossier, prevenu, nature, dateMandatDepotInitiale, dateDerniereGestionAlerte, referenceDate);
    }

    static Function<Row, EtatMisEnExamen> toEtat(final DateMidnight referenceDate) {
        return new Function<Row, EtatMisEnExamen>() {
            @Override
            public EtatMisEnExamen apply(Row row) {
                return Converter.convertRowToEtatMisEnExamen(row, referenceDate);
            }
        };
    }
}
