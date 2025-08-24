package app.utils;

import app.exception.ClientException;

public class ClientValidator {

    public void validateClientName(String name) {
        if (name.length() < 2 || name.length() > 500) {
            throw new ClientException("Client name must be between 2 and 500 charactersy");
        }
    }

    public void validateId(long id) {
        if (id <= 0) {
            throw new ClientException("Invalid client id");
        }
    }
}
