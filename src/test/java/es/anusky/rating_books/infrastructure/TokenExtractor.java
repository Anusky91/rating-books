package es.anusky.rating_books.infrastructure;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenExtractor {

    public static String extract(String encoded) throws DecoderException {
        // 1. Elimina saltos de línea "duros" del quoted-printable (=\r\n)
        String cleaned = encoded.replaceAll("=\\r\\n", "");

        // 2. Decodifica quoted-printable
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String decoded = codec.decode(cleaned);

        // 3. Extrae el token con regex
        Pattern pattern = Pattern.compile("token=([a-f0-9\\-]{36})");
        Matcher matcher = pattern.matcher(decoded);

        if (matcher.find()) {
            String token = matcher.group(1);
            System.out.println("Token extraído: " + token);
            return token;
        }
        System.out.println("No se encontró ningún token.");
        return "";
    }
}
