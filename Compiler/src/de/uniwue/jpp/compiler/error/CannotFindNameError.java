package de.uniwue.jpp.compiler.error;

import de.uniwue.jpp.compiler.syntax.TokenLocation;
import de.uniwue.jpp.compiler.util.NameCache;

public class CannotFindNameError implements Error {
    int name;
    TokenLocation location;

    /**
     * Erstellt einen CannotFindNameError.
     *
     * @param name     Der Name der Variablen (als Index im NameCache).
     * @param location Die Position des Fehlers im Quelltext.
     */
    public CannotFindNameError(int name, TokenLocation location) {
        this.name = name;
        this.location = location;
    }

    /**
     * Gibt den Namen der Variablen zurück.
     *
     * @return Der Name der Variablen (als Index im NameCache).
     */
    public int getName() {
        return name;
    }

    /**
     * Gibt die Fehlermeldung zurück.
     * Der Name der Variablen wird mithilfe des NameCache in einen String umgewandelt.
     *
     * @param names Der NameCache, der die Zuordnung von Namen zu Indizes enthält.
     * @return Die Fehlermeldung.
     */
    @Override
    public String getMessage(NameCache names) {
        String nameString = names.getName(name);
        return "Cannot find name '" + nameString + "' in this scope.";
    }

    /**
     * Gibt die Position des Fehlers im Quelltext zurück.
     *
     * @return Die Position des Fehlers.
     */
    @Override
    public TokenLocation getLocation() {
        return location;
    }
}
