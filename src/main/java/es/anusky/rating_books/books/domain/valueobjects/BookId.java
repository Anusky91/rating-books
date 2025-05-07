package es.anusky.rating_books.books.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class BookId implements Serializable {
    private final Long value;

    public BookId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("El ID del libro debe ser positivo");
        }
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BookId other && value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
