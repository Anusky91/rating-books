package es.anusky.rating_books.ratings.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class RatingId implements Serializable {
    private final Long value;

    public RatingId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("El Id de la puntuación debe ser positivo");
        }
        this.value = value;
    }
}
