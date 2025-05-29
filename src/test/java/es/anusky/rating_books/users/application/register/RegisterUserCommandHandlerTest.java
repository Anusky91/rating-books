package es.anusky.rating_books.users.application.register;

import es.anusky.rating_books.infrastructure.UnitTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RegisterUserCommandHandlerTest extends UnitTestCase {

    RegisterUserCommandHandler handler;
    RegisterUserService service;

    @BeforeEach
    protected void setUp() {
        service = mock(RegisterUserService.class);
        handler = new RegisterUserCommandHandler(service);
    }

    @Test
    void should_delegate_to_registerUserService() {
        RegisterUserCommand command = new RegisterUserCommand(
                "Ana", "Nusk", "anusky", "ana@bookstar.com", "123456789", "superSecurePass",
                "ES", "1990-06-15", "USER", null);

        handler.handle(command);

        verify(service).register(
                eq("Ana"), eq("Nusk"), eq("anusky"), eq("ana@bookstar.com"),
                eq("123456789"), eq("superSecurePass"), eq("ES"),
                eq("1990-06-15"), eq("USER"), eq(null)
        );
    }
}