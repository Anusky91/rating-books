package es.anusky.rating_books.users.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class PhoneNumber implements Serializable {
    private final String value;

    public PhoneNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El número de teléfono es obligatorio");
        }

        value = value.trim();

        if (!value.matches("^\\d{9}$")) {
            throw new IllegalArgumentException("El número debe tener exactamente 9 dígitos");
        }

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PhoneNumber that && value.equals(that.value);
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
