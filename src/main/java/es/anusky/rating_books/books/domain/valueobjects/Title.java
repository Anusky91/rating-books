package es.anusky.rating_books.books.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class Title implements Serializable {
    private final String value;

    public Title(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (value.length() > 200) {
            throw new IllegalArgumentException("El título no puede tener más de 200 caracteres");
        }
        this.value = value.trim();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Title title && value.equals(title.value);
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
