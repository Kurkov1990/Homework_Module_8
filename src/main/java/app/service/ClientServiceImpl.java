package app.service;

import app.db.Database;
import app.model.Client;
import app.utils.ClientValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientServiceImpl implements ClientService {

    private static final Logger LOGGER = Logger.getLogger(ClientServiceImpl.class.getName());
    private final ClientValidator validator = new ClientValidator();

    @Override
    public long create(String name) {
        validator.validateClient(name);
        String sql = "INSERT INTO CLIENT (NAME) VALUES (?)";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating client failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    LOGGER.info(() -> "Client created with ID: " + id);
                    return id;
                } else {
                    throw new SQLException("Creating client failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating client", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getById(long id) {
        if (id <= 0) throw new IllegalArgumentException("Invalid client id");

        String sql = "SELECT NAME FROM CLIENT WHERE ID = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("NAME");
                    LOGGER.info(() -> "Fetched client ID " + id + " with name: " + name);
                    return name;
                } else {
                    throw new SQLException("Client with id " + id + " not found");
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching client by id", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setName(long id, String name) {
        if (id <= 0) throw new IllegalArgumentException("Invalid client id");
        validator.validateClient(name);

        String sql = "UPDATE CLIENT SET NAME = ? WHERE ID = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setLong(2, id);
            int updatedRows = stmt.executeUpdate();
            if (updatedRows == 0) {
                throw new SQLException("Client with id " + id + " not found");
            }
            LOGGER.info(() -> "Updated client ID " + id + " to name: " + name);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating client name", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(long id) {
        if (id <= 0) throw new IllegalArgumentException("Invalid client id");

        String sql = "DELETE FROM CLIENT WHERE ID = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int deletedRows = stmt.executeUpdate();
            if (deletedRows == 0) {
                throw new SQLException("Client with id " + id + " not found");
            }
            LOGGER.info(() -> "Deleted client with ID: " + id);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting client", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Client> listAll() {
        String sql = "SELECT ID, NAME FROM CLIENT";
        List<Client> clients = new ArrayList<>();

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clients.add(new Client(rs.getLong("ID"), rs.getString("NAME")));
            }

            LOGGER.info(() -> "Listed " + clients.size() + " clients from database");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error listing clients", e);
            throw new RuntimeException(e);
        }

        return clients;
    }
}
