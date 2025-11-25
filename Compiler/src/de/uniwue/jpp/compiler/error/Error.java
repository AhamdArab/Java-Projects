package de.uniwue.jpp.compiler.error;

import de.uniwue.jpp.compiler.syntax.TokenLocation;
import de.uniwue.jpp.compiler.util.NameCache;

public interface Error {
    String getMessage(NameCache names);
    TokenLocation getLocation();
}
