package fr.jsmadja.zonzon.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Renouvellements implements Iterable<Renouvellement> {

    private final List<Renouvellement> renouvellements = new ArrayList<Renouvellement>();

    @Override
    public Iterator<Renouvellement> iterator() {
        return this.renouvellements.iterator();
    }

    public int size() {
        return this.renouvellements.size();
    }

    public Renouvellement last() {
        return this.renouvellements.get(this.renouvellements.size() - 1);
    }

    public void add(Renouvellement renouvellement) {
        this.renouvellements.add(renouvellement);
    }

    public Renouvellement get(int i) {
        return this.renouvellements.get(i);
    }

    public Iterable<Renouvellement> subList(int i, int i1) {
        return this.renouvellements.subList(i, i1);
    }
}
