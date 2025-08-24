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

    private <T> T withConnection(SqlExecutor<T> action) {
        try (Connection conn = Database.getInstance().getConnection()) {
            return action.apply(conn);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
            throw new RuntimeException(e);
        }
    }


    private Client mapClient(ResultSet rs) throws SQLException {
        return new Client(rs.getLong("ID"), rs.getString("NAME"));
    }

    @Override
    public long create(String name) {
        validator.validateClientName(name);

        return withConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(ClientSql.INSERT, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, name);
                if (stmt.executeUpdate() == 0) {
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
            }
        });
    }

    @Override
    public String getById(long id) {
        validator.validateId(id);

        return withConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(ClientSql.SELECT_BY_ID)) {
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
            }
        });
    }

    @Override
    public void setName(long id, String name) {
        validator.validateId(id);
        validator.validateClientName(name);

        withConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(ClientSql.UPDATE)) {
                stmt.setString(1, name);
                stmt.setLong(2, id);
                if (stmt.executeUpdate() == 0) {
                    throw new SQLException("Client with id " + id + " not found");
                }
                LOGGER.info(() -> "Updated client ID " + id + " to name: " + name);
                return null;
            }
        });
    }

    @Override
    public void deleteById(long id) {
        validator.validateId(id);

        withConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(ClientSql.DELETE)) {
                stmt.setLong(1, id);
                if (stmt.executeUpdate() == 0) {
                    throw new SQLException("Client with id " + id + " not found");
                }
                LOGGER.info(() -> "Deleted client with ID: " + id);
                return null;
            }
        });
    }

    @Override
    public List<Client> listAll() {
        return withConnection(conn -> {
            List<Client> clients = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(ClientSql.SELECT_ALL);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clients.add(mapClient(rs));
                }
            }
            LOGGER.info(() -> "Listed " + clients.size() + " clients from database");
            return clients;
        });
    }
}
