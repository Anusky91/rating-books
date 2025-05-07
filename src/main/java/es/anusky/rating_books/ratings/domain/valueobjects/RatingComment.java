package es.anusky.rating_books.ratings.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class RatingComment implements Serializable {
    private final String value;

    public RatingComment(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El comentario no puede estar vacío");
        }
        if (value.length() > 1000) {
            throw new IllegalArgumentException("El comentario no puede superar los 1000 caracteres");
        }
        this.value = value.trim();
    }

}
