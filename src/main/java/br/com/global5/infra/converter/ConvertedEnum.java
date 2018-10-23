package br.com.global5.infra.converter;

/**
 * Declares this enum as converted into database, column value of type Y.
 * <p>
 * In addition to implementing {@link #toDbValue()} converted enum should also
 * provide static method for reverse conversion, for instance {@code X fromDbValue(Y)}.
 * This one should throw {@link IllegalArgumentException} just as {@link Enum#valueOf(Class, String)} does.
 */
public interface ConvertedEnum<Y> {

    Y toDbValue();
}
