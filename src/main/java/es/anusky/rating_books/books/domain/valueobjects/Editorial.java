package es.anusky.rating_books.books.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class Editorial implements Serializable {
    private final String value;

    public Editorial(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("La editorial no puede estar vacío");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("La editorial no puede tener más de 100 caracteres");
        }
        this.value = value.trim();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Editorial editorial && value.equals(editorial.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
