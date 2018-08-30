package fr.jsmadja.zonzon.domain;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import fr.jsmadja.zonzon.util.Dates;
import org.joda.time.DateMidnight;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class CabinetTest {

    @Test
    public void test() throws Exception {
        Cabinet cabinet = new Cabinet();
        cabinet.charge("src/test/resources/data.xlsx", DateMidnight.parse("2018-08-28"));
        List<EtatMisEnExamen> etatsMisEnExamenP1 = cabinet.getEtatsMisEnExamen(Priorite.P1);
        assertEquals(newArrayList("01/09/18", "01/09/18", "14/09/18", "25/09/18", "30/09/18", "01/10/18", "01/10/18"),
                from(etatsMisEnExamenP1).transform(new Function<EtatMisEnExamen, String>() {
                    @Override
                    public String apply(EtatMisEnExamen etatMisEnExamen) {
                        return Dates.formatDate(etatMisEnExamen.getDateProchaineEcheance());
                    }
                }).toList());
    }

}
