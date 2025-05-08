package es.anusky.rating_books.users.domain.valueobjects;

import java.io.Serializable;
import java.util.Objects;

public class LastName implements Serializable {

    private final String value;
    public LastName(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Los apellidos son obligatorio");
        }
        value = value.trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Los apellidos no pueden estar vacíos");
        }
        if (value.length() > 200) {
            throw new IllegalArgumentException("Los apellidos no deben tener más de 200 caracteres");
        }
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LastName lastName = (LastName) o;
        return Objects.equals(value, lastName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
