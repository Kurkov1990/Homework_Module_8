package app.dao;

import app.model.Client;
import java.util.List;

public interface ClientDao {

    long insertClient(String name);

    Client findById(long id);

    void updateName(long id, String name);

    void delete(long id);

    List<Client> findAll();
}

