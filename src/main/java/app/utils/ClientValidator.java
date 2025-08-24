package app.utils;

public class ClientValidator {

    public void validateId(long id) {
        if (id <= 0) throw new IllegalArgumentException("Invalid client id");
    }

    public void validateClientName(String name) {
        if (name == null  || name.length() < 2 || name.length() > 500) {
            throw new IllegalArgumentException("Client name must be between 2 and 500 characters");
        }
    }
}
