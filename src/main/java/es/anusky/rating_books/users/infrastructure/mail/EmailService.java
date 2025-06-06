package es.anusky.rating_books.users.infrastructure.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendActivationEmail(String to, String token) {
        String subject = "Activa tu cuenta en BookStar";
        String body = String.format(
                """
                Hola!
            
                Para activar tu cuenta en BookStar, haz clic en el siguiente enlace:
            
                👉 https://bookstar-uolc.onrender.com/bookstar/auth/activate?token=%s
            
                Este enlace expirará en 24 horas.
            
                ¡Gracias por unirte a nuestra comunidad!
                """, token
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("no-reply@bookstar.dev");
        mailSender.send(message);
    }

    public void sendRecoverPasswordEmail(String alias, String to, String token) {
        String subject = "Resetea tu contraseña en BookStar";
        String body = String.format(
                """
                Hola %s!
            
                Para restablecer tu contraseña en BookStar, haz clic en el siguiente enlace:
            
                👉 https://bookstar-uolc.onrender.com/bookstar/auth/reset?token=%s
            
                Este enlace expirará en 1 hora.
            
                ¡Recuerda que tu contraseña queda bloqueada hasta que se complete la operación!
                
                ¡Gracias por confiar en BookStar!
                
                El equipo de BookStar ⭐
                """, alias, token
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("no-reply@bookstar.dev");
        mailSender.send(message);
    }
}

