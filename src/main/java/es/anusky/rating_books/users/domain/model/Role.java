package es.anusky.rating_books.users.domain.model;

import java.util.List;

public enum Role {
    USER,
    ADMIN;

    public List<String> getAuthorities() {
        return switch (this) {
            case ADMIN -> List.of("ROLE_ADMIN", "ROLE_USER");
            case USER -> List.of("ROLE_USER");
        };
    }
}
