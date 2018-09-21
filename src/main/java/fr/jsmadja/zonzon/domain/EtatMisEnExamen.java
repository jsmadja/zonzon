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
    private final Renouvellements renouvellements = new Renouvellements();

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
        this.calculeRenouvellements();
    }

    public int getNombreProlongations() {
        return FluentIterable.from(this.renouvellements).filter(new Predicate<Renouvellement>() {
            @Override
            public boolean apply(Renouvellement r) {
                return r.getDateEcheance().isBefore(EtatMisEnExamen.this.referenceDate);
            }
        }).size();
    }

    public LocalDate getDateEcheanceMD() {
        return this.renouvellements.last().getDateEcheance();
    }

    int getDelaiAvantEcheanceMandatDepot() {
        return Days.daysBetween(referenceDate, getDateEcheanceMD()).getDays();
    }

    public LocalDate getDateDerniereProlongation() {
        if (this.renouvellements.size() < 2) {
            return null;
        }
        return this.renouvellements.get(this.renouvellements.size() - 2).getDateEcheance();
    }

    private void calculeRenouvellements() {
        LocalDate dateProchaineEcheance = calculeDateProlongationInitiale(this.dateMandatDepotInitiale);
        Renouvellement renouvellement = new Renouvellement(dateProchaineEcheance, this.nature, true);
        renouvellements.add(renouvellement);
        while (renouvellement.aEteTraite(this.dateDerniereGestionAlerte)) {
            dateProchaineEcheance = calculeDateProlongation(dateProchaineEcheance);
            renouvellement = new Renouvellement(dateProchaineEcheance, this.nature, false);
            renouvellements.add(renouvellement);
        }
    }

    public Renouvellements getRenouvellements() {
        return this.renouvellements;
    }

    private LocalDate calculeDateProlongationInitiale(LocalDate dateProchaineEcheance) {
        return new LocalDate(dateProchaineEcheance).plusMonths(this.nature.getDelaiProlongationInitiale());
    }

    private LocalDate calculeDateProlongation(LocalDate dateProchaineEcheance) {
        return dateProchaineEcheance.plusMonths(this.nature.getDelaiProlongation());
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