package app.data;

import app.db.Database;
import app.model.Client;
import app.service.ClientSql;
import app.service.SqlExecutor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientDao {

    private static final Logger LOGGER = Logger.getLogger(ClientDao.class.getName());

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

    public long insert(String name) {
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

    public Client findById(long id) {
        return withConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(ClientSql.SELECT_BY_ID)) {
                stmt.setLong(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapClient(rs);
                    } else {
                        return null;
                    }
                }
            }
        });
    }

    public void updateName(long id, String name) {
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

    public void delete(long id) {
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

    public List<Client> findAll() {
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
