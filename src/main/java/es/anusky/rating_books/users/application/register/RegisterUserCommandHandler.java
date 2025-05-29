package es.anusky.rating_books.users.application.register;

import es.anusky.rating_books.cqrs.application.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUserCommandHandler implements CommandHandler<RegisterUserCommand> {

    private final RegisterUserService registerUserService;

    @Override
    public void handle(RegisterUserCommand command) {
        registerUserService.register(command.getFirstName(),
                command.getLastName(),
                command.getAlias(),
                command.getEmail(),
                command.getPhoneNumber(),
                command.getPassword(),
                command.getCountry(),
                command.getBirthDate(),
                command.getRole(),
                command.getAvatarUrl());
    }
}
