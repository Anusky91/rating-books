package es.anusky.rating_books.ratings.domain.valueobjects;

import lombok.Getter;

@Getter
public class RatingComment {
    private final String value;

    public RatingComment(String value) {
        if (value != null && value.length() > 1000) {
            throw new IllegalArgumentException("El comentario no puede superar los 1000 caracteres");
        }
        this.value = (value != null) ? value.trim() : null;
    }
}
