package es.anusky.rating_books.users.infrastructure.controller;

import es.anusky.rating_books.shared.infrastructure.responses.ApiResponse;
import es.anusky.rating_books.users.application.resetpassword.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @PostMapping("/recover")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse initRecover(@RequestBody RecoverPasswordRequest request) {
        resetPasswordService.init(request.alias());
        return new ApiResponse("Ok");
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse reset(@RequestBody ResetPasswordRequest request) {
        resetPasswordService.reset(request.token(), request.newPassword());
        return new ApiResponse("Contraseña restaurada con exito");
    }

    public record RecoverPasswordRequest(String alias) {}
    public record ResetPasswordRequest(String token, String newPassword) {}

}
