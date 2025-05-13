package es.anusky.rating_books.users.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class Password implements Serializable {
    private final String value;

    public Password(String value) {
        value = validate(value);
        this.value = value;
    }

    public Password(String value, boolean isHashed) {
        if (!isHashed) {
            value = validate(value);
        }
        this.value = value;
    }

    private String validate(String raw) {
        raw = raw.trim();
        if (raw.length() < 8) {
            throw new IllegalArgumentException("Debe tener al menos 8 caracteres");
        }
        if (!raw.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Debe tener al menos una minúscula");
        }
        if (!raw.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Debe tener al menos una mayúscula");
        }
        if (!raw.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Debe tener al menos un número");
        }
        if (!raw.matches(".*[^a-zA-Z0-9].*")) {
            throw new IllegalArgumentException("Debe tener al menos un carácter especial");
        }
        return raw;
    }

    public static Password fromEncoded(String hash) {
        return new Password(hash, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "********";
    }

}
