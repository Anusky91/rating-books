package es.anusky.rating_books.users.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Alias implements Serializable {
    private final String value;

    public Alias(String value) {
        if (value == null) {
            throw new IllegalArgumentException("El alias es requerido.");
        }
        value = value.trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("El alias no puede estar vacío.");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("El alias no debe tener más de 50 caracteres.");
        }
        this.value = value;
    }
    @Override
    public boolean equals(Object o) {
        return o instanceof Alias alias && value.equalsIgnoreCase(alias.value);
    }
    @Override
    public int hashCode() {
        return value.toLowerCase().hashCode();
    }
    @Override
    public String toString() {
        return value;
    }
}
