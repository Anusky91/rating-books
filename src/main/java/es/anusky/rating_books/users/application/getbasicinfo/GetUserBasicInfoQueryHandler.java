package es.anusky.rating_books.users.application.getbasicinfo;

import es.anusky.rating_books.cqrs.application.query.QueryHandler;
import es.anusky.rating_books.shared.infrastructure.responses.UserResponse;
import es.anusky.rating_books.users.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserBasicInfoQueryHandler implements QueryHandler<GetUserBasicInfoQuery, UserResponse> {

    private final UserService userService;

    @Override
    public UserResponse handle(GetUserBasicInfoQuery query) {
        return UserResponse.toResponse(userService.findByAlias(query.getAlias()).orElseThrow());
    }
}
