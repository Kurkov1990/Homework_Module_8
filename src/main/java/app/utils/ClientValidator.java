package app.utils;

public class ClientValidator {

    public void validateClient(String name) {
        if (name == null  || name.length() < 2 || name.length() > 500) {
            throw new IllegalArgumentException("Client name must be between 2 and 500 characters");
        }
    }
}
