package es.anusky.rating_books.favorite.domain.valueobjects;

import lombok.Getter;

import java.util.Objects;

@Getter
public class FavoriteId {

    private final Long value;

    public FavoriteId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("El ID del favorito debe ser positivo");
        }
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FavoriteId other && value.equals(other.value);
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
