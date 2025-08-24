package app;

import app.db.Database;
import app.model.Client;
import app.service.ClientService;
import app.service.ClientServiceImpl;
import app.service.DatabaseInitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientServiceDemo {
    private static final Logger LOGGER = Logger.getLogger(ClientServiceDemo.class.getName());

    public static void main(String[] args) {
        new ClientServiceDemo().runDemo();
    }

    private void runDemo() {
        DatabaseInitService databaseInitService = new DatabaseInitService();
        ClientService clientService = new ClientServiceImpl();

        try {
            databaseInitService.migrateDatabase();

            long clientId = clientService.create("Test Client");
            clientService.getById(clientId);
            clientService.setName(clientId, "Test Client Updated");

            List<Client> clients = clientService.listAll();
            LOGGER.log(Level.INFO, () -> {
                StringBuilder sb = new StringBuilder("Listing all clients:\n");
                clients.forEach(c -> sb.append(c.getId()).append(" -> ").append(c.getName()).append("\n"));
                return sb.toString();
            });

            clientService.deleteById(clientId);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in ClientServiceDemo", e);
        } finally {
            Database.getInstance().close();
        }
    }
}
