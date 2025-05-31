package es.anusky.rating_books.shared.domain.enums;

public enum SupportedCountryCode {
    ES, FR, DE, IT, US, AR, MX, BR, CO, PT;

    public static boolean isValid(String code) {
        try {
            SupportedCountryCode.valueOf(code.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
