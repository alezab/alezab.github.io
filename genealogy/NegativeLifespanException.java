package genealogy;

public class NegativeLifespanException extends Exception {
    public NegativeLifespanException(String firstName, String lastName, String birth, String death) {
        super("Data śmierci (" + death + ") osoby " + firstName + " " + lastName + " jest wcześniejsza niż data urodzin (" + birth + ").");
    }
}
