package app.service;

public class ClientSql {

    private ClientSql() {
    }

    public static final String INSERT = "INSERT INTO CLIENT (NAME) VALUES (?)";
    public static final String SELECT_BY_ID = "SELECT ID, NAME FROM CLIENT WHERE ID = ?";
    public static final String UPDATE = "UPDATE CLIENT SET NAME = ? WHERE ID = ?";
    public static final String DELETE = "DELETE FROM CLIENT WHERE ID = ?";
    public static final String SELECT_ALL = "SELECT ID, NAME FROM CLIENT";
}
