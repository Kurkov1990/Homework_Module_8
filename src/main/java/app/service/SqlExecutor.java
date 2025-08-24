package app.service;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlExecutor<T> {
    T apply(Connection conn) throws SQLException;
}
