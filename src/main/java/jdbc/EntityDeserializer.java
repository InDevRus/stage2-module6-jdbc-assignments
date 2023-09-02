package jdbc;

import lombok.SneakyThrows;

import java.sql.ResultSet;

class EntityDeserializer {
    private EntityDeserializer() {
    }

    @SneakyThrows
    static User toUser(ResultSet resultSet) {
        return new User(
                resultSet.getLong("id"),
                resultSet.getString("firstname"),
                resultSet.getString("lastname"),
                resultSet.getInt("age")
        );
    }
}
