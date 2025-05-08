package es.anusky.rating_books.users.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class FirstName implements Serializable {
    private final String value;
    public FirstName(String value) {
        if (value == null) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        value = value.trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("El nombre no debe tener más de 100 caracteres");
        }
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirstName firstName = (FirstName) o;
        return Objects.equals(value, firstName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
