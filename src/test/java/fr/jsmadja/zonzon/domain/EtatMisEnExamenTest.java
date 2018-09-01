package fr.jsmadja.zonzon.domain;

import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EtatMisEnExamenTest {

    @Test
    public void testDelai() {
        EtatMisEnExamen etat = new EtatMisEnExamen("5674", "Julien Bobby", Nature.Delictuelle, LocalDate.parse("2018-01-01"), null, LocalDate.parse("2018-04-01"));
        assertEquals(LocalDate.parse("2018-05-01"), etat.getDateEcheanceMD());
        assertEquals(30, etat.getDelaiAvantEcheanceMandatDepot());
        assertEquals(0, etat.getNombreProlongations());
        assertNull(etat.getDateDerniereProlongation());
    }
}

