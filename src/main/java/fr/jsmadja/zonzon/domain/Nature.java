package fr.jsmadja.zonzon.domain;

public enum Nature {
    Criminelle,
    Delictuelle;

    public static Nature parse(String s) throws RuntimeException {
        if ("C".equals(s)) {
            return Criminelle;
        }
        if ("D".equals(s)) {
            return Delictuelle;
        }
        throw new RuntimeException("Nature invalide: " + s);
    }

}
