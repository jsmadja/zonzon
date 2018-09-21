package fr.jsmadja.zonzon.domain;

public enum Nature {

    Criminelle(12,6),
    Delictuelle(4,4);

    private int delaiProlongationInitiale;
    private int delaiProlongation;

    Nature(int delaiProlongationInitiale, int delaiProlongation) {
        this.delaiProlongationInitiale = delaiProlongationInitiale;
        this.delaiProlongation = delaiProlongation;
    }

    public static Nature parse(String s) throws RuntimeException {
        if ("C".equals(s)) {
            return Criminelle;
        }
        if ("D".equals(s)) {
            return Delictuelle;
        }
        throw new RuntimeException("Nature invalide: " + s);
    }

    public int getDelaiProlongation() {
        return this.delaiProlongation;
    }

    public int getDelaiProlongationInitiale() {
        return this.delaiProlongationInitiale;
    }
}
