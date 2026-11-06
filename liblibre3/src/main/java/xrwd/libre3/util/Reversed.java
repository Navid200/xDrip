package xrwd.libre3.util;

/**
 * Fixed addresses are kept reversed in the source, so they are not found by a plain-text search; this turns them back.
 *
 * @author Asbjørn Aarrestad - 2026.11
 */
public final class Reversed {

    private Reversed() {
    }

    /** The string with its characters in reverse order. */
    public static String of(String reversed) {
        return new StringBuilder(reversed).reverse().toString();
    }
}
