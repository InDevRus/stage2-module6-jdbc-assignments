package jdbc;

import jdbc.loaders.AppPropertiesLoader;
import jdbc.loaders.PropertiesLoader;
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
        private static final PropertiesLoader propertiesLoader = AppPropertiesLoader.getInstance();
        private static final CustomDataSource instance = new CustomDataSource(customConnector, propertiesLoader);
    }

    private final String driver = "org.postgresql.Driver";
    private final String url;
    private final String name;
    private final String password;

    private PrintWriter logWriter;

    private int loginTimeout;

    private final CustomConnector customConnector;

    private final PropertiesLoader propertiesLoader;

    private CustomDataSource(CustomConnector customConnector, PropertiesLoader propertiesLoader) {
        this.customConnector = customConnector;
        this.propertiesLoader = propertiesLoader;

        this.url = propertiesLoader.getByKey("postgres.url");
        this.name = propertiesLoader.getByKey("postgres.name");
        this.password = propertiesLoader.getByKey("postgres.password");
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
