package app.service;

import app.data.ClientDao;
import app.model.Client;
import app.utils.ClientValidator;

import java.util.List;

public class ClientServiceImpl implements ClientService {

    private final ClientValidator validator = new ClientValidator();
    private final ClientDao clientDao = new ClientDao();

    @Override
    public long create(String name) {
        validator.validateClientName(name);
        return clientDao.insert(name);
    }

    @Override
    public String getById(long id) {
        validator.validateId(id);
        Client client = clientDao.findById(id);
        if (client == null) {
            throw new RuntimeException("Client with id " + id + " not found");
        }
        return client.getName();
    }

    @Override
    public void setName(long id, String name) {
        validator.validateId(id);
        validator.validateClientName(name);
        clientDao.updateName(id, name);
    }

    @Override
    public void deleteById(long id) {
        validator.validateId(id);
        clientDao.delete(id);
    }

    @Override
    public List<Client> listAll() {
        return clientDao.findAll();
    }
}
