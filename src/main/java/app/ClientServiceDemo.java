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
            // Виконуємо міграції Flyway
            databaseInitService.migrateDatabase();

            long clientId = createClient(clientService, "Test Client");
            readAndLogClient(clientService, clientId);
            updateClientName(clientService, clientId, "Test Client Updated");
            listAllClients(clientService);
            deleteClient(clientService, clientId);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in ClientServiceDemo", e);
        } finally {
            // Закриваємо пул HikariCP
            Database.getInstance().close();
        }
    }

    private long createClient(ClientService service, String name) {
        long id = service.create(name);
        LOGGER.log(Level.INFO, () -> "Created client with ID: " + id);
        return id;
    }

    private void readAndLogClient(ClientService service, long id) {
        String name = service.getById(id);
        LOGGER.log(Level.INFO, () -> "Client with ID " + id + " has name: " + name);
    }

    private void updateClientName(ClientService service, long id, String newName) {
        service.setName(id, newName);
        LOGGER.log(Level.INFO, () -> "Updated client name for ID " + id);
    }

    private void listAllClients(ClientService service) {
        List<Client> clients = service.listAll();
        LOGGER.log(Level.INFO, () -> {
            StringBuilder sb = new StringBuilder("Listing all clients:\n");
            clients.forEach(c -> sb.append(c.getId()).append(" -> ").append(c.getName()).append("\n"));
            return sb.toString();
        });
    }

    private void deleteClient(ClientService service, long id) {
        service.deleteById(id);
        LOGGER.log(Level.INFO, () -> "Deleted client with ID " + id);
    }
}
