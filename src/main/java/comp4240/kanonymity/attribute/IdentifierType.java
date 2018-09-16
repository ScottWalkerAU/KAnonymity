package comp4240.kanonymity.attribute;

import java.util.Arrays;
import java.util.List;

public enum IdentifierType {
    ID("I", "IDENTIFIER"),
    QID("Q"),
    SENSITIVE("S");

    private List<String> aliases;

    IdentifierType(String... aliases) {
        this.aliases = Arrays.asList(aliases);
    }

    public static IdentifierType getType(String name) {
        name = name.toUpperCase();
        for (IdentifierType type : values()) {
            if (type.name().equals(name) || type.aliases.contains(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("IdentifierType does not contain the type " + name);
    }
}
