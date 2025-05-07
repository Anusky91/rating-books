package es.anusky.rating_books.ratings.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class RatingScore implements Serializable {
    private final int value;

    public RatingScore(int value) {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5");
        }
        this.value = value;
    }

}
