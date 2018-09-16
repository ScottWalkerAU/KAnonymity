package comp4240.kanonymity.attribute;

import java.util.Arrays;
import java.util.List;

public enum AttributeType {
    STRING("TEXT", "NOMINAL"),
    NUMERIC("NUMBER"),
    BINARY,
    DATE;

    private List<String> aliases;

    AttributeType(String... aliases) {
        this.aliases = Arrays.asList(aliases);
    }

    public static AttributeType getType(String name) {
        name = name.toUpperCase();
        for (AttributeType type : values()) {
            if (type.name().equals(name) || type.aliases.contains(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("AttributeType does not contain the type " + name);
    }
}
