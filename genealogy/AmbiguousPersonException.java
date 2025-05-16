package genealogy;

public class AmbiguousPersonException extends Exception {
    public AmbiguousPersonException(String firstName, String lastName) {
        super("W pliku pojawiła się więcej niż jedna osoba o imieniu i nazwisku: " + firstName + " " + lastName);
    }
}
