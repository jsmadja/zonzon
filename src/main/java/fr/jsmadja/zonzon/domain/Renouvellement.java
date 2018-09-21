package fr.jsmadja.zonzon.domain;

import org.joda.time.LocalDate;

public class Renouvellement {
    private final LocalDate dateEcheance;
    private final LocalDate dateDebutPeriodeDeTraitement;
    private final Nature nature;

    Renouvellement(LocalDate dateEcheance, Nature nature, boolean initial) {
        this.dateDebutPeriodeDeTraitement = dateEcheance.minusMonths(initial ? nature.getDelaiProlongationInitiale() : nature.getDelaiProlongation());
        this.dateEcheance = new LocalDate(dateEcheance);
        this.nature = nature;
    }

    boolean aEteTraite(LocalDate dateDerniereGestionAlerte) {
        if (dateDerniereGestionAlerte == null) {
            return false;
        }
        if (dateDerniereGestionAlerte.isAfter(this.dateEcheance)) {
            return true;
        }
        return dateDerniereGestionAlerte.isAfter(dateDebutPeriodeDeTraitement);
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    @Override
    public String toString() {
        return this.nature + " " + this.dateDebutPeriodeDeTraitement + " --> " + this.dateEcheance;
    }
}
