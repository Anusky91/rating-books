package es.anusky.rating_books.ratings.domain.valueobjects;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserId implements Serializable {
    private final Long value;

    public UserId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("El Id del usuario debe ser positivo");
        }
        this.value = value;
    }

}
