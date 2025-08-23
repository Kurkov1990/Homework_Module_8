package app.service;

import org.flywaydb.core.Flyway;
import java.util.Properties;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseInitService {
    private static final Logger LOGGER = Logger.getLogger(DatabaseInitService.class.getName());

    public void migrateDatabase() {
        try {
            Properties props = new Properties();
            try (InputStream input = DatabaseInitService.class.getClassLoader().getResourceAsStream("application.properties")) {
                props.load(input);
            }

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            String flywayLocations = props.getProperty("flyway.locations");

            Flyway flyway = Flyway.configure()
                    .dataSource(url, user, password)
                    .locations(flywayLocations)
                    .load();

            flyway.migrate();
            LOGGER.info("Flyway migrations executed successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to execute Flyway migrations", e);
        }
    }
}
