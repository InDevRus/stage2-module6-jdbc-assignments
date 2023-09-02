package jdbc.loaders;

import lombok.SneakyThrows;
import org.apache.commons.configuration.PropertiesConfiguration;

public class AppPropertiesLoader implements PropertiesLoader {
    private static class PropertiesLoaderHolder {
        private static final PropertiesConfiguration configuration = new PropertiesConfiguration();
        private static final AppPropertiesLoader instance = new AppPropertiesLoader(PropertiesLoaderHolder.configuration);
    }

    private final PropertiesConfiguration configuration;
    private static final String PROPERTIES_FILENAME = "app.properties";

    public static AppPropertiesLoader getInstance() {
        return PropertiesLoaderHolder.instance;
    }

    @SneakyThrows
    private AppPropertiesLoader(PropertiesConfiguration configuration) {
        configuration.load(PROPERTIES_FILENAME);
        this.configuration = configuration;
    }

    @Override
    public String getByKey(String key) {
        return this.configuration.getString(key);
    }
}
