package app;

import app.dao.ClientDao;
import app.dao.ClientDaoImpl;
import app.db.Database;
import app.service.ClientService;
import app.service.ClientServiceImpl;
import app.service.DatabaseInitService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientServiceDemo {
    private static final Logger LOGGER = Logger.getLogger(ClientServiceDemo.class.getName());

    public static void main(String[] args) {
        new ClientServiceDemo().runDemo();
    }

    private void runDemo() {
        DatabaseInitService databaseInitService = new DatabaseInitService();
        ClientDao clientDao = new ClientDaoImpl();
        ClientService clientService = new ClientServiceImpl(clientDao);

        try {
            databaseInitService.migrateDatabase();

            long clientId = clientService.create("Test Client");
            clientService.getById(clientId);
            clientService.setName(clientId, "Test Client Updated");
            clientService.listAll();
            clientService.deleteById(clientId);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in ClientServiceDemo", e);
        } finally {
            Database.getInstance().close();
        }
    }
}
