package es.anusky.rating_books.books.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class Isbn implements Serializable {
    private static final Pattern ISBN_13_REGEX = Pattern.compile("^97[89][- ]?\\d{1,5}[- ]?\\d{1,7}[- ]?\\d{1,7}[- ]?\\d$");

    private final String value;

    public Isbn(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN no puede estar vacío");
        }

        String normalized = value.replaceAll("[\\s-]", "");

        if (!normalized.matches("\\d{13}")) {
            throw new IllegalArgumentException("El ISBN debe tener 13 dígitos");
        }

        if (!ISBN_13_REGEX.matcher(value).matches()) {
            throw new IllegalArgumentException("Formato de ISBN inválido");
        }

        this.value = normalized;
    }

    // Equals y hashCode por valor
    @Override
    public boolean equals(Object o) {
        return o instanceof Isbn isbn && value.equals(isbn.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    // Opcional: toString
    @Override
    public String toString() {
        return value;
    }
}
