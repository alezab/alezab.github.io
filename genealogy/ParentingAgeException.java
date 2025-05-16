package genealogy;

public class ParentingAgeException extends Exception {
    public ParentingAgeException(String parentName, String childName, String reason) {
        super("Nieprawidłowa relacja rodzic-dziecko: " + parentName + " → " + childName + ". Powód: " + reason);
    }
}
