package fr.jsmadja.zonzon;

public enum Nature {
    Criminel,
    Delictuel;

    public static Nature parse(String s) throws RuntimeException {
        if ("C".equals(s)) {
            return Criminel;
        }
        if ("D".equals(s)) {
            return Delictuel;
        }
        throw new RuntimeException("Nature invalide: " + s);
    }

}
