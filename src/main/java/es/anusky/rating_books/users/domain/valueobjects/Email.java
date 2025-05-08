package es.anusky.rating_books.users.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Email implements Serializable {
    private final String value;

    public Email(String value) {
        if (value == null) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (!checkValidEmail(value)) {
            throw new IllegalArgumentException("Formato del email invalido");
        }
        value = value.trim();
        if (value.length() > 320) {
            throw new IllegalArgumentException("El email no puede tener más de 320 caracteres");
        }
        this.value = value;
    }

    private boolean checkValidEmail(String value) {
        String pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return value.matches(pattern);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Email email && value.equalsIgnoreCase(email.value);
    }

    @Override
    public int hashCode() {
        return value.toLowerCase().hashCode();
    }

}
