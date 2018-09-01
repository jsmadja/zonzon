package fr.jsmadja.zonzon.domain;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.joda.time.LocalDate;
import org.joda.time.Days;

import java.util.List;

public class EtatMisEnExamen {

    private static final int SEUIL_PRIORITE_1 = 46;
    private static final int SEUIL_PRIORITE_2 = 80;

    private final String dossier;
    private final String nom;
    private final Nature nature;
    private final LocalDate dateMandatDepotInitiale;
    private final LocalDate dateDerniereGestionAlerte;
    private final LocalDate referenceDate;
    private final List<LocalDate> renouvellements;

    EtatMisEnExamen(String dossier, String nom, Nature nature, LocalDate dateMandatDepotInitiale, LocalDate dateDerniereGestionAlerte, LocalDate referenceDate) {
        if (referenceDate == null) {
            referenceDate = LocalDate.now();
        }
        this.dossier = dossier;
        this.nom = nom;
        this.nature = nature;
        this.dateMandatDepotInitiale = dateMandatDepotInitiale;
        this.dateDerniereGestionAlerte = dateDerniereGestionAlerte;
        this.referenceDate = referenceDate;
        this.renouvellements = this.calculeRenouvellements();
    }

    public int getNombreProlongations() {
        return FluentIterable.from(this.renouvellements).filter(new Predicate<LocalDate>() {
            @Override
            public boolean apply(LocalDate r) {
                return r.isBefore(EtatMisEnExamen.this.referenceDate);
            }
        }).size();
    }

    public LocalDate getDateEcheanceMD() {
        return this.renouvellements.get(this.renouvellements.size() - 1);
    }

    public int getDelaiAvantEcheanceMandatDepot() {
        return Days.daysBetween(referenceDate, getDateEcheanceMD()).getDays();
    }

    public LocalDate getDateDerniereProlongation() {
        if (this.renouvellements.size() < 2) {
            return null;
        }
        return this.renouvellements.get(this.renouvellements.size() - 2);
    }

    private List<LocalDate> calculeRenouvellements() {
        List<LocalDate> renouvellements = Lists.newArrayList();
        LocalDate dateProchaineEcheance = calculeDateProlongationInitiale(this, this.dateMandatDepotInitiale);
        renouvellements.add(new LocalDate(dateProchaineEcheance));
        do {
            dateProchaineEcheance = calculeDateProlongation(this, dateProchaineEcheance);
            if (this.dateDerniereGestionAlerte != null) {
                renouvellements.add(new LocalDate(dateProchaineEcheance));
            }
        } while (dateProchaineEcheance.isBefore(this.referenceDate));
        return renouvellements;
    }

    public List<LocalDate> getRenouvellements() {
        return this.renouvellements;
    }

    private Boolean isDelictuel() {
        return this.nature == Nature.Delictuelle;
    }

    private LocalDate calculeDateProlongationInitiale(EtatMisEnExamen etat, LocalDate dateProchaineEcheance) {
        return etat.isDelictuel() ? new LocalDate(dateProchaineEcheance).plusMonths(4) : new LocalDate(dateProchaineEcheance).plusMonths(12);
    }

    private LocalDate calculeDateProlongation(EtatMisEnExamen etat, LocalDate dateProchaineEcheance) {
        return etat.isDelictuel() ? dateProchaineEcheance.plusMonths(4) : dateProchaineEcheance.plusMonths(6);
    }

    public String getNom() {
        return this.nom;
    }

    public String getDossier() {
        return this.dossier;
    }

    public Nature getNature() {
        return this.nature;
    }

    public LocalDate getDateMandatDepotInitiale() {
        return this.dateMandatDepotInitiale;
    }

    boolean aPriorite(Priorite priorite) {
        switch (priorite) {
            case P1:
                return this.getDelaiAvantEcheanceMandatDepot() < SEUIL_PRIORITE_1;
            case P2:
                return this.getDelaiAvantEcheanceMandatDepot() >= SEUIL_PRIORITE_1 && this.getDelaiAvantEcheanceMandatDepot() < SEUIL_PRIORITE_2;
            case P3:
                return this.getDelaiAvantEcheanceMandatDepot() >= SEUIL_PRIORITE_2;
            default:
                return true;
        }
    }
}