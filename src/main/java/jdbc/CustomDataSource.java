package jdbc;

import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static class CustomDataSourceHolder {
        private static final CustomConnector customConnector = new CustomConnector();
        private static final CustomDataSource instance = new CustomDataSource(customConnector);
    }

    private final String driver = "org.postgresql.Driver";
    private final String url = "jdbc:postgresql://localhost:5432/myfirstdb";
    private final String name = "postgres";
    private final String password = "";

    private PrintWriter logWriter;

    private int loginTimeout;

    private final CustomConnector customConnector;

    private CustomDataSource(CustomConnector customConnector) {
        this.customConnector = customConnector;
    }

    public static CustomDataSource getInstance() {
        return CustomDataSourceHolder.instance;
    }

    @Override
    public Connection getConnection() {
        return customConnector.getConnection(url, this.name, this.password);
    }

    @Override
    public Connection getConnection(String username, String password) {
        return customConnector.getConnection(url, username, password);
    }

    @Override
    public PrintWriter getLogWriter() {
        return logWriter;
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) {
        this.logWriter = printWriter;
    }

    @Override
    public void setLoginTimeout(int loginTimeout) {
        this.loginTimeout = loginTimeout;
    }

    @Override
    public int getLoginTimeout() {
        return this.loginTimeout;
    }

    @Override
    public Logger getParentLogger() {
        return Logger.getGlobal();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) {
        return false;
    }
}
