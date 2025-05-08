package es.anusky.rating_books.shared.infrastructure.responses;

import es.anusky.rating_books.users.domain.model.User;

public record UserResponse(Long id, String alias, String email, String role) {

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getUserId().getValue(),
                user.getAlias().getValue(),
                user.getEmail().getValue(),
                user.getRole().toString()
        );
    }

}
