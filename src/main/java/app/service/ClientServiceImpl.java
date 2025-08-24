package app.service;

import app.dao.ClientDao;
import app.exception.ClientException;
import app.model.Client;
import app.utils.ClientValidator;

import java.util.List;

public class ClientServiceImpl implements ClientService {

    private final ClientValidator validator = new ClientValidator();
    private final ClientDao clientDao;

    public ClientServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public long create(String name) {
        validator.validateClientName(name);
        try {
            return clientDao.insertClient(name);
        } catch (ClientException e) {
            throw new ClientException("Failed to create client", e);
        }
    }

    @Override
    public String getById(long id) {
        validator.validateId(id);
        try {
            Client client = clientDao.findById(id);
            if (client == null) {
                throw new ClientException("Client with id " + id + " not found");
            }
            return client.getName();
        } catch (ClientException e) {
            throw new ClientException("Failed to fetch client by id " + id, e);
        }
    }

    @Override
    public void setName(long id, String name) {
        validator.validateId(id);
        validator.validateClientName(name);
        try {
            clientDao.updateName(id, name);
        } catch (ClientException e) {
            throw new ClientException("Failed to update client with id " + id, e);
        }
    }

    @Override
    public void deleteById(long id) {
        validator.validateId(id);
        try {
            clientDao.delete(id);
        } catch (ClientException e) {
            throw new ClientException("Failed to delete client with id " + id, e);
        }
    }

    @Override
    public List<Client> listAll() {
        try {
            return clientDao.findAll();
        } catch (ClientException e) {
            throw new ClientException("Failed to list clients", e);
        }
    }
}
