package es.anusky.rating_books.books.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class Author implements Serializable {
    private final String value;

    public Author(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("El nombre del autor no puede tener más de 100 caracteres");
        }
        this.value = value.trim();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Author author && value.equals(author.value);
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
