package es.anusky.rating_books.shared.infrastructure.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPublicProfileResponse {
    private Long userId;
    private String alias;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private Integer age;
    private int reviewsCount;
    private double averageScore;
    private List<FavoriteResponse> favorites;
}
