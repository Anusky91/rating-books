package es.anusky.rating_books.users.application.register;

import es.anusky.rating_books.cqrs.application.command.Command;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class RegisterUserCommand implements Command {
    String firstName;
    String lastName;
    String alias;
    String email;
    String phoneNumber;
    String password;
    String country;
    String birthDate;
    String role;
    String avatarUrl;

    public RegisterUserCommand(String firstName,
                               String lastName,
                               String alias,
                               String email,
                               String phoneNumber,
                               String password,
                               String country,
                               String birthDate,
                               String role,
                               String avatarUrl) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.country = country;
        this.birthDate = birthDate;
        this.role = role;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "RegisterUserCommand{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", alias='" + alias + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + "*********" + '\'' +
                ", country='" + country + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", role='" + role + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
