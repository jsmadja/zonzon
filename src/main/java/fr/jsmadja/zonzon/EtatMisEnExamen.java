package fr.jsmadja.zonzon;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.joda.time.DateMidnight;
import org.joda.time.Days;

import javax.annotation.Nullable;
import java.util.List;

public class EtatMisEnExamen {
    private final String dossier;
    private final String nom;
    private final Nature nature;
    private final DateMidnight dateMandatDepotInitiale;
    private final DateMidnight dateDerniereGestionAlerte;
    private final DateMidnight referenceDate;
    private final List<DateMidnight> renouvellements;

    EtatMisEnExamen(String dossier, String nom, Nature nature, DateMidnight dateMandatDepotInitiale, DateMidnight dateDerniereGestionAlerte, DateMidnight referenceDate) {
        if (referenceDate == null) {
            referenceDate = DateMidnight.now();
        }
        this.dossier = dossier;
        this.nom = nom;
        this.nature = nature;
        this.dateMandatDepotInitiale = dateMandatDepotInitiale;
        this.dateDerniereGestionAlerte = dateDerniereGestionAlerte;
        this.referenceDate = referenceDate;
        this.renouvellements = this.calculeRenouvellements();
    }

    int getNombreProlongations() {
        return FluentIterable.from(this.renouvellements).filter(new Predicate<DateMidnight>() {
            @Override
            public boolean apply(@Nullable DateMidnight dateMidnight) {
                return this.test(dateMidnight);
            }

            @Override
            public boolean test(DateMidnight r) {
                return r.isBefore(EtatMisEnExamen.this.referenceDate);
            }
        }).size();
    }

    DateMidnight getDateProchaineEcheance() {
        return this.renouvellements.get(this.renouvellements.size() - 1);
    }

    int getDelaiAvantEcheanceMandatDepot() {
        return Days.daysBetween(referenceDate, getDateProchaineEcheance()).getDays();
    }

    DateMidnight getDateDernierRenouvellement() {
        if(this.renouvellements.size() < 2) {
            return null;
        }
        return this.renouvellements.get(this.renouvellements.size() - 2);
    }

    private List<DateMidnight> calculeRenouvellements() {
        List<DateMidnight> renouvellements = Lists.newArrayList();
        DateMidnight dateProchaineEcheance = calculeDateProlongationInitiale(this, this.dateMandatDepotInitiale);
        renouvellements.add(new DateMidnight(dateProchaineEcheance));
        do {
            dateProchaineEcheance = calculeDateProlongation(this, dateProchaineEcheance);
            if (this.dateDerniereGestionAlerte != null) {
                renouvellements.add(new DateMidnight(dateProchaineEcheance));
            }
        } while (dateProchaineEcheance.isBefore(this.referenceDate));
        return renouvellements;
    }

    List<DateMidnight> getRenouvellements() {
        return this.renouvellements;
    }

    private Boolean isDelictuel() {
        return this.nature == Nature.Delictuel;
    }

    private DateMidnight calculeDateProlongationInitiale(EtatMisEnExamen etat, DateMidnight dateProchaineEcheance) {
        return etat.isDelictuel() ? new DateMidnight(dateProchaineEcheance).plusMonths(4) : new DateMidnight(dateProchaineEcheance).plusMonths(12);
    }

    private DateMidnight calculeDateProlongation(EtatMisEnExamen etat, DateMidnight dateProchaineEcheance) {
        return etat.isDelictuel() ? dateProchaineEcheance.plusMonths(4) : dateProchaineEcheance.plusMonths(6);
    }

    String getNom() {
        return this.nom;
    }

    String getDossier() {
        return this.dossier;
    }

    public Nature getNature() {
        return this.nature;
    }

    DateMidnight getDateMandatDepotInitiale() {
        return this.dateMandatDepotInitiale;
    }

}