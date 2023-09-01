package jdbc;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class CustomConnector {
    @SneakyThrows
    public Connection getConnection(String url) {
        return DriverManager.getConnection(url);
    }

    @SneakyThrows
    public Connection getConnection(String url, String user, String password) {
        return DriverManager.getConnection(url, user, password);
    }
}
