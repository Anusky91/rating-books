package es.anusky.rating_books.shared.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class UserId implements Serializable {
    private final Long value;

    public UserId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("El Id del usuario debe ser positivo");
        }
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
