package es.anusky.rating_books.users.domain.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return switch (this) {
            case ADMIN -> "ROLE_ADMIN";
            case USER -> "ROLE_USER";
        };
    }
}
