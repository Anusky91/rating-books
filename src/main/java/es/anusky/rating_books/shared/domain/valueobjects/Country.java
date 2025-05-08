package es.anusky.rating_books.shared.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class Country implements Serializable {

    private final String code; // Ej: "ES"

    public Country(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("El país es obligatorio.");
        }
        String normalized = code.trim().toUpperCase();
        if (!SupportedCountryCode.isValid(normalized)) {
            throw new IllegalArgumentException("País no soportado: " + code);
        }
        this.code = normalized;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Country c && code.equals(c.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return code;
    }
}
