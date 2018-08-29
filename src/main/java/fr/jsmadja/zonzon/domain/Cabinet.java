package fr.jsmadja.zonzon.domain;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import fr.jsmadja.zonzon.util.Dates;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.joda.time.DateMidnight;

import java.io.File;
import java.util.Comparator;
import java.util.List;

public class Cabinet {

    private List<EtatMisEnExamen> etatsMisEnExamen;
    private DateMidnight referenceDate;

    private static Predicate<Row> noHeader() {
        return new Predicate<Row>() {
            @Override
            public boolean apply(Row row) {
                return row.getRowNum() > 0;
            }
        };
    }

    public void charge(String fichier, DateMidnight referenceDate) throws Exception {
        this.referenceDate = referenceDate;
        Workbook workbook = WorkbookFactory.create(new File(fichier));
        Sheet rows = workbook.getSheetAt(0);
        this.etatsMisEnExamen = FluentIterable.from(rows).filter(noHeader()).transform(toEtat()).toList();
    }

    private EtatMisEnExamen convertRowToEtatMisEnExamen(Row row) {
        String dossier = row.getCell(0).toString();
        String prevenu = row.getCell(1).getStringCellValue();
        Nature nature = Nature.parse(row.getCell(2).getStringCellValue());
        DateMidnight dateMandatDepotInitiale = Dates.toDate(row.getCell(3));
        DateMidnight dateDerniereGestionAlerte = Dates.toDate(row.getCell(4));
        return new EtatMisEnExamen(dossier, prevenu, nature, dateMandatDepotInitiale, dateDerniereGestionAlerte, this.referenceDate);
    }

    private Function<Row, EtatMisEnExamen> toEtat() {
        return new Function<Row, EtatMisEnExamen>() {
            @Override
            public EtatMisEnExamen apply(Row row) {
                return convertRowToEtatMisEnExamen(row);
            }
        };
    }

    public List<EtatMisEnExamen> getEtatsMisEnExamen(final Priorite priorite) {
        return FluentIterable.from(this.etatsMisEnExamen).filter(new Predicate<EtatMisEnExamen>() {
            @Override
            public boolean apply(EtatMisEnExamen etatMisEnExamen) {
                return etatMisEnExamen.aPriorite(priorite);
            }
        }).toSortedList(byDelai());
    }

    private Comparator<EtatMisEnExamen> byDelai() {
        return new Comparator<EtatMisEnExamen>() {
            @Override
            public int compare(EtatMisEnExamen etatMisEnExamen1, EtatMisEnExamen etatMisEnExamen2) {
                return etatMisEnExamen1.getDelaiAvantEcheanceMandatDepot() - etatMisEnExamen2.getDelaiAvantEcheanceMandatDepot();
            }
        };
    }
}
