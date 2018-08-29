package fr.jsmadja.zonzon.domain;

import org.joda.time.DateMidnight;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EtatMisEnExamenTest {

    @Test
    public void testDelai() {
        EtatMisEnExamen etat = new EtatMisEnExamen("5674", "Julien Bobby", Nature.Delictuel, DateMidnight.parse("2018-01-01"), null, DateMidnight.parse("2018-04-01"));
        assertEquals(DateMidnight.parse("2018-05-01"), etat.getDateProchaineEcheance());
        assertEquals(30, etat.getDelaiAvantEcheanceMandatDepot());
        assertEquals(0, etat.getNombreProlongations());
        assertEquals(null, etat.getDateDerniereProlongation());
    }
}

