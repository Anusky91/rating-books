package es.anusky.rating_books.users.application.getbasicinfo;

import es.anusky.rating_books.cqrs.application.query.Query;
import es.anusky.rating_books.shared.infrastructure.responses.UserResponse;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class GetUserBasicInfoQuery implements Query<UserResponse> {

    String alias;

    public GetUserBasicInfoQuery(String alias) {
        super();
        this.alias = alias;
    }
}
