package es.anusky.rating_books.users.domain.valueobjects;

import java.util.*;

public class PasswordMother {

    private static final char[] MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] MINUSCULAS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] NUMEROS = "0123456789".toCharArray();
    private static final char[] SIMBOLOS = "!@#$%^&*()-_=+[]{}".toCharArray();

    private static final Random random = new Random();

    public static Password generate() {
        int totalLength = 12;
        return generate(totalLength);
    }

    public static Password generate(int totalLength) {

        if (totalLength < 4) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres para incluir todos los tipos requeridos");
        }

        List<Character> passwordChars = new ArrayList<>();

        // Asegura uno de cada tipo
        passwordChars.add(randomChar(MAYUSCULAS));
        passwordChars.add(randomChar(MINUSCULAS));
        passwordChars.add(randomChar(NUMEROS));
        passwordChars.add(randomChar(SIMBOLOS));

        // Añade caracteres aleatorios hasta completar el largo
        char[] allChars = concat(MAYUSCULAS, MINUSCULAS, NUMEROS, SIMBOLOS);
        while (passwordChars.size() < totalLength) {
            passwordChars.add(randomChar(allChars));
        }

        // Mezcla para no tener el patrón "obligatorio" al principio
        Collections.shuffle(passwordChars);

        // Convierte a String
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return new Password(password.toString());
    }

    private static char randomChar(char[] source) {
        return source[random.nextInt(source.length)];
    }

    private static char[] concat(char[]... arrays) {
        return Arrays.stream(arrays)
                .flatMapToInt(arr -> new String(arr).chars())
                .mapToObj(c -> (char) c)
                .collect(StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append)
                .toString()
                .toCharArray();
    }
}

