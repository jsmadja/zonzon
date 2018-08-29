package fr.jsmadja.zonzon;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.joda.time.DateMidnight;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import static com.google.common.base.Joiner.on;
import static java.nio.charset.Charset.defaultCharset;

class HtmlExporter {

    private static String DISPLAY = "none";

    static String toHtml(String className, EtatMisEnExamen etat) {
        String renouvellements = FluentIterable
                .from(etat.getRenouvellements().subList(0, etat.getRenouvellements().size() - 1))
                .transform(new Function<DateMidnight, String>() {
                    public String apply(DateMidnight d) {
                        return Dates.formatDate(d);
                    }
                })
                .join(on("<br/>"));
        return "<tr class=\"" + className + "\">\n" +
                "<td class=\"left aligned\">" + etat.getNom() + "</td>\n" +
                "<td class=\"right aligned\">" + etat.getDossier() + "</td>\n" +
                "<td>" + etat.getNature() + "</td>\n" +
                "<td>" + Dates.formatDate(etat.getDateMandatDepotInitiale()) + "</td>\n" +
                "<td>" + Dates.formatDate(etat.getDateDernierRenouvellement()) + "</td>\n" +
                "<td>" + etat.getNombreProlongations() + "</td>\n" +
                "<td><strong>" + Dates.formatDate(etat.getDateProchaineEcheance()) + "</strong></td>\n" +
                "<td><strong>" + etat.getDelaiAvantEcheanceMandatDepot() + " jours</strong></td>\n" +
                "<td style=\"display: " + DISPLAY + ";\">" + renouvellements + "</td>\n" +
                "</tr>\n";
    }

    static String createHtml(Sheet rows, DateMidnight referenceDate) throws IOException {
        String p1 = toHtmlRows(rows, "negative", new Predicate<EtatMisEnExamen>() {
            @Override
            public boolean test(@Nullable EtatMisEnExamen etatMisEnExamen) {
                return etatMisEnExamen.getDelaiAvantEcheanceMandatDepot() < 36;
            }

            @Override
            public boolean apply(@Nullable EtatMisEnExamen etatMisEnExamen) {
                return this.test(etatMisEnExamen);
            }
        }, referenceDate);

        String p2 = toHtmlRows(rows, "warning", new Predicate<EtatMisEnExamen>() {
            @Override
            public boolean test(EtatMisEnExamen etat) {
                return etat.getDelaiAvantEcheanceMandatDepot() > 35 && etat.getDelaiAvantEcheanceMandatDepot() < 67;
            }

            @Override
            public boolean apply(@Nullable EtatMisEnExamen etatMisEnExamen) {
                return this.test(etatMisEnExamen);
            }
        }, referenceDate);

        String p3 = toHtmlRows(rows, "positive", new Predicate<EtatMisEnExamen>() {
            @Override
            public boolean test(EtatMisEnExamen etat) {
                return etat.getDelaiAvantEcheanceMandatDepot() > 66;
            }

            @Override
            public boolean apply(@Nullable EtatMisEnExamen etatMisEnExamen) {
                return this.test(etatMisEnExamen);
            }
        }, referenceDate);

        String content = Files.toString(new File("src/main/resources/template.html"), defaultCharset())
                .replace("__DISPLAY__", DISPLAY)
                .replace("__P1__", p1)
                .replace("__P2__", p2)
                .replace("__P3__", p3);

        return content;
    }

    private static String toHtmlRows(Sheet rows, String className, Predicate<EtatMisEnExamen> condition, DateMidnight referenceDate) {
        ImmutableList<EtatMisEnExamen> sorted = FluentIterable.from(rows).filter(noHeader()).transform(Converter.toEtat(referenceDate)).filter(condition).toSortedList(byDelai());
        return FluentIterable.from(sorted).transform(toHtml(className)).join(on("\n"));
    }

    private static Function<EtatMisEnExamen, Object> toHtml(final String className) {
        return new Function<EtatMisEnExamen, Object>() {
            @Override
            public Object apply(EtatMisEnExamen etat) {
                return HtmlExporter.toHtml(className, etat);
            }
        };
    }

    private static Comparator<EtatMisEnExamen> byDelai() {
        return new Comparator<EtatMisEnExamen>() {
            @Override
            public int compare(EtatMisEnExamen etatMisEnExamen1, EtatMisEnExamen etatMisEnExamen2) {
                return etatMisEnExamen1.getDelaiAvantEcheanceMandatDepot() - etatMisEnExamen2.getDelaiAvantEcheanceMandatDepot();
            }
        };
    }

    private static Predicate<Row> noHeader() {
        return new Predicate<Row>() {
            @Override
            public boolean test(Row row) {
                return row.getRowNum() > 0;
            }

            @Override
            public boolean apply(@Nullable Row row) {
                return this.test(row);
            }
        };
    }

}
