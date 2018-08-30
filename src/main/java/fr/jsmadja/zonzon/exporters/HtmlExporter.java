package fr.jsmadja.zonzon.exporters;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.io.Files;
import fr.jsmadja.zonzon.domain.Cabinet;
import fr.jsmadja.zonzon.domain.EtatMisEnExamen;
import fr.jsmadja.zonzon.domain.Priorite;
import fr.jsmadja.zonzon.util.Dates;
import org.joda.time.DateMidnight;

import java.io.File;

import static com.google.common.base.Joiner.on;
import static java.nio.charset.Charset.defaultCharset;

public class HtmlExporter {

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
                "<td>" + Dates.formatDate(etat.getDateDerniereProlongation()) + "</td>\n" +
                "<td>" + etat.getNombreProlongations() + "</td>\n" +
                "<td><strong>" + Dates.formatDate(etat.getDateProchaineEcheance()) + "</strong></td>\n" +
                "<td><strong>" + Dates.formatDelai(etat.getDateProchaineEcheance()) + "</strong></td>\n" +
                "<td style=\"display: " + DISPLAY + ";\">" + renouvellements + "</td>\n" +
                "</tr>\n";
    }

    public String export(Cabinet cabinet) throws Exception {
        String p1 = FluentIterable.from(cabinet.getEtatsMisEnExamen(Priorite.P1)).transform(toHtml("negative")).join(on("\n"));
        String p2 = FluentIterable.from(cabinet.getEtatsMisEnExamen(Priorite.P2)).transform(toHtml("warning")).join(on("\n"));
        String p3 = FluentIterable.from(cabinet.getEtatsMisEnExamen(Priorite.P3)).transform(toHtml("positive")).join(on("\n"));
        String content = Files.toString(new File("src/main/resources/template.html"), defaultCharset())
                .replace("__DISPLAY__", DISPLAY)
                .replace("__P1__", p1)
                .replace("__P2__", p2)
                .replace("__P3__", p3);
        return content;
    }

    private static Function<EtatMisEnExamen, Object> toHtml(final String className) {
        return new Function<EtatMisEnExamen, Object>() {
            @Override
            public Object apply(EtatMisEnExamen etat) {
                return HtmlExporter.toHtml(className, etat);
            }
        };
    }

}
