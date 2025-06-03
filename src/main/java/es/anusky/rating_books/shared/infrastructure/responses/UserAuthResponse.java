package es.anusky.rating_books.shared.infrastructure.responses;

import lombok.Builder;

@Builder
public record UserAuthResponse(long id,
                               String alias,
                               String email,
                               String role,
                               boolean enable,
                               boolean locked) {
}
